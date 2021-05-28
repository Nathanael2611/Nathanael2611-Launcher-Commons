package fr.nathanael2611.commons.launcher.update;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.nathanael2611.commons.launcher.util.Helpers;

import java.io.IOException;
import java.util.HashMap;

public class IgnoreList
{

    private HashMap<String, String> paths = new HashMap<>();

    public IgnoreList(String toReadURL)
    {
        String content = null;
        try {
            content = Helpers.readJsonFromUrl(toReadURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JsonArray array = new JsonParser().parse(content).getAsJsonArray();
        array.forEach(jsonElement -> {
            JsonObject object = jsonElement.getAsJsonObject();
            paths.put(object.get("relativePath").getAsString(), object.get("type").getAsString());
        });
    }

    public boolean contains(String absolutePath, EazyUpdater updater)
    {
        String relativeLocalPath = absolutePath.replace("\\", "/").substring(updater.getDirectory().getAbsolutePath().length());
        for (String s : paths.keySet())
        {
            if(relativeLocalPath.startsWith(s)) return true;
        }
        return false;
    }

}
