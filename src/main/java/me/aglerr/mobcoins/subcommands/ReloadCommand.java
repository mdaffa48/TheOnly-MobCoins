package me.aglerr.mobcoins.subcommands;

import me.aglerr.mclibs.commands.SubCommand;
import me.aglerr.mclibs.libs.Common;
import me.aglerr.mobcoins.MobCoins;
import me.aglerr.mobcoins.configs.ConfigValue;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ReloadCommand extends SubCommand {

    @NotNull
    @Override
    public String getName() {
        return "reload";
    }

    @Nullable
    @Override
    public String getPermission() {
        return "mobcoins.admin";
    }

    @NotNull
    @Override
    public List<String> parseTabCompletions(JavaPlugin javaPlugin, CommandSender sender, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public void execute(JavaPlugin javaPlugin, CommandSender sender, String[] args) {
        MobCoins plugin = (MobCoins) javaPlugin;

        long startTime = System.currentTimeMillis();
        plugin.reloadEverything();
        long timeTaken = System.currentTimeMillis() - startTime;

        sender.sendMessage(Common.color(ConfigValue.MESSAGES_RELOAD
                .replace("{prefix}", ConfigValue.PREFIX)
                .replace("{ms}", timeTaken + "")));
    }

}
