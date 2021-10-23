package me.aglerr.mobcoins.commands.subcommands;

import me.aglerr.lazylibs.libs.Common;
import me.aglerr.mobcoins.MobCoins;
import me.aglerr.mobcoins.commands.abstraction.SubCommand;
import me.aglerr.mobcoins.configs.Config;
import me.aglerr.mobcoins.configs.ConfigValue;
import me.aglerr.mobcoins.managers.managers.NotificationManager;
import me.aglerr.mobcoins.shops.inventory.ToggleInventory;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
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
        // Get the file configuration of the toggle inventory
        FileConfiguration config = Config.TOGGLE_INVENTORY_CONFIG.getConfig();
        // Check if the player has disabled notification
        String title = config.getString("title");
        int size = config.getInt("size");

        new ToggleInventory(plugin, player, size, title).open(player);
    }

}
