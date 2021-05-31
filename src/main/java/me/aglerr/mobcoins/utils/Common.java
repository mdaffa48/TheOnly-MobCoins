package me.aglerr.mobcoins.utils;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
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

    public static void playSound(@NotNull Player player, @NotNull String path, @NotNull FileConfiguration config){
        boolean enabled = config.getBoolean(path + ".enabled");
        if(!enabled) return;

        Sound sound = XSound.matchXSound(config.getString(path + ".name")).get().parseSound();
        float volume = (float) config.getDouble(path + ".volume");
        float pitch = (float) config.getDouble(path + ".pitch");
        player.playSound(player.getLocation(), sound, volume, pitch);
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

    public static ItemStack createMobCoinItem(String material, String name, List<String> lore, boolean glow, double amount){

        ItemStack stack = null;

        List<String> parsedLore = new ArrayList<>();
        lore.forEach(line -> parsedLore.add(line.replace("{amount}", String.valueOf(amount))));

        if(material.contains(";")){
            String[] split = material.split(";");
            if(split[0].equalsIgnoreCase("head")){
                stack = XMaterial.PLAYER_HEAD.parseItem();
                SkullMeta skullMeta = (SkullMeta) stack.getItemMeta();
                skullMeta.setDisplayName(color(name));
                skullMeta.setLore(color(parsedLore));
                if(glow){
                    skullMeta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
                    skullMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                }
                skullMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                stack.setItemMeta(skullMeta);
            }
        } else {
            ItemBuilder builder = new ItemBuilder(XMaterial.matchXMaterial(material).get().parseItem())
                    .name(color(name))
                    .lore(color(parsedLore))
                    .flags(ItemFlag.HIDE_ATTRIBUTES);

            if(glow) builder.enchant(Enchantment.ARROW_INFINITE).flags(ItemFlag.HIDE_ENCHANTS);
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
}
