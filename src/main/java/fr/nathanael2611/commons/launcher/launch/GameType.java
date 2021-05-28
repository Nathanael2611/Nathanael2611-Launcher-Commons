/*
 * Copyright 2015-2017 Adrien "Litarvan" Navratil
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
import fr.nathanael2611.commons.launcher.login.LoginManager;

import java.io.File;
import java.util.ArrayList;

/**
 * The Game Type
 *
 * <p>
 *     This class contains the specifics informations about a version
 *     or a group of verison of Minecraft.
 *
 *     It contains its main class, and its arguments.
 * </p>
 *
 * @author Litarvan
 * @version 3.0.4
 * @since 2.0.0-SNAPSHOT
 */
public abstract class GameType
{

    public static GameType get(String version)
    {
        if(version.equalsIgnoreCase("1.7.10"))
        {
            return GameType.V1_7_10;
        }
        return GameType.V1_8_HIGHER;
    }


    /**
     * The 1.7.10 Game Type
     */
    public static final GameType V1_7_10 = new GameType()
    {
        @Override
        public String getName()
        {
            return "1.7.10";
        }

        @Override
        public String getMainClass()
        {
            return "net.minecraft.client.main.Main";
        }

        @Override
        public ArrayList<String> getLaunchArgs(BaseLauncher launcher)
        {
            ArrayList<String> arguments = new ArrayList<String>();

            LoginManager manager = launcher.getLoginManager();

            arguments.add("--username=" + manager.getAuthResponse().getSelectedProfile().getName());

            arguments.add("--accessToken");
            arguments.add(manager.getAuthResponse().getAccessToken());

            if (manager.getAuthResponse().getClientToken() != null)
            {
                arguments.add("--clientToken");
                arguments.add(manager.getAuthResponse().getClientToken());
            }

            arguments.add("--version");
            arguments.add(launcher.getMinecraftVersion());

            arguments.add("--gameDir");
            arguments.add(launcher.minecraftDir.getAbsolutePath());

            arguments.add("--assetsDir");
            File assetsDir = launcher.getMinecraftFolder().getAssetsFolder();
            arguments.add(assetsDir.getAbsolutePath());

            arguments.add("--assetIndex");
            arguments.add("1.7.10");

            arguments.add("--userProperties");
            arguments.add("{}");

            arguments.add("--uuid");
            arguments.add(manager.getAuthResponse().getSelectedProfile().getId());

            arguments.add("--userType");
            arguments.add("legacy");

            return arguments;
        }
    };

    /**
     * The 1.8 or higher Game Type
     */
    public static final GameType V1_8_HIGHER = new GameType()
    {
        @Override
        public String getName()
        {
            return "1.8 or higher";
        }

        @Override
        public String getMainClass()
        {
            return "net.minecraft.client.main.Main";
        }

        @Override
        public ArrayList<String> getLaunchArgs(BaseLauncher launcher)
        {
            ArrayList<String> arguments = new ArrayList<String>();

            LoginManager manager = launcher.getLoginManager();

            arguments.add("--username=" + manager.getAuthResponse().getSelectedProfile().getName());

            arguments.add("--accessToken");
            arguments.add(manager.getAuthResponse().getAccessToken());

            if (manager.getAuthResponse().getClientToken() != null)
            {
                arguments.add("--clientToken");
                arguments.add(manager.getAuthResponse().getClientToken());
            }

            arguments.add("--version");
            arguments.add(launcher.getMinecraftVersion());

            arguments.add("--gameDir");
            arguments.add(launcher.minecraftDir.getAbsolutePath());

            arguments.add("--assetsDir");
            File assetsDir = launcher.getMinecraftFolder().getAssetsFolder();
            arguments.add(assetsDir.getAbsolutePath());

            arguments.add("--assetIndex");

            String version = launcher.getMinecraftVersion();

            int first = version.indexOf('.');
            int second = version.lastIndexOf('.');

            if (first != second)
            {
                version = version.substring(0, version.lastIndexOf('.'));
            }

            if (launcher.getMinecraftVersion().equals("1.13.1") || launcher.getMinecraftVersion().equals("1.13.2"))
                version = "1.13.1";

            arguments.add(version);

            arguments.add("--userProperties");
            arguments.add("{}");

            arguments.add("--uuid");
            arguments.add(manager.getAuthResponse().getSelectedProfile().getId());

            arguments.add("--userType");
            arguments.add("legacy");

            return arguments;
        }
    };


    public abstract String getName();

    public abstract ArrayList<String> getLaunchArgs(BaseLauncher launcher);

    public abstract String getMainClass();
}
