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

import java.net.InetSocketAddress;

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

        if(args.length < 1) {
            sender.sendMessage(C.color("&6Doğru Kullanım: &b/" + s + " <oyuncuAdı>"));
            return true;
        }

        User targetUser = ProfilePlugin.getInstance().getUserManager().getUser(args[0]);

        if (targetUser == null) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cVeritabanında &f'" + args[0] + "' &cismine sahip bir oyuncu bulunamadı."));
            return true;
        }

        sender.sendMessage(C.color("&7&m--------------------------------------"));
        sender.sendMessage(C.color("&6Kullanıcı: &f"  + targetUser.getName()));
        sender.sendMessage(C.color("&6UUID: &f" + targetUser.getUniqueUUID()));
        sender.sendMessage(C.color("&6Mevcut IP: &f" + targetUser.getCurrentAddress()));
        sender.sendMessage(C.color("&6Tüm IP adresleri: &f" + targetUser.getIpAddresses()));
        sender.sendMessage(C.color(" "));
        sender.sendMessage(C.color("&6Rütbe: &f" + targetUser.getRankType().getRankName()));
        sender.sendMessage(C.color("&6Sembol: &f" + targetUser.getSymbolType().getDisplayName()));
        sender.sendMessage(C.color(" "));
        sender.sendMessage(C.color("&6Sunucu: &f" + targetUser.getServerName()));
        sender.sendMessage(C.color("&6Kredi Miktarı: &f" + targetUser.getCredit()));
        sender.sendMessage(C.color("&6Aktiflik Durumu: &f" + (targetUser.isOnlineStatus() ? "Aktif" : "Kapalı")));
        sender.sendMessage(C.color(" "));
        sender.sendMessage(C.color("&6İlk giriş: &f" + targetUser.getFirstLoginTime()));
        sender.sendMessage(C.color("&6Son giriş: &f" + targetUser.getLastLoginTime()));
        sender.sendMessage(C.color(" "));
        sender.sendMessage(C.color("&6Freeze Durumu: &f" + (targetUser.isFrozenStatus() ? "&aAçık" : "&cKapalı")));
        sender.sendMessage(C.color("&6Staff Modu: &f" + (targetUser.isStaffMode() ? "&aAçık" : "&cKapalı")));
        sender.sendMessage(C.color("&7&m--------------------------------------"));
        return true;
    }
}
