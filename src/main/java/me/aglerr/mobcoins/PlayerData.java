package me.aglerr.mobcoins;

import me.aglerr.mobcoins.database.SQLDatabase;
import me.aglerr.mobcoins.utils.Common;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class PlayerData {

    private final String uuid;
    private double coins = 0;

    public PlayerData(String uuid){
        this.uuid = uuid;
    }

    public String getUUID() {
        return uuid;
    }

    public String getName(){
        return Bukkit.getOfflinePlayer(UUID.fromString(this.uuid)).getName();
    }

    public double getCoins() {
        return coins;
    }

    public void modifyCoins(CoinAction action, double amount){
        switch(action){
            case ADD:{
                this.coins = (this.coins + amount);
                break;
            }
            case REDUCE:{
                this.coins = (this.coins - amount);
                break;
            }
            case SET:{
                this.coins = amount;
                break;
            }
            default:{
                throw new IllegalStateException("Invalid enum for CoinAction!");
            }
        }
    }

    public void save(SQLDatabase database){
        String command = "SELECT * FROM {table} WHERE `UUID`=?"
                .replace("{table}", database.getTable());

        Common.runTaskAsynchronously(() -> {
            try(Connection connection = database.getConnection()){
                try(PreparedStatement statement = connection.prepareStatement(command)){
                    statement.setString(1, this.uuid);
                    try(ResultSet resultSet = statement.executeQuery()){
                        if(resultSet.next()){
                            database.update(this.uuid, String.valueOf(this.coins));
                        } else {
                            database.insert(this.uuid, String.valueOf(this.coins));
                        }
                    }
                }
            } catch (SQLException e){
                Common.error(true, "Error saving player data!");
                Common.error(true, "UUID: " + this,uuid);
                Common.error(true, "Coins: " + this.coins);
                e.printStackTrace();
            }
        });
    }

    public enum CoinAction{
        ADD,
        REDUCE,
        SET
    }

}
