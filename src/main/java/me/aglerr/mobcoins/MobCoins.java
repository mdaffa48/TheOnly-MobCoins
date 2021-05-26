package me.aglerr.mobcoins;

import me.aglerr.mobcoins.configs.Config;
import me.aglerr.mobcoins.configs.ConfigValue;
import me.aglerr.mobcoins.database.SQLDatabase;
import me.aglerr.mobcoins.managers.ManagerHandler;
import me.aglerr.mobcoins.utils.Common;
import org.bukkit.plugin.java.JavaPlugin;

public class MobCoins extends JavaPlugin {

    private static MobCoins instance;

    private final ManagerHandler managerHandler = new ManagerHandler(this);
    private SQLDatabase database;

    @Override
    public void onEnable(){
        instance = this;

        Common.log(false, Common.getStartupLogo());

        Config.initialize();
        ConfigValue.initializeValue(Config.CONFIG.getConfig());

        database = new SQLDatabase(this);

        this.managerHandler.loadAllManagers();
    }

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
