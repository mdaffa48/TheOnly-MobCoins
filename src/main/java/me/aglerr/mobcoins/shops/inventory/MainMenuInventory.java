package me.aglerr.mobcoins.shops.inventory;

import com.google.common.primitives.Ints;
import fr.mrmicky.fastinv.FastInv;
import me.aglerr.mobcoins.MobCoins;
import me.aglerr.mobcoins.configs.ConfigValue;
import me.aglerr.mobcoins.managers.managers.ShopManager;
import me.aglerr.mobcoins.shops.items.TypeItem;
import me.aglerr.mobcoins.utils.Common;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

public class MainMenuInventory extends FastInv {
    public MainMenuInventory(MobCoins plugin, Player player, int size, String title) {
        super(size, Common.color(title));

        ShopManager shopManager = plugin.getManagerHandler().getShopManager();

        for(TypeItem item : shopManager.getItemsLoader().getMainMenuItems()){

            ItemStack stack = Common.createItemStackWithHeadTextures(player, item);
            setItems(Ints.toArray(item.getSlots()), stack, event -> {

                event.setCancelled(true);

                if(item.getType().equalsIgnoreCase("OPEN_CATEGORY_SHOP")){
                    Bukkit.broadcastMessage("Opening category shop!");
                }

                if(item.getType().equalsIgnoreCase("OPEN_ROTATING_SHOP")){
                    Bukkit.broadcastMessage("Opening rotating shop!");
                }
            });
        }

        if(ConfigValue.AUTO_UPDATE_ENABLED){
            BukkitTask task = Common.runTaskTimer(0, ConfigValue.AUTO_UPDATE_UPDATE_EVERY, () -> {
                for(TypeItem item : shopManager.getItemsLoader().getMainMenuItems()){

                    ItemStack stack = Common.createItemStackWithHeadTextures(player, item);
                    setItems(Ints.toArray(item.getSlots()), stack, event -> {

                        event.setCancelled(true);

                        if(item.getType().equalsIgnoreCase("OPEN_CATEGORY_SHOP")){
                            Bukkit.broadcastMessage("Opening category shop!");
                        }

                        if(item.getType().equalsIgnoreCase("OPEN_ROTATING_SHOP")){
                            Bukkit.broadcastMessage("Opening rotating shop!");
                        }
                    });
                }
            });

            this.addCloseHandler(event -> task.cancel());
        }

    }
}
