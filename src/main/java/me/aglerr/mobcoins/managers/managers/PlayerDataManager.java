package me.aglerr.mobcoins.managers.managers;

import me.aglerr.lazylibs.libs.Common;
import me.aglerr.lazylibs.libs.Executor;
import me.aglerr.mobcoins.MobCoins;
import me.aglerr.mobcoins.PlayerData;
import me.aglerr.mobcoins.configs.ConfigValue;
import me.aglerr.mobcoins.database.SQLDatabase;
import me.aglerr.mobcoins.managers.Manager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.jetbrains.annotations.Nullable;

import java.sql.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerDataManager implements Manager {

    private final Map<String, PlayerData> playerDataMap = new ConcurrentHashMap<>();
    private final Map<String, PlayerData> valueModify = new ConcurrentHashMap<>();
    private List<PlayerData> leaderboard;

    private final MobCoins plugin;

    public PlayerDataManager(MobCoins plugin) {
        this.plugin = plugin;
    }

    @Nullable
    public PlayerData getPlayerData(Player player) {
        return getPlayerData(player.getUniqueId().toString());
    }

    @Nullable
    public PlayerData getPlayerData(String uuid){
        return this.playerDataMap.get(uuid);
    }

    public void overrideData(PlayerData playerData){
        playerDataMap.put(playerData.getUUID(), playerData);
    }

    public void handleLoginEvent(PlayerJoinEvent event) {
        SQLDatabase database = plugin.getDatabase();
        String uuid = event.getPlayer().getUniqueId().toString();

        String command = "SELECT * FROM `" + database.getTable() + "` " +
                         "WHERE `uuid`=?";

        Common.debug("Loading " + event.getPlayer().getName() + " data");

        try (Connection connection = database.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(command)) {
                statement.setString(1, uuid);
                try (ResultSet resultSet = statement.executeQuery()) {

                    PlayerData playerData;
                    if (resultSet.next()) {
                        double coins = Double.parseDouble(resultSet.getString("coins"));
                        playerData = new PlayerData(uuid, coins);
                    } else {
                        playerData = new PlayerData(uuid, ConfigValue.STARTING_BALANCE);
                    }

                    this.playerDataMap.put(uuid, playerData);
                    this.valueModify.put(uuid, playerData.clone());

                    event.getPlayer().sendMessage(Common.color(ConfigValue.MESSAGES_FINISHED_LOAD_DATA
                            .replace("{prefix}", ConfigValue.PREFIX)));

                    Common.debug("Successfully loaded " + event.getPlayer().getName() + " data. (Coins: " + playerData.getCoins() + ")");
                }
            }
        } catch (SQLException e) {
            Common.log(ChatColor.RED, "Error while loading " + event.getPlayer().getName() + " data!");
            e.printStackTrace();
        }
    }

    public void forceSavePlayerData(Player player) {
        String uuid = player.getUniqueId().toString();
        PlayerData playerData = this.playerDataMap.get(uuid);
        PlayerData valueModify = this.valueModify.get(uuid);

        Common.debug("Saving " + player.getName() + " data");

        if (playerData == null) {
            Common.debug("Returned because Player Data is null");
            return;
        }

        if (valueModify == null) {
            Common.debug("Returned because Value Modify is null");
            return;
        }

        double coins1 = playerData.getCoins();
        double coins2 = valueModify.getCoins();

        if(coins1 != coins2){
            Common.debug("Saving data to the database because value is not the same");
            Executor.async(playerData::save);
        }

        this.playerDataMap.remove(uuid);
        this.valueModify.remove(uuid);

        Common.debug("Saving tasks is completed");
    }

    /*public List<PlayerData> getMobcoinsTop() {
        List<PlayerData> convert = new ArrayList<>(this.playerDataMap.values());
        convert.sort((data1, data2) -> {
            Float d1 = (float) data1.getCoins();
            Float d2 = (float) data2.getCoins();

            return d2.compareTo(d1);
        });
        return convert;
    }*/

    public List<PlayerData> getMobcoinsTop(){
        if(leaderboard == null){
            Executor.async(this::updateLeaderboard);
        }
        return leaderboard;
    }

    public void updateLeaderboard(){
        SQLDatabase database = plugin.getDatabase();

        Common.debug("Updating top mobcoins leaderboard!");

        String command = "SELECT * FROM '" + database.getTable() + "'";
        List<PlayerData> playerDataList = new ArrayList<>();

        try(Connection connection = database.getConnection()){
            try(PreparedStatement statement = connection.prepareStatement(command)){
                try(ResultSet resultSet = statement.executeQuery()){
                    while(resultSet.next()){
                        String uuid = resultSet.getString("uuid");
                        double coins = Double.parseDouble(resultSet.getString("coins"));

                        playerDataList.add(new PlayerData(uuid, coins));
                    }
                }
            }
        } catch (SQLException ex){
            Common.log(ChatColor.RED, "There is an error while updating leaderboard!");
            ex.printStackTrace();
        }

        playerDataList.forEach(playerData -> {
            System.out.println(playerData.getName() + " | " + playerData.getCoins());
        });

        playerDataList.sort((data1, data2) -> {
            Float d1 = (float) data1.getCoins();
            Float d2 = (float) data2.getCoins();

            return d2.compareTo(d1);
        });

        leaderboard = playerDataList;
    }

    @Override
    public void load() {

        SQLDatabase database = plugin.getDatabase();
        int timeAndDelay = (20 * ConfigValue.AUTO_SAVE_INTERVAL);
        Executor.asyncTimer(timeAndDelay, timeAndDelay, () -> {
            if(!ConfigValue.AUTO_SAVE_ENABLED) return;

            int totalSaved = 0;

            try(Connection connection = database.getConnection()){
                for(String uuid : this.playerDataMap.keySet()){
                    PlayerData playerData = this.playerDataMap.get(uuid);

                    String rowCommand = "SELECT * FROM {table} WHERE uuid=?"
                            .replace("{table}", database.getTable());

                    try(PreparedStatement statement = connection.prepareStatement(rowCommand)){
                        statement.setString(1, playerData.getUUID());
                        try(ResultSet resultSet = statement.executeQuery()){
                            if(resultSet.next()){

                                String updateCommand = "UPDATE {table} SET coins=? WHERE uuid=?"
                                        .replace("{table}", database.getTable());

                                try(PreparedStatement updateStatement = connection.prepareStatement(updateCommand)){
                                    updateStatement.setString(1, String.valueOf(playerData.getCoins()));
                                    updateStatement.setString(2, playerData.getUUID());
                                    updateStatement.executeUpdate();
                                    Common.debug("Updating " + playerData.getName() + " data (coins: " + playerData.getCoins() + ")");
                                }

                            } else {

                                String insertCommand = "INSERT INTO {table} (uuid, coins) VALUES (?,?);"
                                        .replace("{table}", database.getTable());

                                try(PreparedStatement insertStatement = connection.prepareStatement(insertCommand)){
                                    insertStatement.setString(1, playerData.getUUID());
                                    insertStatement.setString(2, String.valueOf(playerData.getCoins()));
                                    insertStatement.execute();
                                    Common.debug("Inserting " + playerData.getName() + " data (coins: " + playerData.getCoins() + ")");
                                }
                            }
                        }
                    }
                    totalSaved++;
                }

            } catch (SQLException e){
                Common.log(ChatColor.RED, "Failed to save all players data (action: auto-save)");
                e.printStackTrace();
            }

            if(ConfigValue.AUTO_SAVE_SEND_MESSAGE){
                Common.log(ChatColor.RESET, "Successfully saved " + totalSaved + " player data!");
            }

        });

        // Start the leaderboard update task too
        Executor.asyncTimer(0L, 20L * ConfigValue.LEADERBOARD_UPDATE_EVERY, this::updateLeaderboard);

    }

    @Override
    public void save() {
        SQLDatabase database = plugin.getDatabase();
        Common.debug("Trying to save all players data");
        try(Connection connection = database.getConnection()){
            for(String uuid : this.playerDataMap.keySet()){
                PlayerData playerData = this.playerDataMap.get(uuid);
                PlayerData valueModify = this.valueModify.get(uuid);

                if(playerData.getCoins() == valueModify.getCoins()){
                    Common.debug("Not saving " + uuid + "data (Reason: coins amount the same)");
                    continue;
                }

                Common.debug("Trying to save " + uuid + " data");

                String getRowCommand = "SELECT * FROM {table} WHERE uuid=?"
                        .replace("{table}", database.getTable());

                try(PreparedStatement statement = connection.prepareStatement(getRowCommand)){
                    statement.setString(1, playerData.getUUID());
                    try(ResultSet resultSet = statement.executeQuery()){
                        if(resultSet.next()){

                            String updateCommand = "UPDATE {table} SET coins=? WHERE uuid=?"
                                    .replace("{table}", database.getTable());

                            try(PreparedStatement updateStatement = connection.prepareStatement(updateCommand)){
                                updateStatement.setString(1, String.valueOf(playerData.getCoins()));
                                updateStatement.setString(2, playerData.getUUID());
                                updateStatement.executeUpdate();
                                Common.debug("Updating " + playerData.getUUID() + " data (coins: " + playerData.getCoins() + ")");
                            }

                        } else {

                            String insertCommand = "INSERT INTO {table} (uuid, coins) VALUES (?,?);"
                                    .replace("{table}", database.getTable());

                            try(PreparedStatement insertStatement = connection.prepareStatement(insertCommand)){
                                insertStatement.setString(1, playerData.getUUID());
                                insertStatement.setString(2, String.valueOf(playerData.getCoins()));
                                insertStatement.execute();
                                Common.debug("Inserting " + playerData.getUUID() + " data (coins: " + playerData.getCoins() + ")");
                            }

                        }
                    }
                }
            }
        } catch (SQLException e) {
            Common.log(ChatColor.RED, "Error saving all players data!");
            e.printStackTrace();
            return;
        }
        Common.debug("All players data are successfully saved");
    }

}
