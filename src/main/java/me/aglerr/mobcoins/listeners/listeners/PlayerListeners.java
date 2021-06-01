package me.aglerr.mobcoins.listeners.listeners;

import me.aglerr.mobcoins.MobCoins;
import me.aglerr.mobcoins.configs.ConfigValue;
import me.aglerr.mobcoins.managers.managers.PlayerDataManager;
import me.aglerr.mobcoins.managers.managers.SpawnerSpawnManager;
import me.aglerr.mobcoins.utils.Common;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListeners implements Listener {

    private final MobCoins plugin;
    public PlayerListeners(MobCoins plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        PlayerDataManager manager = plugin.getManagerHandler().getPlayerDataManager();
        Common.runTaskAsynchronously(() -> manager.forceLoadPlayerData(event.getPlayer()));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        PlayerDataManager manager = plugin.getManagerHandler().getPlayerDataManager();
        Common.runTaskAsynchronously(() -> manager.forceSavePlayerData(event.getPlayer()));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onCreatureSpawn(CreatureSpawnEvent event){
        SpawnerSpawnManager spawnManager = plugin.getManagerHandler().getSpawnerSpawnManager();
        if(event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.SPAWNER) return;
        if(ConfigValue.DISABLE_MOBCOIN_FROM_SPAWNER){
            spawnManager.addEntity(event.getEntity());
        }
    }

}
