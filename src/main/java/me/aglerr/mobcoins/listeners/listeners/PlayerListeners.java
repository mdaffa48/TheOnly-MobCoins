package me.aglerr.mobcoins.listeners.listeners;

import me.aglerr.mobcoins.MobCoins;
import me.aglerr.mobcoins.managers.ManagerHandler;
import me.aglerr.mobcoins.managers.managers.PlayerDataManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListeners implements Listener {

    private final MobCoins plugin;
    public PlayerListeners(MobCoins plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        ManagerHandler managerHandler = plugin.getManagerHandler();
        PlayerDataManager manager = (PlayerDataManager) managerHandler.getManager(ManagerHandler.ManagerType.PLAYER_DATA_MANAGER);
        manager.handlePlayerJoin(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        ManagerHandler managerHandler = plugin.getManagerHandler();
        PlayerDataManager manager = (PlayerDataManager) managerHandler.getManager(ManagerHandler.ManagerType.PLAYER_DATA_MANAGER);
        manager.handlePlayerQuit(event.getPlayer());
    }

}
