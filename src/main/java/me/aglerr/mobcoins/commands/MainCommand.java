package me.aglerr.mobcoins.commands;

import me.aglerr.lazylibs.libs.Common;
import me.aglerr.mobcoins.MobCoins;
import me.aglerr.mobcoins.commands.abstraction.SubCommand;
import me.aglerr.mobcoins.commands.subcommands.*;
import me.aglerr.mobcoins.configs.ConfigValue;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.*;

public class MainCommand implements CommandExecutor, TabCompleter {

    private final Map<String, SubCommand> subCommandMap = new HashMap<>();
    private static final String COMMAND_NAME = "mobcoins";

    private final MobCoins plugin;

    public MainCommand(MobCoins plugin){
        this.plugin = plugin;

        this.subCommandMap.put("about", new AboutCommand());

        BalanceCommand balanceCommand = new BalanceCommand();
        this.subCommandMap.put("balance", balanceCommand);
        this.subCommandMap.put("bal", balanceCommand);

        this.subCommandMap.put("give", new GiveCommand());
        this.subCommandMap.put("giverandom", new GiveRandomCommand());
        this.subCommandMap.put("help", new HelpCommand());
        this.subCommandMap.put("opencategory", new OpenCategoryCommand());
        this.subCommandMap.put("pay", new PayCommand());
        this.subCommandMap.put("reload", new ReloadCommand());
        this.subCommandMap.put("set", new SetCommand());
        this.subCommandMap.put("shop", new ShopCommand());
        this.subCommandMap.put("take", new TakeCommand());
        this.subCommandMap.put("top", new TopCommand());
        this.subCommandMap.put("withdraw", new WithdrawCommand());
        this.subCommandMap.put("debug", new DebugCommand());
    }

    public void registerThisCommand(){
        plugin.getCommand(COMMAND_NAME).setExecutor(this);
        plugin.getCommand(COMMAND_NAME).setTabCompleter(this);

        FileConfiguration config = plugin.getConfig();

        // Get the aliases from config
        List<String> aliases = config.getStringList("aliases");
        // Add all aliases to the command from the aliases list
        plugin.getCommand(COMMAND_NAME).getAliases().addAll(aliases);

        try{
            final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");

            bukkitCommandMap.setAccessible(true);
            ((CommandMap) bukkitCommandMap.get(Bukkit.getServer())).register(COMMAND_NAME, plugin.getCommand(COMMAND_NAME));

            bukkitCommandMap.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            Common.log(ChatColor.RED, "Failed registering commands");
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        // Return if the args length is 0 and send help messages
        if(args.length == 0){
            sendHelpMessages(sender);
            return true;
        }

        // Trying to get subcommand from 'args[0]'
        SubCommand subCommand = this.subCommandMap.get(args[0].toLowerCase());

        // Return if there is no subcommand with 'args[0]' and send help messages
        if(subCommand == null) {
            sendHelpMessages(sender);
            return true;
        }

        // Check if sub command has permission
        if(subCommand.getPermission() != null){
            // Check if sender/player doesn't have permission for the subcommand
            if(!(sender.hasPermission(subCommand.getPermission()))){
                // Return and send messages
                sender.sendMessage(Common.color(ConfigValue.MESSAGES_NO_PERMISSION
                        .replace("{prefix}", ConfigValue.PREFIX)));
                return true;
            }
        }

        // Execute the sub command
        subCommand.execute(plugin, sender, args);
        return true;

    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        if(args.length == 1){
            List<String> suggestions = new ArrayList<>();

            suggestions.add("help");

            if(sender.hasPermission("mobcoins.balance")){
                suggestions.add("balance");
            }

            if(sender.hasPermission("mobcoins.shop")){
                suggestions.add("shop");
            }

            if(sender.hasPermission("mobcoins.pay")){
                suggestions.add("pay");
            }

            if(sender.hasPermission("mobcoins.top")){
                suggestions.add("top");
            }

            if(sender.hasPermission("mobcoins.admin")){
                suggestions.addAll(Arrays.asList("set", "give", "take", "about", "opencategory", "reload", "giverandom"));
            }

            return suggestions;
        }

        if(args.length >= 2){
            SubCommand subCommand = this.subCommandMap.get(args[0].toLowerCase());
            if(subCommand == null) return null;

            if(subCommand.getPermission() == null){
                return subCommand.parseTabCompletion(plugin, sender, args);
            }
            if(sender.hasPermission(subCommand.getPermission())){
                return subCommand.parseTabCompletion(plugin, sender, args);
            }
            return new ArrayList<>();
        }

        return new ArrayList<>();
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
