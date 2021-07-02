package me.aglerr.mobcoins.managers.managers;

import me.aglerr.lazylibs.libs.Common;
import me.aglerr.mobcoins.MobCoins;
import me.aglerr.mobcoins.api.MobCoinsExpansion;
import me.aglerr.mobcoins.managers.Manager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
        Common.log(ChatColor.RESET, "Notification message!");
        if(WILD_STACKER){
            Common.log(ChatColor.RESET, "WildStacker found, enabling hooks!");
        }

        if(MYTHIC_MOBS){
            Common.log(ChatColor.RESET, "MythicMobs found, enabling hooks!");
        }

        if(PLACEHOLDER_API){
            Common.log(ChatColor.RESET, "PlaceholderAPI found, enabling hooks!");
            new MobCoinsExpansion(plugin).register();
        }
    }

    @Override
    public void load() {
        PluginManager pm = Bukkit.getPluginManager();
        WILD_STACKER = pm.getPlugin("WildStacker") != null;
        MYTHIC_MOBS = pm.getPlugin("MythicMobs") != null;
        PLACEHOLDER_API = pm.getPlugin("PlaceholderAPI") != null;

        notificationMessage();
    }

    @Override
    public void save() {

    }

}
