package me.aglerr.mobcoins.commands.subcommands;

import me.aglerr.mobcoins.MobCoins;
import me.aglerr.mobcoins.commands.abstraction.SubCommand;
import me.aglerr.mobcoins.configs.ConfigValue;
import me.aglerr.mobcoins.managers.managers.ShopManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ShopCommand extends SubCommand {

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

        if(!(sender instanceof Player)) return;
        Player player = (Player) sender;

        ShopManager shopManager = plugin.getManagerHandler().getShopManager();

        String shopBehaviour = ConfigValue.SHOP_BEHAVIOUR;
        if(shopBehaviour.equalsIgnoreCase("MAIN_MENU")){
            shopManager.openInventory(player, ShopManager.InventoryType.MAIN_MENU);
        }
    }

}
