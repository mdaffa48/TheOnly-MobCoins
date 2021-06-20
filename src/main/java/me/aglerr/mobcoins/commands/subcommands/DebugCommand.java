package me.aglerr.mobcoins.commands.subcommands;

import me.aglerr.mobcoins.MobCoins;
import me.aglerr.mobcoins.commands.abstraction.SubCommand;
import me.aglerr.mobcoins.configs.ConfigValue;
import me.aglerr.mobcoins.utils.Common;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DebugCommand extends SubCommand {

    @Nullable
    @Override
    public String getPermission() {
        return null;
    }

    @Nullable
    @Override
    public List<String> parseTabCompletion(MobCoins plugin, CommandSender sender, String[] args) {
        return null;
    }

    @Override
    public void execute(MobCoins plugin, CommandSender sender, String[] args) {

        if(!sender.isOp()){
            return;
        }

        if(ConfigValue.isDebug){
            ConfigValue.isDebug = false;
            sender.sendMessage(Common.color("{prefix} &cYou have disabled debug mode!"
                    .replace("{prefix}", ConfigValue.PREFIX)));
        } else {
            ConfigValue.isDebug = true;
            sender.sendMessage(Common.color("{prefix} &aYou have enabled debug mode!"
                    .replace("{prefix}", ConfigValue.PREFIX)));
        }

    }

}
