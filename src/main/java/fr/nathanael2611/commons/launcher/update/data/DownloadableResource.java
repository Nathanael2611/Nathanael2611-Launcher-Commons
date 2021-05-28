package fr.nathanael2611.commons.launcher.update.data;

import fr.nathanael2611.commons.launcher.update.EazyUpdater;
import fr.nathanael2611.commons.launcher.util.Helpers;
import org.apache.commons.io.FileUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

public class DownloadableResource
{

    private String fileName;
    private String downloadUrl;
    private String relativePath;
    private String md5;
    private int size;

    public DownloadableResource(String fileName, String downloadUrl, String relativePath, String md5, int size)
    {
        this.fileName = fileName;
        this.downloadUrl = downloadUrl;
        this.relativePath = relativePath;
        this.md5 = md5;
        this.size = size;
    }

    public String getFileName()
    {
        return fileName;
    }

    public String getDownloadUrl()
    {
        return downloadUrl;
    }

    public String getRelativePath()
    {
        return relativePath;
    }

    public String getMd5()
    {
        return md5;
    }

    public int getSize()
    {
        return size;
    }

    public void download(File baseDir, EazyUpdater updater)
    {
        baseDir.mkdir();
        new File(baseDir, getRelativePath()).mkdirs();

        File file = new File(new File(baseDir, getRelativePath()), getFileName());

        if(updater.getProfile().getIgnoreList().contains(file.getAbsolutePath(), updater)) return;

        if(file.exists()) {
            String md5 = Helpers.md5File(file);
            if(!md5.equals(getMd5())){
                try {
                    FileUtils.forceDelete(file);
                    Helpers.sendMessageInConsole("Deleting " + file.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else
            {
                updater.incrementDownloadedBytes(this.size);
                return;
            }
        }

        Helpers.sendMessageInConsole("Starting download of " + getDownloadUrl());
        try (BufferedInputStream in = new BufferedInputStream(new URL(getDownloadUrl()).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            byte dataBuffer[] = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
                updater.incrementDownloadedBytes(1024);
            }
        } catch (IOException e) {
        }
    }
}
