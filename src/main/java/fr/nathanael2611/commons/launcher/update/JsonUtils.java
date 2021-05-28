package fr.nathanael2611.commons.launcher.update;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonUtils {

    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static Gson getGson() {
        return gson;
    }
}
