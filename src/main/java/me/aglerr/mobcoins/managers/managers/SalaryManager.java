package me.aglerr.mobcoins.managers.managers;

import me.aglerr.mclibs.libs.Debug;
import me.aglerr.mclibs.libs.Executor;
import me.aglerr.mobcoins.MobCoins;
import me.aglerr.mobcoins.PlayerData;
import me.aglerr.mobcoins.api.MobCoinsAPI;
import me.aglerr.mobcoins.configs.ConfigValue;
import me.aglerr.mobcoins.managers.Manager;
import me.aglerr.mobcoins.objects.NotificationUser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SalaryManager implements Manager {

    private final Map<UUID, Double> salaryMap = new HashMap<>();

    private final MobCoins plugin;
    public SalaryManager(MobCoins plugin){
        this.plugin = plugin;
    }

    public void putOrIncrementPlayerSalary(Player player, double amount){
        // Put if player doesn't have any salary
        this.salaryMap.putIfAbsent(player.getUniqueId(), amount);
        // Check if player already have salary
        if(this.salaryMap.containsKey(player.getUniqueId())){
            // Get player's current salary
            double currentSalary = this.salaryMap.get(player.getUniqueId());
            // Increment the player salary
            this.salaryMap.put(player.getUniqueId(), (currentSalary + amount));
        }
    }

    public void decreaseSalary(Player player, double amount){
        // Check if player already have salary
        if(this.salaryMap.containsKey(player.getUniqueId())){
            // Get player's current salary
            double currentSalary = this.salaryMap.get(player.getUniqueId());
            // Get the total amount by decreasing current salary with the amount to decrease
            double totalAmount = currentSalary - amount;
            // If the decreased amount is below zero, set it to 0
            if(totalAmount < 0 ){
                totalAmount = 0;
            }
            // Update the player salary
            this.salaryMap.put(player.getUniqueId(), totalAmount);
        }
    }

    @Override
    public void load() {
        int timeAndDelay = (20 * ConfigValue.SALARY_MODE_ANNOUNCE_EVERY);
        Executor.asyncTimer(timeAndDelay, timeAndDelay, () -> {
            if(!ConfigValue.SALARY_MODE_ENABLED) return;

            // Loop through all salaries
            for(UUID uuid : this.salaryMap.keySet()){
                // Get the player object from UUID that are stored
                Player player = Bukkit.getPlayer(uuid);
                if(player == null){
                    // Remove player salary if the player doesn't exist
                    this.salaryMap.remove(uuid);
                    continue;
                }

                // Get the salary mobcoins amount
                double salaryAmount = this.salaryMap.get(player.getUniqueId());

                // Check if player should receive mobcoins after the message
                if(ConfigValue.SALARY_MODE_RECEIVE_AFTER_MESSAGE){
                    PlayerData playerData = MobCoinsAPI.getPlayerData(player);
                    if(playerData == null){
                        Debug.send(
                                "Event: Salary Announcement",
                                "No PlayerData found for " + player.getName()
                        );
                        continue;
                    }
                    // Add mobcoins to player's data
                    playerData.addCoins(salaryAmount);
                }

                // Send notification code (message, sound, title, actionbar)
                NotificationManager notificationManager = plugin.getManagerHandler().getNotificationManager();
                NotificationUser notificationUser = notificationManager.getNotificationUser(player);
                notificationUser.sendNotification(salaryAmount, true);

                Debug.send("Salary for " + player.getName() + " (coins: " + salaryAmount + ")");
                // Remove player salary from the map
                this.salaryMap.remove(uuid);
            }
        });
    }

    @Override
    public void save() {

    }

}
