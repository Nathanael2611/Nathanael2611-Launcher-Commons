package fr.nathanael2611.commons.launcher.update.minecraft;


import fr.nathanael2611.commons.launcher.util.MinecraftFolder;

public class Game
{
    private String version;
    private String tweaker;
    private String mainClass;
    private String assetIndex;
    private MinecraftFolder folder;
    private Version.VersionType versionType;
    
    public Game(final String version, final String tweaker, final String mainClass, final String assetIndex, final MinecraftFolder folder, final Version.VersionType versionType) {
        this.version = version;
        this.tweaker = tweaker;
        this.mainClass = mainClass;
        this.assetIndex = assetIndex;
        this.folder = folder;
        this.versionType = versionType;
    }
    
    public String getVersion() {
        return this.version;
    }
    
    public void setVersion(final String version) {
        this.version = version;
    }
    
    public String getTweaker() {
        return this.tweaker;
    }
    
    public void setTweaker(final String tweaker) {
        this.tweaker = tweaker;
    }
    
    public void setMainClass(final String mainClass) {
        this.mainClass = mainClass;
    }
    
    public String getAssetIndex() {
        return this.assetIndex;
    }
    
    public void setAssetIndex(final String assetIndex) {
        this.assetIndex = assetIndex;
    }
    
    public MinecraftFolder getFolder() {
        return this.folder;
    }
    
    public void setFolder(final MinecraftFolder folder) {
        this.folder = folder;
    }
    
    public Version.VersionType getVersionType() {
        System.out.println("Get version type: " + this.versionType);
        return this.versionType;
    }
    
    public void setVersionType(final Version.VersionType versionType) {
        this.versionType = versionType;
    }


}
