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

        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.isOp()) {
                player.sendMessage("§c이 명령어는 OP만 사용할 수 있습니다.");
                return true;
            }
        }

        if (args.length == 0) {
            double currentRatio = plugin.getConfig().getDouble("value", 1.0);
            sender.sendMessage("§a현재 월드 비율: " + currentRatio);
            return true;

        } else if (args.length == 1) {
            try {
                double newRatio = Double.parseDouble(args[0]);

                plugin.getConfig().set("value", newRatio);
                plugin.saveConfig();

                sender.sendMessage("§a월드 비율이 " + newRatio + "(으)로 성공적으로 변경되었습니다.");
                return true;

            } catch (NumberFormatException e) {
                sender.sendMessage("§c잘못된 숫자 형식입니다. /netherratio [숫자]");
                return false;
            }
        } else {
            sender.sendMessage("§c잘못된 사용법입니다. /netherratio [<숫자>]");
            return false;
        }
    }
}
