package me.aglerr.mobcoins.shops.inventory;

import me.aglerr.krakenmobcoins.api.MobCoinsAPI;
import me.aglerr.lazylibs.inventory.LazyInventory;
import me.aglerr.lazylibs.libs.Common;
import me.aglerr.lazylibs.libs.Executor;
import me.aglerr.lazylibs.libs.ItemBuilder;
import me.aglerr.lazylibs.libs.XMaterial;
import me.aglerr.mobcoins.MobCoins;
import me.aglerr.mobcoins.PlayerData;
import me.aglerr.mobcoins.managers.managers.DependencyManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class ConvertInventory extends LazyInventory {

    public ConvertInventory(MobCoins plugin) {
        super(9, "Convert Data Menu");

        setAllItems(plugin);

    }

    private void setAllItems(MobCoins plugin){
        // If super mob coins is enabled, create the item
        if(DependencyManager.SUPER_MOB_COINS){
            // Create the item using item builder
            ItemStack stack = new ItemBuilder(XMaterial.LIME_STAINED_GLASS_PANE.parseItem())
                    .name("&aSuperMobCoins")
                    .lore("&7Convert super mob coins data to this plugin")
                    .build();
            // Set the item in the inventory
            this.setItem(0, stack, event -> {
                // Run the convert method for super mob coins
                superMobCoins(plugin);
            });
        }
    }

    private void superMobCoins(MobCoins plugin){
        // If super mob coins doesn't exist, just return
        if(!DependencyManager.SUPER_MOB_COINS) return;
        // Get the plugin first
        Plugin superMC = Bukkit.getPluginManager().getPlugin("SuperMobCoins");
        // Get the profiles.yml file from their plugin directory
        File file = new File(superMC.getDataFolder(), "profiles.yml");
        // Stop the code if the file doesn't exist
        if(!file.exists()) return;
        // Run an async task to convert the data
        Executor.async(() -> {
            // Get the file configuration
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            // Loop through all profiles
            for(String uuid : config.getConfigurationSection("Profile").getKeys(false)){
                // Get the amount of mobcoins that the player has
                double coins = config.getDouble("Profile." + uuid + ".mobcoins");
                // Create a new player data
                PlayerData playerData = new PlayerData(uuid, coins);
                // Override the data from the map
                plugin.getManagerHandler().getPlayerDataManager().overrideData(playerData);
                // Add log messages to the console
                Common.log(ChatColor.RESET, "Converting " + uuid + " data from SuperMobCoins!");
            }
            Common.log(ChatColor.GREEN, "Successfully converting all data, you're ready to go!");
        });
    }

    private void krakenMobCoins(MobCoins plugin){

    }

}
