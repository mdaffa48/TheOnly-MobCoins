package me.aglerr.mobcoins.listeners.listeners;

import me.aglerr.mobcoins.MobCoins;
import me.aglerr.mobcoins.PlayerData;
import me.aglerr.mobcoins.api.MobCoinsAPI;
import me.aglerr.mobcoins.api.events.MobCoinsReceiveEvent;
import me.aglerr.mobcoins.api.events.MobCoinsRedeemEvent;
import me.aglerr.mobcoins.api.events.MobCoinsSpawnEvent;
import me.aglerr.mobcoins.coinmob.CoinMob;
import me.aglerr.mobcoins.configs.ConfigValue;
import me.aglerr.mobcoins.managers.managers.CoinMobManager;
import me.aglerr.mobcoins.managers.managers.SalaryManager;
import me.aglerr.mobcoins.managers.managers.SpawnerSpawnManager;
import me.aglerr.mobcoins.utils.Common;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

public class EntityDeathListener implements Listener {

    private final MobCoins plugin;
    public EntityDeathListener(MobCoins plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event){
        SpawnerSpawnManager spawnManager = plugin.getManagerHandler().getSpawnerSpawnManager();
        LivingEntity entity = event.getEntity();

        // Check if the entity is spawned from spawner, and removing it
        if(spawnManager.isSpawnFromSpawner(entity)){
            spawnManager.removeEntity(entity);
            return;
        }

        // Return if the entity is in disabled worlds
        if(ConfigValue.DISABLED_WORLDS.contains(entity.getWorld().getName())) return;

        String mobType = entity.getType().name();
        CoinMob coinMob = MobCoinsAPI.getCoinMob(mobType);

        if(coinMob == null) return;
        if(!coinMob.willDropCoins()) return;

        double amountDrop = coinMob.getAmountToDrop();

        // Physical Mobcoin
        if(entity.getKiller() == null){
            MobCoinsSpawnEvent spawnEvent = new MobCoinsSpawnEvent(entity, coinMob, amountDrop);
            Bukkit.getPluginManager().callEvent(spawnEvent);
            if(spawnEvent.isCancelled()) return;

            ItemStack stack = spawnEvent.getItemStack();
            World world = entity.getWorld();
            world.dropItemNaturally(entity.getLocation(), stack);
            return;
        }

        // Virtual Mobcoin
        if(entity.getKiller() != null){
            Player player = entity.getKiller();
            PlayerData playerData = MobCoinsAPI.getPlayerData(player);
            if(playerData == null){
                Common.debug(true,
                        "Event: Entity Death Virtual Mobcoin",
                        "No PlayerData found for " + player.getName()
                );
                return;
            }

            MobCoinsReceiveEvent receiveEvent = new MobCoinsReceiveEvent(player, entity, amountDrop);
            Bukkit.getPluginManager().callEvent(receiveEvent);
            if(receiveEvent.isCancelled()) return;

            if(ConfigValue.SALARY_MODE_ENABLED){
                SalaryManager salaryManager = plugin.getManagerHandler().getSalaryManager();
                if(!ConfigValue.SALARY_MODE_RECEIVE_AFTER_MESSAGE){
                    playerData.addCoins(receiveEvent.getAmountReceived());
                    Common.playSound(player, "sounds.onCoinsReceived", plugin.getConfig());
                    Common.sendTitle(player, "titles.onCoinsReceived", plugin.getConfig(), receiveEvent.getAmountReceived());
                    Common.sendActionBar(player, "actionBar.onCoinsReceived", plugin.getConfig());
                    return;
                }
                salaryManager.putOrIncrementPlayerSalary(player, receiveEvent.getAmountReceived());
                return;
            }

            playerData.addCoins(receiveEvent.getAmountReceived());
            Common.playSound(player, "sounds.onCoinsReceived", plugin.getConfig());
            Common.sendTitle(player, "titles.onCoinsReceived", plugin.getConfig(), receiveEvent.getAmountReceived());
            Common.sendActionBar(player, "actionBar.onCoinsReceived", plugin.getConfig());
        }

    }

}
