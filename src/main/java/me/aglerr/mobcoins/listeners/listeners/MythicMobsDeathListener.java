package me.aglerr.mobcoins.listeners.listeners;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent;
import me.aglerr.lazylibs.libs.Common;
import me.aglerr.mobcoins.MobCoins;
import me.aglerr.mobcoins.PlayerData;
import me.aglerr.mobcoins.api.MobCoinsAPI;
import me.aglerr.mobcoins.api.events.MobCoinsReceiveEvent;
import me.aglerr.mobcoins.api.events.MobCoinsSpawnEvent;
import me.aglerr.mobcoins.coinmob.CoinMob;
import me.aglerr.mobcoins.configs.ConfigValue;
import me.aglerr.mobcoins.managers.managers.NotificationManager;
import me.aglerr.mobcoins.managers.managers.SalaryManager;
import me.aglerr.mobcoins.objects.NotificationUser;
import me.aglerr.mobcoins.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class MythicMobsDeathListener implements Listener {

    private final MobCoins plugin;
    public MythicMobsDeathListener(MobCoins plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDeath(MythicMobDeathEvent event){
        Entity entity = event.getEntity();
        // Get the killed mob type internal name
        String mobType = event.getMobType().getInternalName();

        // Return if the entity is in disabled worlds
        if(ConfigValue.DISABLED_WORLDS.contains(entity.getWorld().getName())) return;

        // Trying to get CoinMob object from mob type name
        CoinMob coinMob = MobCoinsAPI.getCoinMob(mobType);

        // Return if there is no CoinMob for that mob
        if(coinMob == null) return;

        // Return if the mobcoin will not drop
        if(!coinMob.willDropCoins()) return;

        double amountDrop = coinMob.getAmountToDrop();

        // Physical Mobcoin
        // Check if physical mobcoin is enabled
        if(ConfigValue.PHYSICAL_MOBCOIN){
            // Create and call the event
            MobCoinsSpawnEvent spawnEvent = new MobCoinsSpawnEvent((LivingEntity) entity, coinMob, amountDrop);
            Bukkit.getPluginManager().callEvent(spawnEvent);
            if(spawnEvent.isCancelled()) return;

            ItemStack stack = spawnEvent.getItemStack();
            World world = entity.getWorld();
            // Drop the item in entity location
            world.dropItemNaturally(entity.getLocation(), stack);
            Common.debug("Successfully spawned physical mobcoin (coins: " + spawnEvent.getAmountToDrop() + ", mythicmobs: true)");
            return;
        }

        // Virtual Mobcoin
        if(event.getKiller() instanceof Player) {
            Player player = (Player) event.getKiller();
            PlayerData playerData = MobCoinsAPI.getPlayerData(player);
            if (playerData == null) {
                Common.debug(
                        "Event: Mythic Mobs Entity Death Virtual Mobcoin",
                        "No PlayerData found for " + player.getName()
                );
                return;
            }

            MobCoinsReceiveEvent receiveEvent = new MobCoinsReceiveEvent(player, (LivingEntity) entity, amountDrop, true, event.getMobType(), false);
            Bukkit.getPluginManager().callEvent(receiveEvent);
            if (receiveEvent.isCancelled()) return;

            Common.debug(player.getName() + " received virtual mobcoins! (coins: " + receiveEvent.getAmountReceived() + ", reason: entity death, mythicmobs: true)");

            // Check if Salary Mode is enabled
            if (ConfigValue.SALARY_MODE_ENABLED) {
                SalaryManager salaryManager = plugin.getManagerHandler().getSalaryManager();
                // Check if the player should not receive coins after salary message
                if (!ConfigValue.SALARY_MODE_RECEIVE_AFTER_MESSAGE) {
                    // Adding coins directly to player data
                    playerData.addCoins(receiveEvent.getAmountReceived());
                }
                // Add received mobcoins to the salary
                salaryManager.putOrIncrementPlayerSalary(player, receiveEvent.getAmountReceived());
            }

            // Code logic if the salary mode is not enabled
            if(!ConfigValue.SALARY_MODE_ENABLED){
                playerData.addCoins(receiveEvent.getAmountReceived());
            }

            if(ConfigValue.IS_ENABLE_RECEIVE_MOBCOINS_MESSAGE){
                // Get the notification manager
                NotificationManager notificationManager = plugin.getManagerHandler().getNotificationManager();
                // Get the notification user and send the notification
                NotificationUser notificationUser = notificationManager.getNotificationUser(player.getUniqueId());
                notificationUser.sendNotification(receiveEvent.getAmountReceived(), false);
            }
        }
    }
}
