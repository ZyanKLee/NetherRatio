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
            // Show all world pairs and their ratios
            double defaultRatio = plugin.getConfigManager().getDefaultRatio();
            sender.sendMessage(plugin.getMessagesManager().getMessage("command.default-ratio", "ratio", String.valueOf(defaultRatio)));
            
            java.util.Set<String> worlds = plugin.getConfigManager().getOverworldNames();
            if (!worlds.isEmpty()) {
                sender.sendMessage(plugin.getMessagesManager().getMessage("command.world-ratios-header"));
                for (String worldName : worlds) {
                    double ratio = plugin.getConfigManager().getRatioForWorld(worldName);
                    sender.sendMessage(plugin.getMessagesManager().getMessage("command.world-ratio-entry", 
                        "world", worldName, 
                        "ratio", String.valueOf(ratio)));
                }
            }
            return true;

        } else if (args.length == 1) {
            // Check for calc subcommand (use player position)
            if (args[0].equalsIgnoreCase("calc")) {
                return handleCalcCommand(sender, null, null);
            }
            
            // Check for reload subcommand
            if (args[0].equalsIgnoreCase("reload")) {
                plugin.reloadConfig();
                plugin.getConfigManager().reload();
                plugin.getMessagesManager().reload();
                sender.sendMessage(plugin.getMessagesManager().getMessage("command.config-reloaded"));
                return true;
            }
            
            // Otherwise, treat as default ratio value
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

                plugin.getConfigManager().setDefaultRatio(newRatio);

                sender.sendMessage(plugin.getMessagesManager().getMessage("command.default-ratio-updated", "ratio", String.valueOf(newRatio)));
                return true;

            } catch (NumberFormatException e) {
                sender.sendMessage(plugin.getMessagesManager().getMessage("command.invalid-number"));
                return false;
            }
        } else if (args.length == 2) {
            // Set ratio for specific world: /netherratio <ratio> <world>
            try {
                double newRatio = Double.parseDouble(args[0]);
                String worldName = args[1];

                // Validate ratio value
                if (newRatio <= 0 || !Double.isFinite(newRatio)) {
                    sender.sendMessage(plugin.getMessagesManager().getMessage("command.ratio-must-be-positive"));
                    return false;
                }
                
                if (newRatio > 1000) {
                    sender.sendMessage(plugin.getMessagesManager().getMessage("command.ratio-too-large"));
                    return false;
                }

                // Check if world exists in config
                if (!plugin.getConfigManager().getOverworldNames().contains(worldName)) {
                    sender.sendMessage(plugin.getMessagesManager().getMessage("command.world-not-configured", "world", worldName));
                    return false;
                }

                plugin.getConfigManager().setRatioForWorld(worldName, newRatio);
                sender.sendMessage(plugin.getMessagesManager().getMessage("command.world-ratio-updated", 
                    "world", worldName,
                    "ratio", String.valueOf(newRatio)));
                return true;

            } catch (NumberFormatException e) {
                sender.sendMessage(plugin.getMessagesManager().getMessage("command.invalid-number"));
                return false;
            }
        } else if (args.length == 3 && args[0].equalsIgnoreCase("calc")) {
            // calc subcommand with coordinates
            return handleCalcCommand(sender, args[1], args[2]);
        } else {
            sender.sendMessage(plugin.getMessagesManager().getMessage("command.invalid-usage"));
            return false;
        }
    }

    /**
     * Handles the calc subcommand to calculate portal coordinates.
     * 
     * @param sender The command sender
     * @param xArg The X coordinate argument (null to use player position)
     * @param zArg The Z coordinate argument (null to use player position)
     * @return true if the command was successful
     */
    private boolean handleCalcCommand(CommandSender sender, String xArg, String zArg) {
        // Check permission
        if (!sender.hasPermission("nethercorrespondence.calc")) {
            sender.sendMessage(plugin.getMessagesManager().getMessage("command.no-permission"));
            return true;
        }

        // Get player location if no coordinates provided
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }

        double x, z;
        String worldName;

        if (xArg == null || zArg == null) {
            // Use player position
            if (player == null) {
                sender.sendMessage(plugin.getMessagesManager().getMessage("command.calc-console-needs-coords"));
                return false;
            }
            x = player.getLocation().getBlockX();
            z = player.getLocation().getBlockZ();
            worldName = player.getWorld().getName();
        } else {
            // Parse provided coordinates
            if (player == null) {
                sender.sendMessage(plugin.getMessagesManager().getMessage("command.calc-console-needs-world"));
                return false;
            }
            try {
                x = Double.parseDouble(xArg);
                z = Double.parseDouble(zArg);
                worldName = player.getWorld().getName();
            } catch (NumberFormatException e) {
                sender.sendMessage(plugin.getMessagesManager().getMessage("command.calc-invalid-coords"));
                return false;
            }
        }

        // Get the world and calculate
        org.bukkit.World world = plugin.getServer().getWorld(worldName);
        if (world == null) {
            sender.sendMessage(plugin.getMessagesManager().getMessage("command.calc-invalid-world"));
            return false;
        }

        double ratio;
        String targetWorldName;
        double targetX, targetZ;

        if (world.getEnvironment() == org.bukkit.World.Environment.NORMAL) {
            // Overworld to Nether
            org.bukkit.World netherWorld = plugin.getConfigManager().getLinkedNetherWorld(worldName);
            if (netherWorld == null) {
                sender.sendMessage(plugin.getMessagesManager().getMessage("command.calc-no-nether", "world", worldName));
                return false;
            }
            ratio = plugin.getConfigManager().getRatioForWorld(worldName);
            targetWorldName = netherWorld.getName();
            targetX = x / ratio;
            targetZ = z / ratio;
            sender.sendMessage(plugin.getMessagesManager().getMessage("command.calc-result-to-nether",
                "x1", String.format("%.1f", x),
                "z1", String.format("%.1f", z),
                "world1", worldName,
                "x2", String.format("%.1f", targetX),
                "z2", String.format("%.1f", targetZ),
                "world2", targetWorldName));
        } else if (world.getEnvironment() == org.bukkit.World.Environment.NETHER) {
            // Nether to Overworld
            org.bukkit.World overworldWorld = plugin.getConfigManager().getLinkedOverworld(worldName);
            if (overworldWorld == null) {
                sender.sendMessage(plugin.getMessagesManager().getMessage("command.calc-no-overworld", "world", worldName));
                return false;
            }
            ratio = plugin.getConfigManager().getRatioForNetherWorld(worldName);
            targetWorldName = overworldWorld.getName();
            targetX = x * ratio;
            targetZ = z * ratio;
            sender.sendMessage(plugin.getMessagesManager().getMessage("command.calc-result-to-overworld",
                "x1", String.format("%.1f", x),
                "z1", String.format("%.1f", z),
                "world1", worldName,
                "x2", String.format("%.1f", targetX),
                "z2", String.format("%.1f", targetZ),
                "world2", targetWorldName));
        } else {
            sender.sendMessage(plugin.getMessagesManager().getMessage("command.calc-wrong-dimension"));
            return false;
        }

        return true;
    }
}
