package me.aglerr.mobcoins.configs;

import me.aglerr.mobcoins.MobCoins;
import org.bukkit.configuration.file.FileConfiguration;

public class Config {

    public static CustomConfig CONFIG;
    public static CustomConfig MAIN_MENU_CONFIG;
    public static CustomConfig CATEGORY_SHOP_CONFIG;
    public static CustomConfig ROTATING_SHOP_CONFIG;
    public static CustomConfig MOBS;
    public static CustomConfig TEMP_DATA;
    public static CustomConfig CONFIRMATION_MENU_CONFIG;
    public static CustomConfig HOOKS;
    public static CustomConfig TOGGLE_INVENTORY_CONFIG;

    public static void initialize(){
        CONFIG = new CustomConfig(MobCoins.getInstance(), "config.yml", null);
        MAIN_MENU_CONFIG = new CustomConfig(MobCoins.getInstance(), "main_menu.yml", "shops");
        CATEGORY_SHOP_CONFIG = new CustomConfig(MobCoins.getInstance(), "category_shop.yml", "shops/category-shop");
        ROTATING_SHOP_CONFIG = new CustomConfig(MobCoins.getInstance(), "rotating_shop.yml", "shops/rotating-shop");
        MOBS = new CustomConfig(MobCoins.getInstance(), "mobs.yml", null);
        TEMP_DATA = new CustomConfig(MobCoins.getInstance(), "temp_data.yml", null);
        CONFIRMATION_MENU_CONFIG = new CustomConfig(MobCoins.getInstance(), "confirmation_menu.yml", "shops");
        //HOOKS = new CustomConfig(MobCoins.getInstance(), "hooks.yml", null);
        TOGGLE_INVENTORY_CONFIG = new CustomConfig(MobCoins.getInstance(), "toggle_inventory.yml", "inventories");
    }

    public static void reloadAllConfigs(){
        CONFIG.reloadConfig();
        MAIN_MENU_CONFIG.reloadConfig();
        CATEGORY_SHOP_CONFIG.reloadConfig();
        ROTATING_SHOP_CONFIG.reloadConfig();
        TEMP_DATA.reloadConfig();
        CONFIRMATION_MENU_CONFIG.reloadConfig();
        //HOOKS.reloadConfig();
        TOGGLE_INVENTORY_CONFIG.reloadConfig();
    }

}
