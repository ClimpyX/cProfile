package com.climpy.profile.utils;

import com.climpy.profile.ProfilePlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class TaskUtil {
    public TaskUtil() {
    }

    public static void run(Runnable runnable) {
        ProfilePlugin.getInstance().getServer().getScheduler().runTask(ProfilePlugin.getInstance(), runnable);
    }

    public static void runTimer(Runnable runnable, long delay, long timer) {
        ProfilePlugin.getInstance().getServer().getScheduler().runTaskTimer(ProfilePlugin.getInstance(), runnable, delay, timer);
    }

    public static void runTimer(BukkitRunnable runnable, long delay, long timer) {
        runnable.runTaskTimer(ProfilePlugin.getInstance(), delay, timer);
    }

    public static void runLater(Runnable runnable, long delay) {
        ProfilePlugin.getInstance().getServer().getScheduler().runTaskLater(ProfilePlugin.getInstance(), runnable, delay);
    }

    public static void runAsync(Runnable runnable) {
        ProfilePlugin.getInstance().getServer().getScheduler().runTaskAsynchronously(ProfilePlugin.getInstance(), runnable);
    }

    public static void runTimerAsync(Runnable runnable, long delay, long timer) {
        ProfilePlugin.getInstance().getServer().getScheduler().runTaskTimerAsynchronously(ProfilePlugin.getInstance(), runnable, delay, timer);
    }

    public static Thread runAsyncReturn(Runnable runnable) {
        return new Thread(runnable);
    }
}
