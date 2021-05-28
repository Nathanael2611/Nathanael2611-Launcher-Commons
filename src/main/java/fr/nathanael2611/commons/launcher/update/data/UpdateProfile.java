package fr.nathanael2611.commons.launcher.update.data;

import com.google.gson.reflect.TypeToken;
import fr.nathanael2611.commons.launcher.util.Helpers;
import fr.nathanael2611.commons.launcher.update.IgnoreList;
import fr.nathanael2611.commons.launcher.update.JsonUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UpdateProfile {

    private String serverUrl;
    private File directory;
    public List<DownloadableResource> filesToDownload;
    public List<SyncedDirectory> syncedDirectories;
    private IgnoreList ignoreList;

    public UpdateProfile(String serverUrl, File directory) {
        this.serverUrl = serverUrl;
        this.directory = directory;

        try {
            Type listType = new TypeToken<List<DownloadableResource>>() {}.getType();
            filesToDownload = JsonUtils.getGson().fromJson(Helpers.readJsonFromUrl(this.serverUrl + "index.php?action=listfiles&baselink=" + serverUrl), listType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        syncedDirectories = new ArrayList<>();

        filesToDownload.forEach(f ->
        {
            boolean isPathAlreadyContained = false;
            for(SyncedDirectory syncedDirectory : syncedDirectories)
            {
                if(syncedDirectory.getRelativePath().equals(f.getRelativePath()))
                {
                    isPathAlreadyContained = true;
                }
            }
            if(!isPathAlreadyContained) syncedDirectories.add(new SyncedDirectory(f.getRelativePath(), new ArrayList<>()));
        });

        syncedDirectories.forEach( d ->
        {
            filesToDownload.forEach(f ->
            {
                if(f.getRelativePath().equals(d.getRelativePath()))
                {
                    d.getFilesMd5().add(f.getMd5());
                }
            });
        });

        this.ignoreList = new IgnoreList(this.serverUrl + "index.php?action=ignorelist");

    }

    public IgnoreList getIgnoreList()
    {
        return ignoreList;
    }
}
