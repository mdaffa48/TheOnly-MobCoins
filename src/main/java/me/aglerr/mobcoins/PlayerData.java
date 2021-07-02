package me.aglerr.mobcoins;

import me.aglerr.lazylibs.libs.Common;
import me.aglerr.mobcoins.database.SQLDatabase;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class PlayerData implements Cloneable {

    private final String uuid;
    private double coins = 0;

    public PlayerData(String uuid) {
        this.uuid = uuid;
    }

    public PlayerData(String uuid, double coins){
        this.uuid = uuid;
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
        return Bukkit.getOfflinePlayer(UUID.fromString(this.uuid)).getName();
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
        return Common.numberFormat(this.coins);
    }

    /**
     * Get Players Rounded Mobcoins Amount
     * e.x: 1,234.8 -> 1,235
     *
     * @return player's rounded mobcoins amount
     */
    public int getCoinsRounded(){
        return (int) Math.round(this.coins);
    }

    /**
     * Get Players Short Formatted Mobcoins Amount
     * e.x: 1.5K, 5.75K, 25K, 100M
     *
     * @return player's short formatted mobcoins amount
     */
    public String getCoinsShortFormat(){
        return Common.shortFormat(this.coins);
    }

    /**
     * Add coins to player data
     *
     * @param amount the amount coins that will be added
     */
    public void addCoins(double amount){
        this.coins = (this.coins + amount);
    }

    /**
     * Reduce coins from player data
     *
     * @param amount the amount coins that will be reduced
     */
    public void reduceCoins(double amount){
        this.coins = (this.coins - amount);
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
    public void save() {
        // Get the database class
        SQLDatabase database = MobCoins.getInstance().getDatabase();
        // Create the sql command
        String command = "SELECT * FROM {table} WHERE `uuid`=?"
                .replace("{table}", database.getTable());
        // Debug to console;
        Common.debug("Start saving player data");
        // Enhanced try/catch connection
        try (Connection connection = database.getConnection()) {
            // Enhanced try/catch prepared statement
            try (PreparedStatement statement = connection.prepareStatement(command)) {
                // Set the first argument to the uuid
                statement.setString(1, this.uuid);
                // Enhanced try/catch result set
                try (ResultSet resultSet = statement.executeQuery()) {
                    // Check if the uuid exist on the database
                    if (resultSet.next()) {
                        // Update the data
                        database.update(this.uuid, String.valueOf(this.coins));
                    } else {
                        // If the uuid doesn't exist on the database, insert the data
                        database.insert(this.uuid, String.valueOf(this.coins));
                    }
                    // Debug to the console
                    Common.debug("Sucessfully saved " + this.uuid + " data");
                }
            }
        } catch (SQLException e) {
            Common.log(ChatColor.RED, "Error saving player data (UUID: {uuid}, Coins: {coins})"
                    .replace("{uuid}", this.uuid)
                    .replace("{coins}", this.coins + ""));
            e.printStackTrace();
        }
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
