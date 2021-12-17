package me.aglerr.mobcoins.subcommands;

import me.aglerr.mclibs.commands.SubCommand;
import me.aglerr.mclibs.libs.Common;
import me.aglerr.mclibs.libs.Debug;
import me.aglerr.mobcoins.MobCoins;
import me.aglerr.mobcoins.PlayerData;
import me.aglerr.mobcoins.api.MobCoinsAPI;
import me.aglerr.mobcoins.configs.ConfigValue;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SetCommand extends SubCommand {

    @NotNull
    @Override
    public String getName() {
        return "set";
    }

    @Nullable
    @Override
    public String getPermission() {
        return "mobcoins.admin";
    }

    @NotNull
    @Override
    public List<String> parseTabCompletions(JavaPlugin javaPlugin, CommandSender sender, String[] args) {
        if(args.length == 2){
            List<String> suggestions = new ArrayList<>();
            Bukkit.getOnlinePlayers().forEach(player -> suggestions.add(player.getName()));
            return suggestions;
        }

        if(args.length == 3){
            return Collections.singletonList("<amount>");
        }
        return new ArrayList<>();
    }

    @Override
    public void execute(JavaPlugin javaPlugin, CommandSender sender, String[] args) {

        if(args.length < 3){
            sender.sendMessage(Common.color("&cUsage: /mobcoins set <player> <amount>"));
            return;
        }

        Player player = Bukkit.getPlayer(args[1]);
        if(player == null){
            sender.sendMessage(Common.color(ConfigValue.MESSAGES_PLAYER_NOT_EXISTS
                    .replace("{prefix}", ConfigValue.PREFIX)));
            return;
        }

        PlayerData playerData = MobCoinsAPI.getPlayerData(player);
        if(playerData == null){
            Debug.send(
                    "Command: /mobcoins set",
                    "No PlayerData found for " + player.getName()
            );
            return;
        }

        if(!Common.isDouble(args[2])){
            sender.sendMessage(Common.color(ConfigValue.MESSAGES_NOT_INT
                    .replace("{prefix}", ConfigValue.PREFIX)));
            return;
        }

        double amount = Double.parseDouble(args[2]);
        playerData.setCoins(amount);

        sender.sendMessage(Common.color(ConfigValue.MESSAGES_SET_COINS
                .replace("{prefix}", ConfigValue.PREFIX)
                .replace("{player}", player.getName())
                .replace("{amount}", String.valueOf(amount))
                .replace("{amount_rounded}", (int) amount + "")));

        player.sendMessage(Common.color(ConfigValue.MESSAGES_SET_COINS_OTHERS
                .replace("{prefix}", ConfigValue.PREFIX)
                .replace("{amount}", String.valueOf(amount))
                .replace("{amount_rounded}", (int) amount + "")));

        Debug.send(player.getName() + " coins has been set to " + amount);
    }

}
