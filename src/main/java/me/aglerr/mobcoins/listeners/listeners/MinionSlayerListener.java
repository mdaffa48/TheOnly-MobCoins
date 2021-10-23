package me.aglerr.mobcoins.listeners.listeners;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.api.bukkit.BukkitAPIHelper;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import me.aglerr.lazylibs.libs.Common;
import me.aglerr.mobcoins.MobCoins;
import me.aglerr.mobcoins.PlayerData;
import me.aglerr.mobcoins.api.MobCoinsAPI;
import me.aglerr.mobcoins.api.events.MobCoinsReceiveEvent;
import me.aglerr.mobcoins.coinmob.CoinMob;
import me.aglerr.mobcoins.configs.Config;
import me.aglerr.mobcoins.configs.ConfigValue;
import me.aglerr.mobcoins.configs.HooksValue;
import me.aglerr.mobcoins.managers.managers.*;
import me.aglerr.mobcoins.utils.Utils;
import me.jet315.minions.events.SlayerSlayEvent;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class MinionSlayerListener implements Listener {

    private final MobCoins plugin;
    public MinionSlayerListener(MobCoins plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onMinionSlay(SlayerSlayEvent event){
        if(!HooksValue.JETS_MINION_ENABLED){
            return;
        }

        Player player = event.getPlayer();

        if(HooksValue.JETS_MINION_USE_PERMISSION && !player.hasPermission(HooksValue.JETS_MINION_PERMISSION)){
            return;
        }

        if(HooksValue.JETS_MINION_VIRTUAL_MOBCOINS){
            PlayerData playerData = MobCoinsAPI.getPlayerData(player);
            if(playerData == null){
                Common.debug(
                        "Event: Minion Slayer",
                        "No PlayerData found for " + player.getName()
                );
                return;
            }
        }

        SpawnerSpawnManager spawnManager = plugin.getManagerHandler().getSpawnerSpawnManager();
        double totalCoins = 0;

        for(Entity entity : event.getEntitiesToKill()){

            // Check if disable mobcoin from spawner is enabled
            if(ConfigValue.DISABLE_MOBCOIN_FROM_SPAWNER) {
                // Check if the entity is spawned from spawner, and storing it
                if (spawnManager.isSpawnFromSpawner(entity)) {
                    spawnManager.removeEntity(entity);
                    continue;
                }
            }

            // Return if the entity is in disabled worlds
            if(ConfigValue.DISABLED_WORLDS.contains(entity.getWorld().getName())) continue;

            String mobType = entity.getType().name();

            if(DependencyManager.MYTHIC_MOBS){
                // Get the API from MythicMobs
                BukkitAPIHelper mythicMobsAPI = MythicMobs.inst().getAPIHelper();
                // Return if the entity is a mythic mob
                if(mythicMobsAPI.isMythicMob(entity)){
                    MythicMob mythicMob = MythicMobs.inst().getMobManager().getMythicMobInstance(entity).getType();
                    mobType = mythicMob.getInternalName();
                }
            }

            CoinMob coinMob = MobCoinsAPI.getCoinMob(mobType);

            // Return if there is no CoinMob for that mob
            if(coinMob == null) continue;

            // Return if the mobcoin will not drop
            if(!coinMob.willDropCoins()) continue;

            totalCoins += coinMob.getAmountToDrop();
        }

        if(totalCoins <= 0){
            return;
        }

        PlayerDataManager playerDataManager = plugin.getManagerHandler().getPlayerDataManager();

        if(HooksValue.JETS_MINION_VIRTUAL_MOBCOINS){

            Common.debug(player.getName() + " received virtual mobcoins from slayer minion! (coins: " + totalCoins + ")");

        }



    }

}
