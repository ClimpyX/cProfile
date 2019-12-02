package com.climpy.profile.commands;

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

        sender.sendMessage(C.color("&9- Kullanıcı"));
        sender.sendMessage(C.color(" &bIGN: &f" + user.getName()));
        sender.sendMessage(C.color(" &bUUID: &f" + user.getUniqueUUID()));
        sender.sendMessage(C.color(" &bIP: &f0.0.0.0"));
        sender.sendMessage(C.color(" &bRütbe: &f" + user.getRankType()));
        sender.sendMessage(C.color("&9- Sunucu: &f" + user.getServerName()));
        sender.sendMessage(C.color(" &bÇevrim içi: &f-"));
        sender.sendMessage(C.color("&9- Verileri"));
        sender.sendMessage(C.color(" &bSembol: &f" + user.getSymbolType()));
        sender.sendMessage(C.color(" &bKredi: &f" + user.getCredit()));
        sender.sendMessage(C.color(" &bDurumu: &f" + user.isOnlineStatus()));
        sender.sendMessage(C.color("&9- Zamanlama"));
        sender.sendMessage(C.color(" &bİlk giriş: &f" + user.getFirstLoginTime()));
        sender.sendMessage(C.color(" &bSon giriş: &f" + user.getLastLoginTime()));

        return true;
    }
}
