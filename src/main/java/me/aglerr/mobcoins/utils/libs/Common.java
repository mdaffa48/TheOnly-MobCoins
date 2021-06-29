package me.aglerr.mobcoins.utils.libs;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;

import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;

public class Common {

    protected static Plugin instance;
    protected static Locale locale = Locale.ENGLISH;
    protected static String PREFIX = "[LazyLibs]";

    public static boolean DEBUG = false;

    public static void inject(Plugin plugin){
        instance = plugin;
    }

    public static void setLocale(Locale value){
        locale = value;
    }

    public static void setPrefix(String message){
        PREFIX = message;
    }

    public static String color(String s){
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static List<String> color(List<String> strings) {
        return strings.stream().map(Common::color).collect(Collectors.toList());
    }

    public static void log(ChatColor chatColor, String... args){
        for(String message : args){
            Bukkit.getConsoleSender().sendMessage(chatColor + PREFIX + " " + message);
        }
    }

    public static void log(ChatColor chatColor, List<String> args){
        for(String message : args){
            Bukkit.getConsoleSender().sendMessage(chatColor + PREFIX + " " + message);
        }
    }

    public static void debug(String... args){
        if(!DEBUG) return;
        for(String message : args){
            log(ChatColor.YELLOW, message);
        }
    }

    public static boolean isInt(String s){
        try{
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException ex){
            return false;
        }
    }

    public static boolean isDouble(String s){
        try{
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException ex){
            return false;
        }
    }

    public static String numberFormat(double d){
        NumberFormat numberFormat = NumberFormat.getInstance(locale);
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(0);
        return numberFormat.format(d);
    }

    public static String shortFormat(double d){
        if (d < 1000L) {
            return numberFormat(d);
        }
        if (d < 1000000L) {
            return numberFormat(d / 1000L) + "K";
        }
        if (d < 1000000000L) {
            return numberFormat(d / 1000000L) + "M";
        }
        if (d < 1000000000000L) {
            return numberFormat(d / 1000000000L) + "B";
        }
        if (d < 1000000000000000L) {
            return numberFormat(d / 1000000000000L) + "T";
        }
        if (d < 1000000000000000000L) {
            return numberFormat(d / 1000000000000000L) + "Q";
        }
        return String.valueOf((long) d);
    }

    public static boolean hasOffhand(){
        return !Bukkit.getVersion().contains("1.7") &&
                !Bukkit.getVersion().contains("1.8");
    }

    public static String formatTime(int seconds) {
        if (seconds < 60) {
            return seconds + "s";
        }
        int minutes = seconds / 60;
        int s = 60 * minutes;
        int secondsLeft = seconds - s;
        if (minutes < 60) {
            if (secondsLeft > 0) {
                return minutes + "m " + secondsLeft + "s";
            }
            return minutes + "m";
        }
        if (minutes < 1440) {
            String time;
            int hours = minutes / 60;
            time = hours + "h";
            int inMins = 60 * hours;
            int leftOver = minutes - inMins;
            if (leftOver >= 1) {
                time = time + " " + leftOver + "m";
            }
            if (secondsLeft > 0) {
                time = time + " " + secondsLeft + "s";
            }
            return time;
        }
        String time;
        int days = minutes / 1440;
        time = days + "d";
        int inMins = 1440 * days;
        int leftOver = minutes - inMins;
        if (leftOver >= 1) {
            if (leftOver < 60) {
                time = time + " " + leftOver + "m";
            } else {
                int hours = leftOver / 60;
                time = time + " " + hours + "h";
                int hoursInMins = 60 * hours;
                int minsLeft = leftOver - hoursInMins;
                time = time + " " + minsLeft + "m";
            }
        }
        if (secondsLeft > 0) {
            time = time + " " + secondsLeft + "s";
        }
        return time;
    }

}
