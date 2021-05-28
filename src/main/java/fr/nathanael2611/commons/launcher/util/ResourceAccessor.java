package fr.nathanael2611.commons.launcher.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

public class ResourceAccessor
{

    private static final HashMap<String, BufferedImage> IMAGE_CACHE = new HashMap<>();

    private static String resourcePath = "";

    public static URL getResource(String path)
    {
        return ResourceAccessor.class.getResource(resourcePath + path);
    }

    public static BufferedImage getImage(String path, boolean reload)
    {
        try {
            if(reload || !IMAGE_CACHE.containsKey(path))
            {
                BufferedImage img = ImageIO.read(getResource(path));
                IMAGE_CACHE.put(path, img);
                return img;
            } else return IMAGE_CACHE.get(path);
        } catch (IOException e) {
            return null;
        }
    }

    public static BufferedImage getImage(String path)
    {
        return getImage(path, false);
    }

    public static BufferedImage getImageFromWeb(String url, boolean reload)
    {
        try {
            if (reload || !IMAGE_CACHE.containsKey(url))
            {
                BufferedImage img = ImageIO.read(new URL(url));
                IMAGE_CACHE.put(url, img);
                return img;
            } else return IMAGE_CACHE.get(url);
        } catch (IOException ex)
        {
            return null;
        }
    }

    public static BufferedImage getImageFromWeb(String url)
    {
        return getImageFromWeb(url, false);
    }

    public static void setResourcePath(String resourcePath)
    {
        ResourceAccessor.resourcePath = resourcePath;
    }
}
