package com.climpy.profile.commands.staff;

import com.climpy.profile.ProfilePlugin;
import com.climpy.profile.managers.StaffModeManager;
import com.climpy.profile.rank.RankType;
import com.climpy.profile.user.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StaffModeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        Player player = Bukkit.getPlayer(sender.getName());
        if (sender instanceof Player) {
            User user = ProfilePlugin.getInstance().getUserManager().getUser(player.getUniqueId());
            if (!user.getRankType().isAboveOrEqual(RankType.BUILDTEAM)) {
                sender.sendMessage(ChatColor.RED + "Bu işlem için " + RankType.BUILDTEAM
                        .getColor() + RankType.BUILDTEAM.getDisplayName() + ChatColor.RED + " ve üzeri rütbe gereklidir.");

                return true;
            }

            StaffModeManager staffModeManager = ProfilePlugin.getInstance().getStaffModeManager();

            boolean newStaffModeStatus = !user.isStaffMode();
            staffModeManager.setStaffMode(user.getUniqueUUID(), newStaffModeStatus);
            sender.sendMessage(ChatColor.GOLD + "Moderatör Modu: " + (newStaffModeStatus ? (ChatColor.DARK_GREEN + "Açık") : (ChatColor.DARK_RED + "Kapalı")));

            return true;
        }
        sender.sendMessage("Bu islem sadece oyundan yurutelebilir.");
        return true;
    }
}
