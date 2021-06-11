package me.aglerr.mobcoins.listeners.listeners;

import me.aglerr.mobcoins.MobCoins;
import me.aglerr.mobcoins.PlayerData;
import me.aglerr.mobcoins.api.MobCoinsAPI;
import me.aglerr.mobcoins.api.events.MobCoinsReceiveEvent;
import me.aglerr.mobcoins.api.events.MobCoinsSpawnEvent;
import me.aglerr.mobcoins.coinmob.CoinMob;
import me.aglerr.mobcoins.configs.ConfigValue;
import me.aglerr.mobcoins.managers.managers.SalaryManager;
import me.aglerr.mobcoins.managers.managers.SpawnerSpawnManager;
import me.aglerr.mobcoins.utils.Common;
import org.bukkit.Bukkit;
import org.bukkit.World;
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

        // Check if disable mobcoin from spawner is enabled
        if(ConfigValue.DISABLE_MOBCOIN_FROM_SPAWNER) {
            // Check if the entity is spawned from spawner, and storing it
            if (spawnManager.isSpawnFromSpawner(entity)) {
                spawnManager.removeEntity(entity);
                return;
            }
        }

        // Return if the entity is in disabled worlds
        if(ConfigValue.DISABLED_WORLDS.contains(entity.getWorld().getName())) return;

        String mobType = entity.getType().name();

        // Trying to get CoinMob object from mob type name
        CoinMob coinMob = MobCoinsAPI.getCoinMob(mobType);

        // Return if there is no CoinMob for that mob
        if(coinMob == null) return;

        // Return if the mobcoin will not drop
        if(!coinMob.willDropCoins()) return;

        double amountDrop = coinMob.getAmountToDrop();

        // Physical Mobcoin
        if(ConfigValue.PHYSICAL_MOBCOIN){
            MobCoinsSpawnEvent spawnEvent = new MobCoinsSpawnEvent(entity, coinMob, amountDrop);
            Bukkit.getPluginManager().callEvent(spawnEvent);
            if(spawnEvent.isCancelled()) return;

            ItemStack stack = spawnEvent.getItemStack();
            World world = entity.getWorld();
            world.dropItemNaturally(entity.getLocation(), stack);
            Common.debug(true, "Successfully spawned physical mobcoin (coins: " + spawnEvent.getAmountToDrop() + ", mythicmobs: false)");
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

            MobCoinsReceiveEvent receiveEvent = new MobCoinsReceiveEvent(player, entity, amountDrop, false, null);
            Bukkit.getPluginManager().callEvent(receiveEvent);
            if(receiveEvent.isCancelled()) return;

            Common.debug(true, player.getName() + " received virtual mobcoins! (coins: " + receiveEvent.getAmountReceived() + ", reason: entity death, mythicmobs: false)");

            // Check if Salary Mode is enabled
            if(ConfigValue.SALARY_MODE_ENABLED){
                SalaryManager salaryManager = plugin.getManagerHandler().getSalaryManager();
                // Check if the player should not receive coins after salary message
                if(!ConfigValue.SALARY_MODE_RECEIVE_AFTER_MESSAGE){
                    // Adding coins directly to player data
                    playerData.addCoins(receiveEvent.getAmountReceived());
                }
                // Add received mobcoins to the salary
                salaryManager.putOrIncrementPlayerSalary(player, receiveEvent.getAmountReceived());
                return;
            }

            // Code logic if the salary mode is not enabled
            playerData.addCoins(receiveEvent.getAmountReceived());
            Common.playSound(player, "sounds.onCoinsReceived", plugin.getConfig());
            Common.sendTitle(player, "titles.onCoinsReceived", plugin.getConfig(), receiveEvent.getAmountReceived());
            Common.sendActionBar(player, "actionBar.onCoinsReceived", plugin.getConfig());
            player.sendMessage(Common.color(ConfigValue.MESSAGES_COINS_RECEIVED
                    .replace("{prefix}", ConfigValue.PREFIX)
                    .replace("{amount}", String.valueOf(receiveEvent.getAmountReceived()))));
        }

    }

}
