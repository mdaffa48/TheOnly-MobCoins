package me.aglerr.mobcoins.configs;

import org.bukkit.configuration.file.FileConfiguration;

public class HooksValue {

    public static boolean JETS_MINION_ENABLED;
    public static int JETS_MINION_REDUCED_PERCENTAGE;
    public static boolean JETS_MINION_USE_PERMISSION;
    public static String JETS_MINION_PERMISSION;
    public static boolean JETS_MINION_VIRTUAL_MOBCOINS;
    public static boolean JETS_MINION_PHYSICAL_ONLINE;

    public static void initialize(){
        FileConfiguration config = Config.HOOKS.getConfig();

        JETS_MINION_ENABLED = config.getBoolean("jetsMinion.enabled");
        JETS_MINION_REDUCED_PERCENTAGE = config.getInt("jetsMinion.reducedPercentage");
        JETS_MINION_USE_PERMISSION = config.getBoolean("jetsMinion.usePermission");
        JETS_MINION_PERMISSION = config.getString("jetsMinion.permission");
        JETS_MINION_VIRTUAL_MOBCOINS = config.getBoolean("jetsMinion.virtualCoins");
        JETS_MINION_PHYSICAL_ONLINE = config.getBoolean("jetsMinion.physicalCoinsRequiredOnline");
    }

}
