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
     * Get player that redeemed the mobcoins
     *
     * @return player object
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Get PlayerData object of {@link MobCoinsRedeemEvent#getPlayer()}
     *
     * @return {@link me.aglerr.mobcoins.PlayerData}
     */
    public PlayerData getPlayerData() {
        return playerData;
    }

    /**
     * Get the ItemStack that player redeemed
     *
     * @return ItemStack that is involved in this event
     */
    public ItemStack getItemStack() {
        return itemStack;
    }

    /**
     * Get the amount of mobcoins that player redeemed
     *
     * @return the amount mobcoins that player received
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Set the amount of mobcoins that player will receive
     *
     * @param amount the amount mobcoins that player will receive
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }
}
