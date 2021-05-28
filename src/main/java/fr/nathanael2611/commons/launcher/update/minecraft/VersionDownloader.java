package fr.nathanael2611.commons.launcher.update.minecraft;

import fr.nathanael2611.commons.launcher.BaseLauncher;
import fr.nathanael2611.commons.launcher.update.minecraft.download.DownloadEntry;
import fr.nathanael2611.commons.launcher.update.minecraft.download.DownloadManager;
import fr.nathanael2611.commons.launcher.update.minecraft.utils.FileUtils;
import fr.nathanael2611.commons.launcher.update.minecraft.utils.JsonManager;
import fr.nathanael2611.commons.launcher.update.minecraft.versions.*;
import fr.nathanael2611.commons.launcher.util.MinecraftFolder;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

public class VersionDownloader {

    private MinecraftFolder folder;
    private DownloadManager manager;
    private ArrayList<File> natives;

    public VersionDownloader(BaseLauncher launcher){
        this.folder = launcher.getMinecraftFolder();
        this.manager = new DownloadManager(folder);
        this.natives = new ArrayList<>();
    }

    public Game downloadVersion(BaseLauncher launcher, LightVersion version, final CompleteVersion complete) {

        final String index = this.downloadResources(launcher, complete, this.folder.getAssetsFolder());
        this.downloadClient(launcher, complete);



        Game game = new Game(complete.getID(), null, null, index, this.folder, Version.VersionType.VANILLA);
        if(version.getForgeVersion() != null){

            this.downloadForgeLibraries(launcher, version.getForgeVersion());
            complete.setType(Version.VersionType.FORGE);
            game.setVersionType(Version.VersionType.FORGE);
            game.setTweaker(version.getForgeVersion().getTweakers());
        }

        this.downloadLibraries(launcher, complete);

        return game;
    }

    private void downloadForgeLibraries(BaseLauncher launcher, final ForgeVersion version) {
        if (version.getLibraries() == null || version.getLibraries().isEmpty()) {
            final String v = version.getMinecraftVersion();
            if (!v.equals("1.1") && !v.startsWith("1.2.") && !v.equals("1.3.2")) {
                if (!v.startsWith("1.4.")) {
                    try {

                        final File l = new File(this.folder.getLibrariesFolder(), version.getJarMods()[0].getArtifactURL());
                        this.manager.addDownloadableFile(new DownloadEntry(launcher, new URL(version.getJarMods()[0].getDownloads().getArtifact().getUrl()), new File(FileUtils.removeExtension(l.getAbsolutePath()) + ".jar"), version.getJarMods()[0].getDownloads().getArtifact().getSha1()));
                        this.folder.setClientJarSHA1(null);
                    }
                    catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    return;
                }
            }
            try {
                this.folder.setClientJarURL(new URL(version.getJarMods()[0].getDownloads().getArtifact().getUrl()));
                this.folder.setClientJarSHA1(version.getJarMods()[0].getDownloads().getArtifact().getSha1());
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return;
        }
        for (final Library library : version.getLibraries()) {
            try {
                if (library.getURL().contains("typesafe") || (version.getMinecraftVersion().equals("1.7.10") && library.getName().contains("guava") && library.getName().contains("15"))) {
                    continue;
                }
                if (version.getMinecraftVersion().equals("1.7.2") && library.getName().contains("launchwrapper")) {
                    this.manager.addDownloadableFile(new DownloadEntry(launcher, new URL("https://libraries.minecraft.net/net/minecraft/launchwrapper/1.12/launchwrapper-1.12.jar"), new File(this.folder.getLibrariesFolder(), library.getArtifactURL()), null));
                }
                else {
                    this.manager.addDownloadableFile(new DownloadEntry(launcher, new URL(library.getURL()), new File(this.folder.getLibrariesFolder(), library.getArtifactURL()), null));
                }
            }
            catch (MalformedURLException e2) {
                e2.printStackTrace();
            }
        }
    }

    private void downloadLibraries(BaseLauncher launcher, final CompleteVersion complete) {
        final ArrayList<File> natives = new ArrayList<File>();
        for (final Library library : complete.getLibraries()) {

            if (!complete.getID().equals("1.7.10") || !library.getName().contains("guava") || !complete.getVersionType().equals(Version.VersionType.FORGE)) {
                String file = "";
                String classifier = null;
                File local = new File(this.folder.getLibrariesFolder(), String.valueOf(library.getArtifactBaseDir()) + ".jar");
                String sha1 = null;
                if (library.getNative() != null) {
                    classifier = library.getNative();
                    if (classifier != null) {
                        file = library.getArtifactPath(classifier);
                        local = new File(this.folder.getNativesFolder(), library.getArtifactFilename(classifier));
                        natives.add(local);
                        this.manager.getDontDownload().add(local.getAbsolutePath());
                        if (library.getClassifiers() != null) {
                            if (classifier.contains("windows")) {
                                sha1 = library.getClassifiers().getWindows().getSha1();
                            }
                            if (classifier.contains("linux")) {
                                sha1 = library.getClassifiers().getLinux().getSha1();
                            }
                            if (classifier.contains("macos")) {
                                sha1 = library.getClassifiers().getMacos().getSha1();
                            }
                        }
                        else if (library.getDownloads().getArtifact() != null) {
                            sha1 = library.getDownloads().getArtifact().getSha1();
                        }
                    }
                }
                else {
                    file = library.getArtifactPath();
                }
                try {
                    final URL url = new URL("https://libraries.minecraft.net/" + file);
                    if (library.getDownloads().getArtifact() != null) {
                        sha1 = library.getDownloads().getArtifact().getSha1();
                    }
                    this.manager.addDownloadableFile(new DownloadEntry(launcher, url, local, sha1));
                }
                catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }
        this.natives.addAll(natives);
    }

    @SuppressWarnings({ "deprecation", "unchecked" })
	private String downloadResources(BaseLauncher launcher, final CompleteVersion complete, final File assets) {
        InputStream stream = null;
        final File objectsFolder = new File(assets, "objects");
        final File indexesFolder = new File(assets, "indexes");
        if (!objectsFolder.exists()) {
            objectsFolder.mkdirs();
        }
        final AssetIndexInfo indexInfo = complete.getAssetIndex();
        final File indexFile = new File(indexesFolder, String.valueOf(indexInfo.getId()) + ".json");
        try {
            final URL indexUrl = indexInfo.getURL();
            stream = indexUrl.openConnection().getInputStream();
            final String json = IOUtils.toString(stream);
            org.apache.commons.io.FileUtils.writeStringToFile(indexFile, json);
            @SuppressWarnings("rawtypes")
			final AssetIndex index = (AssetIndex) JsonManager.getGson().fromJson(json, (Class) AssetIndex.class);
            for (final Map.Entry<AssetIndex.AssetObject, String> entry : index.getUniqueObjects().entrySet()) {
                final AssetIndex.AssetObject object = entry.getKey();
                final String filename = String.valueOf(object.getHash().substring(0, 2)) + "/" + object.getHash();
                final File file = new File(objectsFolder, filename);
                if (!file.isFile() || org.apache.commons.io.FileUtils.sizeOf(file) != object.getSize()) {
                    final AssetDownloadable downloadable = new AssetDownloadable(launcher, entry.getValue(), object, "http://resources.download.minecraft.net/", objectsFolder);
                    this.manager.addDownloadableFile(downloadable);
                }
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return indexInfo.getId();
        }
        finally {
            IOUtils.closeQuietly(stream);
        }
        IOUtils.closeQuietly(stream);
        return indexInfo.getId();
    }

    public void unzipNatives() {
        for (final File file : this.natives) {
            if (file.exists()) {
                try {
                    FileUtils.unzip(file, this.folder.getNativesFolder());
                    this.manager.getDontDownload().add(file.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        final File meta = new File(this.folder.getNativesFolder(), "META-INF");
        final File md = new File(meta, "MANIFEST.MF");
        if (meta.exists()) {
            FileUtils.removeFolder(meta);
            org.apache.commons.io.FileUtils.deleteQuietly(meta);
        }
        if (md.exists()) {
            md.delete();
        }
    }

    private void downloadClient(BaseLauncher launcher, final CompleteVersion complete) {
        try {
            if (this.folder.getClientJarURL() != null) {
                this.manager.addDownloadableFile(new DownloadEntry(launcher, this.folder.getClientJarURL(), this.folder.getClientJarFile(), complete.getDownloads().getClient().getSha1()));
            }
            else {
                this.manager.addDownloadableFile(new DownloadEntry(launcher, new URL(complete.getDownloads().getClient().getUrl()), this.folder.getClientJarFile(), complete.getDownloads().getClient().getSha1()));
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DownloadManager getDownloadManager() {
        return manager;
    }
}
