package me.aglerr.mobcoins.commands;

import me.aglerr.mobcoins.MobCoins;
import me.aglerr.mobcoins.commands.abstraction.SubCommand;
import me.aglerr.mobcoins.commands.subcommands.*;
import me.aglerr.mobcoins.configs.ConfigValue;
import me.aglerr.mobcoins.utils.Common;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class MainCommand implements CommandExecutor, TabCompleter {

    private final Map<String, SubCommand> subCommandMap = new HashMap<>();

    private final MobCoins plugin;

    public MainCommand(MobCoins plugin){
        this.plugin = plugin;

        this.subCommandMap.put("about", new AboutCommand());

        BalanceCommand balanceCommand = new BalanceCommand();
        this.subCommandMap.put("balance", balanceCommand);
        this.subCommandMap.put("bal", balanceCommand);

        this.subCommandMap.put("help", new HelpCommand());
        this.subCommandMap.put("set", new SetCommand());
        this.subCommandMap.put("give", new GiveCommand());
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
                sender.sendMessage(Common.color(ConfigValue.MESSAGES_NO_PERMISSION
                        .replace("{prefix}", ConfigValue.PREFIX)));
                return true;
            }
        }

        subCommand.execute(plugin, sender, args);
        return true;

    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        if(args.length == 1){
            List<String> suggestions = new ArrayList<>();

            suggestions.add("help");

            if(sender.hasPermission("mobcoins.balance")){ suggestions.add("balance"); }

            if(sender.hasPermission("mobcoins.admin")){
                suggestions.add("set");
                suggestions.add("give");
                suggestions.add("take");
                suggestions.add("about");
            }

            return suggestions;
        }

        if(args.length == 2){
            SubCommand subCommand = this.subCommandMap.get(args[0].toLowerCase());
            if(subCommand == null) return null;

            if(subCommand.getPermission() == null){
                return subCommand.parseTabCompletion(plugin, sender, args);
            }

            if(subCommand.getPermission() != null){
                if(!sender.hasPermission(subCommand.getPermission())){
                    return null;
                } else {
                    return subCommand.parseTabCompletion(plugin, sender, args);
                }
            }

        }

        return null;
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
