package me.aglerr.mobcoins.subcommands;

import me.aglerr.mclibs.commands.SubCommand;
import me.aglerr.mclibs.libs.Common;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AboutCommand extends SubCommand {

    private final String userId = "%%__USER__%%";

    @NotNull
    @Override
    public String getName() {
        return "about";
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
        String belong = "&4This plugin is registered to https://www.spigotmc.org/members/" + this.userId;
        List<String> messages = Arrays.asList(
                "&6&m---------------------------------",
                "&e&lTheOnly &7(MobCoins edition)",
                "&fPlugin Version: &e" + javaPlugin.getDescription().getVersion(),
                "&fAuthors: &e" + javaPlugin.getDescription().getAuthors(),
                belong,
                "&6&m---------------------------------"
        );
        messages.forEach(message -> sender.sendMessage(Common.color(message)));
    }

}
