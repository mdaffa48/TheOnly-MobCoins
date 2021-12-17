package me.aglerr.mobcoins.managers.managers;

import me.aglerr.mclibs.libs.Common;
import me.aglerr.mclibs.libs.Debug;
import me.aglerr.mclibs.libs.Executor;
import me.aglerr.mclibs.mysql.SQLHelper;
import me.aglerr.mobcoins.MobCoins;
import me.aglerr.mobcoins.PlayerData;
import me.aglerr.mobcoins.configs.ConfigValue;
import me.aglerr.mobcoins.database.SQLDatabaseInitializer;
import me.aglerr.mobcoins.managers.Manager;
import me.aglerr.mobcoins.objects.NotificationUser;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.SQLException;
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

    public void load(PlayerJoinEvent event){
        NotificationManager notificationManager = plugin.getManagerHandler().getNotificationManager();
        String name = event.getPlayer().getName();
        String uuid = event.getPlayer().getUniqueId().toString();

        String query = "SELECT * FROM `" + SQLDatabaseInitializer.MOBCOINS_TABLE + "` WHERE uuid=\"" + uuid + "\";";
        SQLHelper.executeQuery(query,
                resultSet -> {
                    PlayerData playerData;
                    if(resultSet.next()){
                        double coins = resultSet.getDouble("coins");
                        playerData = new PlayerData(uuid, name, coins);

                        String notification = resultSet.getString("notification");
                        NotificationUser notificationUser = new NotificationUser(UUID.fromString(uuid));
                        if(notification != null){
                            notificationUser.unwrapOptions(notification);
                        }
                        notificationManager.addUser(notificationUser);
                    } else {
                        playerData = new PlayerData(uuid, name, ConfigValue.STARTING_BALANCE);
                        NotificationUser notificationUser = new NotificationUser(UUID.fromString(uuid));
                        notificationManager.addUser(notificationUser);
                    }
                    this.playerDataMap.put(uuid, playerData);
                    this.valueModify.put(uuid, playerData.clone());

                    Common.sendMessage(event.getPlayer(), ConfigValue.MESSAGES_FINISHED_LOAD_DATA
                            .replace("{prefix}", ConfigValue.PREFIX));
                    Debug.send("Successfully loaded " + event.getPlayer().getName() + " data. (Coins: " + playerData.getCoins() + ")");
                }
        );
    }

    public void save(Player player){
        String uuid = player.getUniqueId().toString();
        PlayerData playerData = this.playerDataMap.get(uuid);
        PlayerData valueModify = this.valueModify.get(uuid);
        Debug.send("Saving " + player.getName() + " data...");
        if (playerData == null) {
            Debug.send("Returned because Player Data is null");
            return;
        }

        if (valueModify == null) {
            Debug.send("Returned because Value Modify is null");
            return;
        }
        NotificationManager notificationManager = plugin.getManagerHandler().getNotificationManager();
        NotificationUser notificationUser = notificationManager.getNotificationUser(uuid);
        Executor.async(() -> playerData.save(notificationUser));
        this.playerDataMap.remove(uuid);
        this.valueModify.remove(uuid);
        notificationManager.removeUser(notificationUser);
        Debug.send("Saving tasks is completed");
    }

    public List<PlayerData> getMobcoinsTop(){
        if(leaderboard == null){
            Executor.async(this::updateLeaderboard);
        }
        return leaderboard;
    }

    public void updateLeaderboard(){
        if(!ConfigValue.LEADERBOARD_ENABLE){
            return;
        }
        Debug.send("Updating top mobcoins leaderboard!");
        List<PlayerData> playerDataList = new ArrayList<>();
        SQLHelper.executeQuery("SELECT * FROM `" + SQLDatabaseInitializer.MOBCOINS_TABLE + "`;",
                resultSet -> {
                    while(resultSet.next()){
                        String uuid = resultSet.getString("uuid");
                        String name = resultSet.getString("name");
                        double coins = resultSet.getDouble("coins");

                        if(playerDataMap.containsKey(uuid)){
                            playerDataList.add(playerDataMap.get(uuid).clone());
                        } else {
                            playerDataList.add(new PlayerData(uuid, name, coins));
                        }
                    }
                }
        );
        playerDataList.sort((data1, data2) -> {
            Float d1 = (float) data1.getCoins();
            Float d2 = (float) data2.getCoins();

            return d2.compareTo(d1);
        });
        leaderboard = playerDataList;
    }

    @Override
    public void load() {
        int timeAndDelay = (20 * ConfigValue.AUTO_SAVE_INTERVAL);
        Executor.asyncTimer(timeAndDelay, timeAndDelay, () -> {
            NotificationManager notificationManager = plugin.getManagerHandler().getNotificationManager();
            if(!ConfigValue.AUTO_SAVE_ENABLED) {
                return;
            }
            int totalSaved = 0;
            try{
                Connection connection = SQLHelper.getConnection();
                for (PlayerData playerData : this.playerDataMap.values()) {
                    NotificationUser notificationUser = notificationManager.getNotificationUser(playerData.getUUID());
                    connection.prepareStatement("REPLACE INTO `" + SQLDatabaseInitializer.MOBCOINS_TABLE + "` VALUES (" +
                            "\"" + playerData.getUUID() + "\", " +
                            "\"" + playerData.getName() + "\", " +
                            playerData.getCoins() + ", " +
                            "\"" + notificationUser.wrapOptions() + "\"" + ");"
                    );
                    totalSaved++;
                }
                connection.close();
            } catch (SQLException ex){
                Common.log("&cFailed to execute auto-save tasks!");
                ex.printStackTrace();
            }
            if(ConfigValue.AUTO_SAVE_SEND_MESSAGE){
                Common.log("&rSuccessfully saved " + totalSaved + " player data!");
            }
        });
        // Start the leaderboard update task too
        Executor.asyncTimer(0L, 20L * ConfigValue.LEADERBOARD_UPDATE_EVERY, this::updateLeaderboard);
    }

    @Override
    public void save() {
        NotificationManager notificationManager = plugin.getManagerHandler().getNotificationManager();
        Debug.send("Trying to save all players data");
        for(String uuid : this.playerDataMap.keySet()){
            PlayerData playerData = this.playerDataMap.get(uuid);
            NotificationUser notificationUser = notificationManager.getNotificationUser(uuid);

            playerData.save(notificationUser);
        }

        /*try(Connection connection = database.getConnection()){
            for(String uuid : this.playerDataMap.keySet()){
                PlayerData playerData = this.playerDataMap.get(uuid);
                PlayerData valueModify = this.valueModify.get(uuid);
                NotificationUser notificationUser = notificationManager.getNotificationUser(uuid);

                if(playerData.getCoins() == valueModify.getCoins()){
                    Debug.send("Not saving " + uuid + "data (Reason: coins amount the same)");
                    continue;
                }

                Debug.send("Trying to save " + uuid + " data");

                String identifier = Bukkit.getOnlineMode() ? "WHERE uuid=?" : "WHERE name=?";
                String getRowCommand = "SELECT * FROM {table} " + identifier
                        .replace("{table}", database.getTable());

                try(PreparedStatement statement = connection.prepareStatement(getRowCommand)){
                    if(Bukkit.getOnlineMode()){
                        statement.setString(1, playerData.getUUID());
                    } else {
                        statement.setString(1, playerData.getName());
                    }
                    try(ResultSet resultSet = statement.executeQuery()){
                        if(resultSet.next()){

                            String updateCommand = "UPDATE {table} SET coins=?, notification=? " + identifier
                                    .replace("{table}", database.getTable());

                            try(PreparedStatement updateStatement = connection.prepareStatement(updateCommand)){
                                updateStatement.setString(1, String.valueOf(playerData.getCoins()));
                                updateStatement.setString(2, notificationUser.wrapOptions());
                                if(Bukkit.getOnlineMode()){
                                    updateStatement.setString(3, playerData.getUUID());
                                } else {
                                    updateStatement.setString(3, playerData.getName());
                                }
                                updateStatement.executeUpdate();
                                Debug.send("Updating " + playerData.getUUID() + " data (coins: " + playerData.getCoins() + ")");
                            }

                        } else {

                            String insertCommand = "INSERT INTO {table} (uuid, name, coins, notification) VALUES (?,?,?);"
                                    .replace("{table}", database.getTable());

                            try(PreparedStatement insertStatement = connection.prepareStatement(insertCommand)){
                                insertStatement.setString(1, playerData.getUUID());
                                insertStatement.setString(2, playerData.getName());
                                insertStatement.setString(3, String.valueOf(playerData.getCoins()));
                                insertStatement.setString(4, notificationUser.wrapOptions());
                                insertStatement.execute();
                                Debug.send("Inserting " + playerData.getUUID() + " data (coins: " + playerData.getCoins() + ")");
                            }

                        }
                    }
                }
            }
        } catch (SQLException e) {
            Common.log(ChatColor.RED, "Error saving all players data!");
            e.printStackTrace();
            return;
        }*/
        Debug.send("All players data are successfully saved");
    }

}
