package me.aglerr.mobcoins.utils;

import me.aglerr.mobcoins.MobCoins;
import me.aglerr.mobcoins.configs.ConfigValue;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.List;
import java.util.stream.Collectors;

public class Common {

    public static String color(String s){
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static List<String> color(List<String> strings) {
        return strings.stream().map(Common::color).collect(Collectors.toList());
    }

    public static String[] getStartupLogo(){
        return new String[]{
                "  _______ _             ____        _       ",
                " |__   __| |           / __ \\      | |      ",
                "    | |  | |__   ___  | |  | |_ __ | |_   _ ",
                "    | |  | '_ \\ / _ \\ | |  | | '_ \\| | | | |",
                "    | |  | | | |  __/ | |__| | | | | | |_| |",
                "    |_|  |_| |_|\\___|  \\____/|_| |_|_|\\__, |",
                "                                       __/ |",
                "                                      |___/ ",
                "  ",
                "                MobCoins edition",
                "           Thanks for using and enjoy!",
                "  "
        };
    }

    public static void log(boolean withPrefix, String... args){
        for(String message : args){
            if (withPrefix) {
                System.out.println("[TheOnly-MobCoins] " + message);
            } else {
                System.out.println(message);
            }
        }
    }

    public static void debug(boolean withPrefix, String... args){
        if(!ConfigValue.isDebug) return;

        for(String message : args){
            if (withPrefix) {
                System.out.println("[TheOnly-MobCoins] " + message);
            } else {
                System.out.println(message);
            }
        }
    }

    public static void error(boolean withPrefix, String... args){
        for(String message : args){
            if (withPrefix) {
                Bukkit.getConsoleSender().sendMessage(color("&c[TheOnly-MobCoins] " + message));
            } else {
                Bukkit.getConsoleSender().sendMessage(color("&c" + message));
            }
        }
    }

    public static void success(boolean withPrefix, String... args){
        for(String message : args){
            if (withPrefix) {
                Bukkit.getConsoleSender().sendMessage(color("&a[TheOnly-MobCoins] " + message));
            } else {
                Bukkit.getConsoleSender().sendMessage(color("&a" + message));
            }
        }
    }

    public static void runTask(Runnable runnable){
        Bukkit.getScheduler().runTask(MobCoins.getInstance(), runnable);
    }

    public static void runTaskAsynchronously(Runnable runnable){
        Bukkit.getScheduler().runTaskAsynchronously(MobCoins.getInstance(), runnable);
    }

}
