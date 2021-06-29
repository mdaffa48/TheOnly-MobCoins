package me.aglerr.mobcoins.utils;

import com.cryptomorin.xseries.XSound;
import com.cryptomorin.xseries.messages.ActionBar;
import com.cryptomorin.xseries.messages.Titles;
import me.aglerr.mobcoins.MobCoins;
import me.aglerr.mobcoins.utils.libs.Common;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

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

        String title = config.getString(path + ".title").replace("{amount}", Common.numberFormat(coinPlaceholder));
        String subTitle = config.getString(path + ".subTitle").replace("{amount}", Common.numberFormat(coinPlaceholder));
        int fadeIn = config.getInt(path + ".fadeIn");
        int stay = config.getInt(path + ".stay");
        int fadeOut = config.getInt(path + ".fadeOut");

        Titles.sendTitle(player, fadeIn, stay, fadeOut, Common.color(title), Common.color(subTitle));
    }

    public static void sendActionBar(@NotNull Player player, @NotNull String path, @NotNull FileConfiguration config, double coinPlaceholder){
        boolean enabled = config.getBoolean(path + ".enabled");
        if(!enabled) return;

        String message = config.getString(path + ".message").replace("{amount}", Common.numberFormat(coinPlaceholder));
        ActionBar.sendActionBar(MobCoins.getInstance(), player, Common.color(message), 60L);
    }

}
