/*
 * Copyright 2015-2016 Adrien "Litarvan" Navratil
 *
 * This file is part of the OpenLauncherLib.

 * The OpenLauncherLib is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The OpenLauncherLib is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with the OpenLauncherLib.  If not, see <http://www.gnu.org/licenses/>.
 */
package fr.nathanael2611.commons.launcher.launch;

import fr.nathanael2611.commons.launcher.BaseLauncher;
import fr.nathanael2611.commons.launcher.update.minecraft.utils.FileUtils;
import fr.nathanael2611.commons.launcher.util.FileList;
import fr.nathanael2611.commons.launcher.util.Helpers;
import fr.nathanael2611.commons.launcher.util.JavaUtil;
import fr.nathanael2611.commons.launcher.util.MinecraftFolder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MinecraftLauncher
{

    private BaseLauncher launcher;

    private String mainClass;

    private String classPath;

    private List<String> vmArgs;

    private List<String> args;

    private boolean redirectErrorStream = true;

    private String macDockName;

    private File directory;


    public MinecraftLauncher(BaseLauncher launcher)
    {
        this.launcher = launcher;
        MinecraftFolder minecraftFolder = launcher.getMinecraftFolder();
        Helpers.sendMessageInConsole("Création d'un profil de lancement pour " + launcher.getMinecraftVersion());
        if(launcher.getForgeVersion() != null) Helpers.sendMessageInConsole("Version de forge définie: " + launcher.getForgeVersion());
        checkFolder(minecraftFolder);
        ClasspathConstructor constructor = new ClasspathConstructor();
        constructor.add(new FileList(FileUtils.listRecursive(minecraftFolder.getLibrariesFolder())).files().match("^(.*\\.((jar)$))*$").get());
        constructor.add(minecraftFolder.getClientJarFile());
        String mainClass = launcher.getForgeVersion() == null ? GameType.get(launcher.getMinecraftVersion()).getMainClass() : "net.minecraft.launchwrapper.Launch";
        String classpath = constructor.make();
        List<String> args = GameType.get(launcher.getMinecraftVersion()).getLaunchArgs(launcher);
        List<String> vmArgs = new ArrayList<String>();
        vmArgs.add("-Djava.library.path=" + minecraftFolder.getNativesFolder().getAbsolutePath());
        vmArgs.add("-Dfml.ignoreInvalidMinecraftCertificates=true");
        vmArgs.add("-Dfml.ignorePatchDiscrepancies=true");
        if(launcher.getForgeVersion() != null)
        {
            args.add("--tweakClass");
            if (GameType.get(launcher.getMinecraftVersion()).equals(GameType.V1_8_HIGHER))
                args.add("net.minecraftforge.fml.common.launcher.FMLTweaker");
            else
                args.add("cpw.mods.fml.common.launcher.FMLTweaker");
        }
        if(launcher.hasOptifine)
        {
            args.add("--tweakClass");
            args.add(GameTweak.OPTIFINE.getTweakClass(GameType.get(launcher.getMinecraftVersion())));
        }
        if(launcher.getMinecraftVersion().startsWith("1.14"))
        {
            args.add("--versionType");
            args.add("release");
        }
        this.mainClass = mainClass;
        this.classPath = classpath;
        this.vmArgs = vmArgs;
        this.args = args;
        this.redirectErrorStream = true;
        this.macDockName = launcher.getLauncherName();
        this.directory = launcher.minecraftDir;
        Helpers.sendMessageInConsole("Création du profil terminée");
    }

    public BaseLauncher getLauncher()
    {
        return launcher;
    }

    public String getMainClass()
    {
        return mainClass;
    }

    public void setMainClass(String mainClass)
    {
        this.mainClass = mainClass;
    }

    public String getClassPath()
    {
        return classPath;
    }

    public void setClassPath(String classPath)
    {
        this.classPath = classPath;
    }

    public List<String> getVmArgs()
    {
        return vmArgs;
    }

    public void setVmArgs(List<String> vmArgs)
    {
        this.vmArgs = vmArgs;
    }

    public List<String> getArgs()
    {
        return args;
    }

    public void setArgs(List<String> args)
    {
        this.args = args;
    }

    public boolean isRedirectErrorStream()
    {
        return redirectErrorStream;
    }

    public void setRedirectErrorStream(boolean redirectErrorStream)
    {
        this.redirectErrorStream = redirectErrorStream;
    }

    public String getMacDockName()
    {
        return macDockName;
    }

    public void setMacDockName(String macDockName)
    {
        this.macDockName = macDockName;
    }

    public File getDirectory()
    {
        return directory;
    }

    public void setDirectory(File directory)
    {
        this.directory = directory;
    }

    public static void checkFolder(MinecraftFolder folder)
    {
        File assetsFolder = folder.getAssetsFolder();
        File libsFolder = folder.getLibrariesFolder();
        File nativesFolder = folder.getNativesFolder();
        File minecraftJar = folder.getClientJarFile();
        if (!assetsFolder.exists() || assetsFolder.listFiles() == null) Helpers.crash("Missing/Empty assets folder !", "(" + assetsFolder.getAbsolutePath() + ")");
        else if (!libsFolder.exists() || libsFolder.listFiles() == null) Helpers.crash("Missing/Empty libraries folder !", "(" + libsFolder.getAbsolutePath() + ")");
        else if (!nativesFolder.exists() || nativesFolder.listFiles() == null) Helpers.crash("Missing/Empty natives folder !",  "(" + nativesFolder.getAbsolutePath() + ")");
        else if (!minecraftJar.exists()) Helpers.crash("Missing main jar !", "(" + minecraftJar.getAbsolutePath() + ")");
    }

    public Process launch() throws LaunchException
    {
        Helpers.sendMessageInConsole("Préparation au lancement de Minecraft...", false);
        ProcessBuilder builder = new ProcessBuilder();
        ArrayList<String> commands = new ArrayList<String>();
        commands.add(JavaUtil.getJavaCommand(getLauncher()));
        commands.addAll(Arrays.asList(JavaUtil.getSpecialArgs()));
        if (getVmArgs() != null) commands.addAll(getVmArgs());
        commands.add("-cp");
        commands.add(getClassPath());
        commands.add(getMainClass());
        if (getArgs() != null) commands.addAll(getArgs());
        if (getDirectory() != null) builder.directory(getDirectory());
        if (isRedirectErrorStream()) builder.redirectErrorStream(true);
        builder.command(commands);
        String entireCommand = "";
        for (String command : commands) entireCommand += command + " ";
        Helpers.sendMessageInConsole("Commande de lancement: " + entireCommand, false);
        Helpers.sendMessageInConsole("Lancement de: " + getMainClass(), false);
        try { return builder.start(); }
        catch (IOException e) { throw new LaunchException("Cannot launch !", e); }
    }

}