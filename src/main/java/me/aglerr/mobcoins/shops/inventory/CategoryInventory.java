package me.aglerr.mobcoins.shops.inventory;

import com.google.common.primitives.Ints;
import fr.mrmicky.fastinv.FastInv;
import me.aglerr.mobcoins.MobCoins;
import me.aglerr.mobcoins.configs.ConfigValue;
import me.aglerr.mobcoins.managers.managers.ShopManager;
import me.aglerr.mobcoins.shops.items.TypeItem;
import me.aglerr.mobcoins.utils.libs.Common;
import me.aglerr.mobcoins.utils.ItemManager;
import me.aglerr.mobcoins.utils.libs.Executor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

public class CategoryInventory extends FastInv {

    public CategoryInventory(MobCoins plugin, Player player, int size, String title) {
        super(size, Common.color(title));

        // Set all items to the inventory
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
        for(TypeItem item : shopManager.getItemsLoader().getCategoryItems()){
            // Create the item
            ItemStack stack = ItemManager.createItemStackWithHeadTextures(player, item, item.getLore());
            // Put the item to the inventory
            this.setItems(Ints.toArray(item.getSlots()), stack, event -> {

                // Inventory Click Event //

                // Cancel the event so player cannot move the item around
                event.setCancelled(true);

                // Just return if the item doesn't have any type
                if(item.getType() == null) return;

                // Check if item type is equals to OPEN_MAIN_MENU
                if(item.getType().equalsIgnoreCase("OPEN_MAIN_MENU")){
                    // Opening the main menu inventory for player
                    shopManager.openInventory(player, ShopManager.InventoryType.MAIN_MENU);
                }

                if(item.getType().equalsIgnoreCase("OPEN_ROTATING_SHOP")){
                    shopManager.openInventory(player, ShopManager.InventoryType.ROTATING_SHOP);
                }

                // Check if item type is equals to OPEN_CATEGORY
                if(item.getType().equalsIgnoreCase("OPEN_CATEGORY")){
                    // Return if the item doesn't have any category set
                    if(item.getCategory() == null){
                        Common.debug(player.getName() + " trying to open a category, but the item doesn't have a category set (item: " + item.getConfigKey() + ")");
                        return;
                    }

                    // Opening category
                    shopManager.openCategoryShop(item.getCategory(), player);
                }

            });

        }

    }

}
