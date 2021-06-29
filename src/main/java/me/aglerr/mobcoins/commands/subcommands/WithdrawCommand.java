package me.aglerr.mobcoins.commands.subcommands;

import me.aglerr.mobcoins.MobCoins;
import me.aglerr.mobcoins.PlayerData;
import me.aglerr.mobcoins.api.MobCoinsAPI;
import me.aglerr.mobcoins.commands.abstraction.SubCommand;
import me.aglerr.mobcoins.configs.ConfigValue;
import me.aglerr.mobcoins.utils.ItemManager;
import me.aglerr.mobcoins.utils.libs.Common;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WithdrawCommand extends SubCommand {

    @Nullable
    @Override
    public String getPermission() {
        return "mobcoins.withdraw";
    }

    @NotNull
    @Override
    public List<String> parseTabCompletion(MobCoins plugin, CommandSender sender, String[] args) {

        if(args.length == 2){
            return Collections.singletonList("<amount>");
        }

        return new ArrayList<>();
    }

    @Override
    public void execute(MobCoins plugin, CommandSender sender, String[] args) {

        if(!(sender instanceof Player)){
            sender.sendMessage(Common.color(ConfigValue.MESSAGES_NO_PERMISSION
                    .replace("{prefix}", ConfigValue.PREFIX)));
            return;
        }


        if(args.length < 2){
            sender.sendMessage(Common.color("&cUsage: /mobcoins withdraw <amount>"));
            return;
        }

        Player player = (Player) sender;
        PlayerData playerData = MobCoinsAPI.getPlayerData(player);

        if(playerData == null){
            Common.debug(
                    "Command: /mobcoins withdraw",
                    "No PlayerData found for " + player.getName()
            );
            return;
        }

        if(!Common.isDouble(args[1])){
            sender.sendMessage(Common.color(ConfigValue.MESSAGES_NOT_INT
                    .replace("{prefix}", ConfigValue.PREFIX)));
            return;
        }

        double amount = Double.parseDouble(args[1]);

        if(playerData.getCoins() < amount){
            sender.sendMessage(Common.color(ConfigValue.MESSAGES_NOT_ENOUGH_COINS
                    .replace("{prefix}", ConfigValue.PREFIX)));
            return;
        }

        if(isInventoryFull(player)){
            sender.sendMessage(Common.color(ConfigValue.MESSAGES_INVENTORY_FULL
                    .replace("{prefix}", ConfigValue.PREFIX)));
            return;
        }

        playerData.reduceCoins(amount);
        ItemStack stack = ItemManager.createMobCoinItem(amount);

        player.getInventory().addItem(stack);
        sender.sendMessage(Common.color(ConfigValue.MESSAGES_WITHDRAW
                .replace("{prefix}", ConfigValue.PREFIX)
                .replace("{amount}", String.valueOf(amount))));

    }

    private boolean isInventoryFull(Player player){
        return player.getInventory().firstEmpty() == -1;
    }

}
