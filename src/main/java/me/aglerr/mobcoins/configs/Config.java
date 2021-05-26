package me.aglerr.mobcoins.configs;

import me.aglerr.mobcoins.MobCoins;
import org.bukkit.configuration.file.FileConfiguration;

public class Config {

    public static CustomConfig CONFIG;
    public static CustomConfig MAIN_MENU_CONFIG;
    public static CustomConfig CATEGORY_SHOP_CONFIG;
    public static CustomConfig ROTATING_SHOP_CONFIG;
    public static CustomConfig MOBS;

    public static void initialize(){
        CONFIG = new CustomConfig(MobCoins.getInstance(), "config.yml", null);
        MAIN_MENU_CONFIG = new CustomConfig(MobCoins.getInstance(), "main_menu.yml", "shops");
        CATEGORY_SHOP_CONFIG = new CustomConfig(MobCoins.getInstance(), "category_shop.yml", "shops");
        ROTATING_SHOP_CONFIG = new CustomConfig(MobCoins.getInstance(), "rotating_shop.yml", "shops");
        MOBS = new CustomConfig(MobCoins.getInstance(), "mobs.yml", null);
    }

    public static void reloadAllConfigs(){
        CONFIG.reloadConfig();
        MAIN_MENU_CONFIG.reloadConfig();
        CATEGORY_SHOP_CONFIG.reloadConfig();
        ROTATING_SHOP_CONFIG.reloadConfig();
    }

}
