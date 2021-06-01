package me.aglerr.mobcoins.api;

import me.aglerr.mobcoins.MobCoins;
import me.aglerr.mobcoins.PlayerData;
import me.aglerr.mobcoins.coinmob.CoinMob;
import me.aglerr.mobcoins.managers.ManagerHandler;
import me.aglerr.mobcoins.managers.managers.PlayerDataManager;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class MobCoinsAPI {

    @Nullable
    public static PlayerData getPlayerData(Player player){
        ManagerHandler managerHandler = MobCoins.getInstance().getManagerHandler();
        return managerHandler.getPlayerDataManager().getPlayerData(player);
    }

    /**
     * Get CoinMob object that loaded from mobs.yml by the mob type
     * @param mobType the mob type string
     * @return the CoinMob object that handle all value on mobs.yml
     */
    @Nullable
    public static CoinMob getCoinMob(String mobType){
        ManagerHandler managerHandler = MobCoins.getInstance().getManagerHandler();
        return managerHandler.getCoinMobManager().getCoinMob(mobType);
    }

}
