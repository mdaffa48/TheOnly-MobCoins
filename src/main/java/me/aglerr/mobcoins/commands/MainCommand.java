package me.aglerr.mobcoins.commands;

import me.aglerr.mobcoins.MobCoins;
import me.aglerr.mobcoins.commands.abstraction.SubCommand;
import me.aglerr.mobcoins.configs.ConfigValue;
import me.aglerr.mobcoins.utils.Common;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainCommand implements CommandExecutor, TabCompleter {

    private final Map<String, SubCommand> subCommandMap = new HashMap<>();

    private final MobCoins plugin;

    public MainCommand(MobCoins plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        if(args.length == 0){
            sendHelpMessages(sender);
            return true;
        }

        SubCommand subCommand = this.subCommandMap.get(args[0].toLowerCase());
        if(subCommand == null) {
            sendHelpMessages(sender);
            return true;
        }

        if(subCommand.getPermission() != null){
            if(!(sender.hasPermission(subCommand.getPermission()))){
                sender.sendMessage(Common.color(ConfigValue.MESSAGES_NO_PERMISSION));
                return true;
            }
        }

        subCommand.execute(plugin, sender, args);
        return true;

    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        if(args.length == 0){
            return Arrays.asList("help");
        }

        return this.subCommandMap.get(args[0].toLowerCase()).parseTabCompletion(plugin, sender, args);
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
