package me.aglerr.mobcoins.objects;

import me.aglerr.lazylibs.libs.Common;
import me.aglerr.mobcoins.configs.Config;
import me.aglerr.mobcoins.configs.ConfigValue;
import me.aglerr.mobcoins.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.UUID;

public class NotificationUser {

    private final UUID uuid;

    private boolean sound;
    private boolean title;
    private boolean actionBar;
    private boolean message;

    public NotificationUser(UUID uuid){
        this.uuid = uuid;

        sound = true;
        title = true;
        actionBar = true;
        message = true;
    }

    public NotificationUser(UUID uuid, boolean isSound, boolean isTitle, boolean isActionBar, boolean isMessage){
        this.uuid = uuid;

        this.sound = isSound;
        this.title = isTitle;
        this.actionBar = isActionBar;
        this.message = isMessage;
    }

    public UUID getUUID(){
        return uuid;
    }

    public boolean isSound() {
        return sound;
    }

    public void setSound(boolean sound) {
        this.sound = sound;
    }

    public boolean isTitle() {
        return title;
    }

    public void setTitle(boolean title) {
        this.title = title;
    }

    public boolean isActionBar() {
        return actionBar;
    }

    public void setActionBar(boolean actionBar) {
        this.actionBar = actionBar;
    }

    public boolean isMessage() {
        return message;
    }

    public void setMessage(boolean message) {
        this.message = message;
    }

    public String wrapOptions(){
        return sound + ";" + title + ";" + actionBar + ";" + message;
    }

    public void unwrapOptions(String text){
        String[] split = text.split(";");
        setSound(Boolean.getBoolean(split[0]));
        setTitle(Boolean.getBoolean(split[1]));
        setActionBar(Boolean.getBoolean(split[2]));
        setMessage(Boolean.getBoolean(split[3]));
    }

    public void sendNotification(double amountReceived, boolean salary){
        Player player = Bukkit.getPlayer(uuid);
        if(player == null){
            return;
        }
        FileConfiguration config = Config.CONFIG.getConfig();
        // Play sound to the player
        if(sound){
            Utils.playSound(player, "sounds.onCoinsReceived", config);
        }
        // Send title to the player
        if(title){
            Utils.sendTitle(player, "titles.onCoinsReceived", config, amountReceived);
        }
        // Send action bar to the player
        if(actionBar){
            Utils.sendActionBar(player, "actionBar.onCoinsReceived", config, amountReceived);
        }
        // Send messages to the player
        if(message && !salary){
            player.sendMessage(Common.color(ConfigValue.MESSAGES_COINS_RECEIVED
                    .replace("{prefix}", ConfigValue.PREFIX)
                    .replace("{amount}", Common.numberFormat(amountReceived))
                    .replace("{amount_rounded}", Utils.integer(amountReceived))));
        }
        // Send message but with salary mode
        if(message && salary){
            ConfigValue.SALARY_MODE_MESSAGES.forEach(message -> player.sendMessage(Common.color(message
                    .replace("{amount}", Common.numberFormat(amountReceived))
                    .replace("{amount_rounded}", Utils.integer(amountReceived)))));
        }
    }

}
