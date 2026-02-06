package org.doraji.nethercorrespondence.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.doraji.nethercorrespondence.Nethercorrespondence;
import org.jetbrains.annotations.NotNull;

public class WorldRatioCommand implements CommandExecutor {

    private final Nethercorrespondence plugin;

    public WorldRatioCommand(Nethercorrespondence plugin) {
        this.plugin = plugin;
    }

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
            try {
                double newRatio = Double.parseDouble(args[0]);

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
