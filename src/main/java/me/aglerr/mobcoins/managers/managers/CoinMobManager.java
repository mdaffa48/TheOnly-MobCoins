package me.aglerr.mobcoins.managers.managers;

import me.aglerr.mobcoins.coinmob.CoinMob;
import me.aglerr.mobcoins.configs.Config;
import me.aglerr.mobcoins.configs.CustomConfig;
import me.aglerr.mobcoins.managers.Manager;
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

        for(String key : mobs.getConfigurationSection("entities").getKeys(false)){
            this.coinMobList.add(new CoinMob(key, mobs.getString("entities." + key + ".amount"), mobs.getDouble("entities." + key + ".chance")));
        }

    }

    @Override
    public void save() {
        this.clearCoinMob();
    }

}
