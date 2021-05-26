package me.aglerr.mobcoins.managers.managers;

import me.aglerr.mobcoins.MobCoins;
import me.aglerr.mobcoins.coinmob.CoinMob;
import me.aglerr.mobcoins.configs.Config;
import me.aglerr.mobcoins.configs.CustomConfig;
import me.aglerr.mobcoins.managers.Manager;
import me.aglerr.mobcoins.utils.Common;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class CoinMobManager implements Manager {

    private final List<CoinMob> coinMobList = new ArrayList<>();

    public List<CoinMob> getCoinMobList() { return coinMobList; }

    public CoinMob getCoinMob(String type){
        return getCoinMobList().stream().filter(coinMob -> coinMob.getType().equalsIgnoreCase(type))
                .findFirst().orElse(null);
    }
    public void clearCoinMob(){
        coinMobList.clear();
    }

    @Override
    public void load() {
        CustomConfig mobsConfig = Config.MOBS;
        FileConfiguration mobs = mobsConfig.getConfig();

        if(!this.coinMobList.isEmpty()) this.coinMobList.clear();

        int totalMobs = 0;
        for(String key : mobs.getConfigurationSection("entities").getKeys(false)){
            totalMobs++;
            this.coinMobList.add(new CoinMob(key, mobs.getString("entities." + key + ".amount"), mobs.getDouble("entities." + key + ".chance")));
        }

        Common.log(true, "Successfully loaded " + totalMobs + " mobs, enjoy!");

    }

    @Override
    public void save() {

    }

}
