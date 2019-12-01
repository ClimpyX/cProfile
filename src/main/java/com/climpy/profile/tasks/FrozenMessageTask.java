package com.climpy.profile.tasks;

import com.climpy.profile.ProfilePlugin;
import com.climpy.profile.managers.FrozenManager;
import com.climpy.profile.user.User;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

public class FrozenMessageTask extends BukkitRunnable {
  private UUID uniqueUUID;
  
  public FrozenMessageTask(UUID uniqueUUID) { this.uniqueUUID = uniqueUUID; }
  
  public synchronized int getTaskId() throws IllegalStateException { return super.getTaskId(); }

  
  public void run() {
    String STRING_SEPARATOR = " \n";
    
    FrozenManager frozenManager = ProfilePlugin.getInstance().getFrozenManager();
    User targetUser = ProfilePlugin.getInstance().getUserManager().getUser(this.uniqueUUID);
    Player targetPlayer = Bukkit.getPlayer(this.uniqueUUID);
    
    if (targetPlayer == null || !targetUser.isFrozenStatus()) {
      frozenManager.removeFrozenPlayer(targetUser.getUniqueUUID());
      
      return;
    }

    targetPlayer.playSound(targetPlayer.getLocation(), Sound.NOTE_BASS, 1.0F, 1.0F);
    for (PotionEffect potionEffect : targetPlayer.getActivePotionEffects()) targetPlayer.removePotionEffect(potionEffect.getType());
    
    targetPlayer.setFlying(false);
    targetPlayer.setAllowFlight(false);
    
    targetPlayer.setSaturation(3.0F);
    targetPlayer.setFoodLevel(20);
    targetPlayer.setFireTicks(0);
    targetPlayer.setHealth(20.0D);
    
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(" \n");
    
    stringBuilder.append(ChatColor.WHITE).append("????").append(ChatColor.RED).append("?").append(ChatColor.WHITE).append("????").append(" \n");
    stringBuilder.append(ChatColor.WHITE).append("???").append(ChatColor.RED).append("?").append(ChatColor.GOLD).append("?").append(ChatColor.RED).append("?").append(ChatColor.WHITE).append("???").append(" \n");
    stringBuilder.append(ChatColor.WHITE).append("??").append(ChatColor.RED).append("?").append(ChatColor.GOLD).append("?").append(ChatColor.BLACK).append("?").append(ChatColor.GOLD).append("?").append(ChatColor.RED).append("?").append(ChatColor.WHITE).append("?? ").append(ChatColor.DARK_RED.toString()).append(ChatColor.BOLD).append("ÇIKIŞ YAPMAYINIZ! \n");
    stringBuilder.append(ChatColor.WHITE).append("??").append(ChatColor.RED).append("?").append(ChatColor.GOLD).append("?").append(ChatColor.BLACK).append("?").append(ChatColor.GOLD).append("?").append(ChatColor.RED).append("?").append(ChatColor.WHITE).append("?? ").append(ChatColor.YELLOW).append("Çıkış yaparsanız yasaklanacaksanız! \n");
    stringBuilder.append(ChatColor.WHITE).append("?").append(ChatColor.RED).append("?").append(ChatColor.GOLD).append("??").append(ChatColor.BLACK).append("?").append(ChatColor.GOLD).append("??").append(ChatColor.RED).append("?").append(ChatColor.WHITE).append("? ").append(ChatColor.YELLOW).append("Lütfen ").append(ChatColor.BOLD).append("Discord").append(ChatColor.YELLOW).append(" adresimize bağlanın:").append(" \n");
    stringBuilder.append(ChatColor.WHITE).append("?").append(ChatColor.RED).append("?").append(ChatColor.GOLD).append("?????").append(ChatColor.RED).append("?").append(ChatColor.WHITE).append("? ").append(ChatColor.GRAY).append("(").append("Discord: discord.gg/cprofile").append(") Kopyala, giriş yap. \n");
    stringBuilder.append(ChatColor.RED).append("?").append(ChatColor.GOLD).append("???").append(ChatColor.BLACK).append("?").append(ChatColor.GOLD).append("???").append(ChatColor.RED).append("?").append(" \n");
    stringBuilder.append(ChatColor.WHITE).append("?????????").append(" \n");
    
    stringBuilder.append(" \n");
    targetPlayer.sendMessage(ProfilePlugin.separatorCheck(stringBuilder));
  }
}
