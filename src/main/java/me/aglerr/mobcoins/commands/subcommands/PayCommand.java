package me.aglerr.mobcoins.commands.subcommands;

import me.aglerr.lazylibs.libs.Common;
import me.aglerr.mobcoins.MobCoins;
import me.aglerr.mobcoins.PlayerData;
import me.aglerr.mobcoins.api.MobCoinsAPI;
import me.aglerr.mobcoins.commands.abstraction.SubCommand;
import me.aglerr.mobcoins.configs.ConfigValue;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PayCommand extends SubCommand {

    @Nullable
    @Override
    public String getPermission() {
        return "mobcoins.pay";
    }

    @NotNull
    @Override
    public List<String> parseTabCompletion(MobCoins plugin, CommandSender sender, String[] args) {

        // mobcoins pay <player> <amount>
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
    public void execute(MobCoins plugin, CommandSender sender, String[] args) {

        if(args.length < 3){
            sender.sendMessage(Common.color("&cUsage: /mobcoins pay <player> <amount>"));
            return;
        }

        if(!(sender instanceof Player)){
            sender.sendMessage(Common.color("&cOnly players may execute this command!"));
            return;
        }

        Player player = (Player) sender;
        Player target = Bukkit.getPlayer(args[1]);

        if(target == null){
            player.sendMessage(Common.color(ConfigValue.MESSAGES_PLAYER_NOT_EXISTS
                    .replace("{prefix}", ConfigValue.PREFIX)));
            return;
        }

        if(!Common.isDouble(args[2])){
            player.sendMessage(Common.color(ConfigValue.MESSAGES_NOT_INT
                    .replace("{prefix}", ConfigValue.PREFIX)));
            return;
        }

        PlayerData playerData = MobCoinsAPI.getPlayerData(player);
        if(playerData == null){
            Common.debug(
                    "Command: /mobcoins pay",
                    "No PlayerData found for " + player.getName() + " (player)"
            );
            return;
        }

        PlayerData targetData = MobCoinsAPI.getPlayerData(player);
        if(targetData == null){
            Common.debug(
                    "Command: /mobcoins pay",
                    "No PlayerData found for " + target.getName() + " (target)"
            );
            return;
        }

        if(player.getName().equals(target.getName())){
            player.sendMessage(Common.color(ConfigValue.MESSAGES_SELF_PAY
                    .replace("{prefix}", ConfigValue.PREFIX)));
            return;
        }

        double amount = Double.parseDouble(args[2]);

        if(amount <= 0){
            player.sendMessage(Common.color(ConfigValue.MESSAGES_MINIMUM_AMOUNT
                    .replace("{prefix}", ConfigValue.PREFIX)));
            return;
        }

        if(playerData.getCoins() < amount){
            player.sendMessage(Common.color(ConfigValue.MESSAGES_NOT_ENOUGH_COINS
                    .replace("{prefix}", ConfigValue.PREFIX)));
            return;
        }

        playerData.reduceCoins(amount);
        targetData.addCoins(amount);

        player.sendMessage(Common.color(ConfigValue.MESSAGES_PAY
                .replace("{prefix}", ConfigValue.PREFIX)
                .replace("{amount}", String.valueOf(amount))
                .replace("{player}", target.getName())));

        target.sendMessage(Common.color(ConfigValue.MESSAGES_PAY_OTHERS
                .replace("{prefix}", ConfigValue.PREFIX)
                .replace("{amount}", String.valueOf(amount))
                .replace("{player}", player.getName())));

    }

}
