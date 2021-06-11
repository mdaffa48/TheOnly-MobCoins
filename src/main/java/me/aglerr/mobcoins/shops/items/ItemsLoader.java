package me.aglerr.mobcoins.shops.items;

import me.aglerr.mobcoins.MobCoins;
import me.aglerr.mobcoins.configs.Config;
import me.aglerr.mobcoins.utils.Common;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class ItemsLoader {

    private final MobCoins plugin;
    public ItemsLoader(MobCoins plugin){
        this.plugin = plugin;
    }

    private final List<TypeItem> mainMenuItems = new ArrayList<>();
    public List<TypeItem> getMainMenuItems() { return mainMenuItems; }

    private final List<TypeItem> additionalRotatingItems = new ArrayList<>();
    public List<TypeItem> getAdditionalRotatingItems() { return additionalRotatingItems; }

    public void loadMainMenuItems(){
        FileConfiguration config = Config.MAIN_MENU_CONFIG.getConfig();
        for(String key : config.getConfigurationSection("items").getKeys(false)){
            String path = "items." + key + ".";
            String type = config.getString(path + "type");
            String category = config.getString(path + "category");
            String material = config.getString(path + "material");
            String name = config.getString(path + "name");
            boolean glow = config.getBoolean(path + "glow");
            List<Integer> slots = config.getIntegerList(path + "slots");
            int amount = config.getInt(path + "amount");
            double price = config.getDouble(path + "price");
            int purchaseLimit = config.getInt(path + "purchaseLimit");
            int stock = config.getInt(path + "stock");
            List<String> lore = config.getStringList(path + "lore");
            List<String> commands = config.getStringList(path + "commands");

            TypeItem typeItem = new TypeItem(key, type, category, material, name, glow, slots,
                    amount, price, purchaseLimit, stock, lore, commands, false);
            this.mainMenuItems.add(typeItem);

        }
    }

    public void loadAdditionalRotatingItems(){
        FileConfiguration config = Config.ROTATING_SHOP_CONFIG.getConfig();
        for(String key : config.getConfigurationSection("items").getKeys(false)){
            String path = "items." + key;
            String type = config.getString(path + ".type");
            String category = config.getString(path + ".category");
            String material = config.getString(path + ".material");
            String name = config.getString(path + ".name");
            boolean glow = config.getBoolean(path + ".glow");
            List<Integer> slots = config.getIntegerList(path + ".slots");
            int amount = config.getInt(path + ".amount");
            double price = config.getDouble(path + ".price");
            int purchaseLimit = config.getInt(path + ".purchaseLimit");
            int stock = config.getInt(path + ".stock");
            List<String> lore = config.getStringList(path + ".lore");
            List<String> commands = config.getStringList(path + ".commands");

            TypeItem typeItem = new TypeItem(key, type, category, material, name, glow, slots,
                    amount, price, purchaseLimit, stock, lore, commands, false);
            this.additionalRotatingItems.add(typeItem);
        }
    }

}
