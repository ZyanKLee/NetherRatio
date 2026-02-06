package org.doraji.nethercorrespondence;

import org.bukkit.plugin.java.JavaPlugin;
import org.doraji.nethercorrespondence.events.PortalTravelListener;
import org.doraji.nethercorrespondence.commands.WorldRatioCommand;

public final class Nethercorrespondence extends JavaPlugin {

    private ConfigManager configManager;
    private MessagesManager messagesManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        this.messagesManager = new MessagesManager(this);
        getLogger().info(messagesManager.getMessage("plugin.enabled"));
        this.configManager = new ConfigManager(this);
        getServer().getPluginManager().registerEvents(new PortalTravelListener(this), this);
        this.getCommand("netherratio").setExecutor(new WorldRatioCommand(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if (messagesManager != null) {
            getLogger().info(messagesManager.getMessage("plugin.disabled"));
        }
        saveConfig();
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
    
    public MessagesManager getMessagesManager() {
        return messagesManager;
    }

}
