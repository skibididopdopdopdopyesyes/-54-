package com.swill.hitbox;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.*;
import java.nio.file.*;

public class Config {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = Paths.get("config", "hitboxexpander.json");
    
    private static boolean enabled = true;
    private static double hitBoxSize = 0.8;
    private static int bypassLevel = 3;
    private static boolean silentMode = false;
    private static boolean onlyInCombat = false;
    
    public static void load() {
        try {
            if (Files.exists(CONFIG_PATH)) {
                String json = Files.readString(CONFIG_PATH);
                ConfigData data = GSON.fromJson(json, ConfigData.class);
                enabled = data.enabled;
                hitBoxSize = data.hitBoxSize;
                bypassLevel = data.bypassLevel;
                silentMode = data.silentMode;
                onlyInCombat = data.onlyInCombat;
            } else {
                save();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            ConfigData data = new ConfigData();
            data.enabled = enabled;
            data.hitBoxSize = hitBoxSize;
            data.bypassLevel = bypassLevel;
            data.silentMode = silentMode;
            data.onlyInCombat = onlyInCombat;
            Files.writeString(CONFIG_PATH, GSON.toJson(data));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static boolean isEnabled() { return enabled; }
    public static void setEnabled(boolean value) { enabled = value; save(); }
    
    public static double getHitBoxSize() { return hitBoxSize; }
    public static void setHitBoxSize(double value) { hitBoxSize = value; save(); }
    
    public static int getBypassLevel() { return bypassLevel; }
    public static void setBypassLevel(int value) { bypassLevel = value; save(); }
    
    public static boolean isSilentMode() { return silentMode; }
    public static void setSilentMode(boolean value) { silentMode = value; save(); }
    
    public static boolean isOnlyInCombat() { return onlyInCombat; }
    public static void setOnlyInCombat(boolean value) { onlyInCombat = value; save(); }
    
    private static class ConfigData {
        boolean enabled = true;
        double hitBoxSize = 0.8;
        int bypassLevel = 3;
        boolean silentMode = false;
        boolean onlyInCombat = false;
    }
}
