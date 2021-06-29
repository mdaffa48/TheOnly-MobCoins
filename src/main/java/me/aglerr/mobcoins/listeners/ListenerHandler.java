package me.aglerr.mobcoins.listeners;

import me.aglerr.mobcoins.MobCoins;
import me.aglerr.mobcoins.listeners.listeners.EntityDeathListener;
import me.aglerr.mobcoins.listeners.listeners.MythicMobsDeathListener;
import me.aglerr.mobcoins.listeners.listeners.PlayerListeners;
import me.aglerr.mobcoins.listeners.listeners.PlayerRedeemCoins;
import me.aglerr.mobcoins.managers.managers.DependencyManager;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public class ListenerHandler {

    private final MobCoins plugin;
    public ListenerHandler(MobCoins plugin){
        this.plugin = plugin;
    }

    public void registerAllListeners(){
        this.register(new PlayerListeners(plugin));
        this.register(new PlayerRedeemCoins(plugin));
        this.register(new EntityDeathListener(plugin));
        if(DependencyManager.MYTHIC_MOBS){
            this.register(new MythicMobsDeathListener(plugin));
        }
    }

    private void register(Listener listener){
        Bukkit.getPluginManager().registerEvents(listener, plugin);
    }

}
