package com.climpy.profile.listeners;

import com.climpy.profile.ProfilePlugin;
import com.climpy.profile.managers.StaffModeManager;
import com.climpy.profile.mongo.CollectionManager;
import com.climpy.profile.user.User;
import com.climpy.profile.utils.MiscUtils;
import com.mongodb.MongoClient;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.logging.Level;

public class ProfileListener implements Listener {

    @EventHandler
    public void asyncJoinEvent(AsyncPlayerPreLoginEvent event) {
        Player player = Bukkit.getPlayer(event.getUniqueId());
        User user = ProfilePlugin.getInstance().getUserManager().getUser(event.getUniqueId());

        if (player != null && player.isOnline()) {
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
            event.setKickMessage(ChatColor.RED + "\nBağlantıyı kestikten sonra çok hızlı giriş yapmayı denediniz.\nBirkaç saniye sonra tekrar deneyin.");
            ProfilePlugin.getInstance().getServer().getScheduler().runTask(ProfilePlugin.getInstance(), () -> player.kickPlayer(ChatColor.RED + "Yinelenen girişten dolayı atıldınız."));
            return;
        }

        if (user == null) {
            user = new User(event.getUniqueId());
            ProfilePlugin.getInstance().getUserManager().getUsers().put(event.getUniqueId(), user);
            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[cProfile] " + user.getName() + " isimli yeni bir oyuncu olusturuluyor, UUID verisi: " + user.getUniqueUUID());
        }

        user.setName(event.getName());
        Bukkit.getScheduler().runTaskLater(ProfilePlugin.getInstance(), user::save, 20L);
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[cProfile] " + user.getName() + " isimli oyuncu " + user.getUniqueUUID() + " UUID'si ile giris yapiyor.");
    }

    @EventHandler
    public void onQuitListener(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        User user = ProfilePlugin.getInstance().getUserManager().getUser(player.getUniqueId());
        if (user == null) {
            return;
        }

        user.setOnlineStatus(false);
        user.save();
        ProfilePlugin.getInstance().getUserManager().getUsers().remove(player.getUniqueId());
    }

    @EventHandler
    public void joinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        User user = ProfilePlugin.getInstance().getUserManager().getUser(player.getUniqueId());

        if (!ProfilePlugin.getInstance().enabled) {
            player.kickPlayer(ChatColor.translateAlternateColorCodes('&', "&e&l" + ProfilePlugin.getInstance().getDescription().getFullName() + "\n\n&cProfiller yüklenemiyor, bu genel bir hatadır.\n&cDestek ile iletişim kurmayı deneyebilirsiniz.\n\n&7&oSorun çözülünce girişler açılacaktır."));
        }

    }
}
