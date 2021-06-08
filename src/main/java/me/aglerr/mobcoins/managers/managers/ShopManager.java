package me.aglerr.mobcoins.managers.managers;

import fr.mrmicky.fastinv.FastInv;
import fr.mrmicky.fastinv.FastInvManager;
import me.aglerr.mobcoins.MobCoins;
import me.aglerr.mobcoins.configs.Config;
import me.aglerr.mobcoins.managers.Manager;
import me.aglerr.mobcoins.shops.inventory.MainMenuInventory;
import me.aglerr.mobcoins.shops.items.ItemsLoader;
import me.aglerr.mobcoins.utils.Common;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ShopManager implements Manager {

    private final ItemsLoader itemsLoader;

    private final MobCoins plugin;
    public ShopManager(MobCoins plugin){
        this.plugin = plugin;
        this.itemsLoader = new ItemsLoader(plugin);
    }

    public void loadItems(){
        this.itemsLoader.loadMainMenuItems();
    }

    public void openInventory(Player player, InventoryType inventoryType){

        // Check if requested inventory is MAIN_MENU
        if(inventoryType == InventoryType.MAIN_MENU){

            FileConfiguration config = Config.MAIN_MENU_CONFIG.getConfig();

            // Create a new inventory instance and open it for player
            String title = config.getString("title");
            int size = config.getInt("size");

            FastInv inventory = new MainMenuInventory(plugin, player, size, title);
            inventory.open(player);
            return;

        }

    }

    public ItemsLoader getItemsLoader() {
        return itemsLoader;
    }

    @Override
    public void load() {
        FastInvManager.register(plugin);
        loadItems();
    }

    @Override
    public void save() {

    }

    public enum InventoryType{
        MAIN_MENU,
        ROTATING_SHOP,
        CATEGORY_SHOP
    }

}
