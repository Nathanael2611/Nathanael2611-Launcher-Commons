package fr.nathanael2611.commons.launcher.update.minecraft.versions;

import fr.nathanael2611.commons.launcher.update.minecraft.utils.OperatingSystem;
import org.apache.commons.lang3.text.StrSubstitutor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({ "unchecked", "deprecation", "serial", "rawtypes" })
public class Library{
	private static final StrSubstitutor SUBSTITUTOR;
    private String name;
    private List<CompatibilityRule> rules;
    private Natives natives;
    private ExtractRules extract;
    public Downloads downloads;
    public String url;
    private String sha1;
    private Downloads.Classifier classifiers;
    
    static {
        SUBSTITUTOR = new StrSubstitutor((Map)new HashMap() {});
    }
    
    public Library() {
    }
    
    public Library(final String name) {
        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException("Library name cannot be null or empty");
        }
        this.name = name;
    }
    
    public Library(final Library library) {
        this.name = library.name;
        if (library.extract != null) {
            this.extract = new ExtractRules(library.extract);
        }
        if (library.rules != null) {
            this.rules = new ArrayList<CompatibilityRule>();
            for (final CompatibilityRule compatibilityRule : library.rules) {
                this.rules.add(new CompatibilityRule(compatibilityRule));
            }
        }
        this.natives = library.natives;
    }
    
    public String getName() {
        return this.name;
    }
    
    public List<CompatibilityRule> getCompatibilityRules() {
        return this.rules;
    }
    
    public boolean appliesToCurrentEnvironment(final CompatibilityRule.FeatureMatcher featureMatcher) {
        if (this.rules == null) {
            return true;
        }
        CompatibilityRule.Action lastAction = CompatibilityRule.Action.DISALLOW;
        for (final CompatibilityRule compatibilityRule : this.rules) {
            final CompatibilityRule.Action action = compatibilityRule.getAppliedAction(featureMatcher);
            if (action != null) {
                lastAction = action;
            }
        }
        return lastAction == CompatibilityRule.Action.ALLOW;
    }
    
    public String getNative() {
        final OperatingSystem system = OperatingSystem.getCurrentPlatform();
        if (this.natives == null) {
            return null;
        }
        if (this.natives.getWindows() != null && system == OperatingSystem.WINDOWS) {
            return "natives-windows";
        }
        if (this.natives.getLinux() != null && system == OperatingSystem.LINUX) {
            return "natives-linux";
        }
        if (this.natives.getOsx() != null && system == OperatingSystem.OSX) {
            return "natives-osx";
        }
        return null;
    }
    
    public ExtractRules getExtractRules() {
        return this.extract;
    }
    
    public Library setExtractRules(final ExtractRules rules) {
        this.extract = rules;
        return this;
    }
    
    public String getArtifactBaseDir() {
        if (this.name == null) {
            throw new IllegalStateException("Cannot get artifact dir of empty/blank artifact");
        }
        final String[] parts = this.name.split(":", 3);
        return String.format("%s/%s/%s", parts[0].replaceAll("\\.", "/"), parts[1], parts[2]);
    }
    
    public String getArtifactURL() {
        if (this.name == null) {
            throw new IllegalStateException("Cannot get artifact dir of empty/blank artifact");
        }
        final String[] parts = this.name.split(":", 4);
        final String text = String.format("%s/%s/%s", parts[0].replaceAll("\\.", "/"), parts[1], parts[2]);
        if (parts.length == 4) {
            return String.valueOf(text) + "/" + parts[1] + "-" + parts[2] + "-" + parts[3] + ".jar";
        }
        return String.valueOf(text) + "/" + parts[1] + "-" + parts[2] + ".jar";
    }
    
    public String getArtifactPath() {
        return this.getArtifactPath(null);
    }
    
    public String getArtifactPath(final String classifier) {
        if (this.name == null) {
            throw new IllegalStateException("Cannot get artifact path of empty/blank artifact");
        }
        return String.format("%s/%s", this.getArtifactBaseDir(), this.getArtifactFilename(classifier));
    }
    
    public String getArtifactFilename(final String classifier) {
        if (this.name == null) {
            throw new IllegalStateException("Cannot get artifact filename of empty/blank artifact");
        }
        final String[] parts = this.name.split(":", 3);
        String result = null;
        if (classifier != null) {
            result = String.format("%s-%s%s.jar", parts[1], parts[2], "-" + classifier);
        }
        else {
            result = String.format("%s-%s.jar", parts[1], parts[2]);
        }
        return Library.SUBSTITUTOR.replace(result);
    }
    
    public Downloads getDownloads() {
        return this.downloads;
    }
    
    public String getURL() {
        return (this.url != null) ? (String.valueOf(this.url) + this.getArtifactURL()) : ("https://libraries.minecraft.net/" + this.getArtifactPath());
    }
    
    public String getSHA1() {
        return this.sha1;
    }
    
    public Downloads.Classifier getClassifiers() {
        return this.classifiers;
    }
    
    public class Natives
    {
        private String osx;
        private String windows;
        private String linux;
        
        public String getOsx() {
            return this.osx;
        }
        
        public void setOsx(final String osx) {
            this.osx = osx;
        }
        
        public String getWindows() {
            return this.windows;
        }
        
        public void setWindows(final String windows) {
            this.windows = windows;
        }
        
        public String getLinux() {
            return this.linux;
        }
        
        public void setLinux(final String linux) {
            this.linux = linux;
        }
    }
}
