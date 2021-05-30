package me.aglerr.mobcoins;

import me.aglerr.mobcoins.database.SQLDatabase;
import me.aglerr.mobcoins.utils.Common;
import org.bukkit.Bukkit;
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

    public String getUUID() {
        return uuid;
    }

    public String getName() {
        return Bukkit.getOfflinePlayer(UUID.fromString(this.uuid)).getName();
    }

    public double getCoins() {
        return coins;
    }

    public String getCoinsFormatted() {
        return Common.getDecimalFormat().format(this.coins);
    }

    public double getCoinsRounded(){
        return Math.round(this.coins);
    }

    public String getCoinsShortFormat(){
        return Common.shortFormat(this.coins);
    }

    public void addCoins(double amount){
        this.coins = (this.coins + amount);
    }

    public void reduceCoins(double amount){
        this.coins = (this.coins - amount);
    }

    public void setCoins(double amount){
        this.coins = amount;
    }

    public void save(SQLDatabase database) {
        String command = "SELECT * FROM {table} WHERE `UUID`=?"
                .replace("{table}", database.getTable());

        Common.debug(true, "Start saving player data");

        try (Connection connection = database.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(command)) {
                statement.setString(1, this.uuid);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        database.update(this.uuid, String.valueOf(this.coins));
                    } else {
                        database.insert(this.uuid, String.valueOf(this.coins));
                    }
                    Common.debug(true, "Sucessfully saved " + this.uuid + " data");
                }
            }
        } catch (SQLException e) {
            Common.error(true,
                    "Error saving player data!",
                    "UUID: " + this.uuid,
                    "Coins: " + this.coins
            );
            e.printStackTrace();
        }

    }

    @NotNull
    public PlayerData clone() {
        try {
            return (PlayerData) super.clone();
        } catch (CloneNotSupportedException var2) {
            throw new Error(var2);
        }
    }

}
