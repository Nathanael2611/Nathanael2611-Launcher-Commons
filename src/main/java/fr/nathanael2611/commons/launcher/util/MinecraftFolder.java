package fr.nathanael2611.commons.launcher.util;

import java.io.File;
import java.net.URL;


public class MinecraftFolder {
	
    private File gameFolder;
    private File librariesFolder;
    private File nativesFolder;
    private File assetsFolder;
    private File clientJarFile;
    private String clientJarSHA1;
    private URL clientJarURL;

    public MinecraftFolder(File gameFolder, File assetsFolder, File librariesFolder, File nativesFolder, File clientJarFile, URL clientJarURL, String clientJarSHA1) {
        this.gameFolder = gameFolder;
        this.librariesFolder = librariesFolder;
        this.nativesFolder = nativesFolder;
        this.assetsFolder = assetsFolder;
        this.clientJarFile = clientJarFile;
        this.clientJarURL = clientJarURL;
        this.clientJarSHA1 = clientJarSHA1;
    }

    public File getGameFolder() {return this.gameFolder;}
    public File getLibrariesFolder() {return this.librariesFolder;}
    public File getNativesFolder() {return this.nativesFolder;}
    public File getAssetsFolder() {return this.assetsFolder;}
    public File getClientJarFile() {return this.clientJarFile;}
    public void setClientJarSHA1(String clientJarSHA1) {this.clientJarSHA1 = clientJarSHA1;}
    public URL getClientJarURL() {return this.clientJarURL;}
    public void setClientJarURL(URL clientJarURL) {this.clientJarURL = clientJarURL;}
}

