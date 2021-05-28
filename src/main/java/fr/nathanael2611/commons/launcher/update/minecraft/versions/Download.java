package fr.nathanael2611.commons.launcher.update.minecraft.versions;

public class Download
{
    private String sha1;
    private String url;
    private String size;
    
    public Download(final String sha1, final String url, final String size) {
        this.sha1 = sha1;
        this.url = url;
        this.size = size;
    }
    
    public String getArtifactURL(final String name) {
        if (name == null) {
            throw new IllegalStateException("Cannot get artifact dir of empty/blank artifact");
        }
        final String[] parts = name.split(":", 4);
        final String text = String.format("%s/%s/%s", parts[0].replaceAll("\\.", "/"), parts[1], parts[2]);
        if (parts.length == 4) {
            return String.valueOf(text) + "/" + parts[1] + "-" + parts[2] + "-" + parts[3] + ".jar";
        }
        return String.valueOf(text) + "/" + parts[1] + "-" + parts[2] + ".jar";
    }
    
    public String getSha1() {
        return this.sha1;
    }
    
    public void setSha1(final String sha1) {
        this.sha1 = sha1;
    }
    
    public String getUrl() {
        return this.url;
    }
    
    public void setUrl(final String url) {
        this.url = url;
    }
    
    public String getSize() {
        return this.size;
    }
    
    public void setSize(final String size) {
        this.size = size;
    }
}
