package me.aglerr.mobcoins.api.events;

import me.aglerr.mobcoins.coinmob.CoinMob;
import me.aglerr.mobcoins.utils.ItemManager;
import me.aglerr.mobcoins.utils.libs.Common;
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
    private ItemStack itemStack;

    public MobCoinsSpawnEvent(LivingEntity entity, CoinMob coinMob, double amountToDrop){
        this.entity = entity;
        this.coinMob = coinMob;
        this.amountToDrop = amountToDrop;
        this.itemStack = ItemManager.createMobCoinItem(amountToDrop);

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
     * Get entity that is involved in this event
     *
     * @return entity that is involved in this event
     */
    public LivingEntity getEntity() {
        return entity;
    }

    /**
     * Get the CoinMob object from this event
     *
     * @return {@link me.aglerr.mobcoins.coinmob.CoinMob}
     */
    public CoinMob getCoinMob() {
        return coinMob;
    }

    /**
     * Get the amount of mobcoins that will drop
     *
     * @return amount of mobcoins that will drop
     */
    public double getAmountToDrop() {
        return amountToDrop;
    }

    /**
     * Set amount of mobcoins that will drop
     *
     * @param amount the amount mobcoins that will drop
     */
    public void setAmountToDrop(double amount){
        this.amountToDrop = amount;
        this.itemStack = ItemManager.createMobCoinItem(amount);
    }

    /**
     * Get the itemstack that will be dropped
     *
     * @return the itemstack that will be dropped
     */
    public ItemStack getItemStack(){
        return itemStack;
    }

}
