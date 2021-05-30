package me.aglerr.mobcoins.utils;

import me.aglerr.mobcoins.MobCoins;
import me.aglerr.mobcoins.configs.ConfigValue;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
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
                Bukkit.getConsoleSender().sendMessage(color("&e[TheOnly-MobCoins] " + message));
            } else {
                Bukkit.getConsoleSender().sendMessage(color("&e" + message));
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

    public static boolean isInt(String s){
        try{
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }

    public static boolean isDouble(String s){
        try{
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }

    public static DecimalFormat getDecimalFormat(){
        return new DecimalFormat("###,###,###,###,###.##");
    }

    public static void runTask(Runnable runnable){
        Bukkit.getScheduler().runTask(MobCoins.getInstance(), runnable);
    }

    public static void runTaskAsynchronously(Runnable runnable){
        Bukkit.getScheduler().runTaskAsynchronously(MobCoins.getInstance(), runnable);
    }

    private static String format(double d){
        NumberFormat format = NumberFormat.getInstance(Locale.ENGLISH);
        format.setMaximumFractionDigits(2);
        format.setMinimumFractionDigits(0);
        return format.format(d);
    }

    public static String shortFormat(double d){
        if (d < 1000L) {
            return format(d);
        }
        if (d < 1000000L) {
            return format(d / 1000L) + "K";
        }
        if (d < 1000000000L) {
            return format(d / 1000000L) + "M";
        }
        if (d < 1000000000000L) {
            return format(d / 1000000000L) + "B";
        }
        if (d < 1000000000000000L) {
            return format(d / 1000000000000L) + "T";
        }
        if (d < 1000000000000000000L) {
            return format(d / 1000000000000000L) + "Q";
        }

        return String.valueOf((long) d);
    }

}
