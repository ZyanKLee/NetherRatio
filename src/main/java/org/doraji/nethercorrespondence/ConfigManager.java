package org.doraji.nethercorrespondence;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages configuration for the NetherRatio plugin.
 * 
 * <p>This class handles loading, accessing, and persisting configuration settings,
 * including the coordinate ratio and world pair mappings for portal travel.</p>
 * 
 * @author xDxRAx (Original Author)
 * @author NetherRatio Team
 * @author ZyanKLee (Maintainer)
 * @version 2.0
 */
public class ConfigManager {

    private final Nethercorrespondence plugin;
    private FileConfiguration config;
    public static final String RATIO_VALUE = "value";
    public static final String WORLD_PAIRS = "world-pairs";
    
    private Map<String, String> overworldToNether;
    private Map<String, String> netherToOverworld;

    /**
     * Constructs a new ConfigManager.
     * 
     * @param plugin The main plugin instance
     */
    public ConfigManager(Nethercorrespondence plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
        this.overworldToNether = new HashMap<>();
        this.netherToOverworld = new HashMap<>();
        loadDefaultSettings();
        loadWorldPairs();
    }

    /**
     * Loads default configuration values if they don't exist.
     */
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
            plugin.getLogger().warning(plugin.getMessagesManager().getMessage("config.no-world-pairs"));
            overworldToNether.put("world", "world_nether");
            netherToOverworld.put("world_nether", "world");
            return;
        }
        
        for (String overworldName : worldPairs.getKeys(false)) {
            String netherName = worldPairs.getString(overworldName);
            if (netherName != null && !netherName.isEmpty()) {
                overworldToNether.put(overworldName, netherName);
                netherToOverworld.put(netherName, overworldName);
                Map<String, String> replacements = new HashMap<>();
                replacements.put("overworld", overworldName);
                replacements.put("nether", netherName);
                plugin.getLogger().info(plugin.getMessagesManager().getMessage("config.world-pair-loaded", replacements));
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

    /**
     * Gets an integer value from the configuration.
     * 
     * @param path The configuration path to retrieve
     * @return The integer value at the specified path
     */
    public int getInt(String path) {
        return config.getInt(path);
    }

    /**
     * Gets a boolean value from the configuration.
     * 
     * @param path The configuration path to retrieve
     * @return The boolean value at the specified path
     */
    public boolean getBoolean(String path) {
        return config.getBoolean(path);
    }

    /**
     * Gets a double value from the configuration.
     * 
     * @param path The configuration path to retrieve
     * @return The double value at the specified path
     */
    public double getDouble(String path) {
        return config.getDouble(path);
    }

    /**
     * Gets a value from the configuration.
     * 
     * @param path The configuration path to retrieve
     * @return The object value at the specified path
     */
    public Object get(String path) {
        return config.get(path);
    }

    /**
     * Gets the underlying FileConfiguration object.
     * 
     * @return The FileConfiguration instance
     */
    public FileConfiguration getConfig() {
        return config;
    }
    
    /**
     * Sets a value in the configuration and saves it.
     * 
     * @param path The configuration path
     * @param value The value to set
     */
    public void setValue(String path, Object value) {
        config.set(path, value);
        plugin.saveConfig();
        // Refresh cached reference to ensure consistency
        this.config = plugin.getConfig();
    }
}
