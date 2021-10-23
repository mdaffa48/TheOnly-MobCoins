package me.aglerr.mobcoins.listeners.listeners;

import me.aglerr.lazylibs.libs.Common;
import me.aglerr.lazylibs.libs.Executor;
import me.aglerr.mobcoins.MobCoins;
import me.aglerr.mobcoins.configs.ConfigValue;
import me.aglerr.mobcoins.database.SQLDatabase;
import me.aglerr.mobcoins.managers.managers.PlayerDataManager;
import me.aglerr.mobcoins.managers.managers.SpawnerSpawnManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerListeners implements Listener {

    private final MobCoins plugin;
    public PlayerListeners(MobCoins plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        PlayerDataManager playerDataManager = plugin.getManagerHandler().getPlayerDataManager();
        event.getPlayer().sendMessage(Common.color(ConfigValue.MESSAGES_LOAD_DATA
                .replace("{prefix}", ConfigValue.PREFIX)));
        Executor.asyncLater(100L, () -> playerDataManager.handleLoginEvent(event));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        PlayerDataManager manager = plugin.getManagerHandler().getPlayerDataManager();
        Executor.async(() -> manager.forceSavePlayerData(event.getPlayer()));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onCreatureSpawn(CreatureSpawnEvent event){
        SpawnerSpawnManager spawnManager = plugin.getManagerHandler().getSpawnerSpawnManager();
        // Return if the entity spawn reason is not from SPAWNER
        if(event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.SPAWNER) return;
        // Check if disable mobcoin from spawner is enabled
        if(ConfigValue.DISABLE_MOBCOIN_FROM_SPAWNER){
            // Store the entity
            spawnManager.addEntity(event.getEntity());
        }
    }

}
