package me.aglerr.mobcoins.managers;

import me.aglerr.mobcoins.MobCoins;
import me.aglerr.mobcoins.managers.managers.*;

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
        this.managerList.put(ManagerType.SPAWNER_SPAWN_MANAGER, new SpawnerSpawnManager());
        this.managerList.put(ManagerType.SALARY_MANAGER, new SalaryManager(plugin.getConfig()));
        this.managerList.put(ManagerType.DEPENDENCY_MANAGER, new DependencyManager());
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

    public CoinMobManager getCoinMobManager(){
        return (CoinMobManager) this.managerList.get(ManagerType.COIN_MOB_MANAGER);
    }

    public PlayerDataManager getPlayerDataManager(){
        return (PlayerDataManager) this.managerList.get(ManagerType.PLAYER_DATA_MANAGER);
    }

    public SpawnerSpawnManager getSpawnerSpawnManager(){
        return (SpawnerSpawnManager) this.managerList.get(ManagerType.SPAWNER_SPAWN_MANAGER);
    }

    public SalaryManager getSalaryManager(){
        return (SalaryManager) this.managerList.get(ManagerType.SALARY_MANAGER);
    }

    public DependencyManager getDependencyManager(){
        return (DependencyManager) this.managerList.get(ManagerType.DEPENDENCY_MANAGER);
    }

    private enum ManagerType{
        COIN_MOB_MANAGER,
        PLAYER_DATA_MANAGER,
        SPAWNER_SPAWN_MANAGER,
        SALARY_MANAGER,
        DEPENDENCY_MANAGER
    }

}
