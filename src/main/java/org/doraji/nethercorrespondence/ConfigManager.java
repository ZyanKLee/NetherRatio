package org.doraji.nethercorrespondence;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {

    private final Nethercorrespondence plugin;
    private FileConfiguration config;

    // 설정 키 (config.yml에 저장될 이름)
    public static final String RATIO_VALUE = "value";

    public ConfigManager(Nethercorrespondence plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
        loadDefaultSettings();
    }

    private void loadDefaultSettings() {
        config.addDefault(RATIO_VALUE, 1);
        config.options().copyDefaults(true);
        plugin.saveConfig();
    }

    public int getInt(String path) {
        return config.getInt(path);
    }

    public boolean getBoolean(String path) {
        return config.getBoolean(path);
    }

    public double getDouble(String path) {
        return config.getDouble(path);
    }

    public Object get(String path) {
        return config.get(path);
    }

    public FileConfiguration getConfig() {
        return config;
    }
}
