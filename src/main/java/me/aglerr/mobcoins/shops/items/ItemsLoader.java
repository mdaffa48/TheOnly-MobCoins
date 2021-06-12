package me.aglerr.mobcoins.shops.items;

import me.aglerr.mobcoins.configs.Config;
import me.aglerr.mobcoins.utils.Common;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class ItemsLoader {

    private final List<TypeItem> mainMenuItems = new ArrayList<>();
    private final List<TypeItem> additionalRotatingItems = new ArrayList<>();
    private final List<TypeItem> rotatingItems = new ArrayList<>();
    private final List<TypeItem> confirmationItems = new ArrayList<>();

    public void loadMainMenuItems(){
        FileConfiguration config = Config.MAIN_MENU_CONFIG.getConfig();
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
                    amount, price, purchaseLimit, stock, lore, commands, false, false);
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
                    amount, price, purchaseLimit, stock, lore, commands, false, false);
            this.additionalRotatingItems.add(typeItem);
        }
    }

    public void loadRotatingItems(){
        FileConfiguration config = Config.ROTATING_SHOP_CONFIG.getConfig();
        FileConfiguration temp = Config.TEMP_DATA.getConfig();

        for(String key : config.getConfigurationSection("shops").getKeys(false)){
            String path = "shops." + key;
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
            boolean special = config.getBoolean(path + ".special");

            TypeItem typeItem = new TypeItem(key, type, category, material, name, glow, slots,
                    amount, price, purchaseLimit, stock, lore, commands, true, special);
            this.rotatingItems.add(typeItem);
        }

        final Map<String, TypeItem> typeItemMap = new HashMap<>();

        final List<TypeItem> normalItems = new ArrayList<>();
        final List<TypeItem> specialItems = new ArrayList<>();
        final List<TypeItem> addLater = new ArrayList<>();

        List<String> storedNormalItems = temp.getStringList("rotatingShop.normalItems");
        List<String> storedSpecialItems = temp.getStringList("rotatingShop.specialItems");

        this.rotatingItems.forEach(item -> {
            typeItemMap.put(item.getConfigKey(), item);

            if(!storedNormalItems.contains(item.getConfigKey()) && !storedSpecialItems.contains(item.getConfigKey())){
                addLater.add(item);
            }

        });

        this.rotatingItems.clear();

        storedNormalItems.forEach(itemKey -> normalItems.add(typeItemMap.get(itemKey)));
        storedSpecialItems.forEach(itemKey -> specialItems.add(typeItemMap.get(itemKey)));

        this.rotatingItems.addAll(normalItems);
        this.rotatingItems.addAll(specialItems);
        this.rotatingItems.addAll(addLater);

    }



    public void loadConfirmationItems(){
        FileConfiguration config = Config.CONFIRMATION_MENU_CONFIG.getConfig();
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
                    amount, price, purchaseLimit, stock, lore, commands, false, false);
            this.confirmationItems.add(typeItem);
        }
    }

    public List<TypeItem> getConfirmationItems() { return confirmationItems; }

    public List<TypeItem> getAdditionalRotatingItems() {
        return additionalRotatingItems;
    }

    public List<TypeItem> getMainMenuItems() {
        return mainMenuItems;
    }

    public List<TypeItem> getRotatingItems(){
        return rotatingItems;
    }

}
