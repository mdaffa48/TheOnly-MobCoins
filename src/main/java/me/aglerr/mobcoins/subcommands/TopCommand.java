package me.aglerr.mobcoins.subcommands;

import me.aglerr.mclibs.commands.SubCommand;
import me.aglerr.mclibs.libs.Common;
import me.aglerr.mobcoins.configs.ConfigValue;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TopCommand extends SubCommand {

    @NotNull
    @Override
    public String getName() {
        return "top";
    }

    @Nullable
    @Override
    public String getPermission() {
        return "mobcoins.top";
    }

    @NotNull
    @Override
    public List<String> parseTabCompletions(JavaPlugin javaPlugin, CommandSender sender, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public void execute(JavaPlugin javaPlugin, CommandSender sender, String[] args) {

        if(!ConfigValue.LEADERBOARD_ENABLE){
            sender.sendMessage(Common.color("&cLeaderboard is disabled!"));
            return;
        }

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
