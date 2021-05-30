package me.aglerr.mobcoins.commands.subcommands;

import me.aglerr.mobcoins.MobCoins;
import me.aglerr.mobcoins.PlayerData;
import me.aglerr.mobcoins.api.MobCoinsAPI;
import me.aglerr.mobcoins.commands.abstraction.SubCommand;
import me.aglerr.mobcoins.configs.ConfigValue;
import me.aglerr.mobcoins.utils.Common;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GiveCommand extends SubCommand {

    /**
     * TODO: Physical Mob Coin
     */

    @Nullable
    @Override
    public String getPermission() {
        return "mobcoins.admin";
    }

    @Nullable
    @Override
    public List<String> parseTabCompletion(MobCoins plugin, CommandSender sender, String[] args) {

        if(args.length == 2){
            return Arrays.asList("physical", "virtual");
        }

        if(args.length == 3){
            List<String> suggestions = new ArrayList<>();
            Bukkit.getOnlinePlayers().forEach(player -> suggestions.add(player.getName()));
            return suggestions;
        }

        if(args.length == 4){
            return Collections.singletonList("<amount>");
        }

        return null;
    }

    @Override
    public void execute(MobCoins plugin, CommandSender sender, String[] args) {

        if(args.length < 4){
            sender.sendMessage(Common.color("&cUsage: /mobcoins give <physical|virtual> <player> <amount>"));
            return;
        }

        Player player = Bukkit.getPlayer(args[2]);
        if(player == null){
            sender.sendMessage(Common.color(ConfigValue.MESSAGES_PLAYER_NOT_EXISTS
                    .replace("{prefix}", ConfigValue.PREFIX)));
            return;
        }

        if(!Common.isDouble(args[3])){
            sender.sendMessage(Common.color(ConfigValue.MESSAGES_NOT_INT
                    .replace("{prefix}", ConfigValue.PREFIX)));
            return;
        }

        double amount = Double.parseDouble(args[3]);
        String type = args[1];

        PlayerData playerData = MobCoinsAPI.getPlayerData(player);
        if(playerData == null){
            Common.debug(true,
                    "Command: /mobcoins give",
                    "No PlayerData found for " + player.getName()
            );
            return;
        }

        if(this.isVirtual(type)){
            playerData.addCoins(amount);

            sender.sendMessage(Common.color(ConfigValue.MESSAGES_ADD_COINS
                    .replace("{prefix}", ConfigValue.PREFIX)
                    .replace("{type}", type)
                    .replace("{amount}", String.valueOf(amount))
                    .replace("{player}", player.getName())));

            player.sendMessage(Common.color(ConfigValue.MESSAGES_ADD_COINS_OTHERS
                    .replace("{prefix}", ConfigValue.PREFIX)
                    .replace("{type}", type)
                    .replace("{amount}", String.valueOf(amount))));
            return;
        }

        if(this.isPhysical(type)){
            sender.sendMessage("Coming Soon!");
            return;
        }

        if(!this.isVirtual(type) || !this.isPhysical(type)){
            sender.sendMessage(Common.color(ConfigValue.MESSAGES_INVALID_TYPE
                    .replace("{prefix}", ConfigValue.PREFIX)));
        }

    }

    private boolean isPhysical(String arg){
        if(arg.equalsIgnoreCase("physical")) return true;
        return arg.equalsIgnoreCase("p");
    }

    private boolean isVirtual(String arg){
        if(arg.equalsIgnoreCase("virtual")) return true;
        return arg.equalsIgnoreCase("v");
    }

}
