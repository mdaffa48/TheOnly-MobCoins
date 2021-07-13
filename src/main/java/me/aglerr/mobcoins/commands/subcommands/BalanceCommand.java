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
import java.util.List;

public class BalanceCommand extends SubCommand {

    @Nullable
    @Override
    public String getPermission() {
        return "mobcoins.balance";
    }

    @NotNull
    @Override
    public List<String> parseTabCompletion(MobCoins plugin, CommandSender sender, String[] args) {

        if(sender.hasPermission("mobcoins.balance.others")){
            if(args.length == 2){
                List<String> suggestions = new ArrayList<>();
                Bukkit.getOnlinePlayers().forEach(player -> suggestions.add(player.getName()));
                return suggestions;
            }
        }

        return new ArrayList<>();
    }

    @Override
    public void execute(MobCoins plugin, CommandSender sender, String[] args) {

        if(args.length == 1){
            if(sender instanceof Player){

                Player player = (Player) sender;
                PlayerData playerData = MobCoinsAPI.getPlayerData(player);
                if(playerData == null) {
                    Common.debug(
                            "Command: /mobcoins balance",
                            "No PlayerData found for " + player.getName()
                    );
                    return;
                }

                sender.sendMessage(Common.color(ConfigValue.MESSAGES_BALANCE
                        .replace("{prefix}", ConfigValue.PREFIX)
                        .replace("{coins}", playerData.getCoinsFormatted())
                        .replace("{coins_rounded}", playerData.getCoinsRounded() + "")));

                return;
            }

            sender.sendMessage(Common.color("&cUsage: /mobcoins balance <player>"));
            return;
        }

        if(args.length == 2){

            if(!(sender.hasPermission("mobcoins.balance.others"))){
                sender.sendMessage(Common.color(ConfigValue.MESSAGES_NO_PERMISSION
                        .replace("{prefix}", ConfigValue.PREFIX)));
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
                Common.debug(
                        "Command: /mobcoins balance [others]",
                        "No PlayerData found for " + player.getName()
                );
                return;
            }

            sender.sendMessage(Common.color(ConfigValue.MESSAGES_BALANCE_OTHERS
                    .replace("{prefix}", ConfigValue.PREFIX)
                    .replace("{player}", player.getName())
                    .replace("{coins}", playerData.getCoinsFormatted())
                    .replace("{coins_rounded}", playerData.getCoinsRounded() + "")));

        }

    }
}
