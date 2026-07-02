package org.doraji.netherratio;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.doraji.netherratio.events.PortalTravelListener;
import org.doraji.netherratio.commands.WorldRatioCommand;

/**
 * NetherRatio Plugin - Customizable Nether-to-Overworld coordinate ratio for portal travel.
 * 
 * <p>This plugin allows server administrators to modify the coordinate conversion ratio
 * used when traveling between the Overworld and Nether dimensions through portals.
 * The default Minecraft ratio is 8:1 (8 blocks in Overworld = 1 block in Nether).</p>
 * 
 * @author xDxRAx (Original Author)
 * @author NetherRatio Team
 * @author ZyanKLee (Maintainer)
 * @version 3.0.2
 */
public final class NetherRatio extends JavaPlugin {

    private ConfigManager configManager;
    private MessagesManager messagesManager;

    /**
     * Called when the plugin is enabled.
     * Initializes configuration, messages, event listeners, and command executors.
     */
    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        this.messagesManager = new MessagesManager(this);
        getLogger().info(messagesManager.getMessage("plugin.enabled"));
        this.configManager = new ConfigManager(this);
        getServer().getPluginManager().registerEvents(new PortalTravelListener(this), this);
        PluginCommand command = this.getCommand("netherratio");
        if (command != null) {
            command.setExecutor(new WorldRatioCommand(this));
        } else {
            getLogger().severe("Command 'netherratio' is not defined in plugin.yml");
        }
    }

    /**
     * Called when the plugin is disabled.
     * Performs cleanup operations.
     *
     * <p>The configuration is intentionally not saved here: all configuration
     * changes are persisted immediately when they are made, and saving the
     * in-memory configuration on shutdown would overwrite any manual edits
     * made to config.yml while the server was running.</p>
     */
    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if (messagesManager != null) {
            getLogger().info(messagesManager.getMessage("plugin.disabled"));
        }
    }

    /**
     * Gets the configuration manager for this plugin.
     * 
     * @return The ConfigManager instance
     */
    public ConfigManager getConfigManager() {
        return configManager;
    }
    
    /**
     * Gets the messages manager for this plugin.
     * 
     * @return The MessagesManager instance
     */
    public MessagesManager getMessagesManager() {
        return messagesManager;
    }

}
