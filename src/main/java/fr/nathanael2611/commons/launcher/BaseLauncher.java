package fr.nathanael2611.commons.launcher;

import fr.nathanael2611.commons.launcher.launch.LaunchManager;
import fr.nathanael2611.commons.launcher.login.LoginManager;
import fr.nathanael2611.commons.launcher.update.UpdateManager;
import fr.nathanael2611.commons.launcher.util.MinecraftFolder;

import java.io.File;

public class BaseLauncher
{

    public final File LAUNCHER_DIR;
    public File minecraftDir;
    public final File USER_INFOS;
    public final File CUSTOM_JRE_DIR;
    public final File CUSTOM_JRE_ZIP;

    private String minecraftVersion = null;
    private String forgeVersion = null;

    private boolean isMCP = false;

    private String launcherName;

    private UpdateManager updateManager;
    private LoginManager loginManager;
    private LaunchManager launchManager;

    public boolean hasOptifine = false;

    public BaseLauncher(String launcherName)
    {
        {
            final String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win"))      this.LAUNCHER_DIR = new File(System.getenv("APPDATA"), "." + launcherName);
            else if (os.contains("mac")) this.LAUNCHER_DIR = new File(System.getenv("user.home") + "/Library/Application Support/", "." + launcherName);
            else                         this.LAUNCHER_DIR = new File(System.getenv("user.home"), "." + launcherName);
            this.minecraftDir = new File(this.LAUNCHER_DIR, "minecraft/");
            this.USER_INFOS = new File(this.LAUNCHER_DIR, "infos.json");
            this.CUSTOM_JRE_DIR = new File(this.LAUNCHER_DIR, "/jre-x64/");
            this.CUSTOM_JRE_ZIP = new File(this.CUSTOM_JRE_DIR, "jre-x64.zip");
        }
    }

    public BaseLauncher complete()
    {
        this.loginManager = new LoginManager(this);
        this.updateManager = new UpdateManager(this);
        this.launchManager = new LaunchManager(this);
        return this;
    }

    public BaseLauncher setVersion(String minecraftVersion, String forgeVersion)
    {
        this.minecraftVersion = minecraftVersion;
        this.forgeVersion = forgeVersion;
        return this;
    }

    public BaseLauncher setVersion(String minecraftVersion, boolean downloadMC)
    {
        this.setVersion(minecraftVersion, null);
        this.isMCP = true;
        return this;
    }

    public UpdateManager getUpdateManager()
    {
        return updateManager;
    }

    public MinecraftFolder getMinecraftFolder() {
        return new MinecraftFolder(
                minecraftDir,
                new File(minecraftDir, "/assets/"),
                new File(minecraftDir, "/libraries/"),
                new File(minecraftDir, "/natives/"),
                new File(minecraftDir, "minecraft.jar"),
                null, null
        );
    }

    public void setMinecraftFolder()
    {

    }

    public String getForgeVersion()
    {
        return forgeVersion;
    }

    public String getMinecraftVersion()
    {
        return minecraftVersion;
    }

    public String getLauncherName()
    {
        return launcherName;
    }

    public LoginManager getLoginManager()
    {
        return loginManager;
    }

    public LaunchManager getLaunchManager()
    {
        return launchManager;
    }

    public boolean isMCP()
    {
        return isMCP;
    }
}
