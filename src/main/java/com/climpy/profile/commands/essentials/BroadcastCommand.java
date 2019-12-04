package com.climpy.profile.commands.essentials;

import com.climpy.profile.ProfilePlugin;
import com.climpy.profile.rank.RankType;
import com.climpy.profile.user.User;
import com.climpy.profile.utils.C;
import com.climpy.profile.utils.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BroadcastCommand implements CommandExecutor {
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

        if (args.length < 1) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cDoğru Kullanım: /" + label + " [bilgilendirme-mesajı..]"));
            return true;
        }

        String broadcastMessage = StringUtil.joinStrings(args, 0);
        Bukkit.broadcastMessage(C.color("&6[Duyuru] &r" + broadcastMessage));
        return true;
    }
}
