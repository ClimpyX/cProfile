package com.climpy.profile.listeners;

import com.climpy.profile.ProfilePlugin;
import com.climpy.profile.managers.FrozenManager;
import com.climpy.profile.managers.StaffModeManager;
import com.climpy.profile.rank.RankType;
import com.climpy.profile.user.User;
import com.climpy.profile.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

public class StaffModeListener implements Listener {
  @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
  private void onPlayerInteractEvent(PlayerInteractEvent event) {
    Player eventPlayer = event.getPlayer();
    Action action = event.getAction();
    
    StaffModeManager staffModeManager = ProfilePlugin.getInstance().getStaffModeManager();
    User user = ProfilePlugin.getInstance().getUserManager().getUser(eventPlayer.getUniqueId());
    
    if (event.hasItem() && (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) &&
      user.getRankType().isAboveOrEqual(RankType.MOD) && user.isStaffMode()) {
      event.setCancelled(true);
      
      ItemStack itemStack = event.getItem(); if (itemStack == null)
        return;  ItemMeta itemMeta = itemStack.getItemMeta(); if (itemMeta == null)
        return; 
      String randomTeleportItemName = ChatColor.AQUA + "Rastgele Teleportasyon";
      String vanishItemName = ChatColor.AQUA + "Görünmezlik: ";
      
      if (itemMeta.getDisplayName().equals(randomTeleportItemName) && itemStack.getType() == Material.WATCH) {
        if (Bukkit.getOnlinePlayers().size() <= 1) {
          eventPlayer.sendMessage(ChatColor.RED + "Işınlanacak herhangi bir oyuncu bulunamadı.");
          
          return;
        } 
        Player randomPlayer = staffModeManager.getRandomPlayer(eventPlayer, true);
        if (randomPlayer == null) {
          eventPlayer.sendMessage(ChatColor.RED + "Işınlanacak oyunu bulunamadı.");
          
          return;
        } 
        String playerName = randomPlayer.getName();
        Location playerLocation = randomPlayer.getLocation();
        eventPlayer.teleport(playerLocation);
        
        eventPlayer.sendMessage(ChatColor.GOLD + "Işınlanma işlemi " + ChatColor.RED + playerName + ChatColor.GOLD + ".");
        
        return;
      } 
      boolean newVanishStatus = !user.isVanishStatus();
      ItemStack vanishItem = ItemUtils.createItem(Material.INK_SACK, !newVanishStatus ? 10 : 8);
      if (itemMeta.getDisplayName().equals(vanishItemName + (!newVanishStatus ? "Açık" : "Kapalı")) && itemStack.getData().equals(vanishItem.getData())) {
        staffModeManager.setVanishMode(user.getUniqueUUID(), newVanishStatus);
      }
    } 
  }


  
  @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
  public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event) {
    Entity entity = event.getRightClicked();
    
    if (entity instanceof Player) {
      Player eventPlayer = event.getPlayer();
      Player targetPlayer = (Player)event.getRightClicked();

      User user = ProfilePlugin.getInstance().getUserManager().getUser(eventPlayer.getUniqueId());
      User targetUser = ProfilePlugin.getInstance().getUserManager().getUser(targetPlayer.getUniqueId());
      
      if (user.getRankType().isAboveOrEqual(RankType.MOD) && user.isStaffMode()) {
        event.setCancelled(true);
        
        ItemStack itemStack = eventPlayer.getItemInHand(); if (itemStack == null)
          return;  ItemMeta itemMeta = itemStack.getItemMeta(); if (itemMeta == null)
          return; 
        String freezeItemName = ChatColor.RED + "Kişiyi Dondur";
        String inspectItemName = ChatColor.AQUA + "Muayene Aleti";
        
        FrozenManager frozenManager = ProfilePlugin.getInstance().getFrozenManager();
        if (itemMeta.getDisplayName().equals(freezeItemName) && itemStack.getType() == Material.CARPET) {
          frozenManager.setFrozenPlayer((CommandSender)eventPlayer, targetUser.getUniqueUUID());
          
          return;
        } 
        if (itemMeta.getDisplayName().equals(inspectItemName) && itemStack.getType() == Material.BOOK) {
          if (targetUser.isStaffMode()) {
            eventPlayer.sendMessage(ChatColor.RED + "Hedef kişi üzerinde yetkili modu aktif olarak gözükmektedir. Bu yüzden, şu anda inventory görüntülenemez.");
            
            return;
          } 
          PlayerInventory playerInventory1 = targetPlayer.getInventory();
          for (int i = 0; i < 35; i++) {
            ItemStack inventoryItem = playerInventory1.getItem(i);
            if (inventoryItem != null) {
              break;
            }
            if (i == 34) {
              eventPlayer.sendMessage(ChatColor.RED + "Hedef kişinin envanteri boş durumda.");
              
              return;
            } 
          } 
          
          eventPlayer.openInventory(StaffModeManager.InspectMenu.createInspectMenu(user.getUniqueUUID(), targetUser.getUniqueUUID()));
          eventPlayer.sendMessage(ChatColor.YELLOW + "Envanteri açılan kişi: " + targetUser.getRankType().getColor() + targetUser.getName());
        } 
      } 
    } 
  }
  
  @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
  public void onEntityTarget(EntityTargetLivingEntityEvent event) {
    LivingEntity livingEntity = event.getTarget();
    if (livingEntity instanceof Player) {
      Player eventPlayer = ((Player)livingEntity).getPlayer();
      User eventUser = ProfilePlugin.getInstance().getUserManager().getUser(eventPlayer.getUniqueId());
      if (eventUser.getRankType().isAboveOrEqual(RankType.MOD) && eventUser.isStaffMode()) {
        event.setCancelled(true);
      }
    } 
  }

  
  @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
  public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
    Entity eventEntity = event.getDamager();
    if (eventEntity instanceof Player) {
      Player eventPlayer = ((Player)eventEntity).getPlayer();
      User eventUser = ProfilePlugin.getInstance().getUserManager().getUser(eventPlayer.getUniqueId());
      if (eventUser.getRankType().isAboveOrEqual(RankType.MOD) && eventUser.isStaffMode()) {
        event.setCancelled(true);
      }
    } 
  }

  
  @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
  public void onEntityDamageEvent(EntityDamageEvent event) {
    Entity eventEntity = event.getEntity();
    
    if (eventEntity instanceof Player) {
      Player eventPlayer = ((Player)eventEntity).getPlayer();
      User eventUser = ProfilePlugin.getInstance().getUserManager().getUser(eventPlayer.getUniqueId());
      if (eventUser.getRankType().isAboveOrEqual(RankType.MOD) && eventUser.isStaffMode()) {
        event.setCancelled(true);
      }
    } 
  }

  
  @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
  public void onBlockBreakEvent(BlockBreakEvent event) {
    Player eventPlayer = event.getPlayer();
    Block block = event.getBlock();
    
    User eventUser = ProfilePlugin.getInstance().getUserManager().getUser(eventPlayer.getUniqueId());
    if (eventUser.getRankType().isAboveOrEqual(RankType.MOD) && eventUser.isStaffMode()) {
      if (block == null)
        return;  event.setCancelled(true);
    } 
  }

  
  @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
  public void onBlockPlaceEvent(BlockPlaceEvent event) {
    Player eventPlayer = event.getPlayer();
    Block block = event.getBlock();
    
    User eventUser = ProfilePlugin.getInstance().getUserManager().getUser(eventPlayer.getUniqueId());
    if (eventUser.getRankType().isAboveOrEqual(RankType.MOD) && eventUser.isStaffMode()) {
      if (block == null)
        return;  event.setCancelled(true);
    } 
  }

  
  @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
  public void onPlayerDropItemEvent(PlayerDropItemEvent event) {
    Player eventPlayer = event.getPlayer();
    User eventUser = ProfilePlugin.getInstance().getUserManager().getUser(eventPlayer.getUniqueId());
    
    if (eventUser.getRankType().isAboveOrEqual(RankType.MOD) && eventUser.isStaffMode()) {
      event.setCancelled(true);
    }
  }

  
  @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
  public void onPlayerPickupItemEvent(PlayerPickupItemEvent event) {
    Player eventPlayer = event.getPlayer();
    User eventUser = ProfilePlugin.getInstance().getUserManager().getUser(eventPlayer.getUniqueId());
    
    if (eventUser.getRankType().isAboveOrEqual(RankType.MOD) && eventUser.isStaffMode()) {
      event.setCancelled(true);
    }
  }

  
  @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
  public void onFoodLevelChangeEvent(FoodLevelChangeEvent event) {
    HumanEntity humanEntity = event.getEntity();
    
    if (humanEntity instanceof Player) {
      User eventUser = ProfilePlugin.getInstance().getUserManager().getUser(humanEntity.getUniqueId());
      
      if (eventUser.getRankType().isAboveOrEqual(RankType.MOD) && eventUser.isStaffMode()) {
        event.setCancelled(true);
      }
    } 
  }

  
  @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
  public void onInventoryClick(InventoryClickEvent event) {
    Player eventPlayer = (Player)event.getWhoClicked();
    User eventUser = ProfilePlugin.getInstance().getUserManager().getUser(eventPlayer.getUniqueId());
    Inventory clickedInventory = event.getInventory();
    
    if (clickedInventory != null && 
      eventUser.isStaffMode()) event.setCancelled(true); 
  }
}
