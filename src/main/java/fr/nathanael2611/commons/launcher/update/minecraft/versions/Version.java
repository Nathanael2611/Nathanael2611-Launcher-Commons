package fr.nathanael2611.commons.launcher.update.minecraft.versions;

import fr.nathanael2611.commons.launcher.BaseLauncher;
import fr.nathanael2611.commons.launcher.update.minecraft.Game;

import java.net.URL;
import java.util.Date;

public class Version
{
    private String id;
    private Date time;
    private Date releaseTime;
    private URL url;
    private fr.nathanael2611.commons.launcher.update.minecraft.Version.VersionType type;
    
    public String getId() {
        return this.id;
    }
    
    public Date getUpdatedTime() {
        return this.time;
    }
    
    public void setUpdatedTime(final Date time) {
        this.time = time;
    }
    
    public Date getReleaseTime() {
        return this.releaseTime;
    }
    
    public void setReleaseTime(final Date time) {
        this.releaseTime = time;
    }
    
    public URL getUrl() {
        return this.url;
    }
    
    public void setUrl(final URL url) {
        this.url = url;
    }
    
    public void setID(final String id) {
        this.id = id;
    }
    
    public fr.nathanael2611.commons.launcher.update.minecraft.Version.VersionType getType() {
        return this.type;
    }
    
    public void setType(final fr.nathanael2611.commons.launcher.update.minecraft.Version.VersionType type) {
        this.type = type;
    }


    public Game update(BaseLauncher launcher){
        return null;
    }
}
