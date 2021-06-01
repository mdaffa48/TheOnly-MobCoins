package me.aglerr.mobcoins.utils;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import com.cryptomorin.xseries.messages.ActionBar;
import com.cryptomorin.xseries.messages.Titles;
import de.tr7zw.changeme.nbtapi.NBTItem;
import fr.mrmicky.fastinv.ItemBuilder;
import me.aglerr.mobcoins.MobCoins;
import me.aglerr.mobcoins.configs.ConfigValue;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Skull;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
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

    public static void runTaskAsynchronously(Runnable runnable){
        Bukkit.getScheduler().runTaskAsynchronously(MobCoins.getInstance(), runnable);
    }

    public static void runTaskTimerAsynchronously(int delay, int time, Runnable runnable){
        Bukkit.getScheduler().runTaskTimerAsynchronously(MobCoins.getInstance(), runnable, delay, time);
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

    public static ItemStack createMobCoinItem( double amount){

        ItemStack stack = null;

        List<String> parsedLore = new ArrayList<>();
        ConfigValue.MOBCOINS_ITEM_LORE.forEach(line -> parsedLore.add(line.replace("{amount}", String.valueOf(amount))));

        if(ConfigValue.MOBCOINS_ITEM_MATERIAL.contains(";")){
            String[] split = ConfigValue.MOBCOINS_ITEM_MATERIAL.split(";");
            if(split[0].equalsIgnoreCase("head")){
                stack = XMaterial.PLAYER_HEAD.parseItem();
                SkullMeta skullMeta = (SkullMeta) stack.getItemMeta();
                skullMeta.setDisplayName(color(ConfigValue.MOBCOINS_ITEM_NAME));
                skullMeta.setLore(color(parsedLore));
                if(ConfigValue.MOBCOINS_ITEM_GLOW){
                    skullMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
                    skullMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                }
                skullMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                stack.setItemMeta(skullMeta);
            }
        } else {
            ItemBuilder builder = new ItemBuilder(XMaterial.matchXMaterial(ConfigValue.MOBCOINS_ITEM_MATERIAL).get().parseItem())
                    .name(color(ConfigValue.MOBCOINS_ITEM_NAME))
                    .lore(color(parsedLore))
                    .flags(ItemFlag.HIDE_ATTRIBUTES);

            if(ConfigValue.MOBCOINS_ITEM_GLOW) builder.enchant(Enchantment.ARROW_INFINITE).flags(ItemFlag.HIDE_ENCHANTS);
            stack = builder.build();
        }
        
        NBTItem nbtItem = new NBTItem(stack);
        nbtItem.setDouble("mobCoinsAmount", amount);
        return nbtItem.getItem();
    }

    public static boolean hasOffhand(){
        if(Bukkit.getVersion().contains("1.7") ||
           Bukkit.getVersion().contains("1.8")
        ) return false;
        return true;
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

        String title = config.getString(path + ".title").replace("{amount}", String.valueOf(coinPlaceholder));
        String subTitle = config.getString(path + ".subTitle").replace("{amount}", String.valueOf(coinPlaceholder));
        int fadeIn = config.getInt(path + ".fadeIn");
        int stay = config.getInt(path + ".stay");
        int fadeOut = config.getInt(path + ".fadeOut");

        Titles.sendTitle(player, fadeIn, stay, fadeOut, Common.color(title), Common.color(subTitle));
    }

    public static void sendActionBar(@NotNull Player player, @NotNull String path, @NotNull FileConfiguration config){
        boolean enabled = config.getBoolean(path + ".enabled");
        if(!enabled) return;

        String message = config.getString(path + ".message");
        ActionBar.sendActionBar(player, Common.color(message));
    }

}
