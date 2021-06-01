package me.aglerr.mobcoins.coinmob;

import java.util.concurrent.ThreadLocalRandom;

public class CoinMob {

    private final String type;
    private final String coinAmount;
    private final double chance;

    public CoinMob(String type, String coinAmount, double chance) {
        this.type = type;
        this.coinAmount = coinAmount;
        this.chance = chance;
    }

    public String getType() {
        return type;
    }

    public String getCoinAmount() {
        return coinAmount;
    }

    public double getChance() {
        return chance;
    }

    public boolean willDropCoins(){
        return ThreadLocalRandom.current().nextDouble(101) <= getChance();
    }

    public double getAmountToDrop(){
        if(this.getCoinAmount().contains("-")){
            String[] split = this.getCoinAmount().split("-");
            double minimumAmount = Double.parseDouble(split[0]);
            double maximumAmount = Double.parseDouble(split[1]);

            return ThreadLocalRandom.current().nextDouble(maximumAmount - minimumAmount) + minimumAmount;
        }
        return Double.parseDouble(this.getCoinAmount());
    }

}
