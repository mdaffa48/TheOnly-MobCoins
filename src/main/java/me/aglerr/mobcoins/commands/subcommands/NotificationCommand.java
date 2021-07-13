package me.aglerr.mobcoins.commands.subcommands;

import me.aglerr.lazylibs.libs.Common;
import me.aglerr.mobcoins.MobCoins;
import me.aglerr.mobcoins.commands.abstraction.SubCommand;
import me.aglerr.mobcoins.configs.ConfigValue;
import me.aglerr.mobcoins.managers.managers.NotificationManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class NotificationCommand extends SubCommand {

    @Override
    public @Nullable String getPermission() {
        return "mobcoins.notification";
    }

    @Override
    public @NotNull List<String> parseTabCompletion(MobCoins plugin, CommandSender sender, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public void execute(MobCoins plugin, CommandSender sender, String[] args) {
        // First of all, this plugin can only be executed by Player
        if(!(sender instanceof Player)){
            sender.sendMessage(Common.color("&cOnly players can execute this command!"));
            return;
        }
        // After the check above, we guaranteed the executor is player
        // So we can get the Player object
        Player player = (Player) sender;
        // Get the notification manager
        NotificationManager notificationManager = plugin.getManagerHandler().getNotificationManager();
        // Check if the player has disabled notification
        if(notificationManager.isMuted(player)){
            // If the player has disabled notification, we want to
            // un-mute them and send them a message
            // First, unmute the player
            notificationManager.unMute(player);
            // Then send them a message
            player.sendMessage(Common.color(ConfigValue.MESSAGES_NOTIFICATION_UN_MUTED
                    .replace("{prefix}", ConfigValue.PREFIX)));
        } else {
            // If the player is enabled notification
            // Mute the notification and send them a message
            // First, we mute the notification for the player
            notificationManager.mute(player);
            // Send the player a message
            player.sendMessage(Common.color(ConfigValue.MESSAGES_NOTIFICATION_MUTED
                    .replace("{prefix}", ConfigValue.PREFIX)));
        }
    }

}
