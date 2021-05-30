package me.aglerr.mobcoins.commands.subcommands;

import me.aglerr.mobcoins.MobCoins;
import me.aglerr.mobcoins.PlayerData;
import me.aglerr.mobcoins.api.MobCoinsAPI;
import me.aglerr.mobcoins.commands.abstraction.SubCommand;
import me.aglerr.mobcoins.configs.ConfigValue;
import me.aglerr.mobcoins.enums.ModifyCoin;
import me.aglerr.mobcoins.utils.Common;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TakeCommand extends SubCommand {

    @Nullable
    @Override
    public String getPermission() {
        return "mobcoins.admin";
    }

    @Nullable
    @Override
    public List<String> parseTabCompletion(MobCoins plugin, CommandSender sender, String[] args) {

        if(args.length == 2){
            List<String> suggestions = new ArrayList<>();
            Bukkit.getOnlinePlayers().forEach(player -> suggestions.add(player.getName()));
            return suggestions;
        }

        if(args.length == 3){
            return Collections.singletonList("<amount>");
        }

        return null;
    }

    @Override
    public void execute(MobCoins plugin, CommandSender sender, String[] args) {

        if(args.length < 3){
            sender.sendMessage(Common.color("&cUsage: /mobcoins take <player> <amount>"));
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
            Common.debug(true,
                    "Command: /mobcoins take",
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
        playerData.modifyCoins(ModifyCoin.REDUCE, amount);

        sender.sendMessage(Common.color(ConfigValue.MESSAGES_REMOVE_COINS
                .replace("{prefix}", ConfigValue.PREFIX)
                .replace("{player}", player.getName())
                .replace("{amount}", String.valueOf(amount))));

        player.sendMessage(Common.color(ConfigValue.MESSAGES_REMOVE_COINS_OTHERS
                .replace("{prefix}", ConfigValue.PREFIX)
                .replace("{amount}", String.valueOf(amount))));

        Common.debug(true,
                player.getName() + " coins has been removed by " + amount
        );

    }

}
