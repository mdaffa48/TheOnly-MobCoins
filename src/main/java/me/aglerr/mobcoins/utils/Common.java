package me.aglerr.mobcoins.utils;

import me.aglerr.mobcoins.configs.ConfigValue;
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

}
