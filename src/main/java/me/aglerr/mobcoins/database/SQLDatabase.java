package me.aglerr.mobcoins.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.aglerr.lazylibs.libs.Common;
import me.aglerr.mobcoins.MobCoins;
import me.aglerr.mobcoins.configs.ConfigValue;
import org.bukkit.ChatColor;

import java.io.File;
import java.sql.*;

public class SQLDatabase {

    private HikariDataSource hikari;

    private final String table = "theonly_mobcoins";

    public String host, database, username, password;
    public int port;
    public boolean useSSL;

    private Connection connection;

    public SQLDatabase(MobCoins plugin){
        hikari = new HikariDataSource();

        Common.log(ChatColor.RESET, "Trying to connect to the database...");
        if(ConfigValue.IS_MYSQL){
            Common.log(ChatColor.RESET, "Database type is MySQL.");
            try{

                hikari.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
                hikari.addDataSourceProperty("serverName", ConfigValue.MYSQL_HOST);
                hikari.addDataSourceProperty("port", ConfigValue.MYSQL_PORT);
                hikari.addDataSourceProperty("databaseName", ConfigValue.MYSQL_DATABASE);
                hikari.addDataSourceProperty("user", ConfigValue.MYSQL_USERNAME);
                hikari.addDataSourceProperty("password", ConfigValue.MYSQL_PASSWORD);
                hikari.addDataSourceProperty("useSSL", ConfigValue.MYSQL_USESSL);
                hikari.addDataSourceProperty("prepStmtCacheSize", 250);
                hikari.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);

                /*host = ConfigValue.MYSQL_HOST;
                database = ConfigValue.MYSQL_DATABASE;
                username = ConfigValue.MYSQL_USERNAME;
                password = ConfigValue.MYSQL_PASSWORD;
                port = ConfigValue.MYSQL_PORT;
                useSSL = ConfigValue.MYSQL_USESSL;
                Class.forName("com.mysql.jdbc.Driver");*/

                String command = "CREATE TABLE IF NOT EXISTS " + this.table + " (uuid Text, coins Text)";
                try(Connection connection = this.getConnection();
                    PreparedStatement statement = connection.prepareStatement(command)){
                    statement.execute();
                }

                updateDatabase();

                Common.log(ChatColor.GREEN, "MySQL connected!");
            } catch (Exception e){
                Common.log(ChatColor.RED, "There is an error connecting with the database!");
                e.printStackTrace();
            }

        } else {
            Common.log(ChatColor.RESET, "Database type is SQLite.");
            try{
                File databaseFile = new File(plugin.getDataFolder(), "database.db");
                if(!databaseFile.exists()){
                    databaseFile.createNewFile();
                }

                HikariConfig hikariConfig = new HikariConfig();
                hikariConfig.setPoolName("MobcoinsSQLitePool");
                hikariConfig.setDriverClassName("org.sqlite.JDBC");
                hikariConfig.setJdbcUrl("jdbc:sqlite:plugins/TheOnly-MobCoins/database.db");
                hikariConfig.setConnectionTestQuery("SELECT 1");
                hikariConfig.setMaxLifetime(60000);
                hikariConfig.setIdleTimeout(45000);
                hikariConfig.setMaximumPoolSize(1);

                hikari = new HikariDataSource(hikariConfig);

                // Class.forName("org.sqlite.JDBC");
                String command = "CREATE TABLE IF NOT EXISTS " + this.table + " (uuid Text, coins Text)";
                try(Connection connection = this.getConnection();
                    PreparedStatement statement = connection.prepareStatement(command)){
                    statement.execute();
                }

                updateDatabase();

                Common.log(ChatColor.GREEN, "SQLite connected!");
            } catch(Exception e){
                Common.log(ChatColor.RED, "There is an error connecting with the database!");
                e.printStackTrace();
            }
        }
    }

    public String getTable(){
        return table;
    }

    public Connection getConnection() throws SQLException{
        return hikari.getConnection();
        /*if(connection == null || connection.isClosed()){
            connection = openConnection();
        }
        return connection;*/
    }

    public Connection openConnection() throws SQLException {
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

    /**
     * This method is to update database below version 1.0.7
     */
    private void updateDatabase() throws SQLException {
        DatabaseMetaData dmd = getConnection().getMetaData();
        ResultSet resultSet = dmd.getColumns(null, null, this.table, "notification");
        if(!resultSet.next()){
            Common.log(ChatColor.RED, "You are using the old database for this plugin, begin updating...");
            String command = "ALTER TABLE " + this.table + " ADD notification text;";

            try(Connection connection = this.getConnection();
                PreparedStatement statement = connection.prepareStatement(command)){
                statement.execute();
            }

            Common.log(ChatColor.GREEN, "Successfully updated the database, enjoy!");
        }
    }

    public void insert(String uuid, String coins, String notification){
        String command = "INSERT INTO " + this.table + " " +
                         "(uuid, coins, notification) VALUES (?, ?, ?);";

        try(Connection connection = this.getConnection()){
            try(PreparedStatement statement = connection.prepareStatement(command)){
                statement.setString(1, uuid);
                statement.setString(2, coins);
                statement.setString(3, notification);
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

    public void update(String uuid, String coins, String notification){
        String command = "UPDATE " + this.table + " " +
                         "SET coins = ?, " +
                             "notification = ? " +
                         "WHERE uuid = ?";

        try(Connection connection = this.getConnection()){
            try(PreparedStatement statement = connection.prepareStatement(command)){
                statement.setString(1, coins);
                statement.setString(2, notification);
                statement.setString(3, uuid);
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

    public void onDisable(){
        if(hikari != null){
            hikari.close();
        }
    }

}
