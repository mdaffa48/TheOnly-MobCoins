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

    private final String userId = "%%__USER__%%";

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
        String premium = "&cThis version is downloaded from SpigotMC";
        String belong = "&4This plugin is registered to https://www.spigotmc.org/members/" + this.userId;
        List<String> messages = Arrays.asList(
                "&6&m---------------------------------",
                "&e&lTheOnly &7(MobCoins edition)",
                "&fPlugin Version: &e" + plugin.getDescription().getVersion(),
                "&fAuthors: &e" + plugin.getDescription().getAuthors().toString()
        );
        if(Common.IS_PREMIUM_VERSION){
            messages.add(premium);
            messages.add(belong);
        }
        messages.add("&6&m---------------------------------");
        messages.forEach(message -> sender.sendMessage(Common.color(message)));
    }

}
