package me.aglerr.mobcoins.commands.subcommands;

import me.aglerr.lazylibs.libs.Common;
import me.aglerr.mobcoins.MobCoins;
import me.aglerr.mobcoins.commands.abstraction.SubCommand;
import me.aglerr.mobcoins.configs.ConfigValue;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DebugCommand extends SubCommand {

    @Nullable
    @Override
    public String getPermission() {
        return null;
    }

    @NotNull
    @Override
    public List<String> parseTabCompletion(MobCoins plugin, CommandSender sender, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public void execute(MobCoins plugin, CommandSender sender, String[] args) {

        if(!sender.isOp()){
            return;
        }

        if(Common.DEBUG){
            Common.DEBUG = false;
            sender.sendMessage(Common.color("{prefix} &cYou have disabled debug mode!"
                    .replace("{prefix}", ConfigValue.PREFIX)));
        } else {
            Common.DEBUG = true;
            sender.sendMessage(Common.color("{prefix} &aYou have enabled debug mode!"
                    .replace("{prefix}", ConfigValue.PREFIX)));
        }

    }

}
