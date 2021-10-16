package me.aglerr.mobcoins.commands.subcommands;

import me.aglerr.mobcoins.MobCoins;
import me.aglerr.mobcoins.commands.abstraction.SubCommand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ConvertDataCommand extends SubCommand {

    @Override
    public @Nullable String getPermission() {
        return "mobcoins.admin";
    }

    @Override
    public @NotNull List<String> parseTabCompletion(MobCoins plugin, CommandSender sender, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public void execute(MobCoins plugin, CommandSender sender, String[] args) {

    }

}
