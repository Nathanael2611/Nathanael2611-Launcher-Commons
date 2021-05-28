package fr.nathanael2611.commons.launcher;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import java.io.IOException;
import java.util.HashMap;

import static fr.nathanael2611.commons.launcher.util.Helpers.readJsonFromUrl;

public class LauncherConfig
{

    private final HashMap<String, JsonElement> ELEMENTS = new HashMap<>();

    public void read(String url)
    {
        ELEMENTS.clear();
        try {
            String json = readJsonFromUrl(url);
            new JsonParser().parse(json).getAsJsonObject().entrySet().forEach(entry -> ELEMENTS.put(entry.getKey(), entry.getValue()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JsonElement get(String key, JsonElement defaultValue)
    {
        return ELEMENTS.getOrDefault(key, defaultValue);
    }

    public String getString(String key)
    {
        return get(key, new JsonPrimitive("")).getAsString();
    }

    public boolean getBoolean(String key)
    {
        return get(key, new JsonPrimitive(false)).getAsBoolean();
    }

    public int getInteger(String key)
    {
        return get(key, new JsonPrimitive(0)).getAsInt();
    }

    public double getDouble(String key)
    {
        return get(key, new JsonPrimitive(0)).getAsDouble();
    }

    public float getFoat(String key)
    {
        return get(key, new JsonPrimitive(0)).getAsFloat();
    }

}
