package me.aglerr.mobcoins.commands.subcommands;

import me.aglerr.mobcoins.MobCoins;
import me.aglerr.mobcoins.commands.abstraction.SubCommand;
import me.aglerr.mobcoins.configs.ConfigValue;
import me.aglerr.mobcoins.utils.Common;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TopCommand extends SubCommand {

    @Nullable
    @Override
    public String getPermission() {
        return "mobcoins.top";
    }

    @Nullable
    @Override
    public List<String> parseTabCompletion(MobCoins plugin, CommandSender sender, String[] args) {
        return null;
    }

    @Override
    public void execute(MobCoins plugin, CommandSender sender, String[] args) {

        // Send messages if the command executor is a player
        if(sender instanceof Player){
            Player player = (Player) sender;

            for(String message : ConfigValue.MESSAGES_TOP_LEADERBOARD){
                String parsedMessage = PlaceholderAPI.setPlaceholders(player, message);
                player.sendMessage(parsedMessage);
            }
            return;
        }

        // Logic when console executes the command.
        Player player = null;
        for(Player onlinePlayers : Bukkit.getOnlinePlayers()){
            player = onlinePlayers;
            break;
        }

        if(player == null){
            sender.sendMessage(Common.color("&cThere is no players online, can't show the leaderboard from console!"));
            return;
        }

        for(String message : ConfigValue.MESSAGES_TOP_LEADERBOARD){
            String parsedMessage = PlaceholderAPI.setPlaceholders(player, message);
            sender.sendMessage(parsedMessage);
        }

    }

}
