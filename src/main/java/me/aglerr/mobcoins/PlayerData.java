package me.aglerr.mobcoins;

public class PlayerData {

    private final String uuid;
    private String name;
    private double coins = 0;

    public PlayerData(String uuid, String name){
        this.uuid = uuid;
        this.name = name;
    }

    public String getUUID() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCoins() {
        return coins;
    }

    public void modifyCoins(CoinAction action, double amount){
        switch(action){
            case ADD:{
                this.coins = (this.coins + amount);
                break;
            }

            case REDUCE:{
                this.coins = (this.coins - amount);
                break;
            }

            case SET:{
                this.coins = amount;
                break;
            }

            default:{
                throw new IllegalStateException("Invalid enum for CoinAction!");
            }

        }
    }

    public enum CoinAction{
        ADD,
        REDUCE,
        SET
    }

}
