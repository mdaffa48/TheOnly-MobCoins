package me.aglerr.mobcoins.listeners;

import me.aglerr.mobcoins.MobCoins;
import me.aglerr.mobcoins.listeners.listeners.PlayerListeners;
import me.aglerr.mobcoins.listeners.listeners.PlayerRedeemCoins;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public class ListenerHandler {

    private final List<Listener> listenerList = new ArrayList<>();

    private final MobCoins plugin;
    public ListenerHandler(MobCoins plugin){
        this.plugin = plugin;

        this.listenerList.add(new PlayerListeners(plugin));
        this.listenerList.add(new PlayerRedeemCoins(plugin));
    }

    public void registerAllListeners(){
        for(Listener listener : this.listenerList){
            Bukkit.getPluginManager().registerEvents(listener, plugin);
        }
    }

}
