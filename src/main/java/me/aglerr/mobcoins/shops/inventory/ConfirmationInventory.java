package me.aglerr.mobcoins.shops.inventory;

import com.google.common.primitives.Ints;
import me.aglerr.lazylibs.inventory.LazyInventory;
import me.aglerr.lazylibs.libs.Common;
import me.aglerr.mobcoins.MobCoins;
import me.aglerr.mobcoins.PlayerData;
import me.aglerr.mobcoins.configs.ConfigValue;
import me.aglerr.mobcoins.managers.managers.PurchaseLimitManager;
import me.aglerr.mobcoins.managers.managers.ShopManager;
import me.aglerr.mobcoins.managers.managers.StockManager;
import me.aglerr.mobcoins.shops.items.TypeItem;
import me.aglerr.mobcoins.utils.ItemManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

/**
 * This inventory used when player want to purchase something on the shop if the options is enabled
 */
public class ConfirmationInventory extends LazyInventory {

    public ConfirmationInventory(MobCoins plugin, Player player, ItemStack stack, ShopManager.InventoryType inventoryType, PlayerData playerData, TypeItem buyItem, @Nullable String category, int size, String title) {
        super(size, Common.color(title));

        this.setAllItems(plugin, player, stack, inventoryType, playerData, buyItem, category);
    }

    private void setAllItems(MobCoins plugin, Player player, ItemStack stack, ShopManager.InventoryType inventoryType, PlayerData playerData, TypeItem buyItem, String category){
        ShopManager shopManager = plugin.getManagerHandler().getShopManager();
        // Loop through all loaded confirmation menu items
        for(TypeItem item : shopManager.getItemsLoader().getConfirmationItems()){
            // Create the item
            ItemStack finalStack = item.getType().equalsIgnoreCase("DISPLAY_ITEM") ? stack : ItemManager.createItemStackWithHeadTextures(player, item, item.getLore());
            // Put the item to the inventory
            this.setItems(Ints.toArray(item.getSlots()), finalStack, event -> {
                // Logic when player confirming buying item
                if(item.getType().equalsIgnoreCase("CONFIRM_BUY")){
                    this.handleConfirming(plugin, player, buyItem, inventoryType, playerData, category);
                }
                // Cancel
                if(item.getType().equalsIgnoreCase("CANCEL_BUY")){
                    // First we need to check if confirmation menu is not from category shop
                    if(category == null){
                        // Open the latest menu
                        shopManager.openInventory(player, inventoryType);
                        return;
                    }
                    // If it is from category shop, we open the category shop back
                    shopManager.openCategoryShop(category, player);
                }
            });
        }
    }

    private void handleConfirming(MobCoins plugin, Player player, TypeItem item, ShopManager.InventoryType inventoryType, PlayerData playerData, String category){
        // Get player data from MobCoinsAPI
        StockManager stockManager = plugin.getManagerHandler().getStockManager();
        PurchaseLimitManager purchaseLimitManager = plugin.getManagerHandler().getPurchaseLimitManager();
        ShopManager shopManager = plugin.getManagerHandler().getShopManager();

        // Reduce player mobcoins
        playerData.reduceCoins(item.getPrice());

        // Handle the stock system for this item
        stockManager.putOrDecrease(item);

        // Handle the purchase limit for this player and item
        purchaseLimitManager.putOrIncreasePlayerPurchaseLimit(player, item);

        // Executes all configured commands on this item
        for(String command : item.getCommands()){
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("{player}", player.getName()));
        }

        // If enabled, inventory will be closed or else the latest shop inventory will open
        if(ConfigValue.CLOSE_AFTER_PURCHASE){
            // Close the inventory
            player.closeInventory();
            return;
        }

        // What should we do If the close after purchase not enabled

        // First, we check if the player not opening confirmation menu from category shop
        if(category == null) {
            // Open the inventory for player
            shopManager.openInventory(player, inventoryType);
            return;
        }

        // If the confirmation menu opened from category shop
        // We open the category shop back
        shopManager.openCategoryShop(category, player);


    }

}
