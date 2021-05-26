package me.aglerr.mobcoins;

import me.aglerr.mobcoins.configs.Config;
import me.aglerr.mobcoins.managers.ManagerLoader;
import me.aglerr.mobcoins.utils.Common;
import org.bukkit.plugin.java.JavaPlugin;

public class MobCoins extends JavaPlugin {

    private static MobCoins instance;

    private final ManagerLoader managerLoader = new ManagerLoader();

    @Override
    public void onEnable(){
        instance = this;

        Common.log(false, Common.getStartupLogo());
        Config.initialize();

        this.managerLoader.loadAllManagers();
    }

    @Override
    public void onDisable(){
        this.managerLoader.saveAllManagers();
    }

    /**
     * Not for regular use.
     * @return this class instance
     */
    public static MobCoins getInstance() {
        return instance;
    }

}
