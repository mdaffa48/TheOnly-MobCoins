package me.aglerr.mobcoins.managers.managers;

import me.aglerr.mobcoins.MobCoins;
import me.aglerr.mobcoins.PlayerData;
import me.aglerr.mobcoins.api.MobCoinsAPI;
import me.aglerr.mobcoins.configs.ConfigValue;
import me.aglerr.mobcoins.managers.Manager;
import me.aglerr.mobcoins.utils.Common;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SalaryManager implements Manager {

    private final Map<UUID, Double> salaryMap = new HashMap<>();

    private final FileConfiguration config;
    public SalaryManager(FileConfiguration config){
        this.config = config;
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

    @Override
    public void load() {
        Common.runTaskTimerAsynchronously(0, (20 * ConfigValue.SALARY_MODE_ANNOUNCE_EVERY), () -> {
            if(!ConfigValue.SALARY_MODE_ENABLED) return;
            // Loop through all salary
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
                        Common.debug(true,
                                "Event: Salary Announcement",
                                "No PlayerData found for " + player.getName()
                        );
                        continue;
                    }
                    // Add mobcoins to player's data
                    playerData.addCoins(salaryAmount);
                }

                // Send notification code (message, sound, title, actionbar)
                ConfigValue.SALARY_MODE_MESSAGES.forEach(message -> player.sendMessage(Common.color(message.replace("{amount}", Common.getDecimalFormat().format(salaryAmount)))));
                Common.playSound(player, "sounds.onCoinsReceived", config);
                Common.sendTitle(player, "titles.onCoinsReceived", config, salaryAmount);
                Common.sendActionBar(player, "actionBar.onCoinsReceived", config);

                Common.debug(true, "Salary for " + player.getName() + " (coins: " + salaryAmount + ")");
                // Remove player salary from the map
                this.salaryMap.remove(uuid);

            }
        });
    }

    @Override
    public void save() {

    }

}
