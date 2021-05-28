package fr.nathanael2611.commons.launcher.launch;

import fr.nathanael2611.commons.launcher.BaseLauncher;

import java.util.Arrays;

public class LaunchManager {

    private BaseLauncher launcher;

    private String serverIp = null;

    public LaunchManager(BaseLauncher launcher)
    {
        this.launcher = launcher;
    }

    public void enableAutoServerConnect(String serverIp)
    {
        this.serverIp = serverIp;
    }

    public void disabledAutoServerConnect()
    {
        this.serverIp = null;
    }

    public void launch()
    {
        MinecraftLauncher launcher = new MinecraftLauncher(this.launcher);

        if(this.serverIp != null)
        {
            String[] parts = this.serverIp.split(":");
            launcher.getArgs().add("--server=" + parts[0]);
            if(parts.length > 1)
            {
                launcher.getArgs().add("--port=" + parts[1]);
            } else {
                launcher.getArgs().add("--port=25565");
            }
        }

        final int minRam = 512;
        launcher.getVmArgs().addAll(Arrays.asList("-Xms" + minRam + "M", "-Xmx" + (int) (this.launcher.getLoginManager().getRam() * 1000) + "M"));
        try {
            launcher.launch();
        } catch (LaunchException e) {
            e.printStackTrace();
        }
        System.exit(0);

    }

}
