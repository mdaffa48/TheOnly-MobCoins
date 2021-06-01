package me.aglerr.mobcoins.api.events;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when player receives mobcoin from killing configured mobs in mobs.yml
 */
public class MobCoinsReceiveEvent extends Event implements Cancellable {

    private boolean isCancelled;

    private final Player player;
    private final LivingEntity entity;
    private double amountReceived;

    public MobCoinsReceiveEvent(Player player, LivingEntity entity, double amountReceived){
        this.player = player;
        this.entity = entity;
        this.amountReceived = amountReceived;

        this.isCancelled = false;
    }

    private static final HandlerList HANDLERS = new HandlerList();

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
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    /**
     * @return player that is involved in this event
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * @return entity that is involved in this event
     */
    public LivingEntity getEntity() {
        return entity;
    }

    /**
     * @return the amount of mobcoins that player received
     */
    public double getAmountReceived() {
        return amountReceived;
    }

    /**
     * @param amountReceived amount of mobcoins that player will receive
     */
    public void setAmountReceived(double amountReceived) {
        this.amountReceived = amountReceived;
    }
}
