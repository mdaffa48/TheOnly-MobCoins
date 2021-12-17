package me.aglerr.mobcoins.shops.inventory;

import com.google.common.primitives.Ints;
import me.aglerr.mclibs.inventory.SimpleInventory;
import me.aglerr.mclibs.libs.Common;
import me.aglerr.mclibs.libs.Debug;
import me.aglerr.mclibs.libs.Executor;
import me.aglerr.mobcoins.MobCoins;
import me.aglerr.mobcoins.PlayerData;
import me.aglerr.mobcoins.api.MobCoinsAPI;
import me.aglerr.mobcoins.configs.Config;
import me.aglerr.mobcoins.configs.ConfigValue;
import me.aglerr.mobcoins.managers.managers.PurchaseLimitManager;
import me.aglerr.mobcoins.managers.managers.ShopManager;
import me.aglerr.mobcoins.managers.managers.StockManager;
import me.aglerr.mobcoins.shops.items.TypeItem;
import me.aglerr.mobcoins.utils.ItemManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class CategoryShopInventory extends SimpleInventory {

    public CategoryShopInventory(MobCoins plugin, Player player, String category, int size, String title) {
        super(size, Common.color(title));

        this.setAllItems(plugin, player, category);

        if(ConfigValue.AUTO_UPDATE_ENABLED){
            // Start the updating task when player open the inventory
            BukkitTask task = Executor.syncTimer(0,
                    ConfigValue.AUTO_UPDATE_UPDATE_EVERY,
                    () -> this.setAllItems(plugin, player, category));

            // Stopping task when player close the inventory
            this.addCloseHandler(event -> task.cancel());
        }

    }

    private void setAllItems(MobCoins plugin, Player player, String category){
        ShopManager shopManager = plugin.getManagerHandler().getShopManager();
        StockManager stockManager = plugin.getManagerHandler().getStockManager();
        PurchaseLimitManager purchaseLimitManager = plugin.getManagerHandler().getPurchaseLimitManager();

        // Loop through all category shop items
        for(TypeItem item : shopManager.getItemsLoader().getCategoryShopItems()){

            // Skip the item if the item is not from selected category
            if(!item.getFileName().equalsIgnoreCase(category)) continue;

            // Parse all internal placeholders
            List<String> lore = new ArrayList<>();
            item.getLore().forEach(line -> {
                String parsedMessage = line
                        .replace("{price}", item.getPrice() + "")
                        .replace("{player_limit}", purchaseLimitManager.getPlayerPurchaseLimit(player, item) + "")
                        .replace("{item_limit}", item.getPurchaseLimit() + "")
                        .replace("{stock}", stockManager.getStockInString(item) + "");

                lore.add(parsedMessage);
            });
            // Create the item stack with parsed lore
            ItemStack stack = ItemManager.createItemStackWithHeadTextures(player, item, lore);

            // Set the item to the inventory
            this.setItems(Ints.toArray(item.getSlots()), stack, event -> {

                // Inventory Click Event //

                // Cancel the event so player cannot move the item
                event.setCancelled(true);

                // Return if the type doesn't have any type
                if(item.getType() == null) return;

                // Check if the item type is OPEN_CATEGORY_SHOP
                if(item.getType().equalsIgnoreCase("OPEN_CATEGORY_SHOP")){
                    // Open the category shop for player
                    shopManager.openInventory(player, ShopManager.InventoryType.CATEGORY_SHOP);
                    return;
                }

                // Check if the item type is OPEN_MAIN_MENU
                if(item.getType().equalsIgnoreCase("OPEN_MAIN_MENU")){
                    // Open the main menu inventory for player
                    shopManager.openInventory(player, ShopManager.InventoryType.MAIN_MENU);
                    return;
                }

                // Check if the item type is OPEN_ROTATING_SHOP
                if(item.getType().equalsIgnoreCase("OPEN_ROTATING_SHOP")){
                    // Open the rotating shop inventory for player
                    shopManager.openInventory(player, ShopManager.InventoryType.ROTATING_SHOP);
                    return;
                }

                // Check if item type is equals to OPEN_CATEGORY
                if(item.getType().equalsIgnoreCase("OPEN_CATEGORY")){
                    // Return if the item doesn't have any category set
                    if(item.getCategory() == null){
                        Debug.send(player.getName() + " trying to open a category, but the item doesn't have a category set (item: " + item.getConfigKey() + ")");
                        return;
                    }

                    // Opening category
                    shopManager.openCategoryShop(item.getCategory(), player);
                    return;
                }

                if(item.getType().equalsIgnoreCase("BUYABLE_ITEM")){
                    this.handleShop(plugin, player, item, stack);
                }
            });
        }
    }

    private void handleShop(MobCoins plugin, Player player, TypeItem item, ItemStack stack){

        StockManager stockManager = plugin.getManagerHandler().getStockManager();
        PurchaseLimitManager purchaseLimitManager = plugin.getManagerHandler().getPurchaseLimitManager();

        // Return if the item is out of stock and send a message
        if(stockManager.getStockInInteger(item) == 0){
            player.sendMessage(Common.color(ConfigValue.MESSAGES_ITEM_OUT_OF_STOCK
                    .replace("{prefix}", ConfigValue.PREFIX)));
            return;
        }

        // Return if the player's purchase limit is equal to the item purchase limit
        int purchaseLimit = purchaseLimitManager.getPlayerPurchaseLimit(player, item);
        if(purchaseLimit >= item.getPurchaseLimit() && item.getPurchaseLimit() >= 0){
            player.sendMessage(Common.color(ConfigValue.MESSAGES_PURCHASE_LIMIT_REACHED
                    .replace("{prefix}", ConfigValue.PREFIX)));
            return;
        }

        // Check if player has enough money
        PlayerData playerData = MobCoinsAPI.getPlayerData(player);
        if(playerData == null){
            Debug.send(
                    "Event: Opening Rotating Shop Inventory",
                    "No PlayerData found for " + player.getName() + " (player)"
            );
            player.closeInventory();
            return;
        }

        double playerCoins = playerData.getCoins();
        if(playerCoins < item.getPrice()){
            player.sendMessage(Common.color(ConfigValue.MESSAGES_NOT_ENOUGH_COINS
                    .replace("{prefix}", ConfigValue.PREFIX)));
            return;
        }

        // Open confirmation menu if it's enabled
        if(ConfigValue.IS_CONFIRMATION_MENU){
            FileConfiguration confirmation = Config.CONFIRMATION_MENU_CONFIG.getConfig();

            String title = confirmation.getString("title");
            int size = confirmation.getInt("size");

            SimpleInventory inventory = new ConfirmationInventory(plugin, player, stack, ShopManager.InventoryType.CATEGORY_SHOP, playerData, item, item.getFileName(), size, title);
            inventory.open(player);
            return;
        }

        // Process the buy
        playerData.reduceCoins(item.getPrice());
        stockManager.putOrDecrease(item);
        purchaseLimitManager.putOrIncreasePlayerPurchaseLimit(player, item);

        // Executes all configured commands within the item
        for(String command : item.getCommands()){
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("{player}", player.getName()));
        }

        // Close inventory if the CLOSE_AFTER_PURCHASE option is enabled
        if(ConfigValue.CLOSE_AFTER_PURCHASE){
            player.closeInventory();
        }

    }

}
