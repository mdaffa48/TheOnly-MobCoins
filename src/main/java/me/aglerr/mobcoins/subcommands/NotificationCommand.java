package me.aglerr.mobcoins.subcommands;

import me.aglerr.mclibs.commands.SubCommand;
import me.aglerr.mclibs.libs.Common;
import me.aglerr.mobcoins.MobCoins;
import me.aglerr.mobcoins.configs.Config;
import me.aglerr.mobcoins.shops.inventory.ToggleInventory;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class NotificationCommand extends SubCommand {

    @NotNull
    @Override
    public String getName() {
        return "notification";
    }

    @Override
    public @Nullable String getPermission() {
        return "mobcoins.notification";
    }

    @Override
    public @NotNull List<String> parseTabCompletions(JavaPlugin javaPlugin, CommandSender sender, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public void execute(JavaPlugin javaPlugin, CommandSender sender, String[] args) {
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

        new ToggleInventory((MobCoins) javaPlugin, player, size, title).open(player);
    }

}
