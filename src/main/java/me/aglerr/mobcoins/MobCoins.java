package me.aglerr.mobcoins;

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
import me.aglerr.mobcoins.utils.libs.Common;
import me.aglerr.mobcoins.utils.ConfigUpdater;
import me.aglerr.mobcoins.utils.UpdateChecker;
import me.aglerr.mobcoins.utils.libs.Executor;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MobCoins extends JavaPlugin {

    private static MobCoins instance;

    private final ManagerHandler managerHandler = new ManagerHandler(this);
    private final ListenerHandler listenerHandler = new ListenerHandler(this);

    private SQLDatabase database;
    private UpdateChecker updateChecker = null;

    /**
     * Plugin startup logic
     */
    @Override
    public void onEnable(){
        // Initialize instance
        instance = this;
        // Injecting libs
        Common.inject(this);
        Common.setPrefix("[TheOnly-Mobcoins]");
        Executor.inject(this);
        // Send the-only logo
        Utils.sendStartupLogo();
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
        // Enable metrics
        new Metrics(this, 11755);
        // Check for the plugin updates
        this.checkForUpdates();
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
        this.updateChecker = UpdateChecker.init(this, 93470);
        // Check the version and then send the messages
        this.updateChecker.requestUpdateCheck().whenComplete((result, error) -> {
            // Messages if the plugin is not up to date
            if(result.requiresUpdate()){
                Common.log(ChatColor.WHITE, "Checking for updates...");
                // The messages
                List<String> messages = new ArrayList<>(Utils.getUpdateMessage());
                messages.addAll(Arrays.asList(
                        " ",
                        " There is a new version of TheOnly-Mobcoins available!",
                        " &aLatest Version: " + result.getNewestVersion(),
                        " &cCurrent Version: " + this.getDescription().getVersion(),
                        " Please update to the newest version. Download:",
                        " &ehttps://www.spigotmc.org/resources/theonly-mobcoins.93470/"
                ));
                Executor.asyncLater(20, () -> Common.log(ChatColor.WHITE, messages));
                return;
            }

            switch(result.getReason()) {
                // Messages if the server using latest version
                case UP_TO_DATE: {
                    Common.log(ChatColor.WHITE, "Checking for updates...");
                    Executor.asyncLater(20, () -> Common.log(ChatColor.GREEN, "You are using the latest available version!"));
                    break;
                }
                // Messages if the server using unreleased version
                case UNRELEASED_VERSION: {
                    Common.log(ChatColor.WHITE, "Checking for updates...");
                    Executor.asyncLater(20, () -> Common.log(ChatColor.WHITE,
                            "===============================================",
                            "You are using unreleased version or development build!",
                            "This version is may be unstable. The latest available version is " + result.getNewestVersion(),
                            "Download them at &ehttps://www.spigotmc.org/resources/theonly-mobcoins.93470/",
                            "==============================================="
                    ));
                    break;
                }
                // Message if the reason is not on the above
                default: {
                    Common.log(ChatColor.WHITE, "Checking for updates...");
                    Executor.asyncLater(20, () -> Common.log(ChatColor.RED,
                            "Could not check for a new version of TheOnly-Mobcoins! (Reason: " + result.getReason() + ")"
                    ));
                }
            }
        });
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

    @Nullable
    public UpdateChecker getUpdateChecker(){
        return this.updateChecker;
    }
}
