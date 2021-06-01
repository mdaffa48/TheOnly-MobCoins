package me.aglerr.mobcoins.api.events;

import me.aglerr.mobcoins.coinmob.CoinMob;
import me.aglerr.mobcoins.utils.Common;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Called when physical mobcoin spawned because entity death
 */
public class MobCoinsSpawnEvent extends Event implements Cancellable {

    private boolean isCancelled;

    private final LivingEntity entity;
    private final CoinMob coinMob;
    private double amountToDrop;

    public MobCoinsSpawnEvent(LivingEntity entity, CoinMob coinMob, double amountToDrop){
        this.entity = entity;
        this.coinMob = coinMob;
        this.amountToDrop = amountToDrop;

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
     * @return entity that is involved in this event
     */
    public LivingEntity getEntity() {
        return entity;
    }

    /**
     * @return get CoinMob object that store chance, drop amount, and chance
     */
    public CoinMob getCoinMob() {
        return coinMob;
    }

    /**
     * @return amount of mobcoins that is dropped
     */
    public double getAmountToDrop() {
        return amountToDrop;
    }

    /**
     * @param amount set the amount mobcoins that will dropped
     */
    public void setAmountToDrop(double amount){
        this.amountToDrop = amount;
    }

    /**
     * @return the itemstack that player will receive
     */
    public ItemStack getItemStack(){
        return Common.createMobCoinItem(getAmountToDrop());
    }

}
