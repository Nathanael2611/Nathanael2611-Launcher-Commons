package fr.nathanael2611.commons.launcher.update.minecraft.versions;

import fr.nathanael2611.commons.launcher.update.minecraft.LightVersion;
import fr.nathanael2611.commons.launcher.update.minecraft.utils.OperatingSystem;

import java.io.File;
import java.util.*;

public class CompleteVersion extends LightVersion
{
    @SuppressWarnings("unused")
	private String inheritsFrom;
    private List<Library> libraries;
    private String mainClass;
    private String incompatibilityReason;
    private String assets;
    private String jar;
    @SuppressWarnings("unused")
	private CompleteVersion savableVersion;
    private transient boolean synced;
    private Downloads downloads;
    private AssetIndexInfo assetIndex;
    
    public CompleteVersion() {
        this.synced = false;
    }

    
    public String getID() {
        return this.getId();
    }

    
    public List<Library> getLibraries() {
        return this.libraries;
    }
    
    public String getMainClass() {
        return this.mainClass;
    }
    
    public String getJar() {
        return (this.jar == null) ? this.getId() : this.jar;
    }
    
    public Collection<Library> getRelevantLibraries() {
        final List<Library> result = new ArrayList<Library>();
        for (final Library library : this.libraries) {
            result.add(library);
        }
        return result;
    }
    
    public Collection<File> getClassPath(final OperatingSystem os, final File base) {
        final Collection<Library> libraries = this.getRelevantLibraries();
        final Collection<File> result = new ArrayList<File>();
        for (final Library library : libraries) {
            if (library.getNative() == null) {
                result.add(new File(base, "libraries/" + library.getArtifactPath()));
            }
        }
        result.add(new File(base, "versions/" + this.getJar() + "/" + this.getJar() + ".jar"));
        return result;
    }
    
    public Set<String> getRequiredFiles(final OperatingSystem os) {
        final Set<String> neededFiles = new HashSet<String>();
        for (final Library library : this.getRelevantLibraries()) {
            if (library.getNative() != null) {
                final String natives = library.getNative();
                if (natives == null) {
                    continue;
                }
                neededFiles.add("libraries/" + library.getArtifactPath(natives));
            }
            else {
                neededFiles.add("libraries/" + library.getArtifactPath());
            }
        }
        return neededFiles;
    }
    
    @Override
    public String toString() {
        return "CompleteVersion{id='" + this.getID() + '\'' + ", libraries=" + this.libraries + ", mainClass='" + this.mainClass + '\'' + ", jar='" + this.jar + '}';
    }
    
    public String getIncompatibilityReason() {
        return this.incompatibilityReason;
    }
    
    public boolean isSynced() {
        return this.synced;
    }
    
    public void setSynced(final boolean synced) {
        this.synced = synced;
    }
    
    public void parseDownloads() {
    }
    
    public AssetIndexInfo getAssetIndex() {
        if (this.assetIndex == null) {
            if (this.assets == null) {
                this.assetIndex = new AssetIndexInfo("legacy");
            }
            else {
                this.assetIndex = new AssetIndexInfo(this.assets);
            }
        }
        return this.assetIndex;
    }
    
    public Downloads getDownloads() {
        return this.downloads;
    }
}
