package fr.nathanael2611.commons.launcher.update;

import fr.nathanael2611.commons.launcher.BaseLauncher;
import fr.nathanael2611.commons.launcher.update.minecraft.Game;
import fr.nathanael2611.commons.launcher.update.minecraft.Version;
import fr.nathanael2611.commons.launcher.update.minecraft.VersionDownloader;
import fr.nathanael2611.commons.launcher.util.Helpers;
import fr.nathanael2611.commons.launcher.util.JavaUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class UpdateManager
{

    private BaseLauncher launcher;

    private String updateMessage;

    private String jreLink = null;
    private String jreMd5 = null;

    private Game game;

    private VersionDownloader minecraftVersionDownloader;

    private ConcurrentLinkedQueue<EazyUpdater> updaters = new ConcurrentLinkedQueue<>();

    private Version version = null;

    private boolean isDownloading = false;
    private boolean isDownloadingMc = false;

    public UpdateManager(BaseLauncher launcher)
    {
        this.launcher = launcher;
    }

    public String getUpdateMessage()
    {
        return updateMessage;
    }

    public void setUpdateMessage(String updateMessage)
    {
        this.updateMessage = updateMessage;
    }

    public void enableCustomJre(String downloadLink, String md5)
    {
        this.jreLink = downloadLink;
        this.jreMd5 = md5;
    }

    public boolean hasCustomJre()
    {
        return this.jreLink != null && this.jreMd5 != null;
    }

    public void setMinecraftVersionDownloader(VersionDownloader minecraftVersionDownloader)
    {
        this.minecraftVersionDownloader = minecraftVersionDownloader;
    }

    public void setGame(Game game)
    {
        this.game = game;
    }

    public Game getGame()
    {
        return game;
    }

    public VersionDownloader getMinecraftVersionDownloader()
    {
        return minecraftVersionDownloader;
    }

    public Version getVersionObj()
    {
        if (this.version == null)
        {
            Helpers.sendMessageInConsole("Getting the version...", false);
            this.version = Version.build(this.launcher);
        }
        return this.version;
    }

    public void updateAll()
    {
        this.isDownloading = true;
        downloadJava();
        if(!this.launcher.isMCP()) downloadMc();
        this.updaters.forEach(updater ->
        {
            updater.startDownload(this);
            this.updaters.remove();
        });
        this.isDownloading = false;
    }

    public void downloadMc()
    {
        isDownloadingMc = true;
        updateMessage = "Verifying assets...";
        Version version = this.getVersionObj();
        updateMessage = "Downloading Minecraft...";
        this.setGame(version.update());
        isDownloadingMc = false;
    }

    public void downloadJava()
    {

        updateMessage = "Java verification";
        Helpers.sendMessageInConsole("Vérification de votre version de Java...");
        if(JavaUtil.hasJava64())
        {
            Helpers.sendMessageInConsole("Vous avez Java 64 ! Tout est bon. ;)");
            return;
        }
        else if(!hasCustomJre())
        {
            Helpers.crash("Vous n'avez pas Java 64 bit !", "Désolé, pour jouer à Minecraft il faut avoir Java \n 64 bit.");
        }


        this.launcher.CUSTOM_JRE_DIR.mkdir();

        try
        {
            if (this.launcher.CUSTOM_JRE_ZIP.exists())
            {
                String md5 = DigestUtils.md5Hex(FileUtils.readFileToByteArray(this.launcher.CUSTOM_JRE_ZIP));
                if (!md5.equalsIgnoreCase(this.jreMd5))
                {
                    Helpers.sendMessageInConsole("Mauvaise version du JRE... Suppression du Custom-JRE actuel...", true);
                    FileUtils.forceDelete(this.launcher.CUSTOM_JRE_ZIP);
                    FileUtils.deleteDirectory(this.launcher.CUSTOM_JRE_DIR);
                } else {
                    Helpers.sendMessageInConsole("Vous avez la bonne version, \"fullgood\"");
                    return;
                }
            }
        } catch (IOException ex){
            ex.printStackTrace();
        }

        updateMessage = "Downloading Java";
        try
        {
            Helpers.sendMessageInConsole("Téléchargement de Java 64...");
            FileUtils.copyURLToFile(new URL(this.jreLink), this.launcher.CUSTOM_JRE_ZIP);
        } catch (IOException e) {
            e.printStackTrace();
        }
        updateMessage = "Unzipping JRE...";
        Helpers.sendMessageInConsole("Unzipping JRE..");
        try {
            fr.nathanael2611.commons.launcher.update.minecraft.utils.FileUtils.unzip(this.launcher.CUSTOM_JRE_ZIP, this.launcher.CUSTOM_JRE_DIR);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isDownloadingMc()
    {
        return isDownloadingMc;
    }

    public boolean isDownloading() {
        return isDownloading;
    }

    public int getTotalFiles()
    {
        if(this.isDownloadingMc && this.minecraftVersionDownloader != null) return this.minecraftVersionDownloader.getDownloadManager().filesToDownload.size();
        return this.getActualUpdater() != null ? this.getActualUpdater().getTotalFilesToDownload() : 0;
    }

    public int getDownloadedFiles()
    {
        if(this.isDownloadingMc && this.minecraftVersionDownloader != null) return this.minecraftVersionDownloader.getDownloadManager().downloadedFile.size();
        return this.getActualUpdater() != null ? this.getActualUpdater().getDownloadedFiles() : 0;
    }

    public EazyUpdater getActualUpdater()
    {
        return this.updaters.peek();
    }

    public ConcurrentLinkedQueue<EazyUpdater> getUpdaters()
    {
        return updaters;
    }
}
