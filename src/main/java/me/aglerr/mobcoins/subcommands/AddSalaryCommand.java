package me.aglerr.mobcoins.subcommands;

import me.aglerr.mclibs.commands.SubCommand;
import me.aglerr.mclibs.libs.Common;
import me.aglerr.mobcoins.MobCoins;
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

public class AddSalaryCommand extends SubCommand {

    @NotNull
    @Override
    public String getName() {
        return "addsalary";
    }

    @Nullable
    @Override
    public String getPermission() {
        return "mobcoins.admin";
    }

    @Nullable
    @Override
    public List<String> parseTabCompletions(JavaPlugin javaPlugin, CommandSender sender, String[] args) {
        // /mobcoins addsalary (player) (amount)
        if(args.length == 2){
            return null;
        }
        if(args.length == 3){
            return Collections.singletonList("(amount)");
        }
        return new ArrayList<>();
    }

    @Override
    public void execute(JavaPlugin javaPlugin, CommandSender sender, String[] args) {
        MobCoins plugin = (MobCoins) javaPlugin;
        if(args.length < 3){
            Common.sendMessage(sender, "&c/mobcoins addsalary (player) (amount)");
            return;
        }
        Player player = Bukkit.getPlayer(args[1]);
        if(player == null){
            Common.sendMessage(sender, ConfigValue.MESSAGES_PLAYER_NOT_EXISTS
                    .replace("{prefix}", ConfigValue.PREFIX));
            return;
        }
        if(!Common.isDouble(args[2])){
            Common.sendMessage(sender, ConfigValue.MESSAGES_NOT_INT
                    .replace("{prefix}", ConfigValue.PREFIX));
            return;
        }
        double amount = Double.parseDouble(args[2]);
        plugin.getManagerHandler().getSalaryManager().putOrIncrementPlayerSalary(player, amount);
        Common.sendMessage(sender, ConfigValue.MESSAGES_SALARY_ADDED
                .replace("{prefix}", ConfigValue.PREFIX)
                .replace("{amount}", amount + "")
                .replace("{player}", player.getName()));
    }

}
