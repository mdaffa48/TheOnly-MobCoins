package me.aglerr.mobcoins.listeners.listeners;

import com.cryptomorin.xseries.XMaterial;
import de.tr7zw.changeme.nbtapi.NBTItem;
import me.aglerr.mobcoins.MobCoins;
import me.aglerr.mobcoins.PlayerData;
import me.aglerr.mobcoins.api.MobCoinsAPI;
import me.aglerr.mobcoins.api.events.MobCoinsRedeemEvent;
import me.aglerr.mobcoins.configs.ConfigValue;
import me.aglerr.mobcoins.utils.Common;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class PlayerRedeemCoins implements Listener {

    private final MobCoins plugin;
    public PlayerRedeemCoins(MobCoins plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){

        Player player = event.getPlayer();
        if(Common.hasOffhand()){
            if(event.getHand() != EquipmentSlot.HAND) return;
        }

        if(event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_AIR) return;

        ItemStack hand = player.getItemInHand();
        if(hand.getType() == Material.AIR) return;

        NBTItem nbtItem = new NBTItem(hand);
        if(nbtItem.getDouble("mobCoinsAmount") == null) return;
        event.setCancelled(true);

        PlayerData playerData = MobCoinsAPI.getPlayerData(player);
        if(playerData == null){
            Common.debug(true,
                    "Event: MobCoins Redeem",
                    "No PlayerData found for " + player.getName()
            );
            return;
        }

        double storedAmount = nbtItem.getDouble("mobCoinsAmount");
        double amount = 0;

        if(hand.getAmount() == 1){
            amount = storedAmount;
        } else if(hand.getAmount() > 1){
            amount = (storedAmount * hand.getAmount());
        }

        MobCoinsRedeemEvent redeemEvent = new MobCoinsRedeemEvent(player, playerData, nbtItem.getItem(), amount);
        Bukkit.getPluginManager().callEvent(redeemEvent);
        if(redeemEvent.isCancelled()) return;

        player.setItemInHand(XMaterial.AIR.parseItem());
        playerData.addCoins(redeemEvent.getAmount());

        player.sendMessage(Common.color(ConfigValue.MESSAGES_REDEEM
                .replace("{prefix}", ConfigValue.PREFIX)
                .replace("{amount}", String.valueOf(redeemEvent.getAmount()))));

        Common.playSound(player, "sounds.onCoinsRedeem", plugin.getConfig());

        Common.debug(true,
                player.getName() + " redeemed " + redeemEvent.getAmount() + " coins"
        );

    }

}
