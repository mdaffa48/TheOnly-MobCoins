package me.aglerr.mobcoins.configs;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigValue {

    public static boolean isDebug = false;

    public static boolean IS_MYSQL;
    public static String MYSQL_HOST;
    public static String MYSQL_DATABASE;
    public static String MYSQL_USERNAME;
    public static String MYSQL_PASSWORD;
    public static int MYSQL_PORT;
    public static boolean MYSQL_USESSL;

    public static void initializeValue(FileConfiguration config){
        isDebug = config.getBoolean("debug");

        IS_MYSQL = config.getBoolean("mysql.enabled");
        MYSQL_HOST = config.getString("mysql.host");
        MYSQL_DATABASE = config.getString("mysql.database");
        MYSQL_USERNAME = config.getString("mysql.username");
        MYSQL_PASSWORD = config.getString("mysql.password");
        MYSQL_PORT = config.getInt("mysql.port");
        MYSQL_USESSL = config.getBoolean("mysql.useSSL");

    }

}
