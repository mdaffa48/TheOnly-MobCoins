package me.aglerr.mobcoins.coinmob;

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
}
