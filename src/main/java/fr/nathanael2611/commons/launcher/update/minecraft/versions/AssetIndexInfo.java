package fr.nathanael2611.commons.launcher.update.minecraft.versions;

import java.net.MalformedURLException;
import java.net.URL;

public class AssetIndexInfo
{
    private URL url;
    protected long totalSize;
    protected String id;
    protected boolean known;
    
    public AssetIndexInfo() {
        this.known = true;
    }
    
    public AssetIndexInfo(final String id) {
        this.known = true;
        this.id = id;
        try {
            this.url = new URL("https://s3.amazonaws.com/Minecraft.Download/indexes/" + id + ".json");
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        this.known = false;
    }
    
    public long getTotalSize() {
        return this.totalSize;
    }
    
    public String getId() {
        return this.id;
    }
    
    public boolean sizeAndHashKnown() {
        return this.known;
    }
    
    public URL getURL() {
        return this.url;
    }
}
