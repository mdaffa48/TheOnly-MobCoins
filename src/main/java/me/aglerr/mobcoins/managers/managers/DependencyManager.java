package me.aglerr.mobcoins.managers.managers;

import me.aglerr.mobcoins.MobCoins;
import me.aglerr.mobcoins.api.MobCoinsExpansion;
import me.aglerr.mobcoins.managers.Manager;
import me.aglerr.mobcoins.utils.Common;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

public class DependencyManager implements Manager {

    private final MobCoins plugin;
    public DependencyManager(MobCoins plugin){
        this.plugin = plugin;
    }

    public static boolean WILD_STACKER;
    public static boolean MYTHIC_MOBS;
    public static boolean PLACEHOLDER_API;

    public void notificationMessage(){
        if(WILD_STACKER){
            Common.log(true, "WildStacker found, enabling hooks!");
        }

        if(MYTHIC_MOBS){
            Common.log(true, "MythicMobs found, enabling hooks!");
        }

        if(PLACEHOLDER_API){
            Common.log(true, "PlaceholderAPI found, enabling hooks!");
            new MobCoinsExpansion(plugin).register();
        }
    }

    @Override
    public void load() {
        PluginManager pm = Bukkit.getPluginManager();
        WILD_STACKER = pm.getPlugin("WildStacker") == null ? false : true;
        MYTHIC_MOBS = pm.getPlugin("MythicMobs") == null ? false : true;
        PLACEHOLDER_API = pm.getPlugin("PlaceholderAPI") == null ? false : true;

        notificationMessage();
    }

    @Override
    public void save() {

    }

}
