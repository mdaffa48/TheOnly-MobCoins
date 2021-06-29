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

public class HelpCommand extends SubCommand {

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
        sendHelpMessages(sender);
    }

    private void sendHelpMessages(CommandSender sender){
        if(sender.hasPermission("mobcoins.admin")){
            ConfigValue.MESSAGES_HELP_ADMIN.forEach(message ->
                    sender.sendMessage(Common.color(message)));
            return;
        }

        ConfigValue.MESSAGES_HELP.forEach(message ->
                sender.sendMessage(Common.color(message)));
    }

}
