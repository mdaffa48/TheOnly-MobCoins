package me.aglerr.mobcoins.database;

import me.aglerr.mclibs.hikaricp.HikariConfig;
import me.aglerr.mclibs.libs.Common;
import me.aglerr.mclibs.mysql.HikariConfigBuilder;
import me.aglerr.mclibs.mysql.SQLHelper;
import me.aglerr.mobcoins.configs.ConfigValue;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class SQLDatabaseInitializer {

    private static final SQLDatabaseInitializer instance = new SQLDatabaseInitializer();

    public static final String MOBCOINS_TABLE = "mobcoins_data";

    public static SQLDatabaseInitializer getInstance(){
        return instance;
    }

    public void init(JavaPlugin plugin){
        HikariConfig config;
        if(ConfigValue.IS_MYSQL){
            config = new HikariConfigBuilder()
                    .setHost(ConfigValue.MYSQL_HOST)
                    .setDatabase(ConfigValue.MYSQL_DATABASE)
                    .setUser(ConfigValue.MYSQL_USERNAME)
                    .setPassword(ConfigValue.MYSQL_PASSWORD)
                    .setPoolName("TheOnlyMobcoins Pool")
                    .setPort(ConfigValue.MYSQL_PORT)
                    .setUseSSL(ConfigValue.MYSQL_USESSL)
                    .buildMysql();
        } else {
            config = new HikariConfigBuilder()
                    .setPoolName("TheOnlyMobcoins Pool")
                    .setPath("plugins/TheOnly-MobCoins/database.db")
                    .buildSQLite();
        }

        if(!SQLHelper.createConnection(ConfigValue.IS_MYSQL, "plugins/TheOnly-MobCoins/database.db", config)){
            Common.log("Couldn't connect to the database", "Make sure all information is correct!");
            Bukkit.getPluginManager().disablePlugin(plugin);
            return;
        }

        createMobcoinsTable();
    }

    private void createMobcoinsTable(){
        SQLHelper.executeUpdate("CREATE TABLE IF NOT EXISTS " + MOBCOINS_TABLE + " (" +
                "uuid VARCHAR(36) PRIMARY KEY, " +
                "name TEXT, " +
                "coins DOUBLE, " +
                "notification TEXT" +
                ");"
        );
    }

}
