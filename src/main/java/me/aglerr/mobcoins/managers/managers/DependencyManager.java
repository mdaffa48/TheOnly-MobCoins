package me.aglerr.mobcoins.managers.managers;

import me.aglerr.mobcoins.MobCoins;
import me.aglerr.mobcoins.managers.Manager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

public class DependencyManager implements Manager {

    public static boolean WILD_STACKER;
    public static boolean MYTHIC_MOBS;
    public static boolean PLACEHOLDER_API;

    public void registerPlaceholderAPI(){
        if(!PLACEHOLDER_API) return;

    }

    @Override
    public void load() {
        PluginManager pm = Bukkit.getPluginManager();
        WILD_STACKER = pm.getPlugin("WildStacker") == null ? false : true;
        MYTHIC_MOBS = pm.getPlugin("MythicMobs") == null ? false : true;
        PLACEHOLDER_API = pm.getPlugin("PlaceholderAPI") == null ? false : true;

        registerPlaceholderAPI();
    }

    @Override
    public void save() {

    }

}
