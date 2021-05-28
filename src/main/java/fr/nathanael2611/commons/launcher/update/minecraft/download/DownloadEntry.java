package fr.nathanael2611.commons.launcher.update.minecraft.download;


import fr.nathanael2611.commons.launcher.BaseLauncher;
import fr.nathanael2611.commons.launcher.update.minecraft.utils.FileUtils;
import fr.nathanael2611.commons.launcher.util.MinecraftFolder;

import java.io.*;
import java.math.BigInteger;
import java.net.URL;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DownloadEntry
{

    private URL url;
    private File target;
    private File destination;
    private String sha1;
    private MinecraftFolder folder;
    public DownloadEntry(BaseLauncher launcher, final URL url, final File destination, final String sha1) {
        this.url = url;
        this.destination = destination;
        this.sha1 = sha1;
        this.folder = launcher.getMinecraftFolder();
    }
    
    public boolean needDownload() {
        if (this.destination == null) {
            return false;
        }
        if (!this.destination.exists()) {
            return true;
        }
        if (this.sha1 == null) {
            return false;
        }
        if (FileUtils.getSHA1(this.destination).equalsIgnoreCase(this.sha1)) {
            return false;
        }
        if (!this.destination.getParentFile().getAbsolutePath().equals(this.folder.getNativesFolder().getAbsolutePath())) {
            this.destination.delete();
            return true;
        }
        return false;
    }
    
    public void download() {
        try {
            org.apache.commons.io.FileUtils.copyURLToFile(this.url, this.destination);
        }
        catch (IOException e) {
            if (!(e instanceof FileNotFoundException)) {
                e.printStackTrace();
            }
        }
    }
    
    public String copyAndDigest(final InputStream inputStream, final OutputStream outputStream, final String algorithm, final int hashLength) throws IOException {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance(algorithm);
        }
        catch (NoSuchAlgorithmException e) {
            closeSilently(inputStream);
            closeSilently(outputStream);
            throw new RuntimeException("Missing Digest." + algorithm, e);
        }
        final byte[] buffer = new byte[65536];
        try {
            for (int read = inputStream.read(buffer); read >= 1; read = inputStream.read(buffer)) {
                digest.update(buffer, 0, read);
                outputStream.write(buffer, 0, read);
            }
        }
        finally {
            closeSilently(inputStream);
            closeSilently(outputStream);
        }
        closeSilently(inputStream);
        closeSilently(outputStream);
        return String.format("%1$0" + hashLength + "x", new BigInteger(1, digest.digest()));
    }
    
    public static void closeSilently(final Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            }
            catch (IOException ex) {}
        }
    }
    
    public void setTarget(final File target) {
        this.target = target;
    }
    
    public URL getURL() {
        return this.url;
    }
    
    public File getTarget() {
        return this.target;
    }
    
    protected void ensureFileWritable(final File target) {
        if (target.getParentFile() != null && !target.getParentFile().isDirectory() && !target.getParentFile().mkdirs() && !target.getParentFile().isDirectory()) {
            throw new RuntimeException("Could not create directory " + target.getParentFile());
        }
        if (target.isFile() && !target.canWrite()) {
            throw new RuntimeException("Do not have write permissions for " + target + " - aborting!");
        }
    }
    
    public static String getDigest(final File file, final String algorithm, final int hashLength) {
        DigestInputStream stream = null;
        try {
            stream = new DigestInputStream(new FileInputStream(file), MessageDigest.getInstance(algorithm));
            final byte[] buffer = new byte[65536];
            int read;
            do {
                read = stream.read(buffer);
            } while (read > 0);
        }
        catch (Exception ignored) {
            return null;
        }
        finally {
            closeSilently(stream);
        }
        closeSilently(stream);
        return String.format("%1$0" + hashLength + "x", new BigInteger(1, stream.getMessageDigest().digest()));
    }
    
    public File getDestination() {
        if (this.getTarget() == null) {
            return this.destination;
        }
        return this.getTarget();
    }


}
