package com.climpy.profile.listeners;

import com.climpy.profile.ProfilePlugin;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.climpy.profile.rank.RankType;
import com.climpy.profile.user.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class SecurityListener implements Listener {
  private final List<String> BLOCKED_NAMES;
  private final Pattern NAME_PATTERN;
  private final ProfilePlugin profilePlugin;
  
  public SecurityListener(ProfilePlugin profilePlugin) {
    this.BLOCKED_NAMES = Arrays.asList(new String[] {"elice", "andora" });
    this.NAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_\\s]{1,16}$");


    
    this.profilePlugin = profilePlugin;
  }
  
  @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
  public void onPlayerLoginEvent(PlayerLoginEvent event) {
    PlayerLoginEvent.Result eventResult = event.getResult();
    if (eventResult == PlayerLoginEvent.Result.ALLOWED) {
      Player player = event.getPlayer();
      
      User eventUser = ProfilePlugin.getInstance().getUserManager().getUser(player.getUniqueId());
      if (eventUser == null)
        return; 
      if (eventUser.getName() == null)
        return;  if (!eventUser.isFirstJoin() && !eventUser.getName().equals(player.getName())) {
        this.profilePlugin.getLogger().info("Ad dogrulama: " + player.getName() + " gecersiz bir ada sahip olmakla isaretlendi (sahtecilik, orjinal ad:" + eventUser.getName() + ") ");
        event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Kullanıcı bulundu ancak geçersiz bir kullanıcı adınız var. Orjinal kullanıcı adı " + eventUser.getName() + ", lütfen kullanıcı adını doğru giriniz.");
      } 
      
      if (eventUser.getRankType().isAboveOrEqual(RankType.MOD))
        return;  Matcher matcher = this.NAME_PATTERN.matcher(player.getName());
      
      if (!matcher.find()) {
        this.profilePlugin.getLogger().info("Ad dogrulama: " + player.getName() + " gecersiz bir ada sahip olmakla isaretlendi (devre disi birakmak icin 'cProfiles' eklentisinin yapilandirmasinda bulunan ad dogrulama ozelligini kapatin)");
        event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Geçersiz oyuncu adı tespit edildi. (^[a-zA-Z0-9_]{1,16}$)");
      } 
      
      for (String blockedName : this.BLOCKED_NAMES) {
        if (!player.getName().toLowerCase().equals(blockedName.toLowerCase()))
          continue;  event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Geçersiz oyuncu adı tespit edildi. (" + blockedName + ")");
      } 
    } 
  }
}
