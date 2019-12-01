package com.climpy.profile.managers;

import com.climpy.profile.ProfilePlugin;
import com.climpy.profile.rank.RankType;
import com.climpy.profile.user.User;
import com.climpy.profile.utils.ItemUtils;
import com.climpy.profile.utils.MiscUtils;
import com.climpy.profile.utils.TimeUtils;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class StaffModeManager {
  private final ProfilePlugin profilePlugin;
  private Map<UUID, ItemStack[]> inventoryContents;
  
  public void setInventoryContents(Map<UUID, ItemStack[]> inventoryContents) { this.inventoryContents = inventoryContents; } private Map<UUID, ItemStack[]> armorContents; private Set<UUID> staffMode; private Set<UUID> vanished; public void setArmorContents(Map<UUID, ItemStack[]> armorContents) { this.armorContents = armorContents; } public void setStaffMode(Set<UUID> staffMode) { this.staffMode = staffMode; } public void setVanished(Set<UUID> vanished) { this.vanished = vanished; }
  
  public ProfilePlugin getProfilePlugin() { return this.profilePlugin; }
  public Map<UUID, ItemStack[]> getInventoryContents() { return this.inventoryContents; } public Map<UUID, ItemStack[]> getArmorContents() { return this.armorContents; }
  public Set<UUID> getStaffMode() { return this.staffMode; } public Set<UUID> getVanished() { return this.vanished; }
  
  public StaffModeManager(ProfilePlugin profilePlugin) {
    this.profilePlugin = profilePlugin;
    
    this.staffMode = new HashSet<>();
    this.vanished = new HashSet<>();
    
    this.inventoryContents = (Map)new HashMap<>();
    this.armorContents = (Map)new HashMap<>();
  }
  
  public void setStaffMode(UUID targetUUID, boolean newStaffModeStatus) {
    User targetUser = ProfilePlugin.getInstance().getUserManager().getUser(targetUUID);
    targetUser.setStaffMode(newStaffModeStatus);
    
    Player targetPlayer = Bukkit.getPlayer(targetUUID);
    PlayerInventory targetInventory = targetPlayer.getInventory();
    
    if (targetUser.isStaffMode() && !this.staffMode.contains(targetUser.getUniqueUUID())) {
      this.staffMode.add(targetUser.getUniqueUUID());
      savePlayerInventory(targetUser.getUniqueUUID(), targetInventory);
      
      targetInventory.clear();
      targetInventory.setHelmet(null);
      targetInventory.setChestplate(null);
      targetInventory.setLeggings(null);
      targetInventory.setBoots(null);
      
      setStaffItems(targetUser.getUniqueUUID());
      setVanishMode(targetUser.getUniqueUUID(), true);
      
      if (targetPlayer.getGameMode() != GameMode.CREATIVE) {
        targetPlayer.setGameMode(GameMode.CREATIVE);
        targetPlayer.setAllowFlight(true);
        targetPlayer.setFlying(true);
      } 
      
      targetPlayer.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 2147483647, 0));
      
      return;
    } 
    this.staffMode.remove(targetUser.getUniqueUUID());
    setVanishMode(targetUser.getUniqueUUID(), false);
    
    targetPlayer.getInventory().clear();
    loadPlayerInventory(targetUser.getUniqueUUID(), targetInventory);
    
    if (targetPlayer.getGameMode() != GameMode.SURVIVAL) {
      targetPlayer.setGameMode(GameMode.SURVIVAL);
      targetPlayer.setAllowFlight(false);
      targetPlayer.setFlying(false);
    } 
    
    targetPlayer.removePotionEffect(PotionEffectType.NIGHT_VISION);
  }
  
  public void setVanishMode(UUID targetUUID, boolean newVanishStatus) {
    Player targetPlayer = Bukkit.getPlayer(targetUUID);
    PlayerInventory targetInventory = targetPlayer.getInventory();
    User targetUser = ProfilePlugin.getInstance().getUserManager().getUser(targetUUID);
    targetUser.setVanishStatus(newVanishStatus);
    
    if (targetUser.isVanishStatus() && !this.vanished.contains(targetUser.getUniqueUUID())) {
      this.vanished.add(targetUser.getUniqueUUID());
      
      for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
        User onlineUser = ProfilePlugin.getInstance().getUserManager().getUser(onlinePlayer.getUniqueId());
        if (onlinePlayer == targetPlayer) {
          continue;
        }
        if (!onlineUser.getRankType().isAboveOrEqual(RankType.CHAT_MOD)) {
          onlinePlayer.hidePlayer(targetPlayer);
        }
      } 
      
      if (targetUser.isStaffMode()) {
        targetInventory.setItem(8, setVanishItem(targetUser.getUniqueUUID()));
      }
      
      return;
    } 
    
    this.vanished.remove(targetUser.getUniqueUUID());
    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
      onlinePlayer.showPlayer(targetPlayer);
    }
    
    if (targetUser.isStaffMode()) {
      targetInventory.setItem(8, setVanishItem(targetUser.getUniqueUUID()));
    }
  }
  
  private void setStaffItems(UUID targetUUID) {
    Player targetPlayer = Bukkit.getPlayer(targetUUID);
    PlayerInventory targetInventory = targetPlayer.getInventory();
    
    targetInventory.setItem(0, ItemUtils.createItem(Material.COMPASS, ChatColor.AQUA + "Teleport Pusulası"));
    targetInventory.setItem(1, ItemUtils.createItem(Material.BOOK, ChatColor.AQUA + "Muayene Aleti"));
    targetInventory.setItem(2, ItemUtils.createItem(Material.WOOD_AXE, ChatColor.AQUA + "World Edit"));
    targetInventory.setItem(3, ItemUtils.createItem(Material.CARPET, ChatColor.RED + "Kişiyi Dondur", (short)1));
    targetInventory.setItem(7, ItemUtils.createItem(Material.WATCH, ChatColor.AQUA + "Rastgele Teleportasyon"));
    targetInventory.setItem(8, setVanishItem(targetUUID));
    
    targetPlayer.updateInventory();
  }
  
  private ItemStack setVanishItem(UUID targetUUID) {
    User targetUser = ProfilePlugin.getInstance().getUserManager().getUser(targetUUID);
    return ItemUtils.createItem(Material.INK_SACK, ChatColor.AQUA + (targetUser.isVanishStatus() ? "Görünmezlik: Açık" : "Görünmezlik: Kapalı"), (short) (targetUser.isVanishStatus() ? 10 : 8), Enchantment.DURABILITY);
  }
  
  public void savePlayerInventory(UUID targetUUID, PlayerInventory targetInventory) {
    this.inventoryContents.put(targetUUID, targetInventory.getContents());
    this.armorContents.put(targetUUID, targetInventory.getArmorContents());
  }
  
  public void loadPlayerInventory(UUID targetUUID, PlayerInventory targetInventory) {
    if (this.inventoryContents.containsKey(targetUUID) || this.armorContents.containsKey(targetUUID)) {
      
      ItemStack[] inventoryContents = this.inventoryContents.get(targetUUID);
      ItemStack[] armorContents = this.armorContents.get(targetUUID);
      
      if (inventoryContents.length > 0 || armorContents.length > 0) {
        targetInventory.setContents(inventoryContents);
        targetInventory.setArmorContents(armorContents);
      } 
      
      this.inventoryContents.remove(targetUUID);
      this.armorContents.remove(targetUUID);
    } 
  }

  
  public Player getRandomPlayer(Player selfPlayer) { return getRandomPlayer(selfPlayer, false); }

  
  public Player getRandomPlayer(Player selfPlayer, boolean staffModeIgnore) {
    Random randomPlayerNumber = new Random();
    List<UUID> onlinePlayers = new ArrayList<>();
    for (Player player : Bukkit.getOnlinePlayers()) {
      User onlineUser = ProfilePlugin.getInstance().getUserManager().getUser(player.getUniqueId());
      if ((staffModeIgnore && onlineUser.isStaffMode()) || (selfPlayer != null && player.getUniqueId().equals(selfPlayer.getUniqueId()))) {
        continue;
      }
      onlinePlayers.add(player.getUniqueId());
    } 
    
    if (onlinePlayers.isEmpty()) {
      return null;
    }
    
    int number = randomPlayerNumber.nextInt(onlinePlayers.size());
    return Bukkit.getPlayer(onlinePlayers.get(number));
  }
  
  public static class InspectMenu { public String toString() { return "StaffManager.InspectMenu()"; } public int hashCode() { int result = 1; return 1; } protected boolean canEqual(Object other) { return other instanceof InspectMenu; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof InspectMenu)) return false;  InspectMenu other = (InspectMenu)o; return !!other.canEqual(this); }

    
    public static Inventory createInspectMenu(UUID basicUniqueUUID, UUID targetUniqueUUID) {
      User basicUser = ProfilePlugin.getInstance().getUserManager().getUser(basicUniqueUUID);
      final User targetUser = ProfilePlugin.getInstance().getUserManager().getUser(targetUniqueUUID);
      ChatColor rankColor = targetUser.getRankType().getColor();
      
      Player basicPlayer = Bukkit.getPlayer(basicUser.getUniqueUUID());
      final Player targetPlayer = Bukkit.getPlayer(targetUser.getUniqueUUID());
      
      final Inventory toReturn = Bukkit.createInventory((InventoryHolder)basicPlayer, 54, ChatColor.GRAY + "Envanter: " + rankColor + targetUser.getName());
      final PlayerInventory playerInventory = targetPlayer.getInventory();
      
      (new BukkitRunnable() {
          public void run() {
            ItemStack[] contentsItem = playerInventory.getContents();
            toReturn.setContents(contentsItem);
            
            for (int i = 36; i <= 44; i++) {
              toReturn.setItem(i, ItemUtils.createItem(Material.STAINED_GLASS_PANE, "", 1, (short)11));
            }
            
            toReturn.setItem(45, (playerInventory.getHelmet() != null) ? playerInventory.getHelmet() : ItemUtils.createItem(Material.IRON_FENCE, ChatColor.WHITE + "Kafalık: " + ChatColor.RED + "Boş"));
            toReturn.setItem(46, (playerInventory.getChestplate() != null) ? playerInventory.getChestplate() : ItemUtils.createItem(Material.IRON_FENCE, ChatColor.WHITE + "Göğüslük: " + ChatColor.RED + "Boş"));
            toReturn.setItem(47, (playerInventory.getLeggings() != null) ? playerInventory.getLeggings() : ItemUtils.createItem(Material.IRON_FENCE, ChatColor.WHITE + "Pantolon: " + ChatColor.RED + "Boş"));
            toReturn.setItem(48, (playerInventory.getBoots() != null) ? playerInventory.getBoots() : ItemUtils.createItem(Material.IRON_FENCE, ChatColor.WHITE + "Bot: " + ChatColor.RED + "Boş"));
            
            boolean frozenStatus = targetUser.isFrozenStatus();
            List<String> effects = new ArrayList<>();
            
            for (PotionEffect potionEffect : targetPlayer.getActivePotionEffects()) {
              String effectName = potionEffect.getType().getName();
              int effectAmplifier = potionEffect.getAmplifier();
              
              effects.add(ChatColor.WHITE + effectName.replace("_", " ") + " " + effectAmplifier);
              effects.add(ChatColor.YELLOW + " - Süre: " + ChatColor.WHITE + TimeUtils.getDurationWords(potionEffect.getDuration(), true, true));
            } 
            
            int targetHealth = (int)targetPlayer.getHealth();
            toReturn.setItem(50, ItemUtils.createItem(Material.SPECKLED_MELON, ChatColor.RED.toString() + targetPlayer.getHealth() + " ❤HP", targetHealth));
            toReturn.setItem(51, ItemUtils.createItem(Material.POTION, ChatColor.AQUA + "İksir Güçleri", (short)1, effects));
            toReturn.setItem(52, ItemUtils.createItem(Material.CARPET, ChatColor.AQUA + "Dondurulmuşluk: " + (frozenStatus ? (ChatColor.GREEN + "Açık") : (ChatColor.RED + "Kapalı")), (short)1));
            
            World world = targetPlayer.getLocation().getWorld();
            double xValue = targetPlayer.getLocation().getX();
            double yValue = targetPlayer.getLocation().getY();
            double zValue = targetPlayer.getLocation().getZ();
            
            List<String> location = new ArrayList<>();
            location.add(ChatColor.GRAY + "Bulunduğu Dünya: " + ChatColor.WHITE + world.getName() + " (" + world.getWorldType().getName() + ")");
            location.add(ChatColor.GRAY + "X: " + ChatColor.WHITE + xValue + ChatColor.GRAY + ", Y: " + ChatColor.WHITE + yValue + ChatColor.GRAY + ", Z: " + ChatColor.WHITE + zValue);
            
            toReturn.setItem(53, ItemUtils.createItem(Material.WATCH, ChatColor.DARK_AQUA + MiscUtils.addNameAdditional(targetUser.getName(), true) + " Koordinatlar:", location));
          }
        }).runTaskTimer((Plugin)ProfilePlugin.getInstance(), 0L, 20L);
      
      return toReturn;
    } }

}
