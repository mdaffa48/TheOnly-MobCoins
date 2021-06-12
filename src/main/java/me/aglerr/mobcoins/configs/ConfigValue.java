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
    public static boolean CLOSE_AFTER_PURCHASE;
    public static boolean IS_CONFIRMATION_MENU;

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
    public static String MESSAGES_COINS_RECEIVED;
    public static String MESSAGES_ITEM_OUT_OF_STOCK;
    public static String MESSAGES_PURCHASE_LIMIT_REACHED;

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

    // Shops
    public static String SHOP_BEHAVIOUR;
    public static boolean AUTO_UPDATE_ENABLED;
    public static int AUTO_UPDATE_UPDATE_EVERY;

    // Placeholders
    public static String PLACEHOLDER_UNLIMITED_STOCK;
    public static String PLACEHOLDER_OUT_OF_STOCK;

    // Rotating Shop
    public static int DEFAULT_NORMAL_TIME_RESET;
    public static int DEFAULT_SPECIAL_TIME_RESET;

    // Normal Items Refresh Actions

    // Broadcast Message
    public static boolean NORMAL_IS_BROADCAST_MESSAGE;
    public static List<String> NORMAL_BROADCAST_MESSAGE_MESSAGES;

    // Executes Commands
    public static boolean NORMAL_IS_COMMAND;
    public static List<String> NORMAL_COMMAND_COMMANDS;

    // Special Items Refresh Actions

    // Broadcast Message
    public static boolean SPECIAL_IS_BROADCAST_MESSAGE;
    public static List<String> SPECIAL_BROADCAST_MESSAGE_MESSAGES;

    // Executes Commands
    public static boolean SPECIAL_IS_COMMAND;
    public static List<String> SPECIAL_COMMAND_COMMANDS;


    public static void initializeValue(){
        FileConfiguration config = Config.CONFIG.getConfig();
        FileConfiguration rotatingShop = Config.ROTATING_SHOP_CONFIG.getConfig();

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
        MESSAGES_COINS_RECEIVED = config.getString("messages.coinsReceived");
        MESSAGES_ITEM_OUT_OF_STOCK = config.getString("messages.itemOutOfStock");
        MESSAGES_PURCHASE_LIMIT_REACHED = config.getString("messages.purchaseLimitReached");

        MESSAGES_HELP = config.getStringList("messages.help");
        MESSAGES_HELP_ADMIN = config.getStringList("messages.helpAdmin");
        DISABLED_WORLDS = config.getStringList("disabledWorlds");

        STARTING_BALANCE = config.getDouble("starting-balance");

        PHYSICAL_MOBCOIN = config.getBoolean("mobcoins.physicalMobCoin");
        DISABLE_MOBCOIN_FROM_SPAWNER = config.getBoolean("mobcoins.disableMobCoinFromSpawner");
        CLOSE_AFTER_PURCHASE = config.getBoolean("shops.closeAfterPurchase");
        IS_CONFIRMATION_MENU = config.getBoolean("shops.confirmationMenu");

        MOBCOINS_ITEM_MATERIAL = config.getString("mobcoinsItem.material");
        MOBCOINS_ITEM_NAME = config.getString("mobcoinsItem.name");
        MOBCOINS_ITEM_GLOW = config.getBoolean("mobcoinsItem.glow");
        MOBCOINS_ITEM_LORE = config.getStringList("mobcoinsItem.lore");

        SALARY_MODE_ENABLED = config.getBoolean("salaryMode.enabled");
        SALARY_MODE_ANNOUNCE_EVERY = config.getInt("salaryMode.announceEvery");
        SALARY_MODE_RECEIVE_AFTER_MESSAGE = config.getBoolean("salaryMode.receiveAfterMessage");
        SALARY_MODE_MESSAGES = config.getStringList("salaryMode.messages");

        SHOP_BEHAVIOUR = config.getString("shops.shopBehaviour");
        AUTO_UPDATE_ENABLED = config.getBoolean("shops.autoUpdate.enabled");
        AUTO_UPDATE_UPDATE_EVERY = config.getInt("shops.autoUpdate.updateEvery");

        PLACEHOLDER_UNLIMITED_STOCK = config.getString("placeholders.unlimitedStock");
        PLACEHOLDER_OUT_OF_STOCK = config.getString("placeholders.outOfStock");

        DEFAULT_NORMAL_TIME_RESET = rotatingShop.getInt("rotatingShop.normalTimeReset");
        DEFAULT_SPECIAL_TIME_RESET = rotatingShop.getInt("rotatingShop.specialTimeReset");

        NORMAL_IS_BROADCAST_MESSAGE = rotatingShop.getBoolean("rotatingShop.refreshActions.normalItems.broadcastMessage.enabled");
        NORMAL_BROADCAST_MESSAGE_MESSAGES = rotatingShop.getStringList("rotatingShop.refreshActions.normalItems.broadcastMessage.messages");

        NORMAL_IS_COMMAND = rotatingShop.getBoolean("rotatingShop.refreshActions.normalItems.commands.enabled");
        NORMAL_COMMAND_COMMANDS = rotatingShop.getStringList("rotatingShop.refreshActions.normalItems.commands.commands");

        SPECIAL_IS_BROADCAST_MESSAGE = rotatingShop.getBoolean("rotatingShop.refreshActions.specialItems.broadcastMessage.enabled");
        SPECIAL_BROADCAST_MESSAGE_MESSAGES = rotatingShop.getStringList("rotatingShop.refreshActions.specialItems.broadcastMessage.messages");

        SPECIAL_IS_COMMAND = rotatingShop.getBoolean("rotatingShop.refreshActions.specialItems.commands.enabled");
        SPECIAL_COMMAND_COMMANDS = rotatingShop.getStringList("rotatingShop.refreshActions.specialItems.commands.commands");

    }

}
