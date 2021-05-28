package fr.nathanael2611.commons.launcher.update.minecraft.versions;


import fr.nathanael2611.commons.launcher.BaseLauncher;
import fr.nathanael2611.commons.launcher.update.minecraft.download.DownloadEntry;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.GZIPInputStream;

public class AssetDownloadable extends DownloadEntry
{

    private final AssetIndex.AssetObject asset;
    private final File destination;
    
    public AssetDownloadable(BaseLauncher launcher, final String name, final AssetIndex.AssetObject asset, final String url, final File destination) throws MalformedURLException {
        super(launcher, new URL(String.valueOf(url) + createPathFromHash(asset.getHash())), null, null);
        this.asset = asset;
        this.destination = destination;
        this.setTarget(new File(destination, createPathFromHash(asset.getHash())));
    }
    
    protected static String createPathFromHash(final String hash) {
        return String.valueOf(hash.substring(0, 2)) + "/" + hash;
    }
    
    @Override
    public void download() {
        try {
            final File localAsset = super.getTarget();
            final File localCompressed = this.asset.hasCompressedAlternative() ? new File(this.destination, createPathFromHash(this.asset.getCompressedHash())) : null;
            final URL remoteAsset = this.getURL();
            URL url;
            if (this.asset.hasCompressedAlternative()) {
                final StringBuilder sb = new StringBuilder(String.valueOf(this.getURL().toString()));

                url = new URL(sb.append(createPathFromHash(this.asset.getCompressedHash())).toString());
            }
            else {
                url = null;
            }
            final URL remoteCompressed = url;
            this.ensureFileWritable(localAsset);
            if (localCompressed != null) {
                super.ensureFileWritable(localCompressed);
            }
            if (localAsset.isFile()) {
                if (FileUtils.sizeOf(localAsset) == this.asset.getSize()) {
                    return;
                }
                FileUtils.deleteQuietly(localAsset);
            }
            if (localCompressed != null && localCompressed.isFile()) {
                final String localCompressedHash = DownloadEntry.getDigest(localCompressed, "SHA", 40);
                if (localCompressedHash.equalsIgnoreCase(this.asset.getCompressedHash())) {
                    this.decompressAsset(localAsset, localCompressed);
                    return;
                }
                FileUtils.deleteQuietly(localCompressed);
            }
            if (remoteCompressed != null && localCompressed != null) {
                final HttpURLConnection connection = this.makeConnection(remoteCompressed);
                final int status = connection.getResponseCode();
                if (status / 100 != 2) {
                    throw new RuntimeException("Server responded with " + status);
                }
                final InputStream inputStream = connection.getInputStream();
                final FileOutputStream outputStream = new FileOutputStream(localCompressed);
                final String hash = this.copyAndDigest(inputStream, outputStream, "SHA", 40);
                if (hash.equalsIgnoreCase(this.asset.getCompressedHash())) {
                    this.decompressAsset(localAsset, localCompressed);
                    return;
                }
                FileUtils.deleteQuietly(localCompressed);
                throw new RuntimeException(String.format("Hash did not match downloaded compressed asset (Expected %s, downloaded %s)", this.asset.getCompressedHash(), hash));
            }
            else {
                final HttpURLConnection connection = this.makeConnection(remoteAsset);
                final int status = connection.getResponseCode();
                if (status / 100 != 2) {
                    throw new RuntimeException("Server responded with " + status);
                }
                final InputStream inputStream = connection.getInputStream();
                final FileOutputStream outputStream = new FileOutputStream(localAsset);
                final String hash = this.copyAndDigest(inputStream, outputStream, "SHA", 40);
                if (hash.equalsIgnoreCase(this.asset.getHash())) {
                    return;
                }
                FileUtils.deleteQuietly(localAsset);
                throw new RuntimeException(String.format("Hash did not match downloaded asset (Expected %s, downloaded %s)", this.asset.getHash(), hash));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    protected HttpURLConnection makeConnection(final URL url) throws IOException {
        final HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setUseCaches(false);
        connection.setDefaultUseCaches(false);
        connection.setRequestProperty("Cache-Control", "no-store,max-age=0,no-cache");
        connection.setRequestProperty("Expires", "0");
        connection.setRequestProperty("Pragma", "no-cache");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(30000);
        return connection;
    }
    
    @SuppressWarnings("deprecation")
	protected void decompressAsset(final File localAsset, final File localCompressed) throws IOException {
        final OutputStream outputStream = FileUtils.openOutputStream(localAsset);
        final InputStream inputStream = new GZIPInputStream(FileUtils.openInputStream(localCompressed));
        String hash = null;
        try {
            hash = this.copyAndDigest(inputStream, outputStream, "SHA", 40);
        }
        finally {
            IOUtils.closeQuietly(outputStream);
            IOUtils.closeQuietly(inputStream);
        }
        IOUtils.closeQuietly(outputStream);
        IOUtils.closeQuietly(inputStream);
        if (hash.equalsIgnoreCase(this.asset.getHash())) {
            return;
        }
        FileUtils.deleteQuietly(localAsset);
        throw new RuntimeException("Had local compressed asset but unpacked hash did not match (expected " + this.asset.getHash() + " but had " + hash + ")");
    }
    
    @Override
    public boolean needDownload() {
        return !this.getTarget().exists();
    }

}
