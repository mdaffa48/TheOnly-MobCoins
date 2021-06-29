package me.aglerr.mobcoins.commands.subcommands;

import me.aglerr.mobcoins.MobCoins;
import me.aglerr.mobcoins.commands.abstraction.SubCommand;
import me.aglerr.mobcoins.utils.libs.Common;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AboutCommand extends SubCommand {

    private final String userId = "%%__USER__%%";

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
        String belong = "&4This plugin is registered to https://www.spigotmc.org/members/" + this.userId;
        List<String> messages = Arrays.asList(
                "&6&m---------------------------------",
                "&e&lTheOnly &7(MobCoins edition)",
                "&fPlugin Version: &e" + plugin.getDescription().getVersion(),
                "&fAuthors: &e" + plugin.getDescription().getAuthors(),
                belong,
                "&6&m---------------------------------"
        );
        messages.forEach(message -> sender.sendMessage(Common.color(message)));
    }

}
