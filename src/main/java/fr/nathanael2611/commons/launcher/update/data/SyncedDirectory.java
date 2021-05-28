package fr.nathanael2611.commons.launcher.update.data;

import fr.nathanael2611.commons.launcher.update.EazyUpdater;
import fr.nathanael2611.commons.launcher.util.Helpers;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class SyncedDirectory {

    private String relativePath;
    private List<String> filesMd5;

    public SyncedDirectory(String relativePath, List<String> filesMd5)
    {
        this.relativePath = relativePath;
        this.filesMd5 = filesMd5;
    }

    public String getRelativePath()
    {
        return relativePath;
    }

    public List<String> getFilesMd5()
    {
        return filesMd5;
    }

    public void removeSurplus(File baseDir, EazyUpdater updater)
    {
        File dir = new File(baseDir, relativePath);
        if(dir.listFiles() != null) {
            for (File f : Objects.requireNonNull(dir.listFiles(), "Pas fou")) {
                if(f.isFile())
                {
                    if (!filesMd5.contains(Helpers.md5File(f))) {
                        if(!updater.getProfile().getIgnoreList().contains(f.getAbsolutePath(), updater)) {
                            try {
                                Helpers.sendMessageInConsole("Deleting " + f.getAbsolutePath());
                                FileUtils.forceDelete(f);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

}
