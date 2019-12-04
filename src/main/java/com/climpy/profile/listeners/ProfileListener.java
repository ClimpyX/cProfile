package com.climpy.profile.listeners;

import com.climpy.profile.ProfileAPI;
import com.climpy.profile.ProfilePlugin;
import com.climpy.profile.managers.StaffModeManager;
import com.climpy.profile.mongo.CollectionManager;
import com.climpy.profile.user.User;
import com.climpy.profile.utils.C;
import com.climpy.profile.utils.MiscUtils;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

public class ProfileListener implements Listener {
    //First Join
    private Date date = new Date();
    private String currentTime = new SimpleDateFormat("HH:mm").format(date);
    private Date date1 = new Date();
    private String currentDate = new SimpleDateFormat("dd/MM/yyyy").format(date1);


    @EventHandler
    public void asyncJoinEvent(AsyncPlayerPreLoginEvent event) {
        if (!ProfilePlugin.getInstance().isLoaded()) {
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
            event.setKickMessage(C.color("&6[cProfile]\n\n&cSunucu yeniden başlatılıyor.."));
            return;
        }

        Player player = Bukkit.getPlayer(event.getUniqueId());
        if (player != null && player.isOnline()) {
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
            event.setKickMessage(ChatColor.RED + "\nBağlantıyı kestikten sonra çok hızlı giriş yapmayı denediniz.\nBirkaç saniye sonra tekrar deneyin.");
            ProfilePlugin.getInstance().getServer().getScheduler().runTask(ProfilePlugin.getInstance(), () -> player.kickPlayer(ChatColor.RED + "Yinelenen girişten dolayı atıldınız."));
            return;
        }

        List<String> players = new ArrayList<>();
        try (MongoCursor mongoCursor = ProfileAPI.findAll("user").iterator()) {
            while (mongoCursor.hasNext()) {
                players.add(((Document) mongoCursor.next()).getString("name"));
            }
        }

        for (String originalName : players) {
            String name = event.getName();
            if(!originalName.equals(name)) {
                event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
                event.setKickMessage(ChatColor.RED + "Orijinal kullanıcı adı ile giriş yapınız. (" + originalName + ")");
                return;
            }
        }

        User user = ProfilePlugin.getInstance().getUserManager().getUser(event.getUniqueId());
        if (user == null) {
            user = new User(event.getUniqueId());
            ProfilePlugin.getInstance().getUserManager().getUsers().put(event.getUniqueId(), user);
            user.setName(event.getName());

            if (user.getFirstLoginTime() == null) {
                user.setFirstLoginTime(currentDate + " " + currentTime);
            }

            if (user.getCurrentAddress() == null) {
                user.setCurrentAddress(event.getAddress().getHostAddress());
            }

            if (!user.getIpAddresses().contains(event.getAddress().getHostAddress())) {
                user.getIpAddresses().add(event.getAddress().getHostAddress());
            }

            user.setLastLoginTime(currentDate + " " + currentTime);
            Bukkit.getScheduler().runTaskLater(ProfilePlugin.getInstance(), user::save, 20L);
        }

        if (!user.getIpAddresses().contains(event.getAddress().getHostAddress())) {
            user.getIpAddresses().add(event.getAddress().getHostAddress());
        }

        user.setName(event.getName());
        Bukkit.getScheduler().runTaskLater(ProfilePlugin.getInstance(), user::save, 20L);
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
        if (user == null)
            return;

        user.setLastLoginTime(currentDate + " " + currentTime);
        user.setOnlineStatus(true);
        user.save();
    }
}
