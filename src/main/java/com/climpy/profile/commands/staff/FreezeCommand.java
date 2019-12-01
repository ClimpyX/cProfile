package com.climpy.profile.commands.staff;

import com.climpy.profile.ProfilePlugin;
import com.climpy.profile.managers.FrozenManager;
import com.climpy.profile.rank.RankType;
import com.climpy.profile.user.User;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

public class FreezeCommand implements CommandExecutor, TabExecutor {
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    Player player = Bukkit.getPlayer(sender.getName());
    if (sender instanceof Player) {
      User user = ProfilePlugin.getInstance().getUserManager().getUser(player.getUniqueId());
      if (!user.getRankType().isAboveOrEqual(RankType.MOD)) {
        sender.sendMessage(ChatColor.RED + "Bu işlem için " + RankType.MOD
            .getColor() + RankType.MOD.getDisplayName() + ChatColor.RED + " ve üzeri rütbe gereklidir.");
        
        return true;
      } 
    } 
    
    if (args.length < 1) {
      sender.sendMessage(ChatColor.RED + "Kullanım: /" + label + " <oyuncuAdı/hepsi>");
      return true;
    } 
    
    FrozenManager frozenManager = ProfilePlugin.getInstance().getFrozenManager();
    if (args[0].equals("all") || args[0].equals("hepsi") || args[0].equals("herkes")) {
      long newFrozenTime = System.currentTimeMillis() + frozenManager.getDefaultFrozenDuration();
      frozenManager.setFrozenPlayer(sender, Long.valueOf(newFrozenTime));
      return true;
    } 
    Player targetPlayer = Bukkit.getPlayerExact(args[0]);
    if (targetPlayer == null) {
      sender.sendMessage(String.format(ChatColor.RED + "Oyuncu bulunamadı.", new Object[] { args[0] }));
      return true;
    } 
    
    if (targetPlayer == player) {
      sender.sendMessage(ChatColor.RED + "Kendi kendinizi donduramazsınız.");
      return true;
    }

    User targetUser = ProfilePlugin.getInstance().getUserManager().getUser(targetPlayer.getUniqueId());
    frozenManager.setFrozenPlayer(sender, targetUser.getUniqueUUID());
    
    if (targetUser.isFrozenStatus()) {
      Bukkit.dispatchCommand(sender, "msg " + targetPlayer.getName() + " Hile şüphesinden dolayı donduruldunuz. Lütfen talimatlara uyun ardından ise " +
              "adresine katılın. Eğer sunucudan çıkış yaparsanız, ekran paylaşımı durumunu reddetmeniz nedeniyle hesabınız üzerine kalıcı bir yasak uygulanacaktır.");
    }

    
    return true;
  }



  
  public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
    Validate.notNull(sender, "Sender boş olamaz");
    Validate.notNull(args, "Arguments boş olamaz");
    Validate.notNull(label, "Alias boş olamaz");
    
    if (args.length == 1) {
      List<String> onlinePlayers = new ArrayList<>();
      
      for (Player player : Bukkit.getOnlinePlayers()) {
        if (player.canSee(player) && player.getName().toLowerCase().startsWith(args[0].toLowerCase())) {
          onlinePlayers.add(player.getName());
        }
      } 
      
      return onlinePlayers;
    }
    return null;
  }

}
