package com.climpy.profile.commands.symbol;

import com.climpy.profile.ProfilePlugin;
import com.climpy.profile.rank.RankType;
import com.climpy.profile.symbol.SymbolType;
import com.climpy.profile.user.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;

public class SetSymbolCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        User user = ProfilePlugin.getInstance().getUserManager().getUser(sender.getName());
        Player player = (Player) sender;

        if (sender instanceof Player && !player.isOp() && !user.getRankType().isAboveOrEqual(RankType.ADMIN)) {
            sender.sendMessage(ChatColor.RED + "Bu komutu gerçekleştirmek için " + RankType.ADMIN.getDisplayName() + ChatColor.RED + " veya üzeri rütbe gerekiyor.");
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&cDoğru Kullanım: /" + label + " [Oyuncu-Adı] <symbolID>"));
            return true;
        }

        User targetUser = ProfilePlugin.getInstance().getUserManager().getUser(args[0]);

        if (targetUser == null) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&f" + args[0] + " &cismine sahip bir oyuncu bulunamadı."));
            return true;
        }

        SymbolType symbolType;

        try {
            symbolType = SymbolType.valueOf(args[1].toUpperCase(Locale.ENGLISH));
        } catch (Exception e) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&cGirilen &f" + args[1] + " &csembolü bulunamadı. "));
            return true;
        }

        Player t = Bukkit.getPlayer(args[0]);

        targetUser.setSymbolType(symbolType);
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',user.getRankType().getColor() + t.getName() + " &aoyuncusunun sembolü &f" + symbolType.getDisplayName() + " &aolarak güncellendi."));
        t.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aSembolünüz &f" + symbolType.getDisplayName() + " &aolarak güncellendi."));
        user.save();
        return true;
    }
}
