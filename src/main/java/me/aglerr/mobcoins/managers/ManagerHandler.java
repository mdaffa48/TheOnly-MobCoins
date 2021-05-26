package me.aglerr.mobcoins.managers;

import me.aglerr.mobcoins.MobCoins;
import me.aglerr.mobcoins.managers.managers.CoinMobManager;
import me.aglerr.mobcoins.managers.managers.PlayerDataManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManagerHandler {

    private final Map<ManagerType, Manager> managerList = new HashMap<>();

    private final MobCoins plugin;

    public ManagerHandler(MobCoins plugin){
        this.plugin = plugin;
        this.managerList.put(ManagerType.COIN_MOB_MANAGER, new CoinMobManager());
        this.managerList.put(ManagerType.PLAYER_DATA_MANAGER, new PlayerDataManager(plugin));
    }

    public void loadAllManagers(){
        for(ManagerType managerType : this.managerList.keySet()){
            this.managerList.get(managerType).load();
        }
    }

    public void saveAllManagers(){
        for(ManagerType managerType : this.managerList.keySet()){
            this.managerList.get(managerType).save();
        }
    }

    public Manager getManager(ManagerType managerType){
        return this.managerList.get(managerType);
    }

    public enum ManagerType{
        COIN_MOB_MANAGER,
        PLAYER_DATA_MANAGER
    }

}
