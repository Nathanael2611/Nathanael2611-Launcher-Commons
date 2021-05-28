package fr.nathanael2611.commons.launcher.update;

import fr.nathanael2611.commons.launcher.BaseLauncher;
import fr.nathanael2611.commons.launcher.update.data.UpdateProfile;
import fr.nathanael2611.commons.launcher.util.Helpers;

import java.io.File;
import java.io.IOException;

public class EazyUpdater
{

    private String serverUrl;
    private File directory;
    private UpdateProfile profile;

    private boolean willRemoveSurplus = true;

    private final int totalFilesToDownload;
    private int downloadedFiles = 0;

    private boolean downloadStarted = false;
    private boolean downloadFinished = false;

    private boolean checkingStarted = false;
    private boolean checkingFinished = false;

    private final long totalBytesToDownload;
    private long downloadedBytes;

    public EazyUpdater(String serverUrl, File directory) throws IOException
    {
        this.serverUrl = serverUrl;

        if(directory.isFile()) throw new IOException("The specified directory is a file");

        this.directory = directory;
        this.profile = new UpdateProfile(serverUrl, directory);

        long[] bytes = new long[1];
        this.profile.filesToDownload.forEach(f -> bytes[0] += f.getSize());
        this.totalBytesToDownload = bytes[0];

        this.totalFilesToDownload = this.profile.filesToDownload.size();



    }

    public UpdateProfile getProfile()
    {
        return profile;
    }

    public File getDirectory()
    {
        return directory;
    }

    public int getDownloadedFiles()
    {
        return downloadedFiles;
    }

    public int getTotalFilesToDownload()
    {
        return totalFilesToDownload;
    }

    public void startDownload(UpdateManager manager)
    {
        this.downloadStarted = true;
        this.checkingStarted = true;
        manager.setUpdateMessage("Checking files");
        Helpers.sendMessageInConsole("Checking files");
        if(this.willRemoveSurplus) this.profile.syncedDirectories.forEach(dir -> dir.removeSurplus(this.directory, this));
        this.checkingFinished = true;
        manager.setUpdateMessage("Downloading files...");
        Helpers.sendMessageInConsole("Downloading files...");
        this.profile.filesToDownload.forEach(file ->
        {
            file.download(this.directory, this);
            this.downloadedFiles ++;
        });
        this.downloadFinished = true;
    }

    public long getDownloadedBytes()
    {
        return downloadedBytes;
    }

    public void incrementDownloadedBytes(long val)
    {
        this.downloadedBytes += val;
    }

    public long getTotalBytesToDownload() {
        return totalBytesToDownload;
    }

    public boolean isDownloadFinished()
    {
        return downloadFinished;
    }

    public boolean isDownloadStarted()
    {
        return downloadStarted;
    }

    public boolean isCheckingStarted()
    {
        return checkingStarted;
    }

    public boolean isCheckingFinished()
    {
        return checkingFinished;
    }

    public void setWillRemoveSurplus(boolean willRemoveSurplus)
    {
        this.willRemoveSurplus = willRemoveSurplus;
    }

    public static EazyUpdater fromLauncher(BaseLauncher launcher, String serverUrl) throws IOException
    {
        return new EazyUpdater(serverUrl, launcher.minecraftDir);
    }

}
