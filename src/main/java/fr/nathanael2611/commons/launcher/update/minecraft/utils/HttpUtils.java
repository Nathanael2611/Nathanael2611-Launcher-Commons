package fr.nathanael2611.commons.launcher.update.minecraft.utils;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;

public class HttpUtils{
	
    @SuppressWarnings("deprecation")
	public static String performGet(final URL url, final Proxy proxy) throws IOException {
        final HttpURLConnection connection = (HttpURLConnection)url.openConnection(proxy);
        connection.setConnectTimeout(15000);
        connection.setReadTimeout(60000);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT x.y; rv:10.0) Gecko/20100101 Firefox/10.0");
        final InputStream inputStream = connection.getInputStream();
        try {
            return IOUtils.toString(inputStream);
        }
        finally {
            IOUtils.closeQuietly(inputStream);
        }
    }
}
