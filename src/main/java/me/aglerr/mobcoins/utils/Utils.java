package me.aglerr.mobcoins.utils;

import me.aglerr.lazylibs.libs.Common;
import me.aglerr.lazylibs.libs.ReflectionUtils;
import me.aglerr.lazylibs.libs.XSound;
import me.aglerr.lazylibs.libs.messages.ActionBar;
import me.aglerr.lazylibs.libs.messages.Titles;
import me.aglerr.mobcoins.MobCoins;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

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
                .replace("{amount}", Common.numberFormat(coinPlaceholder))
                .replace("{amount_rounded}", (int) coinPlaceholder + "");

        String subTitle = config.getString(path + ".subTitle")
                .replace("{amount}", Common.numberFormat(coinPlaceholder))
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
                .replace("{amount}", Common.numberFormat(coinPlaceholder))
                .replace("{amount_rounded}", (int) coinPlaceholder + "");

        if(Bukkit.getVersion().contains("1.8")){
            ActionBar8.sendActionBarMessage(player, Common.color(message), 3, MobCoins.getInstance());
            return;
        }

        ActionBar.sendActionBar(MobCoins.getInstance(), player, Common.color(message), 60L);
    }

    public static void sendMessage(Player player, String message){
        if(message.equalsIgnoreCase("")){
            return;
        }
        player.sendMessage(message);
    }

}
