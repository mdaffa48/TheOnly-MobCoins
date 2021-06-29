package me.aglerr.mobcoins.commands.abstraction;

import me.aglerr.mobcoins.MobCoins;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class SubCommand {

    @Nullable
    public abstract String getPermission();

    @NotNull
    public abstract List<String> parseTabCompletion(MobCoins plugin, CommandSender sender, String[] args);

    public abstract void execute(MobCoins plugin, CommandSender sender, String[] args);

}
