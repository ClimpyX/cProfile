package com.climpy.profile.managers;

import com.climpy.profile.ProfilePlugin;
import com.climpy.profile.rank.RankType;
import com.climpy.profile.tasks.FrozenMessageTask;
import com.climpy.profile.user.User;
import com.climpy.profile.user.UserManager;
import com.climpy.profile.utils.TimeUtils;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class FrozenManager {
  private String STRING_SEPARATOR = "\n";
  private final ProfilePlugin profilePlugin;
  private Map<UUID, FrozenMessageTask> frozenAlertTask;
  private HashSet<UUID> frozenPlayers;
  private long defaultFrozenDuration;
  private boolean serverFrozen;
  
  public String getSTRING_SEPARATOR() { getClass(); return "\n"; }
  public ProfilePlugin getProfilePlugin() { return this.profilePlugin; }

  public Map<UUID, FrozenMessageTask> getFrozenAlertTask() { return this.frozenAlertTask; }
  public HashSet<UUID> getFrozenPlayers() { return this.frozenPlayers; }
  
  public long getDefaultFrozenDuration() { return this.defaultFrozenDuration; }
  public boolean isServerFrozen() { return this.serverFrozen; } public FrozenManager(ProfilePlugin basicPlugin) { this.STRING_SEPARATOR = "\n"; this.defaultFrozenDuration = TimeUnit.MINUTES.toMillis(120L); this.serverFrozen = false;

    
    this.profilePlugin = basicPlugin;
    this.frozenPlayers = new HashSet<>();
    this.frozenAlertTask = new HashMap<>(); }

  
  public void setFrozenPlayer(CommandSender senderPlayer, UUID targetUUID) {
    User targetUser = ProfilePlugin.getInstance().getUserManager().getUser(targetUUID);
    StaffModeManager staffManager = ProfilePlugin.getInstance().getStaffModeManager();
    
    Player targetPlayer = Bukkit.getPlayer(targetUUID);
    ChatColor rankColor = targetUser.getRankType().getColor();
    
    if (this.frozenPlayers.contains(targetUUID) && !targetUser.isFrozenStatus()) {
      addFrozenPlayer(targetUUID);
      
      senderPlayer.sendMessage(ChatColor.GREEN + targetUser.getName() + " adlı oyuncu donduruldu.");
      for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
        if (onlinePlayer == senderPlayer)
          continue; 
        User onlineUser = ProfilePlugin.getInstance().getUserManager().getUser(onlinePlayer.getUniqueId());
        if (onlineUser.getRankType().isAboveOrEqual(RankType.MOD)) onlinePlayer.sendMessage(rankColor + targetUser.getName() + ChatColor.GOLD + " adlı oyuncu donduruldu.");
      
      } 
      targetUser.setFrozenStatus(true);
      if (targetUser.getRankType().isAboveOrEqual(RankType.MOD)) {
        if (targetUser.isStaffMode()) {
          targetPlayer.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "Dondurulma sebebi ile yetkili modu kapatıldı.");
          staffManager.setStaffMode(targetUser.getUniqueUUID(), false);
        } 
        
        if (targetUser.isVanishStatus()) {
          targetPlayer.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "Dondurulma sebebi ile görünmezlik modu kapatıldı.");
          staffManager.setVanishMode(targetUser.getUniqueUUID(), false);
        } 
        
        if (targetUser.isStaffChat()) {
          targetPlayer.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "Dondurulma sebebi ile yetkili sohbeti kapatıldı.");
          targetUser.setStaffChat(false);
        } 
      } 

      this.profilePlugin.getLogger().log(Level.INFO, targetUser.getName() + " adlı oyuncu donduruldu (Dondurucu: " + senderPlayer.getName() + ")");
    } else {
      removeFrozenPlayer(targetUUID);
      
      if (targetPlayer != null) {
        targetPlayer.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD + "Yetkili personel tarafından çözüldünüz.");
        targetPlayer.sendMessage(ChatColor.GREEN + "Artık hareket edebilir, etkileşime geçebilirsiniz.");
      } 
      
      senderPlayer.sendMessage(ChatColor.GREEN + targetUser.getName() + " artık donmuş durumda değil.");
      for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
        if (onlinePlayer == senderPlayer)
          continue; 
        User onlineUser = ProfilePlugin.getInstance().getUserManager().getUser(onlinePlayer.getUniqueId());
        if (onlineUser.getRankType().isAboveOrEqual(RankType.MOD)) onlinePlayer.sendMessage(rankColor + targetUser.getName() + ChatColor.GOLD + " adlı oyuncu çözüldü.");
      
      } 
      targetUser.setFrozenStatus(false);
      this.profilePlugin.getLogger().log(Level.INFO, targetUser.getName() + " artık donmuş durumda değil (Çözen:" + senderPlayer.getName());
    } 
  }
  
  public void setFrozenPlayer(CommandSender senderPlayer, Long frozenTime) {
    if (!this.serverFrozen) {
      for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
        User onlineUser = ProfilePlugin.getInstance().getUserManager().getUser(onlinePlayer.getUniqueId());
        if (!onlineUser.getRankType().isAboveOrEqual(RankType.MOD)) {
          if (this.frozenPlayers.contains(onlineUser.getUniqueUUID())) {
            this.frozenPlayers.add(onlinePlayer.getUniqueId());
          }
          if (!this.frozenAlertTask.containsKey(onlinePlayer.getUniqueId())) {
            FrozenMessageTask frozenAlertTask = new FrozenMessageTask(onlinePlayer.getUniqueId());
            frozenAlertTask.runTaskTimerAsynchronously((Plugin)ProfilePlugin.getInstance(), 0L, 100L);
            this.frozenAlertTask.put(onlinePlayer.getUniqueId(), frozenAlertTask);
          } 
          
          onlineUser.setFrozenStatus(true);
        } 
      } 

      
      senderPlayer.sendMessage(ChatColor.GREEN + "Tüm oyuncular " + ChatColor.BOLD + TimeUtils.getDurationWords(frozenTime.longValue() - System.currentTimeMillis(), true, true) + ChatColor.GREEN + " donduruldu.");
      
      for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
        if (onlinePlayer == senderPlayer)
          continue; 
        User onlineUser = ProfilePlugin.getInstance().getUserManager().getUser(onlinePlayer.getUniqueId());
        if (!onlineUser.getRankType().isAboveOrEqual(RankType.MOD)) {
          getClass();
          getClass();
          getClass(); getClass(); String stringBuilder = "\n" + ChatColor.RED.toString() + ChatColor.BOLD + "Üzgünüz! " + ChatColor.GOLD + TimeUtils.getDurationWords(frozenTime.longValue() - System.currentTimeMillis(), true, true) + " donduruldunuz." + "\n" + ChatColor.DARK_RED.toString() + ChatColor.BOLD + "* Bu işlem tüm oyunculara uygulanmıştır" + "\n" + "\n";

          
          onlinePlayer.sendMessage(stringBuilder); continue;
        } 
        onlinePlayer.sendMessage(ChatColor.GREEN + "Tüm oyuncular " + ChatColor.BOLD + TimeUtils.getDurationWords(frozenTime.longValue() - System.currentTimeMillis(), true, true) + ChatColor.GREEN + " donduruldu.");
      } 

      
      this.serverFrozen = true;
      this.profilePlugin.getLogger().log(Level.INFO, "Tüm oyuncular " + senderPlayer.getName() + " tarafından " + TimeUtils.getDurationWords(frozenTime.longValue() - System.currentTimeMillis(), true, true) + " donduruldu.");
    } else {
      for (Player player : Bukkit.getOnlinePlayers()) {
        clearServerFrozenPlayer(player.getUniqueId());
      }
      
      senderPlayer.sendMessage(ChatColor.GREEN + "Tüm oyuncular artık donmuş durumda değil.");
      
      for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
        if (onlinePlayer == senderPlayer)
          continue; 
        User onlineUser = ProfilePlugin.getInstance().getUserManager().getUser(onlinePlayer.getUniqueId());
        if (!onlineUser.getRankType().isAboveOrEqual(RankType.MOD)) {
          getClass(); getClass();
          
          getClass(); getClass(); String stringBuilder = "\n" + ChatColor.GOLD + "Tüm oyuncular başarıyla çözüldü! İyi eğlenceler." + "\n" + ChatColor.DARK_RED.toString() + ChatColor.BOLD + "* Bu işlem tüm oyunculara uygulanmıştır." + "\n" + "\n";

          
          onlinePlayer.sendMessage(stringBuilder); continue;
        } 
        onlinePlayer.sendMessage(ChatColor.GREEN + "Tüm oyuncular artık donmuş durumda değil.");
      } 

      
      this.serverFrozen = false;
      this.profilePlugin.getLogger().log(Level.INFO, "Tüm oyuncular " + senderPlayer.getName() + " tarafından çözüldü.");
    } 
  }
  
  public void addFrozenPlayer(UUID uniqueUUID) {
    if (!this.frozenPlayers.contains(uniqueUUID)) {
      this.frozenPlayers.add(uniqueUUID);
      
      if (!this.frozenAlertTask.containsKey(uniqueUUID)) {
        FrozenMessageTask frozenAlertTask = new FrozenMessageTask(uniqueUUID);
        frozenAlertTask.runTaskTimerAsynchronously((Plugin)ProfilePlugin.getInstance(), 0L, 100L);
        this.frozenAlertTask.put(uniqueUUID, frozenAlertTask);
      } 
      
      Player targetPlayer = Bukkit.getPlayer(uniqueUUID);
      targetPlayer.setGameMode(GameMode.SURVIVAL);
    } 
  }
  
  public void removeFrozenPlayer(UUID uniqueUUID) {
    if (!this.frozenPlayers.contains(uniqueUUID)) {
      this.frozenPlayers.remove(uniqueUUID);
      
      if (this.frozenAlertTask.containsKey(uniqueUUID)) {
        ((FrozenMessageTask)this.frozenAlertTask.get(uniqueUUID)).cancel();
        this.frozenAlertTask.remove(uniqueUUID);
      } 
    } 
  }
  
  public void clearServerFrozenPlayer(UUID uniqueUUID) {
      removeFrozenPlayer(uniqueUUID);
      
      UserManager userManager = ProfilePlugin.getInstance().getUserManager();
      User onlineUser = userManager.getUser(uniqueUUID);
      
      if (onlineUser.isFrozenStatus())
        onlineUser.setFrozenStatus(false); 
    } 
}
