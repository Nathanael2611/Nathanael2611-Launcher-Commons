package fr.nathanael2611.commons.launcher.update.minecraft;

import com.google.gson.JsonSyntaxException;
import fr.nathanael2611.commons.launcher.BaseLauncher;
import fr.nathanael2611.commons.launcher.launch.GameType;
import fr.nathanael2611.commons.launcher.update.minecraft.utils.HttpUtils;
import fr.nathanael2611.commons.launcher.update.minecraft.utils.JsonManager;
import fr.nathanael2611.commons.launcher.update.minecraft.versions.CompleteVersion;
import fr.nathanael2611.commons.launcher.update.minecraft.versions.VersionsLoader;

import java.io.IOException;
import java.net.Proxy;
import java.net.URL;

public interface Version {

    String getMCVersion();
    Game update();
    URL getURL();
    CompleteVersion getCompleteVersion();


    public static Version build(BaseLauncher launcher)
    {
        try {
            VersionsLoader versionsLoader = new VersionsLoader();

            versionsLoader.loadOfficialVersions();

            LightVersion version = (LightVersion)versionsLoader.getVersion(launcher.getMinecraftVersion());



            versionsLoader.loadAllForgeVersion();

            if(launcher.getForgeVersion() != null && versionsLoader.containsForgeVersion(launcher.getForgeVersion()))
            {
                version.setForgeVersion( versionsLoader.getLoadedForgeVersion(launcher.getForgeVersion()));
            }

            CompleteVersion complete = null;
            //TODO
            try {
                complete = (CompleteVersion) JsonManager.getGson().fromJson(HttpUtils.performGet(version.getUrl(), Proxy.NO_PROXY), (Class) CompleteVersion.class);
            } catch (JsonSyntaxException | IOException ex2) {
                ex2.printStackTrace();
            }
            version.setCompleteVersion(complete);


            CompleteVersion finalComplete = complete;
            return new Version() {

                @Override
                public String getMCVersion() {
                    return launcher.getMinecraftVersion();
                }

                @Override
                public Game update() {
                    return version.update(launcher);
                }

                @Override
                public URL getURL() {
                    return version.getUrl();
                }

                @Override
                public CompleteVersion getCompleteVersion() {
                    return finalComplete;
                }
            };
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    enum VersionType{
        VANILLA,
        FORGE,
    }

}
