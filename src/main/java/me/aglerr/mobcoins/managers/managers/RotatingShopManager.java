package me.aglerr.mobcoins.managers.managers;

import com.cryptomorin.xseries.messages.Titles;
import me.aglerr.mobcoins.MobCoins;
import me.aglerr.mobcoins.configs.Config;
import me.aglerr.mobcoins.configs.ConfigValue;
import me.aglerr.mobcoins.configs.CustomConfig;
import me.aglerr.mobcoins.managers.Manager;
import me.aglerr.mobcoins.shops.items.TypeItem;
import me.aglerr.mobcoins.utils.Common;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class RotatingShopManager implements Manager {

    private final MobCoins plugin;
    public RotatingShopManager(MobCoins plugin){
        this.plugin = plugin;
    }

    private int normalTime = 0;
    private int specialTime = 0;

    public int getNormalTime(){
        return normalTime;
    }

    public int getSpecialTime(){
        return specialTime;
    }

    public String getFormattedNormalTime(){
        return Common.getFormattedTime(normalTime);
    }

    public String getFormattedSpecialTime(){
        return Common.getFormattedTime(specialTime);
    }

    public void shuffleNormalItemsAndResetStockAndPurchaseLimit(){

        ShopManager shopManager = plugin.getManagerHandler().getShopManager();

        // Set the normal time back to the default
        normalTime = ConfigValue.DEFAULT_NORMAL_TIME_RESET;

        // Create an empty list so we know the list of all normal items
        List<TypeItem> normalItems = new ArrayList<>();

        // Loop through rotating items list
        for(TypeItem item : shopManager.getItemsLoader().getRotatingItems()){

            // Skip if the item is a special item
            if(item.isSpecial()) continue;

            // Add the item to the list
            normalItems.add(item);

        }

        // Remove the item from the rotating items list if the item is a normal item
        shopManager.getItemsLoader().getRotatingItems().removeIf(item -> !item.isSpecial());

        StockManager stockManager = plugin.getManagerHandler().getStockManager();
        PurchaseLimitManager purchaseLimitManager = plugin.getManagerHandler().getPurchaseLimitManager();

        // Loop through all normal items
        for(TypeItem item : normalItems){
            // Reset all of normal items stock
            stockManager.clearStockForItem(item);
            // Reset all of normal items purchase limit
            purchaseLimitManager.clearPurchaseLimitForItem(item);
        }

        // Shuffle the normal items
        Collections.shuffle(normalItems);

        // Add them back to the rotating items list
        shopManager.getItemsLoader().getRotatingItems().addAll(normalItems);

    }

    public void shuffleSpecialItemsAndResetStockAndPurchaseLimit(){
        ShopManager shopManager = plugin.getManagerHandler().getShopManager();

        // Set the special time back to the default
        specialTime = ConfigValue.DEFAULT_SPECIAL_TIME_RESET;

        // Create an empty list so we know the list of all normal items
        List<TypeItem> specialItems = new ArrayList<>();

        // Loop through rotating items list
        for(TypeItem item : shopManager.getItemsLoader().getRotatingItems()){

            // Skip the item if not a special item
            if(!item.isSpecial()) continue;

            // Add the item to the list
            specialItems.add(item);

        }

        // Remove the item from the rotating items list if the item is a special item
        shopManager.getItemsLoader().getRotatingItems().removeIf(TypeItem::isSpecial);

        StockManager stockManager = plugin.getManagerHandler().getStockManager();
        PurchaseLimitManager purchaseLimitManager = plugin.getManagerHandler().getPurchaseLimitManager();

        // Loop through all special items
        for(TypeItem item : specialItems){
            // Reset all of special items stock
            stockManager.clearStockForItem(item);
            // Reset all of special items purchase limit
            purchaseLimitManager.clearPurchaseLimitForItem(item);
        }

        // Shuffle the normal items
        Collections.shuffle(specialItems);

        // Add them back to the rotating items list
        shopManager.getItemsLoader().getRotatingItems().addAll(specialItems);

    }

    @Override
    public void load() {
        FileConfiguration config = Config.TEMP_DATA.getConfig();
        FileConfiguration rotating = Config.ROTATING_SHOP_CONFIG.getConfig();

        // Check if there is no normal time saved
        int savedNormalTime = config.getInt("rotatingShop.normalTime");
        int savedSpecialTime = config.getInt("rotatingShop.specialTime");

        // If saved normal time is below or equals to 0, assign the default time
        if(savedNormalTime <= 0){
            normalTime = ConfigValue.DEFAULT_NORMAL_TIME_RESET;
        } else {
            // If the saved normal time is not below or equals to 0, assign the saved time
            normalTime = savedNormalTime;
        }

        // If saved normal time is below or equals to 0, assign the default time
        if(savedSpecialTime <= 0){
            specialTime = ConfigValue.DEFAULT_SPECIAL_TIME_RESET;
        } else {
            // If the saved normal time is not below or equals to 0, assign the saved time
            specialTime = savedSpecialTime;
        }

        // Run task timer for handling the time and refreshing normal/special items event
        Common.runTaskTimerAsynchronously(0, 20, () -> {

            if(normalTime <= 0){
                // Play bunch of events (send messages, titles, sound, commands)
                Bukkit.getOnlinePlayers().forEach(player -> {
                    // Send Messages
                    if(ConfigValue.NORMAL_IS_BROADCAST_MESSAGE){
                        ConfigValue.NORMAL_BROADCAST_MESSAGE_MESSAGES.forEach(message -> player.sendMessage(Common.color(message)));
                    }
                    // Send Titles
                    Common.sendTitle(player, "rotatingShop.refreshActions.normalItems.titles", rotating, 0);
                    // Play Sound
                    Common.playSound(player, "rotatingShop.refreshActions.normalItems.sound", rotating);
                });
                // Executes command
                if(ConfigValue.NORMAL_IS_COMMAND){
                    ConfigValue.NORMAL_COMMAND_COMMANDS.forEach(message ->
                            Common.runTask(() ->
                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), message)));
                }
                // Shuffle/Rotate the item and reset stock and purchase limit
                this.shuffleNormalItemsAndResetStockAndPurchaseLimit();
                return;
            }

            if(specialTime <= 0){
                // Play bunch of events (send messages, titles, sound, commands)
                Bukkit.getOnlinePlayers().forEach(player -> {
                    // Send Messages
                    if(ConfigValue.SPECIAL_IS_BROADCAST_MESSAGE){
                        ConfigValue.SPECIAL_BROADCAST_MESSAGE_MESSAGES.forEach(message -> player.sendMessage(Common.color(message)));
                    }
                    // Send Titles
                    Common.sendTitle(player, "rotatingShop.refreshActions.specialItems.titles", rotating, 0);
                    // Play Sound
                    Common.playSound(player, "rotatingShop.refreshActions.specialItems.sound", rotating);
                });
                // Executes command
                if(ConfigValue.SPECIAL_IS_COMMAND){
                    ConfigValue.SPECIAL_COMMAND_COMMANDS.forEach(message -> Common.runTask(() ->
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), message)));
                }
                // Shuffle/Rotate the item and reset stock and purchase limit
                this.shuffleSpecialItemsAndResetStockAndPurchaseLimit();
                return;
            }

            normalTime--;
            specialTime--;

        });

    }

    @Override
    public void save() {
        CustomConfig temp = Config.TEMP_DATA;
        FileConfiguration config = temp.getConfig();
        ShopManager shopManager = plugin.getManagerHandler().getShopManager();

        // Save the current normal and special time to the config
        config.set("rotatingShop.normalTime", normalTime);
        config.set("rotatingShop.specialTime", specialTime);

        // Create a List of which normal/special items that should be stored
        List<String> normalItemList = new ArrayList<>();
        List<String> specialItemList = new ArrayList<>();

        // Loop through all loaded rotating items
        for(TypeItem item : shopManager.getItemsLoader().getRotatingItems()){

            // Check if the item is a normal item and then store it on the list
            if(!item.isSpecial()){
                normalItemList.add(item.getConfigKey());
            }

            // Check if the item is a normal item and then store it on the list
            if(item.isSpecial()){
                specialItemList.add(item.getConfigKey());
            }

        }

        // Set the stored normal/special items to the config
        config.set("rotatingShop.normalItems", normalItemList);
        config.set("rotatingShop.specialItems", specialItemList);

        // Save the config
        temp.saveConfig();

    }

}
