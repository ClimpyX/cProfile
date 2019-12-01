package com.climpy.profile.listeners;

import com.climpy.profile.ProfilePlugin;
import com.climpy.profile.managers.FrozenManager;
import com.climpy.profile.rank.RankType;
import com.climpy.profile.user.User;
import com.climpy.profile.user.UserManager;
import com.climpy.profile.utils.fanciful.FancyMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.*;

public class FrozenListener implements Listener {
  @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
  public void onBlockPlaceEvent(BlockPlaceEvent event) {
    Player eventPlayer = event.getPlayer();
    User user = ProfilePlugin.getInstance().getUserManager().getUser(eventPlayer.getUniqueId());
    if (user.isFrozenStatus()) {
      Block block = event.getBlock();
      if (block == null)
        return; 
      eventPlayer.sendMessage(ChatColor.RED + "Blok koyamazsın, dondurulmuş durumdasın.");
      event.setCancelled(true);
    } 
  }
  
  @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
  public void onBlockBreakEvent(BlockBreakEvent event) {
    Player eventPlayer = event.getPlayer();
    User user = ProfilePlugin.getInstance().getUserManager().getUser(eventPlayer.getUniqueId());
    if (user.isFrozenStatus()) {
      Block block = event.getBlock();
      if (block == null)
        return; 
      eventPlayer.sendMessage(ChatColor.RED + "Blok kıramazsınız, donduruldunuz.");
      event.setCancelled(true);
    } 
  }
  
  @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
  public void onProjectileLaunch(ProjectileLaunchEvent event) {
    Projectile projectile = event.getEntity();
    if (projectile instanceof Player) {
      Player eventPlayer = (Player)event.getEntity().getShooter();
      User user = ProfilePlugin.getInstance().getUserManager().getUser(eventPlayer.getUniqueId());
      
      if (user.isFrozenStatus()) {
        eventPlayer.sendMessage(ChatColor.RED + "Öğe ile etkileşim kuramazsanız.");
        event.setCancelled(true);
      } 
    } 
  }
  
  @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
  public void onPlayerPickupItemEvent(PlayerPickupItemEvent event) {
    Player eventPlayer = event.getPlayer();
    User user = ProfilePlugin.getInstance().getUserManager().getUser(eventPlayer.getUniqueId());
    if (user.isFrozenStatus()) event.setCancelled(true); 
  }
  
  @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
  public void onPlayerInteractEvent(PlayerInteractEvent event) {
    Player eventPlayer = event.getPlayer();
    User user = ProfilePlugin.getInstance().getUserManager().getUser(eventPlayer.getUniqueId());
    if (user.isFrozenStatus()) event.setCancelled(true); 
  }
  
  @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
  public void onPlayerItemDamageEvent(PlayerBedEnterEvent event) {
    Player eventPlayer = event.getPlayer();
    User user = ProfilePlugin.getInstance().getUserManager().getUser(eventPlayer.getUniqueId());
    
    if (user.isFrozenStatus()) {
      eventPlayer.sendMessage(ChatColor.RED + "Yatakta yatamazsın.");
      event.setCancelled(true);
    } 
  }
  
  @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
  public void onPlayerBucketEmptyEvent(PlayerBucketEmptyEvent event) {
    Player eventPlayer = event.getPlayer();
    User user = ProfilePlugin.getInstance().getUserManager().getUser(eventPlayer.getUniqueId());
    if (user.isFrozenStatus()) event.setCancelled(true); 
  }
  
  @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
  public void onPlayerBucketFillEvent(PlayerBucketFillEvent event) {
    Player eventPlayer = event.getPlayer();
    User user = ProfilePlugin.getInstance().getUserManager().getUser(eventPlayer.getUniqueId());
    if (user.isFrozenStatus()) event.setCancelled(true); 
  }
  
  @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
  public void onPlayerChangedWorldEvent(PlayerChangedWorldEvent event) {
    Player eventPlayer = event.getPlayer();
    User user = ProfilePlugin.getInstance().getUserManager().getUser(eventPlayer.getUniqueId());
    if (user.isFrozenStatus()) {
      World oldWorld = eventPlayer.getWorld();
      
      int oldX = eventPlayer.getLocation().getBlockX();
      int oldY = eventPlayer.getLocation().getBlockY();
      int oldZ = eventPlayer.getLocation().getBlockZ();
      
      float oldPitch = eventPlayer.getLocation().getPitch();
      float oldYaw = eventPlayer.getLocation().getYaw();
      
      Location newLocation = new Location(oldWorld, oldX, oldY, oldZ, oldYaw, oldPitch);
      eventPlayer.teleport(newLocation, PlayerTeleportEvent.TeleportCause.PLUGIN);
      eventPlayer.sendMessage(ChatColor.RED + "Dünya değiştiremezsin, donmuş durumdasın.");
    } 
  }
  
  @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
  public void onPlayerDropItemEvent(PlayerDropItemEvent event) {
    Player eventPlayer = event.getPlayer();
    User user = ProfilePlugin.getInstance().getUserManager().getUser(eventPlayer.getUniqueId());
    if (user.isFrozenStatus()) event.setCancelled(true); 
  }
  
  @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
  public void onPlayerGameModeChangeEvent(PlayerGameModeChangeEvent event) {
    Player eventPlayer = event.getPlayer();
    User user = ProfilePlugin.getInstance().getUserManager().getUser(eventPlayer.getUniqueId());
    if (user.isFrozenStatus()) event.setCancelled(true); 
  }
  
  @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
  public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event) {
    Player eventPlayer = event.getPlayer();
    User user = ProfilePlugin.getInstance().getUserManager().getUser(eventPlayer.getUniqueId());
    if (user.isFrozenStatus()) event.setCancelled(true); 
  }
  
  @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
  public void onPlayerLevelChangeEvent(PlayerLevelChangeEvent event) {
    Player eventPlayer = event.getPlayer();
    User user = ProfilePlugin.getInstance().getUserManager().getUser(eventPlayer.getUniqueId());
    if (user.isFrozenStatus()) {
      int oldLevel = event.getOldLevel();
      
      eventPlayer.giveExpLevels(0);
      eventPlayer.setLevel(oldLevel);
    } 
  }
  
  @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
  public void onPlayerPortalEvent(PlayerPortalEvent event) {
    Player eventPlayer = event.getPlayer();
    User user = ProfilePlugin.getInstance().getUserManager().getUser(eventPlayer.getUniqueId());
    if (user.isFrozenStatus()) event.setCancelled(true); 
  }
  
  @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
  public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
    Entity entity = event.getEntity();
    if (entity instanceof Player) {
      Player attackerPlayer = (Player) event.getDamager();
      Player targetPlayer = (Player) event.getEntity();
      
      User attackerUser = ProfilePlugin.getInstance().getUserManager().getUser(attackerPlayer.getUniqueId());
      User targetUser = ProfilePlugin.getInstance().getUserManager().getUser(targetPlayer.getUniqueId());
      
      if (attackerUser.isFrozenStatus()) {
        attackerPlayer.sendMessage(ChatColor.RED + "Donmuş iken oyunculara saldıramazsın.");
        
        return;
      } 
      if (targetUser.isFrozenStatus()) {
        ChatColor rankColor = targetUser.getRankType().getColor();
        attackerPlayer.sendMessage(rankColor + targetUser.getName() + ChatColor.RED + " Şu anda donmuş, ona saldıramazsın.");
      } 
    } 
  }
  
  @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
  public void onPlayerMoveBlockEvent(PlayerMoveEvent event) {
    Player eventPlayer = event.getPlayer();
    User user = ProfilePlugin.getInstance().getUserManager().getUser(eventPlayer.getUniqueId());
    FrozenManager frozenManager = ProfilePlugin.getInstance().getFrozenManager();
    
    if (user.isFrozenStatus()) {
      frozenManager.addFrozenPlayer(user.getUniqueUUID());
      event.setCancelled(true);
    } 
  }
  
  @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
  public void onPlayerMoveEvent(PlayerMoveEvent event) {
    Player eventPlayer = event.getPlayer();
    Location getFrom = event.getFrom();
    Location getTo = event.getTo();
    
    User user = ProfilePlugin.getInstance().getUserManager().getUser(eventPlayer.getUniqueId());
    if (user.isFrozenStatus() && (getFrom.getX() != getTo.getX() || getFrom.getZ() != getTo.getZ())) {
      eventPlayer.sendMessage(ChatColor.RED + "İşlem reddedildi, şuan donduruldu.");
      eventPlayer.teleport(getFrom.setDirection(getTo.getDirection()));
    } 
  }
  
  @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
  public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
    Player eventPlayer = event.getPlayer();
    User user = ProfilePlugin.getInstance().getUserManager().getUser(eventPlayer.getUniqueId());
    if (user.isFrozenStatus()) {
      eventPlayer.sendMessage(ChatColor.RED + "Şu an komut kullanamazsınız.");
      event.setCancelled(true);
    } 
  }
  
  @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
  public void onPlayerQuitEvent(PlayerQuitEvent event) {
    Player eventPlayer = event.getPlayer();
    
    UserManager userManager = ProfilePlugin.getInstance().getUserManager();
    User user = userManager.getUser(eventPlayer.getUniqueId());
    
    if (user.isFrozenStatus()) {
      FancyMessage fancyMessage = new FancyMessage();
      
      fancyMessage.text(ChatColor.DARK_RED.toString() + ChatColor.BOLD + user.getName() + " donmuş haldeyken çıkış yaptı.");
      fancyMessage.tooltip(ChatColor.GREEN + user.getName() + " için süresiz uzaklaştırma uygula.");
      fancyMessage.command("/ban " + user.getName() + " Ekran Paylaşımını Reddetme (SS)");
      
      for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
        User onlineUser = userManager.getUser(onlinePlayer.getUniqueId());
        RankType rankType = onlineUser.getRankType();
        
        if (rankType.isAboveOrEqual(RankType.MOD)) {
          fancyMessage.send(onlinePlayer);
        }
      } 
    } 
  }
  
  @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
  public void onPlayerKickEvent(PlayerKickEvent event) {
    Player eventPlayer = event.getPlayer();
    
    UserManager userManager = ProfilePlugin.getInstance().getUserManager();
    User user = userManager.getUser(eventPlayer.getUniqueId());
    
    if (user.isFrozenStatus()) {
      FancyMessage fancyMessage = new FancyMessage();
        fancyMessage.text(ChatColor.DARK_RED.toString() + ChatColor.BOLD + user.getName() + " donmuş haldeyken çıkış yaptı.");
      fancyMessage.tooltip(ChatColor.GREEN + user.getName() + " için süresiz uzaklaştırma uygula.");
      fancyMessage.command("/ban " + user.getName() + " Ekran Paylaşımını Reddetme (SS)");
      
      for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
        User onlineUser = userManager.getUser(onlinePlayer.getUniqueId());
        RankType rankType = onlineUser.getRankType();
        
        if (rankType.isAboveOrEqual(RankType.MOD)) {
          fancyMessage.send(onlinePlayer);
        }
      } 
    } 
  }
  
  @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
  public void onPlayerJoinEvent(PlayerJoinEvent event) {
    Player eventPlayer = event.getPlayer();
    
    UserManager userManager = ProfilePlugin.getInstance().getUserManager();
    User user = userManager.getUser(eventPlayer.getUniqueId());
    
    if (user.isFrozenStatus()) {
      FancyMessage fancyMessage = new FancyMessage();
      fancyMessage.text(ChatColor.DARK_RED.toString() + ChatColor.BOLD + user.getName() + " katıldı, fakat donmuş durumda.");
      fancyMessage.tooltip(ChatColor.GREEN + user.getName() + " için süresiz uzaklaştırma uygula.");
      fancyMessage.command("/ban " + user.getName() + " Ekran Paylaşımını Reddetme (SS)");
      
      for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
        User onlineUser = userManager.getUser(onlinePlayer.getUniqueId());
        RankType rankType = onlineUser.getRankType();
        
        if (rankType.isAboveOrEqual(RankType.MOD))
          fancyMessage.send(onlinePlayer); 
      } 
    } 
  }
}
