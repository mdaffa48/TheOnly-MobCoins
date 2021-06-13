package me.aglerr.mobcoins.shops.items;

import me.aglerr.mobcoins.MobCoins;
import me.aglerr.mobcoins.configs.Config;
import me.aglerr.mobcoins.configs.CustomConfig;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;

public class ItemsLoader {

    private final MobCoins plugin;
    public ItemsLoader(MobCoins plugin){
        this.plugin = plugin;
    }

    private final List<TypeItem> mainMenuItems = new ArrayList<>();
    private final List<TypeItem> additionalRotatingItems = new ArrayList<>();
    private final List<TypeItem> rotatingItems = new ArrayList<>();
    private final List<TypeItem> confirmationItems = new ArrayList<>();
    private final List<TypeItem> categoryItems = new ArrayList<>();
    private final List<TypeItem> categoryShopItems = new ArrayList<>();

    private final Map<String, FileConfiguration> categoryShopFiles = new HashMap<>();

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
                    amount, price, purchaseLimit, stock, lore, commands, false, false,
                    null);
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
                    amount, price, purchaseLimit, stock, lore, commands, false, false,
                    null);
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
                    amount, price, purchaseLimit, stock, lore, commands, true, special,
                    null);
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
                    amount, price, purchaseLimit, stock, lore, commands, false, false,
                    null);
            this.confirmationItems.add(typeItem);
        }
    }

    public void loadCategoryItems(){
        FileConfiguration config = Config.CATEGORY_SHOP_CONFIG.getConfig();
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
                    amount, price, purchaseLimit, stock, lore, commands, false, false,
                    null);
            this.categoryItems.add(typeItem);
        }
    }

    public void loadCategoryShopItems(){

        File directory = new File(plugin.getDataFolder() + File.separator + "categories");
        if(!directory.exists()){
            directory.mkdirs();
        }

        // Get all files on the 'categories' folder
        File[] files = new File(plugin.getDataFolder() + File.separator + "categories").listFiles();

        // Return if 'categories' folder doesn't have any files
        // And create an example file
        if(files.length <= 0) {

            // We only want to create an example files once
            // So we store boolean in the temporary data configuration

            CustomConfig temp = Config.TEMP_DATA;
            FileConfiguration config = temp.getConfig();

            // Get the boolean from the config file
            boolean exampleFile = config.getBoolean("createExampleFile");

            // If return false, we create the example files
            if(!exampleFile){

                // Get the path to the categories folder
                String path = plugin.getDataFolder() + File.separator + "categories" + File.separator;

                // Create armor.yml example file
                plugin.saveResource(path + "armor.yml", false);

                // Create weaponsAndTools.yml example file
                plugin.saveResource(path + "weaponsAndTools.yml", false);

                // Set 'true' on the 'createExampleFile' so we know we have created the file
                config.set("createExampleFile", true);

                // Save the config
                temp.saveConfig();

            }

        }

        // Create a null FileConfiguration
        FileConfiguration config;

        for(File file : files){
            // Instantiate the config
            config = YamlConfiguration.loadConfiguration(file);
            String fileName = file.getName();

            // Store the config in a map with file name as the key
            this.categoryShopFiles.put(fileName, config);

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
                        amount, price, purchaseLimit, stock, lore, commands, false, false,
                        fileName);

                this.categoryShopItems.add(typeItem);
            }

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

    public List<TypeItem> getCategoryItems(){
        return categoryItems;
    }

    public List<TypeItem> getCategoryShopItems() {
        return categoryShopItems;
    }

    public Map<String, FileConfiguration> getCategoryShopFiles() {
        return categoryShopFiles;
    }

}
