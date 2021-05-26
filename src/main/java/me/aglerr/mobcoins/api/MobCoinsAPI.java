package me.aglerr.mobcoins.api;

import me.aglerr.mobcoins.MobCoins;
import me.aglerr.mobcoins.PlayerData;
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

}
