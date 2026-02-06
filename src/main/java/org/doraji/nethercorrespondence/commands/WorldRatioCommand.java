package org.doraji.nethercorrespondence.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.doraji.nethercorrespondence.Nethercorrespondence;
import org.jetbrains.annotations.NotNull;

/**
 * Command executor for the /netherratio command.
 * 
 * <p>Allows administrators to view and modify the Nether-to-Overworld coordinate ratio,
 * as well as reload the plugin configuration.</p>
 * 
 * @author xDxRAx (Original Author)
 * @author NetherRatio Team
 * @author ZyanKLee (Maintainer)
 * @version 2.0.2
 */
public class WorldRatioCommand implements CommandExecutor {

    private final Nethercorrespondence plugin;

    /**
     * Constructs a new WorldRatioCommand.
     * 
     * @param plugin The main plugin instance
     */
    public WorldRatioCommand(Nethercorrespondence plugin) {
        this.plugin = plugin;
    }

    /**
     * Executes the netherratio command.
     * 
     * @param sender The command sender
     * @param command The command being executed
     * @param label The alias used for the command
     * @param args The command arguments
     * @return true if the command was successful, false otherwise
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!sender.hasPermission("nethercorrespondence.netherratio")) {
            sender.sendMessage(plugin.getMessagesManager().getMessage("command.no-permission"));
            return true;
        }

        if (args.length == 0) {
            double currentRatio = plugin.getConfigManager().getDouble("value");
            sender.sendMessage(plugin.getMessagesManager().getMessage("command.current-ratio", "ratio", String.valueOf(currentRatio)));
            return true;

        } else if (args.length == 1) {
            // Check for reload subcommand
            if (args[0].equalsIgnoreCase("reload")) {
                plugin.reloadConfig();
                plugin.getConfigManager().reload();
                plugin.getMessagesManager().reload();
                sender.sendMessage(plugin.getMessagesManager().getMessage("command.config-reloaded"));
                return true;
            }
            
            // Otherwise, treat as ratio value
            try {
                double newRatio = Double.parseDouble(args[0]);

                // Validate ratio value
                if (newRatio <= 0 || !Double.isFinite(newRatio)) {
                    sender.sendMessage(plugin.getMessagesManager().getMessage("command.ratio-must-be-positive"));
                    return false;
                }
                
                if (newRatio > 1000) {
                    sender.sendMessage(plugin.getMessagesManager().getMessage("command.ratio-too-large"));
                    return false;
                }

                plugin.getConfigManager().setValue("value", newRatio);

                sender.sendMessage(plugin.getMessagesManager().getMessage("command.ratio-updated", "ratio", String.valueOf(newRatio)));
                return true;

            } catch (NumberFormatException e) {
                sender.sendMessage(plugin.getMessagesManager().getMessage("command.invalid-number"));
                return false;
            }
        } else {
            sender.sendMessage(plugin.getMessagesManager().getMessage("command.invalid-usage"));
            return false;
        }
    }
}
