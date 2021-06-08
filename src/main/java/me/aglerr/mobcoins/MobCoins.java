package me.aglerr.mobcoins;

import me.aglerr.mobcoins.commands.MainCommand;
import me.aglerr.mobcoins.configs.Config;
import me.aglerr.mobcoins.configs.ConfigValue;
import me.aglerr.mobcoins.database.SQLDatabase;
import me.aglerr.mobcoins.listeners.ListenerHandler;
import me.aglerr.mobcoins.managers.ManagerHandler;
import me.aglerr.mobcoins.utils.Common;
import org.bukkit.plugin.java.JavaPlugin;

public class MobCoins extends JavaPlugin {

    private static MobCoins instance;

    private final ManagerHandler managerHandler = new ManagerHandler(this);
    private final ListenerHandler listenerHandler = new ListenerHandler(this);

    private SQLDatabase database;

    /*
      TODO Add more commands
      TODO Shop (main menu, category shop, rotating shop)
      TODO Limit purchase
      TODO Stock system
     */

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
        ConfigValue.initializeValue(Config.CONFIG.getConfig());

        // Initialize database
        database = new SQLDatabase(this);

        // Calling load() method from all Managers
        this.managerHandler.loadAllManagers();

        // Register all listeners
        this.listenerHandler.registerAllListeners();

        // Register main commands
        new MainCommand(this).registerThisCommand();
    }

    /**
     * Plugin disable logic
     */
    @Override
    public void onDisable(){
        this.managerHandler.saveAllManagers();
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
