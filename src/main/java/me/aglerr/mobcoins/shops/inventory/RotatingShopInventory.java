package me.aglerr.mobcoins.shops.inventory;

import com.google.common.primitives.Ints;
import fr.mrmicky.fastinv.FastInv;
import me.aglerr.mobcoins.MobCoins;
import me.aglerr.mobcoins.configs.ConfigValue;
import me.aglerr.mobcoins.managers.managers.ShopManager;
import me.aglerr.mobcoins.shops.items.TypeItem;
import me.aglerr.mobcoins.utils.Common;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

public class RotatingShopInventory extends FastInv {

    public RotatingShopInventory(MobCoins plugin, Player player, int size, String title) {
        super(size, Common.color(title));

        // Placing all additional rotating shop items
        this.setAllItems(plugin.getManagerHandler().getShopManager(), player);

        if(ConfigValue.AUTO_UPDATE_ENABLED){
            // Start the updating task when player open the inventory
            BukkitTask task = Common.runTaskTimer(0,
                    ConfigValue.AUTO_UPDATE_UPDATE_EVERY,
                    () -> this.setAllItems(plugin.getManagerHandler().getShopManager(), player));

            // Stopping task when player close the inventory
            this.addCloseHandler(event -> task.cancel());
        }

    }

    private void setAllItems(ShopManager shopManager, Player player){
        // Loop through all loaded additional rotating items
        for(TypeItem item : shopManager.getItemsLoader().getAdditionalRotatingItems()){
            // Create the item
            ItemStack stack = Common.createItemStackWithHeadTextures(player, item);
            // Put the item on the inventory
            this.setItems(Ints.toArray(item.getSlots()), stack);
        }
    }

}
