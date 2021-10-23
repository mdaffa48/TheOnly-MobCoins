package me.aglerr.mobcoins.managers.managers;

import me.aglerr.mobcoins.configs.Config;
import me.aglerr.mobcoins.configs.CustomConfig;
import me.aglerr.mobcoins.managers.Manager;
import me.aglerr.mobcoins.objects.NotificationUser;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

public class NotificationManager implements Manager {

    private final Map<UUID, NotificationUser> userMap = new HashMap<>();

    public NotificationUser getNotificationUser(String uuid){
        return getNotificationUser(UUID.fromString(uuid));
    }

    public NotificationUser getNotificationUser(Player player){
        return getNotificationUser(player.getUniqueId());
    }

    public NotificationUser getNotificationUser(UUID uuid){
        return userMap.get(uuid);
    }

    public void addUser(NotificationUser notificationUser){
        userMap.putIfAbsent(notificationUser.getUUID(), notificationUser);
    }

    public void removeUser(UUID uuid){
        userMap.remove(uuid);
    }

    public void removeUser(NotificationUser notificationUser){
        userMap.remove(notificationUser.getUUID());
    }

    /**
     * Manager load logic, called on server startup
     */
    @Override
    public void load() {

    }

    /**
     * Manager save logic, called on server stopped
     */
    @Override
    public void save() {

    }

}
