package me.aglerr.mobcoins;

import me.aglerr.mclibs.mysql.SQLHelper;
import me.aglerr.mobcoins.database.SQLDatabaseInitializer;
import me.aglerr.mobcoins.objects.NotificationUser;
import me.aglerr.mobcoins.utils.Utils;
import org.jetbrains.annotations.NotNull;

public class PlayerData implements Cloneable {

    private final String uuid;
    private final String name;
    private double coins = 0;

    public PlayerData(String uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    public PlayerData(String uuid, String name, double coins){
        this.uuid = uuid;
        this.name = name;
        this.coins = coins;
    }

    /**
     * Get Players UUID
     *
     * @return player's uuid
     */
    public String getUUID() {
        return uuid;
    }

    /**
     * Get Players Name
     *
     * @return player's name
     */
    public String getName() {
        return name;
    }

    /**
     * Get Players Mobcoins Amount
     *
     * @return player's mobcoins amount
     */
    public double getCoins() {
        return coins;
    }

    /**
     * Get Players Formatted Mobcoins Amount
     * e.x: 1,234.56
     *
     * @return player's formatted mobcoins amount
     */
    public String getCoinsFormatted() {
        return Utils.digits(this.coins);
    }

    /**
     * Get Players Rounded Mobcoins Amount
     * e.x: 1,234.8 -> 1,235
     *
     * @return player's rounded mobcoins amount
     */
    public int getCoinsRounded(){
        return (int) this.coins;
    }

    /**
     * Get Players Short Formatted Mobcoins Amount
     * e.x: 1.5K, 5.75K, 25K, 100M
     *
     * @return player's short formatted mobcoins amount
     */
    public String getCoinsShortFormat(){
        return Utils.digits(this.coins);
    }

    /**
     * Add coins to player data
     *
     * @param amount the amount coins that will be added
     */
    public void addCoins(double amount){
        this.coins = this.coins + amount;
    }

    /**
     * Reduce coins from player data
     *
     * @param amount the amount coins that will be reduced
     */
    public void reduceCoins(double amount){
        this.coins = this.coins - amount;
    }

    /**
     * Set amount of coins to player data
     *
     * @param amount the amount coins that will be set
     */
    public void setCoins(double amount){
        this.coins = amount;
    }

    /**
     * Save player data to the database (call it async if can)
     */
    public void save(NotificationUser notificationUser) {
        SQLHelper.executeUpdate("REPLACE INTO `" + SQLDatabaseInitializer.MOBCOINS_TABLE + "` VALUES (" +
                "\"" + this.uuid + "\", " +
                "\"" + this.name + "\", " +
                this.coins + ", " +
                "\"" + notificationUser.wrapOptions() + "\"" + ");"
        );
    }

    /**
     * Clone the current PlayerData object
     * Mainly this method used to compare the mobcoins amount on saving
     * Take a look at {@link me.aglerr.mobcoins.managers.managers.PlayerDataManager}
     *
     * @return cloned PlayerData object
     */
    @NotNull
    public PlayerData clone() {
        try {
            return (PlayerData) super.clone();
        } catch (CloneNotSupportedException var2) {
            throw new Error(var2);
        }
    }

}
