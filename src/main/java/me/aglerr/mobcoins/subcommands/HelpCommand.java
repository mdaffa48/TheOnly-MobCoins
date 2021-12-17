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

public class HelpCommand extends SubCommand {

    @NotNull
    @Override
    public String getName() {
        return "help";
    }

    @Nullable
    @Override
    public String getPermission() {
        return null;
    }

    @NotNull
    @Override
    public List<String> parseTabCompletions(JavaPlugin javaPlugin, CommandSender sender, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public void execute(JavaPlugin javaPlugin, CommandSender sender, String[] args) {
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
