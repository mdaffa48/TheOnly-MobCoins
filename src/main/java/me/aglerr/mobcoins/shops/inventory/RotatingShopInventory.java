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

public class RotatingShopInventory extends SimpleInventory {

    public RotatingShopInventory(MobCoins plugin, Player player, int size, String title) {
        super(size, Common.color(title));

        // Placing all additional rotating shop items
        this.setAllItems(plugin, player);

        if(ConfigValue.AUTO_UPDATE_ENABLED){
            // Start the updating task when player open the inventory
            BukkitTask task = Executor.syncTimer(0,
                    ConfigValue.AUTO_UPDATE_UPDATE_EVERY,
                    () -> this.setAllItems(plugin, player));

            // Stopping task when player close the inventory
            this.addCloseHandler(event -> task.cancel());
        }

    }

    private void setAllItems(MobCoins plugin, Player player){
        ShopManager shopManager = plugin.getManagerHandler().getShopManager();
        // Loop through all loaded additional rotating items
        for(TypeItem item : shopManager.getItemsLoader().getAdditionalRotatingItems()){
            // Create the item
            ItemStack stack = ItemManager.createItemStackWithHeadTextures(player, item, item.getLore());
            // Put the item on the inventory
            this.setItems(Ints.toArray(item.getSlots()), stack, event -> {

                // Cancel the event so player cannot move items on the inventory
                event.setCancelled(true);

                // Return if the item doesn't have any type
                if(item.getType() == null) return;

                // Open category shop, if the type of the item is OPEN_CATEGORY_SHOP
                if(item.getType().equalsIgnoreCase("OPEN_CATEGORY_SHOP")){
                    shopManager.openInventory(player, ShopManager.InventoryType.CATEGORY_SHOP);
                }

                // Open main menu page, if the type of the item is OPEN_MAIN_MENU
                if(item.getType().equalsIgnoreCase("OPEN_MAIN_MENU")){
                    shopManager.openInventory(player, ShopManager.InventoryType.MAIN_MENU);
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
                }

            });
        }

        FileConfiguration rotating = Config.ROTATING_SHOP_CONFIG.getConfig();
        StockManager stockManager = plugin.getManagerHandler().getStockManager();
        PurchaseLimitManager purchaseLimitManager = plugin.getManagerHandler().getPurchaseLimitManager();

        // Get the pre-configured slot for normal items.
        List<Integer> normalSlots = rotating.getIntegerList("rotatingShop.shopSlot.normalItems");

        // Get the pre-configured slot for special items.
        List<Integer> specialSlots = rotating.getIntegerList("rotatingShop.shopSlot.specialItems");

        // Counter for normal items and special items
        int normalItemCount = 0;
        int specialItemCount = 0;

        for(TypeItem item : shopManager.getItemsLoader().getRotatingItems()){

            // Skip the items if the item is a special item
            if(item.isSpecial()) continue;

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

            // Create the item stack
            ItemStack stack = ItemManager.createItemStackWithHeadTextures(player, item, lore);

            // Place the item on the inventory
            this.setItem(normalSlots.get(normalItemCount), stack,
                    event -> this.handleShop(plugin, player, item, event.getCurrentItem()));

            normalItemCount++;
            if(normalItemCount == normalSlots.size()) break;

        }

        for(TypeItem item : shopManager.getItemsLoader().getRotatingItems()){

            // Skip the items if the item is a normal item
            if(!item.isSpecial()) continue;

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

            // Create the item stack
            ItemStack stack = ItemManager.createItemStackWithHeadTextures(player, item, lore);

            // Place the item on the inventory
            this.setItem(specialSlots.get(specialItemCount), stack,
                    event -> this.handleShop(plugin, player, item, event.getCurrentItem()));

            specialItemCount++;
            if(specialItemCount == specialSlots.size()) break;

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

            SimpleInventory inventory = new ConfirmationInventory(plugin, player, stack, ShopManager.InventoryType.ROTATING_SHOP, playerData, item, null, size, title);
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
