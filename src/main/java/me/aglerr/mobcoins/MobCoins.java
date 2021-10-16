package me.aglerr.mobcoins;

import me.aglerr.lazylibs.LazyLibs;
import me.aglerr.lazylibs.inventory.LazyInventory;
import me.aglerr.lazylibs.inventory.LazyInventoryManager;
import me.aglerr.lazylibs.libs.Common;
import me.aglerr.lazylibs.libs.ConfigUpdater;
import me.aglerr.lazylibs.libs.UpdateChecker;
import me.aglerr.mobcoins.commands.MainCommand;
import me.aglerr.mobcoins.configs.Config;
import me.aglerr.mobcoins.configs.ConfigValue;
import me.aglerr.mobcoins.database.SQLDatabase;
import me.aglerr.mobcoins.listeners.ListenerHandler;
import me.aglerr.mobcoins.managers.ManagerHandler;
import me.aglerr.mobcoins.managers.managers.CoinMobManager;
import me.aglerr.mobcoins.managers.managers.RotatingShopManager;
import me.aglerr.mobcoins.managers.managers.ShopManager;
import me.aglerr.mobcoins.metrics.Metrics;
import me.aglerr.mobcoins.shops.items.ItemsLoader;
import me.aglerr.mobcoins.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MobCoins extends JavaPlugin {

    private static final int RESOURCE_ID = 93470;
    private static final String SPIGOT_RESOURCE = "https://www.spigotmc.org/resources/93470/";
    private static final String MC_MARKET_RESOURCE = "https://www.mc-market.org/resources/20645/";
    private static final String SPIGOT = "Spigot";
    private static final String MC_MARKET = "MC-Market";
    private static final String DONATION_LINK = "https://paypal.me/mdaffa48/";

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
        // Injecting libs
        LazyLibs.inject(this);
        Common.setPrefix("[TheOnly-Mobcoins]");
        // Send the-only logo
        Utils.sendStartupLogo();
        // Update the config
        this.updateConfig();
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
        // Enable metrics
        new Metrics(this, 11755);
        // Check for the plugin updates
        this.checkForUpdates();
        // Start update leaderboard task
    }

    /**
     * Plugin disable logic
     */
    @Override
    public void onDisable(){
        this.managerHandler.saveAllManagers();
    }

    public void reloadEverything(){
        ShopManager shopManager = this.managerHandler.getShopManager();
        RotatingShopManager rotatingShopManager = this.managerHandler.getRotatingShopManager();
        CoinMobManager coinMobManager = this.managerHandler.getCoinMobManager();
        ItemsLoader itemsLoader = shopManager.getItemsLoader();
        // First of all, close all inventory first
        LazyInventoryManager.closeAll();
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

    private void updateConfig(){
        File configFile = new File(this.getDataFolder(), "config.yml");
        try{
            ConfigUpdater.update(this, "config.yml", configFile, new ArrayList<>());
        } catch(IOException e){
            Common.log(ChatColor.RED, "Failed to update the config.yml");
            e.printStackTrace();
        }
    }

    private void checkForUpdates(){
        // Return if notify update is disabled
        if(!ConfigValue.NOTIFY_UPDATE) return;
        // Initialize the update checker
        UpdateChecker.init(this, RESOURCE_ID)
                .setFreeDownloadLink(SPIGOT_RESOURCE)
                .setPaidDownloadLink(MC_MARKET_RESOURCE)
                .setNameFreeVersion(SPIGOT)
                .setNamePaidVersion(MC_MARKET)
                .setDonationLink(DONATION_LINK)
                .setColoredConsoleOutput(true)
                .setNotifyOpsOnJoin(true)
                .checkNow();
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
