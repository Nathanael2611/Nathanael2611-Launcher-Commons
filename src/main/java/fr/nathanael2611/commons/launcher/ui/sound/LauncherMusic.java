package fr.nathanael2611.commons.launcher.ui.sound;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;

public class LauncherMusic
{

    public static void read(URL url)
    {
        LauncherMusic.url = url;
        LauncherMusic.clip = Applet.newAudioClip(url);
        LauncherMusic.clip.loop();
    }

    private static URL url;
    private static AudioClip clip;

}
