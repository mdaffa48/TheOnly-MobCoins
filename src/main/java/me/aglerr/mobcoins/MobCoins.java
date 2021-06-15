package me.aglerr.mobcoins;

import fr.mrmicky.fastinv.FastInvManager;
import me.aglerr.mobcoins.commands.MainCommand;
import me.aglerr.mobcoins.configs.Config;
import me.aglerr.mobcoins.configs.ConfigValue;
import me.aglerr.mobcoins.database.SQLDatabase;
import me.aglerr.mobcoins.listeners.ListenerHandler;
import me.aglerr.mobcoins.managers.ManagerHandler;
import me.aglerr.mobcoins.managers.managers.CoinMobManager;
import me.aglerr.mobcoins.managers.managers.RotatingShopManager;
import me.aglerr.mobcoins.managers.managers.ShopManager;
import me.aglerr.mobcoins.shops.items.ItemsLoader;
import me.aglerr.mobcoins.utils.Common;
import me.aglerr.mobcoins.utils.ConfigUpdater;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MobCoins extends JavaPlugin {

    private static MobCoins instance;

    private final ManagerHandler managerHandler = new ManagerHandler(this);
    private final ListenerHandler listenerHandler = new ListenerHandler(this);

    private SQLDatabase database;

    /**
     * Plugin startup logic
     */
    @Override
    public void onEnable(){
        // Initialize instance
        instance = this;

        Common.log(false, Common.getStartupLogo());

        // Initialize all config
        Config.initialize();

        // Initialize all config value
        ConfigValue.initializeValue();

        // Initialize database
        database = new SQLDatabase(this);

        // Calling load() method from all Managers
        this.managerHandler.loadAllManagers();

        // Register all listeners
        this.listenerHandler.registerAllListeners();

        // Register main commands
        new MainCommand(this).registerThisCommand();

        // Update the config
        this.updateConfig();
    }

    /**
     * Plugin disable logic
     */
    @Override
    public void onDisable(){
        this.managerHandler.saveAllManagers();
    }

    private void updateConfig(){
        File configFile = new File(this.getDataFolder(), "config.yml");
        try{
            ConfigUpdater.update(this, "config.yml", configFile, new ArrayList<>());
        } catch(IOException e){
            Common.error(true, "Failed to update the config.yml");
            e.printStackTrace();
        }
    }

    public void reloadEverything(){
        ShopManager shopManager = this.managerHandler.getShopManager();
        RotatingShopManager rotatingShopManager = this.managerHandler.getRotatingShopManager();
        CoinMobManager coinMobManager = this.managerHandler.getCoinMobManager();
        ItemsLoader itemsLoader = shopManager.getItemsLoader();

        // Reload all configuration
        Config.reloadAllConfigs();

        // Re-initialize the config value
        ConfigValue.initializeValue();

        // Save the rotating items to the config
        rotatingShopManager.save();

        // Clear all items from the ItemsLoader and then load it back
        itemsLoader.clearAllItems();
        shopManager.loadItems();

        // Clear all coin mobs and then load it back
        coinMobManager.clearCoinMob();
        coinMobManager.load();

    }

    /**
     * Not for regular use.
     * @return this class instance
     */
    public static MobCoins getInstance() {
        return instance;
    }

    public SQLDatabase getDatabase() {
        return database;
    }

    public ManagerHandler getManagerHandler() {
        return managerHandler;
    }
}
