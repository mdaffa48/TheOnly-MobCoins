package me.aglerr.mobcoins.commands.subcommands;

import me.aglerr.mobcoins.MobCoins;
import me.aglerr.mobcoins.commands.abstraction.SubCommand;
import me.aglerr.mobcoins.configs.ConfigValue;
import me.aglerr.mobcoins.utils.Common;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class AboutCommand extends SubCommand {

    @Nullable
    @Override
    public String getPermission() {
        return "mobcoins.admin";
    }

    @Nullable
    @Override
    public List<String> parseTabCompletion(MobCoins plugin, CommandSender sender, String[] args) {
        return null;
    }

    @Override
    public void execute(MobCoins plugin, CommandSender sender, String[] args) {

        List<String> messages = Arrays.asList(
                "&6&m---------------------------------",
                "&e&lTheOnly &7(MobCoins edition)",
                "&fPlugin Version: &e" + plugin.getDescription().getVersion(),
                "&fAuthors: &e" + plugin.getDescription().getAuthors().toString(),
                "&f",
                "&cPlugin registered to:",
                "&7https://www.spigotmc.org/members/" + ConfigValue.USER_ID + "/",
                "&6&m---------------------------------"
        );

        messages.forEach(message -> sender.sendMessage(Common.color(message)));

    }

}
