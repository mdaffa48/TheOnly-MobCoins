package me.aglerr.mobcoins.subcommands;

import me.aglerr.mclibs.commands.SubCommand;
import me.aglerr.mclibs.libs.Common;
import me.aglerr.mclibs.libs.Debug;
import me.aglerr.mobcoins.MobCoins;
import me.aglerr.mobcoins.PlayerData;
import me.aglerr.mobcoins.api.MobCoinsAPI;
import me.aglerr.mobcoins.configs.ConfigValue;
import me.aglerr.mobcoins.utils.ItemManager;
import me.aglerr.mobcoins.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class GiveRandomCommand extends SubCommand {

    @NotNull
    @Override
    public String getName() {
        return "giverandom";
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
            return Arrays.asList("physical", "virtual");
        }
        if(args.length == 3){
            List<String> suggestions = new ArrayList<>();
            Bukkit.getOnlinePlayers().forEach(player -> suggestions.add(player.getName()));
            return suggestions;
        }
        if(args.length == 4){
            return Collections.singletonList("<minimum amount>");
        }
        if(args.length == 5){
            return Collections.singletonList("<maximum amount>");
        }
        return new ArrayList<>();
    }

    @Override
    public void execute(JavaPlugin javaPlugin, CommandSender sender, String[] args) {
        if(args.length < 5){
            sender.sendMessage(Common.color("&cUsage: /mobcoins giverandom <physical|virtual> <player> <minimum> <maximum>"));
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

        if(!Common.isDouble(args[4])){
            sender.sendMessage(Common.color(ConfigValue.MESSAGES_NOT_INT
                    .replace("{prefix}", ConfigValue.PREFIX)));
            return;
        }

        double minimum = Double.parseDouble(args[3]);
        double maximum = Double.parseDouble(args[4]);
        double amount = this.getAmountFromTwoNumber(minimum, maximum);
        String type = args[1];

        PlayerData playerData = MobCoinsAPI.getPlayerData(player);
        if(playerData == null){
            Debug.send(
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
                    .replace("{amount}", Utils.digits(amount))
                    .replace("{player}", player.getName())
                    .replace("{amount_rounded}", (int) amount + "")));

            player.sendMessage(Common.color(ConfigValue.MESSAGES_ADD_COINS_OTHERS
                    .replace("{prefix}", ConfigValue.PREFIX)
                    .replace("{type}", type)
                    .replace("{amount}", Utils.digits(amount))
                    .replace("{amount_rounded}", (int) amount + "")));
            return;
        }

        if(this.isPhysical(type)){
            ItemStack stack = ItemManager.createMobCoinItem(amount);

            sender.sendMessage(Common.color(ConfigValue.MESSAGES_ADD_COINS
                    .replace("{prefix}", ConfigValue.PREFIX)
                    .replace("{type}", type)
                    .replace("{amount}", Utils.digits(amount))
                    .replace("{player}", player.getName())
                    .replace("{amount_rounded}", (int) amount + "")));

            player.sendMessage(Common.color(ConfigValue.MESSAGES_ADD_COINS_OTHERS
                    .replace("{prefix}", ConfigValue.PREFIX)
                    .replace("{type}", type)
                    .replace("{amount}", Utils.digits(amount))
                    .replace("{amount_rounded}", (int) amount + "")));

            player.getInventory().addItem(stack);
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

    private double getAmountFromTwoNumber(double minimum, double maximum){
        return ThreadLocalRandom.current().nextDouble(maximum - minimum) + minimum;
    }

}
