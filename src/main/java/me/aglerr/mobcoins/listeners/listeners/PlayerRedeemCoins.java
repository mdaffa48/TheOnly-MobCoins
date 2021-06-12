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
        // Check if server version is greater than 1.8 ( has offhand)
        if(Common.hasOffhand()){
            // Return if player not clicking on main hand
            // Need to check this otherwise the event gonna fired 2 times for both arms
            if(event.getHand() != EquipmentSlot.HAND) return;
        }

        // Return if the action is not RIGHT_CLICK_BLOCK and RIGHT_CLICK_AIR
        if(event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_AIR) return;

        // Get player item on main hand
        // Need to use deprecated event because 1.8 doesn't have the method for the non-deprecated method
        ItemStack hand = player.getItemInHand();

        // Return if the type of the item is AIR
        if(hand.getType() == Material.AIR) return;

        // Get NBTItem object of the item
        NBTItem nbtItem = new NBTItem(hand);
        // Return if item is not the mobcoin item by checking the tags
        if(!nbtItem.hasKey("mobCoinsAmount")) return;
        event.setCancelled(true);

        // Get PlayerData object from Player
        PlayerData playerData = MobCoinsAPI.getPlayerData(player);
        // Return if player doesn't have any data
        if(playerData == null){
            Common.debug(true,
                    "Event: MobCoins Redeem",
                    "No PlayerData found for " + player.getName()
            );
            return;
        }

        // Get the amount that is stored inside the item
        double storedAmount = nbtItem.getDouble("mobCoinsAmount");
        double amount = 0;

        // Check if the item amount is 1
        if(hand.getAmount() == 1){
            // Make the final amount equals as the stored amount
            amount = storedAmount;

        // Check if the amount is greater than 1
        } else if(hand.getAmount() > 1){
            // Make the final amount equals stored amount multiply by the amount of item
            amount = (storedAmount * hand.getAmount());
        }

        // Create the event and then call it
        MobCoinsRedeemEvent redeemEvent = new MobCoinsRedeemEvent(player, playerData, nbtItem.getItem(), amount);
        Bukkit.getPluginManager().callEvent(redeemEvent);
        if(redeemEvent.isCancelled()) return;

        // Remove the item from the player's main hand
        player.setItemInHand(XMaterial.AIR.parseItem());

        // Add mobcoins to player data
        playerData.addCoins(redeemEvent.getAmount());

        // Send notification such as message and sound
        player.sendMessage(Common.color(ConfigValue.MESSAGES_REDEEM
                .replace("{prefix}", ConfigValue.PREFIX)
                .replace("{amount}", String.valueOf(redeemEvent.getAmount()))));
        Common.playSound(player, "sounds.onCoinsRedeem", plugin.getConfig());

        Common.debug(true, player.getName() + " redeemed " + redeemEvent.getAmount() + " coins");

    }

}
