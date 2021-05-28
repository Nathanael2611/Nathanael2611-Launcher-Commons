package fr.nathanael2611.commons.launcher.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.io.*;
import java.lang.management.ManagementFactory;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Helpers
{
    public static String readJsonFromUrl(String url) throws IOException
    {
        URL urlObject;
        URLConnection uc;
        StringBuilder parsedContentFromUrl = new StringBuilder();
        urlObject = new URL(url);
        uc = urlObject.openConnection();
        uc.connect();
        uc = urlObject.openConnection();
        uc.addRequestProperty("User-Agent",
                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
        uc.getInputStream();
        InputStream is = uc.getInputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));

        int ch;
        while ((ch = in.read()) != -1) {
            parsedContentFromUrl.append((char) ch);
        }
        return parsedContentFromUrl.toString();
    }

    public static String md5File(File file)
    {
        try {
            Helpers.sendMessageInConsole("Getting md5 of " + file.getAbsolutePath());
            return DigestUtils.md5Hex(
                    FileUtils.readFileToByteArray(file)
            );
        } catch (IOException e) {
            return "";
        }
    }


    public static void crash(final String crashTitle, final String crashMessage) {
        JOptionPane.showMessageDialog(null, crashMessage, crashTitle, 0);
        System.exit(0);
    }

    public static void sendError(final String errorTitle, final String errorMessage)
    {
        JOptionPane.showMessageDialog(null, errorMessage, errorTitle, JOptionPane.ERROR_MESSAGE);
    }

    public static boolean isInteger(final String s) {
        boolean isValid = true;
        try {
            Integer.parseInt(s);
        }
        catch (NumberFormatException nfe) {
            isValid = false;
        }
        return isValid;
    }

    public static void sendMessageInConsole(final String message, final boolean isError)
    {
        PrintStream stream = System.out;
        if (isError)
        {
            stream = System.err;
        }
        stream.println("[" + new SimpleDateFormat("dd/MM/yyyy").format(new Date()) + "] [Nathanael2611-Commons-Launcher] " + message);
    }

    public static void sendMessageInConsole(final String message) {
        sendMessageInConsole(message, false);
    }

    public static int getTotalRamGB()
    {
        long memorySize = ((com.sun.management.OperatingSystemMXBean) ManagementFactory
                .getOperatingSystemMXBean()).getTotalPhysicalMemorySize();
        return (int) ((((memorySize) / 1024) / 1024) / 1024);
    }

}
