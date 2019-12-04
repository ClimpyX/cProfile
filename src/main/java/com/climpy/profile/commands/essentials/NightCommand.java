package com.climpy.profile.commands.essentials;

import com.climpy.profile.ProfilePlugin;
import com.climpy.profile.rank.RankType;
import com.climpy.profile.user.User;
import com.climpy.profile.utils.C;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NightCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        User user = ProfilePlugin.getInstance().getUserManager().getUser(sender.getName());
        Player player = Bukkit.getPlayer(sender.getName());
        if (sender instanceof Player) {
            if (!player.isOp() && !user.getRankType().isAboveOrEqual(RankType.MOD)) {
                sender.sendMessage(ChatColor.RED + "Bu komutu gerçekleştirmek için " + RankType.MOD.getDisplayName() + ChatColor.RED + " veya üzeri rütbe gerekiyor.");
                return true;
            }
        }

        if(args.length < 1) {
            player.sendMessage(C.color("&cDoğru Kullanım: /" + label + " [all/player]"));
            return true;
        }

        if(args[0].equals("all")) {
            player.getWorld().setFullTime(18000L);
            player.sendMessage(C.color("&aTüm oyuncular için şimdi akşam vakti."));
            return true;
        }

        if(args[0].equals("player")) {
            player.setPlayerTime(18000L, false);
            player.sendMessage(C.color("&aSadece sizin için şimdi akşam vakti."));
            return true;
        }
        return true;
    }
}
