package me.aglerr.mobcoins;

import me.aglerr.mclibs.MCLibs;
import me.aglerr.mclibs.commands.SpigotCommand;
import me.aglerr.mclibs.inventory.SimpleInventoryManager;
import me.aglerr.mclibs.libs.Common;
import me.aglerr.mclibs.libs.ConfigUpdater;
import me.aglerr.mclibs.mysql.SQLHelper;
import me.aglerr.mobcoins.database.SQLDatabaseInitializer;
import me.aglerr.mobcoins.subcommands.*;
import me.aglerr.mobcoins.configs.Config;
import me.aglerr.mobcoins.configs.ConfigValue;
import me.aglerr.mobcoins.listeners.ListenerHandler;
import me.aglerr.mobcoins.managers.ManagerHandler;
import me.aglerr.mobcoins.managers.managers.CoinMobManager;
import me.aglerr.mobcoins.managers.managers.RotatingShopManager;
import me.aglerr.mobcoins.managers.managers.ShopManager;
import me.aglerr.mobcoins.metrics.Metrics;
import me.aglerr.mobcoins.shops.items.ItemsLoader;
import me.aglerr.mobcoins.utils.Utils;
import org.bukkit.command.CommandSender;
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

    private final ManagerHandler managerHandler = new ManagerHandler(this);
    private final ListenerHandler listenerHandler = new ListenerHandler(this);

    /**
     * Plugin startup logic
     */
    @Override
    public void onEnable(){
        // Injecting libs
        MCLibs.init(this);
        Common.setPrefix("[TheOnly-Mobcoins]");
        // Send the-only logo
        Utils.sendStartupLogo();
        // Initialize all config
        Config.initialize();
        // Update the config
        this.updateConfig();
        // Initialize all config value
        ConfigValue.initializeValue();
        // Initialize database
        SQLDatabaseInitializer.getInstance().init(this);
        // Calling load() method from all Managers
        this.managerHandler.loadAllManagers();
        // Register all listeners
        this.listenerHandler.registerAllListeners();
        // Register main commands
        this.registerCommands();
        // Enable metrics
        new Metrics(this, 11755);
        // Check for the database, if it's not successfully connected, disable the plugin
    }

    /**
     * Plugin disable logic
     */
    @Override
    public void onDisable(){
        this.managerHandler.saveAllManagers();
        SQLHelper.close();
    }

    public void reloadEverything(){
        ShopManager shopManager = this.managerHandler.getShopManager();
        RotatingShopManager rotatingShopManager = this.managerHandler.getRotatingShopManager();
        CoinMobManager coinMobManager = this.managerHandler.getCoinMobManager();
        ItemsLoader itemsLoader = shopManager.getItemsLoader();
        // First of all, close all inventory first
        SimpleInventoryManager.closeAll();
        // Reload all configuration
        Config.reloadAllConfigs();
        // Re-initialize the config value
        ConfigValue.initializeValue();
        //HooksValue.initialize();
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
        //File hooksFile = new File(this.getDataFolder(), "hooks.yml");
        try{
            ConfigUpdater.update(this, "config.yml", configFile, new ArrayList<>());
            //ConfigUpdater.update(this, "hooks.yml", hooksFile, new ArrayList<>());
        } catch(IOException e){
            Common.log("&cFailed to update the config.yml");
            e.printStackTrace();
        }
        Config.CONFIG.reloadConfig();
    }

    private void registerCommands(){
        new SpigotCommand(
                this,
                "mobcoins",
                ConfigValue.ALIASES,
                null,
                this::sendHelpMessages,
                onNoPermission -> onNoPermission.sendMessage(Common.color(ConfigValue.MESSAGES_NO_PERMISSION
                        .replace("{prefix}", ConfigValue.PREFIX))),
                this::sendHelpMessages,
                new AboutCommand(),
                new AddSalaryCommand(),
                new BalanceCommand(),
                new GiveCommand(),
                new GiveRandomCommand(),
                new HelpCommand(),
                new NotificationCommand(),
                new OpenCategoryCommand(),
                new PayCommand(),
                new ReloadCommand(),
                new RemoveSalaryCommand(),
                new SetCommand(),
                new ShopCommand(),
                new TakeCommand(),
                new TopCommand(),
                new WithdrawCommand()
        ).register();
    }

    private void sendHelpMessages(CommandSender sender){
        if(sender.hasPermission("mobcoins.admin")){
            ConfigValue.MESSAGES_HELP_ADMIN.forEach(message ->
                    sender.sendMessage(Common.color(message)));
            return;
        }
        ConfigValue.MESSAGES_HELP.forEach(message ->
                sender.sendMessage(Common.color(message)));
    }

    public ManagerHandler getManagerHandler() {
        return managerHandler;
    }
}
