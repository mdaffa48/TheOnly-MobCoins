package me.aglerr.mobcoins.managers.managers;

import me.aglerr.mclibs.libs.Common;
import me.aglerr.mclibs.libs.CustomConfig;
import me.aglerr.mclibs.libs.Executor;
import me.aglerr.mobcoins.MobCoins;
import me.aglerr.mobcoins.configs.Config;
import me.aglerr.mobcoins.configs.ConfigValue;
import me.aglerr.mobcoins.managers.Manager;
import me.aglerr.mobcoins.shops.items.TypeItem;
import me.aglerr.mobcoins.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

public class CategoryShopManager implements Manager {

    private final MobCoins plugin;
    public CategoryShopManager(MobCoins plugin){
        this.plugin = plugin;
    }

    private int resetTime = 0;
    private int DEFAULT_RESET_TIME;

    public String getFormattedResetTime(){
        return Utils.formatTime(resetTime);
    }

    private void resetPurchaseLimitAndStockInCategoryShop(){
        // Get the stock manager
        StockManager stockManager = plugin.getManagerHandler().getStockManager();
        // Get the purchase limit manager
        PurchaseLimitManager purchaseLimitManager = plugin.getManagerHandler().getPurchaseLimitManager();
        // Loop through all category shop items
        for(TypeItem item : plugin.getManagerHandler().getShopManager().getItemsLoader().getCategoryShopItems()){
            // Reset all items stock for this item
            stockManager.clearStockForItem(item);
            // Reset all purchase limit for this item
            purchaseLimitManager.clearPurchaseLimitForItem(item);
        }
    }

    @Override
    public void load() {
        FileConfiguration category = Config.CATEGORY_SHOP_CONFIG.getConfig();
        FileConfiguration temp = Config.TEMP_DATA.getConfig();
        DEFAULT_RESET_TIME = category.getInt("categoryShop.resetTime");
        // Get the saved category reset time
        int savedResetTime = temp.getInt("categoryShop.resetTime");
        // If saved reset time is below or equals to 0, assign the default time
        if(savedResetTime <= 0){
            resetTime = DEFAULT_RESET_TIME;
        } else {
            // If the saved reset time is not below or not equals to 0, assign the saved time
            resetTime = savedResetTime;
        }
        // Run the time task
        Executor.asyncTimer(0, 20, () -> {
            // Check if the reset time is or below 0
            if(resetTime <= 0){
                // Set the reset time back to the default time
                resetTime = DEFAULT_RESET_TIME;
                // Loop through all online players
                Bukkit.getOnlinePlayers().forEach(player -> {
                    // Broadcast the message
                    if(ConfigValue.CATEGORY_BROADCAST_ENABLED){
                        ConfigValue.CATEGORY_BROADCAST_MESSAGES.forEach(message ->
                                player.sendMessage(Common.color(message)));
                    }
                    // Send Titles
                    Utils.sendTitle(player, "categoryShop.refreshActions.titles", category, 0);
                    // Play Sound
                    Utils.playSound(player, "categoryShop.refreshActions.sound", category);
                });
                // Executes command
                if(ConfigValue.CATEGORY_COMMANDS_ENABLED){
                    ConfigValue.CATEGORY_COMMANDS_COMMANDS.forEach(command ->
                            Executor.sync(() -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command)));
                }
                // Reset purchase limit and stock
                this.resetPurchaseLimitAndStockInCategoryShop();
                return;
            }
            // Decrease reset time by 1 every second
            resetTime--;
        });
    }

    @Override
    public void save() {
        // Get the temp config
        CustomConfig temp = Config.TEMP_DATA;
        // Get the temp config file configuration
        FileConfiguration config = temp.getConfig();
        // Set the reset time category shop
        config.set("categoryShop.resetTime", resetTime);
        // Save the config
        temp.saveConfig();
    }

}
