package me.aglerr.mobcoins.managers.managers;

import me.aglerr.mobcoins.MobCoins;
import me.aglerr.mobcoins.PlayerData;
import me.aglerr.mobcoins.database.SQLDatabase;
import me.aglerr.mobcoins.managers.Manager;
import me.aglerr.mobcoins.utils.Common;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class PlayerDataManager implements Manager {

    private final Map<String, PlayerData> playerDataMap = new HashMap<>();

    private final MobCoins plugin;

    public PlayerDataManager(MobCoins plugin){
        this.plugin = plugin;
    }

    @Override
    public void load() {
        SQLDatabase database = plugin.getDatabase();

        String command = "SELECT * FROM {table}"
                .replace("{table}", database.getTable());

        Common.runTaskAsynchronously(() -> {
            try(Connection connection = database.getConnection()){
                try(PreparedStatement statement = connection.prepareStatement(command)){
                    try(ResultSet resultSet = statement.executeQuery()){
                        while(resultSet.next()){

                            String uuid = resultSet.getString("uuid");
                            String coins = resultSet.getString("coins");

                            PlayerData playerData = new PlayerData(uuid);
                            playerData.modifyCoins(PlayerData.CoinAction.SET, Double.parseDouble(coins));

                            this.playerDataMap.put(uuid, playerData);

                        }
                    }
                }
            } catch (SQLException e){
                Common.error(true, "Error while loading all players data!");
                e.printStackTrace();
            }
        });
    }

    @Override
    public void save() {
        SQLDatabase database = plugin.getDatabase();
        for(String uuid : this.playerDataMap.keySet()){
            this.playerDataMap.get(uuid).save(database);
        }
    }

}
