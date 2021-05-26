package me.aglerr.mobcoins.database;

import me.aglerr.mobcoins.MobCoins;
import me.aglerr.mobcoins.configs.ConfigValue;
import me.aglerr.mobcoins.utils.Common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLDatabase {

    private final String table = "theonly_mobcoins";

    public String host, database, username, password;
    public int port;
    public boolean useSSL;

    private final MobCoins plugin;

    public SQLDatabase(MobCoins plugin){
        this.plugin = plugin;

        Common.log(true, "Trying to connect to the database...");
        if(ConfigValue.IS_MYSQL){
            Common.log(true, "Database type is MySQL.");
            try{

                host = ConfigValue.MYSQL_HOST;
                database = ConfigValue.MYSQL_DATABASE;
                username = ConfigValue.MYSQL_USERNAME;
                password = ConfigValue.MYSQL_PASSWORD;
                port = ConfigValue.MYSQL_PORT;
                useSSL = ConfigValue.MYSQL_USESSL;

                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = this.getConnection();

                String command = "CREATE TABLE IF NOT EXISTS " + this.table + " (uuid Text, coins Text)";
                PreparedStatement statement = connection.prepareStatement(command);
                statement.execute();

                Common.log(true, "MySQL connected!");
                statement.close();
                connection.close();

            } catch (Exception e){
                Common.log(true, "There is an error connecting with the database!");
                e.printStackTrace();
            }

        } else {
            Common.log(true, "Database type is SQLite.");
            try{

                Class.forName("org.sqlite.JDBC");
                Connection connection = this.getConnection();

                String command = "CREATE TABLE IF NOT EXISTS " + this.table + " (uuid Text, coins Text)";
                PreparedStatement statement = connection.prepareStatement(command);
                statement.execute();

                Common.log(true, "SQLite connected!");
                statement.close();
                connection.close();

            } catch(Exception e){
                Common.log(true, "There is an error connecting with the database!");
                e.printStackTrace();
            }
        }

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
        try{

            String command = "INSERT INTO {table} (uuid, coins) VALUES ('{uuid}', '{coins}');"
                    .replace("{table}", this.table)
                    .replace("{uuid}", uuid)
                    .replace("{coins}", coins);

            Connection connection = this.getConnection();
            PreparedStatement statement = connection.prepareStatement(command);
            statement.execute();

            statement.close();
            connection.close();

        } catch (SQLException e){
            Common.log(true, "Error while inserting data.");
            Common.log(true, "UUID: " + uuid);
            Common.log(true, "Coins: " + coins);
            e.printStackTrace();
        }

    }

    public void update(String uuid, String coins){
        try{

            String command = "UPDATE {table} SET coins='{coins}' WHERE uuid='{uuid}'"
                    .replace("{table}", this.table)
                    .replace("{uuid}", uuid)
                    .replace("{coins}", coins);

            Connection connection = this.getConnection();
            PreparedStatement statement = connection.prepareStatement(command);
            statement.execute();

            statement.close();
            connection.close();

        } catch (SQLException e){
            Common.log(true, "Error while updating data.");
            Common.log(true, "UUID: " + uuid);
            Common.log(true, "Coins: " + coins);
            e.printStackTrace();
        }
    }

}
