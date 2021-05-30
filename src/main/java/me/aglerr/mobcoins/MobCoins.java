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

    /**
     * TODO Add more commands
     * TODO Shop (main menu, category shop, rotating shop)
     * TODO Limit purchase
     * TODO Stock system
     * TODO MythicMobs integration
     * TODO Salary system
     */

    @Override
    public void onEnable(){
        instance = this;

        Common.log(false, Common.getStartupLogo());

        Config.initialize();
        ConfigValue.initializeValue(Config.CONFIG.getConfig());

        database = new SQLDatabase(this);

        this.managerHandler.loadAllManagers();
        this.listenerHandler.registerAllListeners();
        this.registerCommands();
    }

    @Override
    public void onDisable(){
        this.managerHandler.saveAllManagers();
    }

    private void registerCommands(){
        MainCommand mainCommand = new MainCommand(this);
        this.getCommand("mobcoins").setExecutor(mainCommand);
        this.getCommand("mobcoins").setTabCompleter(mainCommand);
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
