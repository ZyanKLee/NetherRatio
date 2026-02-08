package org.doraji.netherratio.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.doraji.netherratio.NetherRatio;
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
 * @version 2.3.2
 */
public class WorldRatioCommand implements CommandExecutor {

    private final NetherRatio plugin;

    /**
     * Constructs a new WorldRatioCommand.
     * 
     * @param plugin The main plugin instance
     */
    public WorldRatioCommand(NetherRatio plugin) {
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

        if (!sender.hasPermission("netherratio.netherratio")) {
            sender.sendMessage(plugin.getMessagesManager().getMessage("command.no-permission"));
            return true;
        }

        if (args.length == 0) {
            // Default: show all ratios (same as 'list')
            return handleListCommand(sender);
        }

        String subcommand = args[0].toLowerCase();

        switch (subcommand) {
            case "list":
                return handleListCommand(sender);
            
            case "set":
                return handleSetCommand(sender, args);
            
            case "reload":
                return handleReloadCommand(sender);
            
            case "calc":
                return handleCalcCommand(sender, args);
            
            default:
                sender.sendMessage(plugin.getMessagesManager().getMessage("command.invalid-usage"));
                return false;
        }
    }

    /**
     * Handles the list subcommand to display all ratios.
     * 
     * @param sender The command sender
     * @return true if the command was successful
     */
    private boolean handleListCommand(CommandSender sender) {
        double defaultRatio = plugin.getConfigManager().getDefaultRatio();
        sender.sendMessage(plugin.getMessagesManager().getMessage("command.default-ratio", "ratio", String.valueOf(defaultRatio)));
        
        java.util.Set<String> worlds = plugin.getConfigManager().getOverworldNames();
        if (!worlds.isEmpty()) {
            sender.sendMessage(plugin.getMessagesManager().getMessage("command.world-ratios-header"));
            for (String worldName : worlds) {
                double ratio = plugin.getConfigManager().getRatioForWorld(worldName);
                java.util.Map<String, String> replacements = new java.util.HashMap<>();
                replacements.put("world", worldName);
                replacements.put("ratio", String.valueOf(ratio));
                sender.sendMessage(plugin.getMessagesManager().getMessage("command.world-ratio-entry", replacements));
            }
        }
        return true;
    }

    /**
     * Handles the set subcommand to set ratios.
     * 
     * @param sender The command sender
     * @param args The command arguments
     * @return true if the command was successful
     */
    private boolean handleSetCommand(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(plugin.getMessagesManager().getMessage("command.set-usage"));
            return false;
        }

        try {
            double newRatio = Double.parseDouble(args[1]);

            // Validate ratio value
            if (newRatio <= 0 || !Double.isFinite(newRatio)) {
                sender.sendMessage(plugin.getMessagesManager().getMessage("command.ratio-must-be-positive"));
                return false;
            }
            
            if (newRatio > 1000) {
                sender.sendMessage(plugin.getMessagesManager().getMessage("command.ratio-too-large"));
                return false;
            }

            if (args.length == 2) {
                // Set default ratio: /netherratio set <ratio>
                plugin.getConfigManager().setDefaultRatio(newRatio);
                sender.sendMessage(plugin.getMessagesManager().getMessage("command.default-ratio-updated", "ratio", String.valueOf(newRatio)));
                return true;
            } else {
                // Set world-specific ratio: /netherratio set <ratio> <world>
                String worldName = args[2];

                // Check if world exists in config
                if (!plugin.getConfigManager().getOverworldNames().contains(worldName)) {
                    sender.sendMessage(plugin.getMessagesManager().getMessage("command.world-not-configured", "world", worldName));
                    return false;
                }

                plugin.getConfigManager().setRatioForWorld(worldName, newRatio);
                java.util.Map<String, String> replacements = new java.util.HashMap<>();
                replacements.put("world", worldName);
                replacements.put("ratio", String.valueOf(newRatio));
                sender.sendMessage(plugin.getMessagesManager().getMessage("command.world-ratio-updated", replacements));
                return true;
            }

        } catch (NumberFormatException e) {
            sender.sendMessage(plugin.getMessagesManager().getMessage("command.invalid-number"));
            return false;
        }
    }

    /**
     * Handles the reload subcommand.
     * 
     * @param sender The command sender
     * @return true if the command was successful
     */
    private boolean handleReloadCommand(CommandSender sender) {
        plugin.reloadConfig();
        plugin.getConfigManager().reload();
        plugin.getMessagesManager().reload();
        sender.sendMessage(plugin.getMessagesManager().getMessage("command.config-reloaded"));
        return true;
    }

    /**
     * Handles the calc subcommand to calculate portal coordinates.
     * 
     * @param sender The command sender
     * @param args The command arguments (includes 'calc' as first element)
     * @return true if the command was successful
     */
    private boolean handleCalcCommand(CommandSender sender, String[] args) {
        // Check permission
        if (!sender.hasPermission("netherratio.calc")) {
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

        if (args.length == 1) {
            // Use player position: /netherratio calc
            if (player == null) {
                sender.sendMessage(plugin.getMessagesManager().getMessage("command.calc-console-needs-coords"));
                return false;
            }
            x = player.getLocation().getBlockX();
            z = player.getLocation().getBlockZ();
            worldName = player.getWorld().getName();
        } else if (args.length == 3) {
            // Parse provided coordinates: /netherratio calc <x> <z>
            if (player == null) {
                sender.sendMessage(plugin.getMessagesManager().getMessage("command.calc-console-needs-world"));
                return false;
            }
            try {
                x = Double.parseDouble(args[1]);
                z = Double.parseDouble(args[2]);
                worldName = player.getWorld().getName();
            } catch (NumberFormatException e) {
                sender.sendMessage(plugin.getMessagesManager().getMessage("command.calc-invalid-coords"));
                return false;
            }
        } else {
            sender.sendMessage(plugin.getMessagesManager().getMessage("command.calc-usage"));
            return false;
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
            java.util.Map<String, String> replacements = new java.util.HashMap<>();
            replacements.put("x1", String.format("%.1f", x));
            replacements.put("z1", String.format("%.1f", z));
            replacements.put("world1", worldName);
            replacements.put("x2", String.format("%.1f", targetX));
            replacements.put("z2", String.format("%.1f", targetZ));
            replacements.put("world2", targetWorldName);
            sender.sendMessage(plugin.getMessagesManager().getMessage("command.calc-result-to-nether", replacements));
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
            java.util.Map<String, String> replacements = new java.util.HashMap<>();
            replacements.put("x1", String.format("%.1f", x));
            replacements.put("z1", String.format("%.1f", z));
            replacements.put("world1", worldName);
            replacements.put("x2", String.format("%.1f", targetX));
            replacements.put("z2", String.format("%.1f", targetZ));
            replacements.put("world2", targetWorldName);
            sender.sendMessage(plugin.getMessagesManager().getMessage("command.calc-result-to-overworld", replacements));
        } else {
            sender.sendMessage(plugin.getMessagesManager().getMessage("command.calc-wrong-dimension"));
            return false;
        }

        return true;
    }
}
