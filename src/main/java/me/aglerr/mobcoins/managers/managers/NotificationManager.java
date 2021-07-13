package me.aglerr.mobcoins.managers.managers;

import me.aglerr.mobcoins.configs.Config;
import me.aglerr.mobcoins.configs.CustomConfig;
import me.aglerr.mobcoins.managers.Manager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

public class NotificationManager implements Manager {

    private final Set<UUID> muted = new HashSet<>();

    public void mute(Player player){
        // Add the player uuid to the muted list
        muted.add(player.getUniqueId());
    }

    public void unMute(Player player){
        // Remove the player from the muted list
        muted.remove(player.getUniqueId());
    }

    public boolean isMuted(Player player){
        return muted.contains(player.getUniqueId());
    }

    /**
     * Manager load logic, called on server startup
     */
    @Override
    public void load() {
        // First, get the temp_data.yml file configuration
        FileConfiguration config = Config.TEMP_DATA.getConfig();
        // Loop through all 'notification' string list
        for(String uuid : config.getStringList("notification")){
            // Add the uuid to the hash set
            muted.add(UUID.fromString(uuid));
        }
    }

    /**
     * Manager save logic, called on server stopped
     */
    @Override
    public void save() {
        // Get the temp_data.yml file
        CustomConfig temp = Config.TEMP_DATA;
        // Get the temp_data.yml configuration file
        FileConfiguration config = temp.getConfig();
        // Create a new list to get all muted uuid
        List<String> mutedList = new ArrayList<>();
        // Loop through all muted hashset
        for(UUID uuid : muted){
            // Add the uuid to the list
            mutedList.add(uuid.toString());
        }
        // After the loop is completed, store it onto the temp_data.yml configuration
        config.set("notification", mutedList);
        // Save the config
        temp.saveConfig();
    }

}
