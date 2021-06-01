package me.aglerr.mobcoins.configs;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class ConfigValue {

    public static boolean isDebug = true;
    public static String PREFIX;

    public static boolean IS_MYSQL;
    public static String MYSQL_HOST;
    public static String MYSQL_DATABASE;
    public static String MYSQL_USERNAME;
    public static String MYSQL_PASSWORD;
    public static int MYSQL_PORT;
    public static boolean MYSQL_USESSL;

    public static double STARTING_BALANCE;

    public static boolean PHYSICAL_MOBCOIN;
    public static boolean DISABLE_MOBCOIN_FROM_SPAWNER;

    public static String MESSAGES_NO_PERMISSION;
    public static String MESSAGES_BALANCE;
    public static String MESSAGES_BALANCE_OTHERS;
    public static String MESSAGES_PLAYER_NOT_EXISTS;
    public static String MESSAGES_NOT_INT;
    public static String MESSAGES_SET_COINS;
    public static String MESSAGES_SET_COINS_OTHERS;
    public static String MESSAGES_REMOVE_COINS;
    public static String MESSAGES_REMOVE_COINS_OTHERS;
    public static String MESSAGES_ADD_COINS;
    public static String MESSAGES_ADD_COINS_OTHERS;
    public static String MESSAGES_INVALID_TYPE;
    public static String MESSAGES_NOT_ENOUGH_COINS;
    public static String MESSAGES_SELF_PAY;
    public static String MESSAGES_PAY;
    public static String MESSAGES_PAY_OTHERS;
    public static String MESSAGES_REDEEM;

    public static List<String> MESSAGES_HELP;
    public static List<String> MESSAGES_HELP_ADMIN;
    public static List<String> DISABLED_WORLDS;

    public static String USER_ID = "%%__USER__%%";

    // Mobcoins Item
    public static String MOBCOINS_ITEM_MATERIAL;
    public static String MOBCOINS_ITEM_NAME;
    public static boolean MOBCOINS_ITEM_GLOW;
    public static List<String> MOBCOINS_ITEM_LORE;

    // Salary Mode
    public static boolean SALARY_MODE_ENABLED;
    public static int SALARY_MODE_ANNOUNCE_EVERY;
    public static boolean SALARY_MODE_RECEIVE_AFTER_MESSAGE;
    public static List<String> SALARY_MODE_MESSAGES;

    public static void initializeValue(FileConfiguration config){
        PREFIX = config.getString("messages.prefix");

        IS_MYSQL = config.getBoolean("mysql.enabled");
        MYSQL_HOST = config.getString("mysql.host");
        MYSQL_DATABASE = config.getString("mysql.database");
        MYSQL_USERNAME = config.getString("mysql.username");
        MYSQL_PASSWORD = config.getString("mysql.password");
        MYSQL_PORT = config.getInt("mysql.port");
        MYSQL_USESSL = config.getBoolean("mysql.useSSL");

        MESSAGES_NO_PERMISSION = config.getString("messages.noPermission");
        MESSAGES_BALANCE = config.getString("messages.balance");
        MESSAGES_BALANCE_OTHERS = config.getString("messages.balanceOthers");
        MESSAGES_PLAYER_NOT_EXISTS = config.getString("messages.playerNotExists");
        MESSAGES_NOT_INT = config.getString("messages.notInt");
        MESSAGES_SET_COINS = config.getString("messages.setCoins");
        MESSAGES_SET_COINS_OTHERS = config.getString("messages.setCoinsOthers");
        MESSAGES_REMOVE_COINS = config.getString("messages.removeCoins");
        MESSAGES_REMOVE_COINS_OTHERS = config.getString("messages.removeCoinsOthers");
        MESSAGES_ADD_COINS = config.getString("messages.addCoins");
        MESSAGES_ADD_COINS_OTHERS = config.getString("messages.addCoinsOthers");
        MESSAGES_INVALID_TYPE = config.getString("messages.invalidType");
        MESSAGES_NOT_ENOUGH_COINS = config.getString("messages.notEnoughCoins");
        MESSAGES_SELF_PAY = config.getString("messages.selfPay");
        MESSAGES_PAY = config.getString("messages.pay");
        MESSAGES_PAY_OTHERS = config.getString("messages.payOthers");
        MESSAGES_REDEEM = config.getString("messages.redeem");

        MESSAGES_HELP = config.getStringList("messages.help");
        MESSAGES_HELP_ADMIN = config.getStringList("messages.helpAdmin");
        DISABLED_WORLDS = config.getStringList("disabledWorlds");

        STARTING_BALANCE = config.getDouble("starting-balance");

        PHYSICAL_MOBCOIN = config.getBoolean("mobcoins.physicalMobCoin");
        DISABLE_MOBCOIN_FROM_SPAWNER = config.getBoolean("mobcoins.disableMobCoinFromSpawner");

        MOBCOINS_ITEM_MATERIAL = config.getString("mobcoinsItem.material");
        MOBCOINS_ITEM_NAME = config.getString("mobcoinsItem.name");
        MOBCOINS_ITEM_GLOW = config.getBoolean("mobcoinsItem.glow");
        MOBCOINS_ITEM_LORE = config.getStringList("mobcoinsItem.lore");

        SALARY_MODE_ENABLED = config.getBoolean("salaryMode.enabled");
        SALARY_MODE_ANNOUNCE_EVERY = config.getInt("salaryMode.announceEvery");
        SALARY_MODE_RECEIVE_AFTER_MESSAGE = config.getBoolean("salaryMode.receiveAfterMessage");
        SALARY_MODE_MESSAGES = config.getStringList("salaryMode.messages");

    }

}
