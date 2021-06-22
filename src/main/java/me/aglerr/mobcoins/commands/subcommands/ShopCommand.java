package me.aglerr.mobcoins.commands.subcommands;

import me.aglerr.mobcoins.MobCoins;
import me.aglerr.mobcoins.commands.abstraction.SubCommand;
import me.aglerr.mobcoins.configs.ConfigValue;
import me.aglerr.mobcoins.managers.managers.ShopManager;
import me.aglerr.mobcoins.utils.Common;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ShopCommand extends SubCommand {

    @Nullable
    @Override
    public String getPermission() {
        return "mobcoins.shop";
    }

    @Nullable
    @Override
    public List<String> parseTabCompletion(MobCoins plugin, CommandSender sender, String[] args) {

        if(args.length == 2){
            if(sender.hasPermission("mobcoins.shop.others")){
                List<String> suggestions = new ArrayList<>();
                Bukkit.getOnlinePlayers().forEach(player -> suggestions.add(player.getName()));
                return suggestions;
            }
        }

        return null;
    }

    @Override
    public void execute(MobCoins plugin, CommandSender sender, String[] args) {

        ShopManager shopManager = plugin.getManagerHandler().getShopManager();
        String shopBehaviour = ConfigValue.SHOP_BEHAVIOUR;

        if(args.length == 1){
            if(!(sender instanceof Player)){
                sender.sendMessage(Common.color("&cUsage: /mobcoins shop <player>"));
                return;
            }

            Player player = (Player) sender;
            this.openInventory(player, shopBehaviour, shopManager);
            return;
        }

        if(args.length == 2){
            if(!(sender.hasPermission("mobcoins.shop.others"))){
                sender.sendMessage(Common.color(ConfigValue.MESSAGES_NO_PERMISSION));
                return;
            }

            Player player = Bukkit.getPlayer(args[1]);
            if(player == null){
                sender.sendMessage(Common.color(ConfigValue.MESSAGES_PLAYER_NOT_EXISTS
                        .replace("{prefix}", ConfigValue.PREFIX)));
                return;
            }

            this.openInventory(player, shopBehaviour, shopManager);
            return;
        }

    }

    private void openInventory(Player player, String shopBehaviour, ShopManager shopManager){
        if(shopBehaviour.equalsIgnoreCase("MAIN_MENU")){
            shopManager.openInventory(player, ShopManager.InventoryType.MAIN_MENU);
            return;
        }

        if(shopBehaviour.equalsIgnoreCase("ROTATING_SHOP")){
            shopManager.openInventory(player, ShopManager.InventoryType.ROTATING_SHOP);
            return;
        }

        if(shopBehaviour.equalsIgnoreCase("CATEGORY_SHOP")){
            shopManager.openInventory(player, ShopManager.InventoryType.CATEGORY_SHOP);
            return;
        }
    }

}
