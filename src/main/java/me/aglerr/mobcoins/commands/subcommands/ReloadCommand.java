package me.aglerr.mobcoins.commands.subcommands;

import me.aglerr.mobcoins.MobCoins;
import me.aglerr.mobcoins.commands.abstraction.SubCommand;
import me.aglerr.mobcoins.configs.ConfigValue;
import me.aglerr.mobcoins.utils.libs.Common;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ReloadCommand extends SubCommand {

    @Nullable
    @Override
    public String getPermission() {
        return "mobcoins.admin";
    }

    @NotNull
    @Override
    public List<String> parseTabCompletion(MobCoins plugin, CommandSender sender, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public void execute(MobCoins plugin, CommandSender sender, String[] args) {

        long startTime = System.currentTimeMillis();
        plugin.reloadEverything();
        long timeTaken = System.currentTimeMillis() - startTime;

        sender.sendMessage(Common.color(ConfigValue.MESSAGES_RELOAD
                .replace("{prefix}", ConfigValue.PREFIX)
                .replace("{ms}", timeTaken + "")));

    }

}
