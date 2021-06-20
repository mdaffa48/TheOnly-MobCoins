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

    /**
     * Get the CoinMob's mob type
     *
     * @return mob type in string
     */
    public String getType() {
        return type;
    }

    /**
     * Get the CoinMob's dropped coin amount
     * (the amount on the mobs.yml)
     * Mainly it will return a double, but check '-' for ranged drop amount
     *
     * @return the coin amount
     */
    public String getCoinAmount() {
        return coinAmount;
    }

    /**
     * Get the CoinMob drop chance
     *
     * @return the drop chance
     */
    public double getChance() {
        return chance;
    }

    public boolean willDropCoins(){
        double random = ThreadLocalRandom.current().nextDouble(101);
        double chance = this.getChance();
        System.out.println("Is " + random + " <= " + chance);
        return random <= chance;
    }

    /**
     * Get the amount of dropped coins from {@link CoinMob#getCoinAmount()}
     *
     * @return amount of dropped coins
     */
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
