package me.aglerr.mobcoins.utils.libs;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public class Executor {

    protected static Plugin instance;

    public static void inject(Plugin plugin){
        instance = plugin;
    }

    public static void sync(Runnable runnable){
        Bukkit.getScheduler().runTask(instance, runnable);
    }

    public static BukkitTask syncTimer(long delay, long time, Runnable runnable){
        return Bukkit.getScheduler().runTaskTimer(instance, runnable, delay, time);
    }

    public static void syncLater(long delay, Runnable runnable){
        Bukkit.getScheduler().runTaskLater(instance, runnable, delay);
    }

    public static void async(Runnable runnable){
        Bukkit.getScheduler().runTaskAsynchronously(instance, runnable);
    }

    public static void asyncTimer(long delay, long time, Runnable runnable){
        Bukkit.getScheduler().runTaskTimerAsynchronously(instance, runnable, delay, time);
    }

    public static void asyncLater(long delay, Runnable runnable){
        Bukkit.getScheduler().runTaskLaterAsynchronously(instance, runnable, delay);
    }

}
