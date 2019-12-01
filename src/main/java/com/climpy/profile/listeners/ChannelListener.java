package com.climpy.profile.listeners;

import com.climpy.profile.ProfilePlugin;
import com.climpy.profile.events.ServerChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.logging.Level;

public class ChannelListener implements PluginMessageListener {

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] bytes) {
        if(channel.equals("BungeeCord")) {

        }
    }

    public static void sendToServer(Player targetPlayer, String serverName) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        if (targetPlayer == null || serverName.isEmpty()) {
            return;
        }

        ServerChangeEvent serverChangeEvent = new ServerChangeEvent(targetPlayer.getUniqueId(), serverName);
        Bukkit.getPluginManager().callEvent(serverChangeEvent);

        if (serverChangeEvent.isCancelled()) {
            return;
        }

        Bukkit.getScheduler().runTaskLaterAsynchronously(ProfilePlugin.getInstance(), () -> {
            try {
                dataOutputStream.writeUTF("Connect");
                dataOutputStream.writeUTF(serverName);

                targetPlayer.sendPluginMessage(ProfilePlugin.getInstance(), "BungeeCord", byteArrayOutputStream.toByteArray());
            } catch (Exception ex) {
                ProfilePlugin.getInstance().getLogger().log(Level.WARNING, ex.getMessage());
            }
        },30L);
    }
}
