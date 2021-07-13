package me.aglerr.mobcoins.api;

import me.aglerr.mobcoins.MobCoins;
import me.aglerr.mobcoins.PlayerData;
import me.aglerr.mobcoins.coinmob.CoinMob;
import me.aglerr.mobcoins.managers.ManagerHandler;
import me.aglerr.mobcoins.managers.managers.CoinMobManager;
import me.aglerr.mobcoins.managers.managers.PlayerDataManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

public class MobCoinsAPI {

    /**
     * Get PlayerData object to modify/get player mobcoins
     *
     * @param player player object
     * @return {@link me.aglerr.mobcoins.PlayerData}
     */
    @Nullable
    public static PlayerData getPlayerData(Player player){
        PlayerDataManager data = MobCoins.getInstance().getManagerHandler().getPlayerDataManager();
        return data.getPlayerData(player);
    }

    /**
     * Get CoinMob object that loaded from mobs.yml by the mob type
     *
     * @param mobType the mob type in string
     * @return {@link me.aglerr.mobcoins.coinmob.CoinMob}
     */
    @Nullable
    public static CoinMob getCoinMob(String mobType){
        CoinMobManager coinMobManager = MobCoins.getInstance().getManagerHandler().getCoinMobManager();
        return coinMobManager.getCoinMob(mobType);
    }

}
