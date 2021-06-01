package me.aglerr.mobcoins.api.events;

import me.aglerr.mobcoins.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Called when player redeemed physical mobcoin.
 */
public class MobCoinsRedeemEvent extends Event implements Cancellable {

    private boolean isCancelled;

    private final Player player;
    private final PlayerData playerData;
    private final ItemStack itemStack;
    private double amount;

    private static final HandlerList HANDLERS = new HandlerList();

    public MobCoinsRedeemEvent(Player player, PlayerData playerData, ItemStack itemStack, double amount) {
        this.player = player;
        this.playerData = playerData;
        this.itemStack = itemStack;
        this.amount = amount;
        this.isCancelled = false;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    /**
     * @return player that redeemed mobcoins item
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * @return player data
     */
    public PlayerData getPlayerData() {
        return playerData;
    }

    /**
     * @return ItemStack that is involved in this event
     */
    public ItemStack getItemStack() {
        return itemStack;
    }

    /**
     * @return the amount mobcoins that player received
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Set the amount mobcoins that player receives
     * @param amount the amount mobcoins that player will receives
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }
}
