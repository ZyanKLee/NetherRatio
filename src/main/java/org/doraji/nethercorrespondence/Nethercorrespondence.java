package org.doraji.nethercorrespondence;

import org.bukkit.plugin.java.JavaPlugin;
import org.doraji.nethercorrespondence.events.PortalTravelListener;
import org.doraji.nethercorrespondence.commands.WorldRatioCommand;

public final class Nethercorrespondence extends JavaPlugin {

    private ConfigManager configManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        getLogger().info("차원대응 플러그인이 활성화되었습니다.");
        this.configManager = new ConfigManager(this);
        getServer().getPluginManager().registerEvents(new PortalTravelListener(this), this);
        this.getCommand("netherratio").setExecutor(new WorldRatioCommand(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public ConfigManager getSettingsManager() {
        return configManager;
    }

}
