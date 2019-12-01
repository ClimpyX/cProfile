package com.climpy.profile.commands.rank;

import com.climpy.profile.ProfilePlugin;
import com.climpy.profile.rank.RankType;
import com.climpy.profile.user.User;
import com.climpy.profile.user.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class ListRankCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        User userPlayer = ProfilePlugin.getInstance().getUserManager().getUser(sender.getName());
        Player player = Bukkit.getPlayer(sender.getName());
        if (sender instanceof Player) {
            if (!player.isOp() && !userPlayer.getRankType().isAboveOrEqual(RankType.ADMIN)) {
                sender.sendMessage(ChatColor.RED + "Bu komutu gerçekleştirmek için " + RankType.ADMIN.getDisplayName() + ChatColor.RED + " veya üzeri rütbe gerekiyor.");
                return true;
            }
        }

        String STRING_SEPARATOR = "\n";
        StringBuilder stringBuilder = new StringBuilder();

        UserManager userManager = ProfilePlugin.getInstance().getUserManager();
        for (RankType rankType : RankType.values()) {
            List<User> rankUser = (List)userManager.getUsers().values().stream().filter(user -> (user.getRankType() == rankType)).collect(Collectors.toList());
            stringBuilder.append(ChatColor.GRAY + "- ").append(rankType.getPrefix()).append(rankType.getColor()).append(rankType.getDisplayName()).append(rankType.getSuffix()).append(ChatColor.GRAY).append(", ").append(rankUser.size()).append(" kişi(ler)");
            stringBuilder.append("\n");
        }

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&dRütbeler kullanımlarına göre global olarak listelendi;"));
        sender.sendMessage(ProfilePlugin.separatorCheck(stringBuilder));
        return true;
    }
}
