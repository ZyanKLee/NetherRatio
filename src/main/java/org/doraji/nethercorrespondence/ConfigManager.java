package org.doraji.nethercorrespondence;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

public class ConfigManager {

    private final Nethercorrespondence plugin;
    private FileConfiguration config;
    public static final String RATIO_VALUE = "value";
    public static final String WORLD_PAIRS = "world-pairs";
    
    private Map<String, String> overworldToNether;
    private Map<String, String> netherToOverworld;

    public ConfigManager(Nethercorrespondence plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
        this.overworldToNether = new HashMap<>();
        this.netherToOverworld = new HashMap<>();
        loadDefaultSettings();
        loadWorldPairs();
    }

    private void loadDefaultSettings() {
        config.addDefault(RATIO_VALUE, 8);
        config.options().copyDefaults(true);
        plugin.saveConfig();
    }
    
    /**
     * Loads world pairs from configuration and builds bidirectional mapping.
     */
    private void loadWorldPairs() {
        overworldToNether.clear();
        netherToOverworld.clear();
        
        ConfigurationSection worldPairs = config.getConfigurationSection(WORLD_PAIRS);
        if (worldPairs == null) {
            // Use default mapping if not configured
            plugin.getLogger().warning("No world-pairs configured, using default (world -> world_nether)");
            overworldToNether.put("world", "world_nether");
            netherToOverworld.put("world_nether", "world");
            return;
        }
        
        for (String overworldName : worldPairs.getKeys(false)) {
            String netherName = worldPairs.getString(overworldName);
            if (netherName != null && !netherName.isEmpty()) {
                overworldToNether.put(overworldName, netherName);
                netherToOverworld.put(netherName, overworldName);
                plugin.getLogger().info("Loaded world pair: " + overworldName + " <-> " + netherName);
            }
        }
    }
    
    /**
     * Gets the linked nether world for the given overworld.
     * 
     * @param overworldName The name of the overworld
     * @return The linked nether world, or null if not found
     */
    public World getLinkedNetherWorld(String overworldName) {
        String netherName = overworldToNether.get(overworldName);
        if (netherName == null) {
            return null;
        }
        return Bukkit.getWorld(netherName);
    }
    
    /**
     * Gets the linked overworld for the given nether world.
     * 
     * @param netherName The name of the nether world
     * @return The linked overworld, or null if not found
     */
    public World getLinkedOverworld(String netherName) {
        String overworldName = netherToOverworld.get(netherName);
        if (overworldName == null) {
            return null;
        }
        return Bukkit.getWorld(overworldName);
    }
    
    /**
     * Reloads the configuration and world pairs.
     */
    public void reload() {
        plugin.reloadConfig();
        this.config = plugin.getConfig();
        loadWorldPairs();
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
