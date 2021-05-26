package me.aglerr.mobcoins.managers;

import me.aglerr.mobcoins.managers.managers.CoinMobManager;

import java.util.ArrayList;
import java.util.List;

public class ManagerLoader {

    private final List<Manager> managerList = new ArrayList<>();

    public ManagerLoader(){
        this.managerList.add(new CoinMobManager());
    }

    public void loadAllManagers(){
        for(Manager manager : this.managerList){
            manager.load();
        }
    }

    public void saveAllManagers(){
        for(Manager manager : this.managerList){
            manager.save();
        }
    }

}
