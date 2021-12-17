package me.aglerr.mobcoins.utils;

import me.aglerr.mclibs.MCLibs;
import me.aglerr.mclibs.libs.Common;
import me.aglerr.mclibs.xseries.XSound;
import me.aglerr.mclibs.xseries.messages.ActionBar;
import me.aglerr.mclibs.xseries.messages.Titles;
import me.aglerr.mobcoins.MobCoins;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;

public class Utils {

    public static void sendStartupLogo(){
        for(String message : getStartupLogo()){
            System.out.println(message);
        }
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

    public static List<String> getUpdateMessage(){
        return Arrays.asList(
                "  _    _ _____  _____       _______ ______  _____ ",
                " | |  | |  __ \\|  __ \\   /\\|__   __|  ____|/ ____|",
                " | |  | | |__) | |  | | /  \\  | |  | |__  | (___  ",
                " | |  | |  ___/| |  | |/ /\\ \\ | |  |  __|  \\___ \\ ",
                " | |__| | |    | |__| / ____ \\| |  | |____ ____) |",
                "  \\____/|_|    |_____/_/    \\_\\_|  |______|_____/ "
        );
    }

    public static void playSound(@NotNull Player player, @NotNull String path, @NotNull FileConfiguration config){
        boolean enabled = config.getBoolean(path + ".enabled");
        if(!enabled) return;

        Sound sound = XSound.matchXSound(config.getString(path + ".name")).get().parseSound();
        float volume = (float) config.getDouble(path + ".volume");
        float pitch = (float) config.getDouble(path + ".pitch");
        player.playSound(player.getLocation(), sound, volume, pitch);
    }

    public static void sendTitle(@NotNull Player player, @NotNull String path, @NotNull FileConfiguration config, double coinPlaceholder){
        boolean enabled = config.getBoolean(path + ".enabled");
        if(!enabled) return;

        String title = config.getString(path + ".title")
                .replace("{amount}", digits(coinPlaceholder))
                .replace("{amount_rounded}", (int) coinPlaceholder + "");

        String subTitle = config.getString(path + ".subTitle")
                .replace("{amount}", digits(coinPlaceholder))
                .replace("{amount_rounded}", (int) coinPlaceholder + "");

        int fadeIn = config.getInt(path + ".fadeIn");
        int stay = config.getInt(path + ".stay");
        int fadeOut = config.getInt(path + ".fadeOut");

        Titles.sendTitle(player, fadeIn, stay, fadeOut, Common.color(title), Common.color(subTitle));
    }

    public static void sendActionBar(@NotNull Player player, @NotNull String path, @NotNull FileConfiguration config, double coinPlaceholder){
        boolean enabled = config.getBoolean(path + ".enabled");
        if(!enabled) return;

        String message = config.getString(path + ".message")
                .replace("{amount}", digits(coinPlaceholder))
                .replace("{amount_rounded}", (int) coinPlaceholder + "");

        if(Bukkit.getVersion().contains("1.8")){
            ActionBar8.sendActionBarMessage(player, Common.color(message), 3, MCLibs.INSTANCE);
            return;
        }

        ActionBar.sendActionBar(MCLibs.INSTANCE, player, Common.color(message), 60L);
    }

    public static String integer(double amount){
        return String.valueOf((int) amount);
    }

    public static String digits(double d){
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(0);
        return numberFormat.format(d);
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
