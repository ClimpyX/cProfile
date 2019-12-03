package com.climpy.profile.commands.coin;

import com.climpy.profile.ProfilePlugin;
import com.climpy.profile.rank.RankType;
import com.climpy.profile.user.User;
import com.climpy.profile.utils.C;
import com.climpy.profile.utils.NumberUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CoinsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command label, String s, String[] args) {
        Player player = Bukkit.getPlayer(sender.getName());
        User user = ProfilePlugin.getInstance().getUserManager().getUser(sender.getName());
        if (sender instanceof Player) {
            if (!player.isOp() && !user.getRankType().isAboveOrEqual(RankType.ADMIN)) {
                sender.sendMessage(ChatColor.RED + "Bu komutu gerçekleştirmek için " + RankType.MODPLUS.getDisplayName() + ChatColor.RED + " veya üzeri rütbe gerekiyor.");
                return true;
            }
        }

        if(args.length < 3) {

            sender.sendMessage(C.color("  &7&m-&aa&m+-&7&m------------&f &e&lKredi Komutları &7&m------------&aa&m-+&7&m-"));
            sender.sendMessage(C.color(" &a&l* &8/&7kredi <ayarla/sil/ver> [oyuncuAdı..] [kredi-miktarı..]"));
            sender.sendMessage(C.color(" &a&l* &8/&7kredi <set/remove/give> [oyuncuAdı..] [kredi-miktarı..]"));
            sender.sendMessage(C.color("     &7&m-&aa&m+-&7&m--------------------------------------&aa&m-+&7&m-"));
            return true;
        }

      //  System.out.println("" + args[0] + args[1] + args[2] +  args[3] + args[4]);

        if(args[0].equalsIgnoreCase("ayarla") || args[0].equalsIgnoreCase("set")) {
            User targetUser = ProfilePlugin.getInstance().getUserManager().getUser(args[1]);

            if (targetUser == null) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cVeritabanında &f'" + args[0] + "' &cismine sahip bir oyuncu bulunamadı."));
                return true;
            }

            Player t = Bukkit.getPlayer(args[1]);
            targetUser.setCredit(Long.parseLong(args[2]));

            sender.sendMessage(C.color(user.getRankType().getColor() + t.getName() + " &aaoyuncusunun kredi değeri &f" + NumberUtil.formatNumberByDecimal(Double.parseDouble(args[2])) + " &aaolarak güncellendi."));
            t.sendMessage(C.color("&aaKredi değeriniz &f" + NumberUtil.formatNumberByDecimal(Double.parseDouble(args[2])) + " &aaolarak güncellendi."));
            user.save();
            return true;
        }

        if(args[0].equalsIgnoreCase("sil") || args[0].equalsIgnoreCase("remove")) {
            User targetUser = ProfilePlugin.getInstance().getUserManager().getUser(args[1]);

            if (targetUser == null) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cVeritabanında &f'" + args[0] + "' &cismine sahip bir oyuncu bulunamadı."));
                return true;
            }

            Player t = Bukkit.getPlayer(args[1]);
            long amount = Long.parseLong(args[2]);

            targetUser.setCredit(targetUser.getCredit() - amount);

            sender.sendMessage(C.color(user.getRankType().getColor() + t.getName() + " &aaoyuncusunun kredi değeri &f" + NumberUtil.formatNumberByDecimal(Double.parseDouble(args[2])) + " &aakadar silindi."));
            t.sendMessage(C.color("&aaKredi değeriniz &f" + NumberUtil.formatNumberByDecimal(Double.parseDouble(args[2])) + " &aakadar silindi."));
            user.save();
            return true;
        }

        if(args[0].equalsIgnoreCase("ver") || args[0].equalsIgnoreCase("give")) {
            User targetUser = ProfilePlugin.getInstance().getUserManager().getUser(args[1]);

            if (targetUser == null) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cVeritabanında &f'" + args[0] + "' &cismine sahip bir oyuncu bulunamadı."));
                return true;
            }

            Player t = Bukkit.getPlayer(args[1]);
            long amount = Long.parseLong(args[2]);

            targetUser.setCredit(targetUser.getCredit() + amount);

            sender.sendMessage(C.color(user.getRankType().getColor() + t.getName() + " &aaoyuncusunun kredi değeri &f" + NumberUtil.formatNumberByDecimal(Double.parseDouble(args[2])) + " &aakadar eklendi."));
            t.sendMessage(C.color("&aaKredi değerinize &f" + NumberUtil.formatNumberByDecimal(Double.parseDouble(args[2])) + " &aakadar eklendi."));
            user.save();
            return true;
        }
        return true;
    }
}
