package org.doraji.netherratio;

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
 * @version 2.1.0
 */
public class ConfigManager {

    private final NetherRatio plugin;
    private FileConfiguration config;
    public static final String RATIO_VALUE = "value";
    public static final String WORLD_PAIRS = "world-pairs";
    
    private Map<String, String> overworldToNether;
    private Map<String, String> netherToOverworld;
    private Map<String, Double> worldPairRatios;
    private double defaultRatio;

    /**
     * Constructs a new ConfigManager.
     * 
     * @param plugin The main plugin instance
     */
    public ConfigManager(NetherRatio plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
        this.overworldToNether = new HashMap<>();
        this.netherToOverworld = new HashMap<>();
        this.worldPairRatios = new HashMap<>();
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
     * Also loads per-world ratios with backward compatibility for global ratio.
     */
    private void loadWorldPairs() {
        overworldToNether.clear();
        netherToOverworld.clear();
        worldPairRatios.clear();
        
        // Load default/global ratio
        defaultRatio = config.getDouble(RATIO_VALUE, 8.0);
        
        ConfigurationSection worldPairs = config.getConfigurationSection(WORLD_PAIRS);
        if (worldPairs == null) {
            // Use default mapping if not configured
            plugin.getLogger().warning(plugin.getMessagesManager().getMessage("config.no-world-pairs"));
            overworldToNether.put("world", "world_nether");
            netherToOverworld.put("world_nether", "world");
            worldPairRatios.put("world", defaultRatio);
            return;
        }
        
        for (String overworldName : worldPairs.getKeys(false)) {
            Object value = worldPairs.get(overworldName);
            String netherName;
            double ratio;
            
            if (value instanceof ConfigurationSection) {
                // New format: world-pairs.world.nether and world-pairs.world.ratio
                ConfigurationSection pairConfig = (ConfigurationSection) value;
                netherName = pairConfig.getString("nether");
                ratio = pairConfig.getDouble("ratio", defaultRatio);
            } else if (value instanceof String) {
                // Old format: world-pairs.world: world_nether (uses global ratio)
                netherName = (String) value;
                ratio = defaultRatio;
            } else {
                plugin.getLogger().warning("Invalid world pair configuration for: " + overworldName);
                continue;
            }
            
            if (netherName != null && !netherName.isEmpty()) {
                overworldToNether.put(overworldName, netherName);
                netherToOverworld.put(netherName, overworldName);
                worldPairRatios.put(overworldName, ratio);
                
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
     * Gets the ratio for a specific overworld.
     * 
     * @param overworldName The name of the overworld
     * @return The ratio for this world pair, or the default ratio if not configured
     */
    public double getRatioForWorld(String overworldName) {
        return worldPairRatios.getOrDefault(overworldName, defaultRatio);
    }
    
    /**
     * Gets the ratio for a world pair based on nether world name.
     * 
     * @param netherName The name of the nether world
     * @return The ratio for this world pair, or the default ratio if not configured
     */
    public double getRatioForNetherWorld(String netherName) {
        String overworldName = netherToOverworld.get(netherName);
        if (overworldName == null) {
            return defaultRatio;
        }
        return worldPairRatios.getOrDefault(overworldName, defaultRatio);
    }
    
    /**
     * Sets the ratio for a specific world pair.
     * 
     * @param overworldName The overworld name
     * @param ratio The ratio to set
     */
    public void setRatioForWorld(String overworldName, double ratio) {
        worldPairRatios.put(overworldName, ratio);
        
        // Update config structure
        String netherName = overworldToNether.get(overworldName);
        if (netherName != null) {
            config.set(WORLD_PAIRS + "." + overworldName + ".nether", netherName);
            config.set(WORLD_PAIRS + "." + overworldName + ".ratio", ratio);
            plugin.saveConfig();
            this.config = plugin.getConfig();
        }
    }
    
    /**
     * Gets all configured overworld names.
     * 
     * @return Set of overworld names
     */
    public java.util.Set<String> getOverworldNames() {
        return overworldToNether.keySet();
    }
    
    /**
     * Gets the default ratio.
     * 
     * @return The default ratio value
     */
    public double getDefaultRatio() {
        return defaultRatio;
    }
    
    /**
     * Sets the default ratio.
     * 
     * @param ratio The default ratio to set
     */
    public void setDefaultRatio(double ratio) {
        this.defaultRatio = ratio;
        config.set(RATIO_VALUE, ratio);
        plugin.saveConfig();
        this.config = plugin.getConfig();
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
