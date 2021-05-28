package fr.nathanael2611.commons.launcher.update.minecraft;

import fr.nathanael2611.commons.launcher.BaseLauncher;
import fr.nathanael2611.commons.launcher.update.UpdateManager;
import fr.nathanael2611.commons.launcher.update.minecraft.versions.CompleteVersion;
import fr.nathanael2611.commons.launcher.update.minecraft.versions.ForgeVersion;

import java.net.URL;

public class LightVersion extends fr.nathanael2611.commons.launcher.update.minecraft.versions.Version {
    private CompleteVersion completeVersion;
    private ForgeVersion forgeVersion;

    public void setId(String id)
    {
        super.setID(id);
    }

    public void setUrl(URL url)
    {
        super.setUrl(url);
    }

    public void setType(fr.nathanael2611.commons.launcher.update.minecraft.Version.VersionType type)
    {
        super.setType(type);
    }

    public void setForgeVersion(ForgeVersion forgeVersion)
    {
        this.forgeVersion = forgeVersion;
    }

    public ForgeVersion getForgeVersion()
    {
        return forgeVersion;
    }

    @Override
    public URL getUrl() {
        return super.getUrl();
    }

    public String getId() {
        return super.getId();
    }

    public fr.nathanael2611.commons.launcher.update.minecraft.Version.VersionType getVersionType() {
        return super.getType();
    }

    public CompleteVersion getCompleteVersion() {
        return completeVersion;
    }

    public void setCompleteVersion(CompleteVersion completeVersion) {
        this.completeVersion = completeVersion;
    }


    @Override
    public Game update(BaseLauncher launcher)
    {
        UpdateManager manager = launcher.getUpdateManager();
        manager.setMinecraftVersionDownloader(new VersionDownloader(launcher));
        Game game  = manager.getMinecraftVersionDownloader().downloadVersion(launcher, this, this.getCompleteVersion());
        manager.getMinecraftVersionDownloader().getDownloadManager().check();
        manager.getMinecraftVersionDownloader().getDownloadManager().startDownload();
        manager.getMinecraftVersionDownloader().unzipNatives();
        manager.getMinecraftVersionDownloader().getDownloadManager().check();

        return game;
    }


}
