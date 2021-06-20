package me.aglerr.mobcoins.api;

import me.aglerr.mobcoins.MobCoins;
import me.aglerr.mobcoins.PlayerData;
import me.aglerr.mobcoins.configs.ConfigValue;
import me.aglerr.mobcoins.managers.managers.CategoryShopManager;
import me.aglerr.mobcoins.managers.managers.PlayerDataManager;
import me.aglerr.mobcoins.managers.managers.RotatingShopManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MobCoinsExpansion extends PlaceholderExpansion {

    private final MobCoins plugin;
    public MobCoinsExpansion(MobCoins plugin){
        this.plugin = plugin;
    }

    /**
     * Because this is an internal class,
     * you must override this method to let PlaceholderAPI know to not unregister your expansion class when
     * PlaceholderAPI is reloaded
     *
     * @return true to persist through reloads
     */
    @Override
    public boolean persist(){
        return true;
    }

    /**
     * Because this is a internal class, this check is not needed
     * and we can simply return {@code true}
     *
     * @return Always true since it's an internal class.
     */
    @Override
    public boolean canRegister(){
        return true;
    }

    /**
     * The name of the person who created this expansion should go here.
     * <br>For convienience do we return the author from the plugin.yml
     *
     * @return The name of the author as a String.
     */
    @Override
    public String getAuthor(){
        return plugin.getDescription().getAuthors().toString();
    }

    /**
     * The placeholder identifier should go here.
     * <br>This is what tells PlaceholderAPI to call our onRequest
     * method to obtain a value if a placeholder starts with our
     * identifier.
     * <br>The identifier has to be lowercase and can't contain _ or %
     *
     * @return The identifier in {@code %<identifier>_<value>%} as String.
     */
    @Override
    public String getIdentifier(){
        return "mobcoins";
    }

    /**
     * This is the version of the expansion.
     * <br>You don't have to use numbers, since it is set as a String.
     *
     * For convienience do we return the version from the plugin.yml
     *
     * @return The version as a String.
     */
    @Override
    public String getVersion(){
        return plugin.getDescription().getVersion();
    }

    /**
     * This is the method called when a placeholder with our identifier
     * is found and needs a value.
     * <br>We specify the value identifier in this method.
     * <br>Since version 2.9.1 can you use OfflinePlayers in your requests.
     *
     * @param  player
     *         A Player object.
     * @param  identifier
     *         A String containing the identifier/value.
     *
     * @return possibly-null String of the requested identifier.
     */
    @Override
    public String onPlaceholderRequest(Player player, String identifier){

        if(player == null){
            return "";
        }

        PlayerDataManager playerDataManager = plugin.getManagerHandler().getPlayerDataManager();
        RotatingShopManager rotatingShopManager = plugin.getManagerHandler().getRotatingShopManager();
        CategoryShopManager categoryShopManager = plugin.getManagerHandler().getCategoryShopManager();

        // %mobcoins_balance%
        if(identifier.equalsIgnoreCase("balance")){
            PlayerData playerData = MobCoinsAPI.getPlayerData(player);
            return playerData == null ? "0" : String.valueOf(playerData.getCoins());
        }

        // %mobcoins_balance_formatted%
        if(identifier.equalsIgnoreCase("balance_formatted")){
            PlayerData playerData = MobCoinsAPI.getPlayerData(player);
            return playerData == null ? "0" : playerData.getCoinsFormatted();
        }

        // %mobcoins_balance_rounded%
        if(identifier.equalsIgnoreCase("balance_rounded")){
            PlayerData playerData = MobCoinsAPI.getPlayerData(player);
            return playerData == null ? "0" : String.valueOf(playerData.getCoinsRounded());
        }

        // %mobcoins_balance_shortformat%
        if(identifier.equalsIgnoreCase("balance_shortformat")){
            PlayerData playerData = MobCoinsAPI.getPlayerData(player);

            // Return '0' if player doesn't have any data
            if(playerData == null){
                return "0";
            }

            // Return the rounded coins if player has less than 1k coins
            if(playerData.getCoins() < 1000){
                return String.valueOf(playerData.getCoinsRounded());
            }

            return playerData.getCoinsShortFormat();
        }

        // %mobcoins_normaltime%
        if(identifier.equalsIgnoreCase("normaltime")){
            return rotatingShopManager.getFormattedNormalTime();
        }

        // %mobcoins_specialtime%
        if(identifier.equalsIgnoreCase("specialtime")){
            return rotatingShopManager.getFormattedSpecialTime();
        }

        // %mobcoins_categorytime%
        if(identifier.equalsIgnoreCase("categorytime")){
            return categoryShopManager.getFormattedResetTime();
        }

        // %mobcoins_top_name_<index>%
        if(identifier.startsWith("top_name")){

            // Get the mobcoins top in a list
            List<PlayerData> topList = playerDataManager.getMobcoinsTop();

            // Split the '_' from the identifier
            String[] split = identifier.split("_");

            // Get the index from the identifier
            int index = Integer.parseInt(split[2]) - 1;

            // Get the player data, using try catch for IndexOutOfBoundsException
            try{
                PlayerData playerData = topList.get(index);
                // Return the desired value
                return playerData.getName();
            } catch (IndexOutOfBoundsException e){
                // If there is no player from that index, return the empty value
                return ConfigValue.TOP_NAME_IF_EMPTY;
            }

        }

        // %mobcoins_top_balance_default_<index>%
        if(identifier.startsWith("top_balance_default")){

            // Get the mobcoins top in a list
            List<PlayerData> topList = playerDataManager.getMobcoinsTop();

            // Split the '_' from the identifier
            String[] split = identifier.split("_");

            // Get the index from the identifier
            int index = Integer.parseInt(split[3]) - 1;

            // Get the player data, using try catch for IndexOutOfBoundsException
            try{
                PlayerData playerData = topList.get(index);
                // Return the desired value
                return String.valueOf(playerData.getCoins());
            } catch (IndexOutOfBoundsException e){
                // If there is no player from that index, return the empty value
                return ConfigValue.TOP_BALANCE_IF_EMPTY;
            }
        }

        // %mobcoins_top_balance_rounded_<index>%
        if(identifier.startsWith("top_balance_rounded")){

            // Get the mobcoins top in a list
            List<PlayerData> topList = playerDataManager.getMobcoinsTop();

            // Split the '_' from the identifier
            String[] split = identifier.split("_");

            // Get the index from the identifier
            int index = Integer.parseInt(split[3]) - 1;

            // Get the player data, using try catch for IndexOutOfBoundsException
            try{
                PlayerData playerData = topList.get(index);
                // Return the desired value
                return String.valueOf(playerData.getCoinsRounded());
            } catch (IndexOutOfBoundsException e){
                // If there is no player from that index, return the empty value
                return ConfigValue.TOP_BALANCE_IF_EMPTY;
            }
        }

        // %mobcoins_top_balance_formatted_<index>%
        if(identifier.startsWith("top_balance_formatted")){

            // Get the mobcoins top in a list
            List<PlayerData> topList = playerDataManager.getMobcoinsTop();

            // Split the '_' from the identifier
            String[] split = identifier.split("_");

            // Get the index from the identifier
            int index = Integer.parseInt(split[3]) - 1;

            // Get the player data, using try catch for IndexOutOfBoundsException
            try{
                PlayerData playerData = topList.get(index);
                // Return the desired value
                return playerData.getCoinsFormatted();
            } catch (IndexOutOfBoundsException e){
                // If there is no player from that index, return the empty value
                return ConfigValue.TOP_BALANCE_IF_EMPTY;
            }
        }

        // %mobcoins_top_balance_shortformat_<index>%
        if(identifier.startsWith("top_balance_shortformat")){

            // Get the mobcoins top in a list
            List<PlayerData> topList = playerDataManager.getMobcoinsTop();

            // Split the '_' from the identifier
            String[] split = identifier.split("_");

            // Get the index from the identifier
            int index = Integer.parseInt(split[3]) - 1;

            // Get the player data, using try catch for IndexOutOfBoundsException
            try{
                PlayerData playerData = topList.get(index);
                // Return the rounded coins if player has less than 1k coins
                if(playerData.getCoins() < 1000){
                    return String.valueOf(playerData.getCoinsRounded());
                }
                // Return the desired value
                return playerData.getCoinsShortFormat();
            } catch (IndexOutOfBoundsException e){
                // If there is no player from that index, return the empty value
                return ConfigValue.TOP_BALANCE_IF_EMPTY;
            }
        }

        // %mobcoins_top_uuid_<index>%
        if(identifier.startsWith("top_uuid")){

            // Get the mobcoins top in a list
            List<PlayerData> topList = playerDataManager.getMobcoinsTop();

            // Split the '_' from the identifier
            String[] split = identifier.split("_");

            // Get the index from the identifier
            int index = Integer.parseInt(split[2]) - 1;

            // Get the player data, using try catch for IndexOutOfBoundsException
            try{
                PlayerData playerData = topList.get(index);
                // Return the desired value
                return playerData.getUUID();
            } catch (IndexOutOfBoundsException e){
                // If there is no player from that index, return the empty value
                return ConfigValue.TOP_UUID_IF_EMPTY;
            }
        }

        // We return null if an invalid placeholder (f.e. %someplugin_placeholder3%)
        // was provided
        return null;
    }

}
