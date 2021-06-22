package me.aglerr.mobcoins.listeners.listeners;

import me.aglerr.mobcoins.MobCoins;
import me.aglerr.mobcoins.configs.ConfigValue;
import me.aglerr.mobcoins.managers.managers.PlayerDataManager;
import me.aglerr.mobcoins.managers.managers.SpawnerSpawnManager;
import me.aglerr.mobcoins.utils.Common;
import me.aglerr.mobcoins.utils.UpdateChecker;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.EulerAngle;

import java.util.Arrays;
import java.util.List;

public class PlayerListeners implements Listener {

    private final MobCoins plugin;
    public PlayerListeners(MobCoins plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        PlayerDataManager manager = plugin.getManagerHandler().getPlayerDataManager();
        Common.runTaskAsynchronously(() -> manager.forceLoadPlayerData(event.getPlayer()));

        // Giving notify update notification
        // Return if notify update is disabled
        if(!ConfigValue.NOTIFY_UPDATE) return;
        // Get the UpdateResult
        UpdateChecker.UpdateResult result = plugin.getUpdateChecker().getLastResult();
        // Return if the update result is null
        if(result == null) return;
        // Get the Player object
        Player player = event.getPlayer();
        // Check if the plugin require an update
        if(result.requiresUpdate()){
            // Send the messages
            List<String> messages = Arrays.asList(
                    "&6======================================",
                    " &fThere is a new version of TheOnly-Mobcoins",
                    " &aLatest Version: " + result.getNewestVersion(),
                    " &cCurrent Version: " + plugin.getDescription().getVersion(),
                    " &fPlease update to the newest version. Download:",
                    " &ehttps://www.spigotmc.org/resources/theonly-mobcoins.93470/",
                    "&6======================================"
            );
            messages.forEach(message -> player.sendMessage(Common.color(message)));
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        PlayerDataManager manager = plugin.getManagerHandler().getPlayerDataManager();
        Common.runTaskAsynchronously(() -> manager.forceSavePlayerData(event.getPlayer()));
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
