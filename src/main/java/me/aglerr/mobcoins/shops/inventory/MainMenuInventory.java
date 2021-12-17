package me.aglerr.mobcoins.shops.inventory;

import com.google.common.primitives.Ints;
import me.aglerr.mclibs.inventory.SimpleInventory;
import me.aglerr.mclibs.libs.Common;
import me.aglerr.mclibs.libs.Debug;
import me.aglerr.mclibs.libs.Executor;
import me.aglerr.mobcoins.MobCoins;
import me.aglerr.mobcoins.configs.ConfigValue;
import me.aglerr.mobcoins.managers.managers.ShopManager;
import me.aglerr.mobcoins.shops.items.TypeItem;
import me.aglerr.mobcoins.utils.ItemManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

public class MainMenuInventory extends SimpleInventory {

    public MainMenuInventory(MobCoins plugin, Player player, int size, String title) {
        super(size, Common.color(title));

        // Placing all loaded main menu items
        this.setAllItems(plugin.getManagerHandler().getShopManager(), player);

        if(ConfigValue.AUTO_UPDATE_ENABLED){
            // Start the updating task when player open the inventory
            BukkitTask task = Executor.syncTimer(0,
                    ConfigValue.AUTO_UPDATE_UPDATE_EVERY,
                    () -> this.setAllItems(plugin.getManagerHandler().getShopManager(), player));

            // Stopping task when player close the inventory
            this.addCloseHandler(event -> task.cancel());
        }
    }

    private void setAllItems(ShopManager shopManager, Player player){
        // Loop through all loaded main menu items
        for(TypeItem item : shopManager.getItemsLoader().getMainMenuItems()){

            // Create the item
            ItemStack stack = ItemManager.createItemStackWithHeadTextures(player, item, item.getLore());

            // Put the item on the inventory
            setItems(Ints.toArray(item.getSlots()), stack, event -> {

                // Inventory Click Event //

                // Cancelling the event so player can not move items on the inventory
                event.setCancelled(true);

                // Opening category shop If the item type is OPEN_CATEGORY_SHOP
                if(item.getType().equalsIgnoreCase("OPEN_CATEGORY_SHOP")){
                    shopManager.openInventory(player, ShopManager.InventoryType.CATEGORY_SHOP);
                }

                // Opening rotating shop If the item type is OPEN_ROTATING_SHOP
                if(item.getType().equalsIgnoreCase("OPEN_ROTATING_SHOP")){
                    shopManager.openInventory(player, ShopManager.InventoryType.ROTATING_SHOP);
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
    }

}
