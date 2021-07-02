package me.aglerr.mobcoins.database;

import me.aglerr.lazylibs.libs.Common;
import me.aglerr.mobcoins.MobCoins;
import me.aglerr.mobcoins.configs.ConfigValue;
import org.bukkit.ChatColor;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLDatabase {

    private final String table = "theonly_mobcoins";

    public String host, database, username, password;
    public int port;
    public boolean useSSL;

    public SQLDatabase(MobCoins plugin){
        Common.log(ChatColor.RESET, "Trying to connect to the database...");
        if(ConfigValue.IS_MYSQL){
            Common.log(ChatColor.RESET, "Database type is MySQL.");
            try{
                host = ConfigValue.MYSQL_HOST;
                database = ConfigValue.MYSQL_DATABASE;
                username = ConfigValue.MYSQL_USERNAME;
                password = ConfigValue.MYSQL_PASSWORD;
                port = ConfigValue.MYSQL_PORT;
                useSSL = ConfigValue.MYSQL_USESSL;
                Class.forName("com.mysql.jdbc.Driver");
                try(Connection connection = this.getConnection()){
                    String command = "CREATE TABLE IF NOT EXISTS " + this.table + " (uuid Text, coins Text)";
                    try(PreparedStatement statement = connection.prepareStatement(command)){
                        statement.execute();
                    }
                }
                Common.log(ChatColor.GREEN, "MySQL connected!");
            } catch (Exception e){
                Common.log(ChatColor.RED, "There is an error connecting with the database!");
                e.printStackTrace();
            }
            return;
        }

        Common.log(ChatColor.RESET, "Database type is SQLite.");
        try{
            File databaseFile = new File(plugin.getDataFolder(), "database.db");
            if(!databaseFile.exists()){
                databaseFile.createNewFile();
            }
            Class.forName("org.sqlite.JDBC");
            try(Connection connection = this.getConnection()){
                String command = "CREATE TABLE IF NOT EXISTS " + this.table + " (uuid Text, coins Text)";
                try(PreparedStatement statement = connection.prepareStatement(command)){
                    statement.execute();
                }
            }
            Common.log(ChatColor.GREEN, "SQLite connected!");
        } catch(Exception e){
            Common.log(ChatColor.RED, "There is an error connecting with the database!");
            e.printStackTrace();
        }
    }

    public String getTable(){
        return table;
    }

    public Connection getConnection() throws SQLException {
        if(ConfigValue.IS_MYSQL){
            String url = "jdbc:mysql://{host}:{port}/{database}?verifyServerCertificate=false&useSSL={useSSL}"
                    .replace("{host}", this.host)
                    .replace("{port}", String.valueOf(this.port))
                    .replace("{database}", this.database)
                    .replace("{useSSL}", String.valueOf(this.useSSL));
            return DriverManager.getConnection(url, this.username, this.password);
        }

        return DriverManager.getConnection("jdbc:sqlite:plugins/TheOnly-MobCoins/database.db");
    }

    public void insert(String uuid, String coins){
        String command = "INSERT INTO " + this.table + " " +
                         "(uuid, coins) VALUES (?, ?);";

        try(Connection connection = this.getConnection()){
            try(PreparedStatement statement = connection.prepareStatement(command)){
                statement.setString(1, uuid);
                statement.setString(2, coins);
                statement.execute();
            }
        } catch (SQLException e){
            Common.log(ChatColor.RED,
                    "Error while inserting data.",
                    "UUID: " + uuid,
                    "Coins: " + coins
            );
            e.printStackTrace();
        }
    }

    public void update(String uuid, String coins){
        String command = "UPDATE " + this.table + " " +
                         "SET coins = ? " +
                         "WHERE uuid = ?";

        try(Connection connection = this.getConnection()){
            try(PreparedStatement statement = connection.prepareStatement(command)){
                statement.setString(1, coins);
                statement.setString(2, uuid);
                statement.executeUpdate();
            }
        } catch (SQLException e){
            Common.log(ChatColor.RED,
                    "Error while updating data.",
                    "UUID: " + uuid,
                    "Coins: " + coins
            );
            e.printStackTrace();
        }
    }

}
