package com.climpy.profile.commands.rank;

import com.climpy.profile.ProfilePlugin;
import com.climpy.profile.rank.RankType;
import com.climpy.profile.user.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;

public class SetRankCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        User user = ProfilePlugin.getInstance().getUserManager().getUser(sender.getName());
        Player player = Bukkit.getPlayer(sender.getName());
        if (sender instanceof Player) {
            if (!player.isOp() && !user.getRankType().isAboveOrEqual(RankType.ADMIN)) {
                sender.sendMessage(ChatColor.RED + "Bu komutu gerçekleştirmek için " + RankType.ADMIN.getDisplayName() + ChatColor.RED + " veya üzeri rütbe gerekiyor.");
                return true;
            }
        }

        if (args.length < 2) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bDoğru Kullanım: &6/" + label + " [Oyuncu-Adı] <RankAdı>"));
            sender.sendMessage(" ");
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Rütbe grupları: &f" + RankType.getAllRanks()));
            return true;
        }

        User targetUser = ProfilePlugin.getInstance().getUserManager().getUser(args[0]);

        if (targetUser == null) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f" + args[0] + " &cismine sahip bir oyuncu bulunamadı."));
            return true;
        }

        RankType rankType;

        try {
            rankType = RankType.valueOf(args[1].toUpperCase(Locale.ENGLISH));
        } catch (Exception e) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cGirilen &f" + args[1] + " &crütbesi bulunamadı. "));
            return true;
        }

        Player t = Bukkit.getPlayer(args[0]);

        if (rankType.ordinal() <= user.getRankType().ordinal() && !player.isOp()) {
            sender.sendMessage(ChatColor.RED + "Size eşit veya yüksek bir rütbeyi başkasına veremezsiniz.");
            return true;
        }


            RankType previousRank = targetUser.getRankType();
            targetUser.setRankType(rankType);

            if (targetUser != user) {
                sender.sendMessage(ChatColor.GREEN + "Seçilen oyuncu " + rankType.getColor() + rankType.getDisplayName() + ChatColor.GREEN + " sıralamasına " + (previousRank.isAboveOrEqual(RankType.getRankOrDefault(rankType.getRankName())) ? "düşürüldü" : "yükseltildi") + ".");
                user.save();
            }

            t.sendMessage(ChatColor.GREEN + "Rütbeniz " + rankType.getColor() + rankType.getDisplayName() + ChatColor.GREEN + " sıralamasına " + (
                    previousRank.isAboveOrEqual(RankType.getRankOrDefault(rankType.getRankName())) ? "düşürüldü" : "yükseltildi") + ".");

            user.save();
            return true;
        }
    }
