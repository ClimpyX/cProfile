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

public class RemoveCoinsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command label, String s, String[] args) {
        Player player = (Player) sender;
        User user = ProfilePlugin.getInstance().getUserManager().getUser(sender.getName());

        if (sender instanceof Player && !player.isOp() && user.getRankType().isAboveOrEqual(RankType.MODPLUS)) {
            sender.sendMessage(ChatColor.RED + "Bu komutu gerçekleştirmek için " + RankType.MODPLUS.getDisplayName() + ChatColor.RED + " veya üzeri rütbe gerekiyor.");
            return true;
        }

        if(args.length < 2) {
            sender.sendMessage(C.color("&bDoğru Kullanım: &6/" + label.getName() + " <oyuncu-Adı> [Para Miktarı]"));
            return true;
        }

        User targetUser = ProfilePlugin.getInstance().getUserManager().getUser(args[0]);

        if (targetUser == null) {
            sender.sendMessage(C.color("&f" + args[1] + " &cismine sahip bir oyuncu bulunamadı."));
            return true;
        }

        Player t = Bukkit.getPlayer(args[0]);
        long amount = Long.parseLong(args[1]);

        targetUser.setCredit(targetUser.getCredit() - amount);

        sender.sendMessage(C.color(user.getRankType().getColor() + t.getName() + " &aoyuncusunun kredi değeri &f" + NumberUtil.formatNumberByDecimal(Double.parseDouble(args[1])) + " &akadar silindi."));
        t.sendMessage(C.color("&aKredi değeriniz &f" + NumberUtil.formatNumberByDecimal(Double.parseDouble(args[1])) + " &akadar silindi."));
        user.save();
        return true;
    }
}
