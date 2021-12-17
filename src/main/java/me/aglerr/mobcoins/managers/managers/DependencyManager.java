package me.aglerr.mobcoins.managers.managers;

import me.aglerr.mclibs.libs.Common;
import me.aglerr.mobcoins.MobCoins;
import me.aglerr.mobcoins.api.MobCoinsExpansion;
import me.aglerr.mobcoins.managers.Manager;
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
    public static boolean SUPER_MOB_COINS;
    public static boolean JETS_MINION;

    public void notificationMessage(){
        if(WILD_STACKER){
            Common.log("&rWildStacker found, enabling hooks!");
        }

        if(MYTHIC_MOBS){
            Common.log("&rMythicMobs found, enabling hooks!");
        }

        if(PLACEHOLDER_API){
            Common.log("&rPlaceholderAPI found, enabling hooks!");
            new MobCoinsExpansion(plugin).register();
        }

        /*if(JETS_MINION){
            Common.log(ChatColor.RESET, "JetsMinion found, enabling hooks!");
        }*/
    }

    @Override
    public void load() {
        PluginManager pm = Bukkit.getPluginManager();
        WILD_STACKER = pm.getPlugin("WildStacker") != null;
        MYTHIC_MOBS = pm.getPlugin("MythicMobs") != null;
        PLACEHOLDER_API = pm.getPlugin("PlaceholderAPI") != null;
        SUPER_MOB_COINS = pm.getPlugin("SuperMobCoins") != null;
        JETS_MINION = pm.getPlugin("JetsMinion") != null;

        notificationMessage();
    }

    @Override
    public void save() {

    }

}
