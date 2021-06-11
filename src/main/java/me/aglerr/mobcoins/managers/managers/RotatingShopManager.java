package me.aglerr.mobcoins.managers.managers;

import com.cryptomorin.xseries.messages.Titles;
import me.aglerr.mobcoins.configs.Config;
import me.aglerr.mobcoins.configs.ConfigValue;
import me.aglerr.mobcoins.configs.CustomConfig;
import me.aglerr.mobcoins.managers.Manager;
import me.aglerr.mobcoins.utils.Common;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

public class RotatingShopManager implements Manager {

    private int normalTime = 0;
    private int specialTime = 0;

    public int getNormalTime(){
        return normalTime;
    }

    public int getSpecialTime(){
        return specialTime;
    }

    public String getFormattedNormalTime(){
        return Common.getFormattedTime(normalTime);
    }

    public String getFormattedSpecialTime(){
        return Common.getFormattedTime(specialTime);
    }

    @Override
    public void load() {
        FileConfiguration config = Config.TEMP_DATA.getConfig();
        FileConfiguration rotating = Config.ROTATING_SHOP_CONFIG.getConfig();

        // Check if there is no normal time saved
        int savedNormalTime = config.getInt("rotatingShop.normalTime");
        int savedSpecialTime = config.getInt("rotatingShop.specialTime");

        // If saved normal time is below or equals to 0, assign the default time
        if(savedNormalTime <= 0){
            normalTime = ConfigValue.DEFAULT_NORMAL_TIME_RESET;
        } else {
            // If the saved normal time is not below or equals to 0, assign the saved time
            normalTime = savedNormalTime;
        }

        // If saved normal time is below or equals to 0, assign the default time
        if(savedSpecialTime <= 0){
            specialTime = ConfigValue.DEFAULT_SPECIAL_TIME_RESET;
        } else {
            // If the saved normal time is not below or equals to 0, assign the saved time
            specialTime = savedSpecialTime;
        }

        // Run task timer for handling the time and refreshing normal/special items event
        Common.runTaskTimerAsynchronously(0, 20, () -> {

            if(normalTime <= 0){

                // Play bunch of events (send messages, titles, sound, commands)
                Bukkit.getOnlinePlayers().forEach(player -> {
                    // Send Messages
                    if(ConfigValue.NORMAL_IS_BROADCAST_MESSAGE){
                        ConfigValue.NORMAL_BROADCAST_MESSAGE_MESSAGES.forEach(message -> player.sendMessage(Common.color(message)));
                    }

                    // Send Titles
                    Common.sendTitle(player, "rotatingShop.refreshActions.normalItems.titles", rotating, 0);

                    // Play Sound
                    Common.playSound(player, "rotatingShop.refreshActions.normalItems.sound", rotating);

                    if(ConfigValue.NORMAL_IS_COMMAND){
                        ConfigValue.NORMAL_COMMAND_COMMANDS.forEach(message -> {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), message.replace("{player}", player.getName()));
                        });
                    }
                });
            }

            if(specialTime <= 0){

                // Play bunch of events (send messages, titles, sound, commands)
                Bukkit.getOnlinePlayers().forEach(player -> {
                    // Send Messages
                    if(ConfigValue.SPECIAL_IS_BROADCAST_MESSAGE){
                        ConfigValue.SPECIAL_BROADCAST_MESSAGE_MESSAGES.forEach(message -> player.sendMessage(Common.color(message)));
                    }

                    // Send Titles
                    Common.sendTitle(player, "rotatingShop.refreshActions.specialItems.titles", rotating, 0);

                    // Play Sound
                    Common.playSound(player, "rotatingShop.refreshActions.specialItems.sound", rotating);

                    if(ConfigValue.SPECIAL_IS_COMMAND){
                        ConfigValue.SPECIAL_COMMAND_COMMANDS.forEach(message -> {
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), message.replace("{player}", player.getName()));
                        });
                    }
                });

            }

            normalTime--;
            specialTime--;
        });

    }

    @Override
    public void save() {
        CustomConfig temp = Config.TEMP_DATA;
        FileConfiguration config = temp.getConfig();

        config.set("rotatingShop.normalTime", normalTime);
        config.set("rotatingShop.specialTime", specialTime);

        temp.saveConfig();

    }

}
