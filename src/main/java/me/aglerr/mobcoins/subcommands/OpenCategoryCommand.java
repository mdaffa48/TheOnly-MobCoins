package me.aglerr.mobcoins.subcommands;

import me.aglerr.mclibs.commands.SubCommand;
import me.aglerr.mclibs.libs.Common;
import me.aglerr.mobcoins.MobCoins;
import me.aglerr.mobcoins.configs.ConfigValue;
import me.aglerr.mobcoins.managers.managers.ShopManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class OpenCategoryCommand extends SubCommand {

    @NotNull
    @Override
    public String getName() {
        return "opencategory";
    }

    @Nullable
    @Override
    public String getPermission() {
        return "mobcoins.admin";
    }

    @NotNull
    @Override
    public List<String> parseTabCompletions(JavaPlugin javaPlugin, CommandSender sender, String[] args) {
        MobCoins plugin = (MobCoins) javaPlugin;
        ShopManager shopManager = plugin.getManagerHandler().getShopManager();

        if(args.length == 2){
            return new ArrayList<>(shopManager.getItemsLoader().getCategoryShopFiles().keySet());
        }

        if(args.length == 3){
            List<String> suggestions = new ArrayList<>();
            for(Player player : Bukkit.getOnlinePlayers()){
                suggestions.add(player.getName());
            }
            return suggestions;
        }

        return new ArrayList<>();
    }

    @Override
    public void execute(JavaPlugin javaPlugin, CommandSender sender, String[] args) {
        MobCoins plugin = (MobCoins) javaPlugin;
        if(args.length < 2){
            sender.sendMessage(Common.color("&cUsage: /mobcoins opencategory <category> [player]"));
            return;
        }
        ShopManager shopManager = plugin.getManagerHandler().getShopManager();
        if(args.length == 2){
            if(!(sender instanceof Player)){
                sender.sendMessage(Common.color("&cUsage: /mobcoins opencategory <category> [player]"));
                return;
            }
            Player player = (Player) sender;
            String category = args[1];

            FileConfiguration categoryConfig = shopManager.getItemsLoader().getCategoryShopFiles().get(category);

            if(categoryConfig == null){
                sender.sendMessage(Common.color(ConfigValue.MESSAGES_CATEGORY_NOT_EXISTS
                        .replace("{prefix}", ConfigValue.PREFIX)
                        .replace("{category}", category)));
                return;
            }

            shopManager.openCategoryShop(category, player);

        }

    }

}
