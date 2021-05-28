package fr.nathanael2611.commons.launcher.update.minecraft.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonManager
{
    private static Gson gson;
    
    public static Gson init() {
        return new GsonBuilder().serializeNulls().setPrettyPrinting().disableHtmlEscaping().create();
    }
    
    public JsonManager() {
        JsonManager.gson = init();
    }
    
    public static Gson getGson() {
        return JsonManager.gson;
    }
    
    public static void setGson(final Gson gson) {
        JsonManager.gson = gson;
    }
}
