package com.climpy.profile.commands;

import com.climpy.profile.ProfilePlugin;
import com.climpy.profile.rank.RankType;
import com.climpy.profile.user.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UserCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        User user = ProfilePlugin.getInstance().getUserManager().getUser(sender.getName());
        Player player = Bukkit.getPlayer(sender.getName());
        if (sender instanceof Player) {
            if (!user.getRankType().isAboveOrEqual(RankType.MOD)) {
                sender.sendMessage(ChatColor.RED + "Bu komutu gerçekleştirmek için " + RankType.MOD.getDisplayName() + ChatColor.RED + " veya üzeri rütbe gerekiyor.");
                return true;
            }
        }

         if(args.length == 0) {
            sender.sendMessage(ChatColor.GREEN + "Veriler alınıyor..");
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(ProfilePlugin.getInstance(), new Runnable() {
                public void run() {
                    sender.sendMessage(ChatColor.GREEN + "Veriler başarıyla alındı.");
                    UserMenu.INVENTORY.open(player);
                }
            }, 20 * 2);
            return true;
        }

        player.sendMessage(args[1] + " aktiflik durumu: " + (user.isOnlineStatus() ? "a" : "b"));
        return true;
    }
}
