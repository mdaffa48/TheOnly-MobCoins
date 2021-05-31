# TheOnly-MobCoins
This is my upcoming premium MobCoins plugin.<br>
<br>
Example API Usage:<br>
```java
public void modifyPlayerCoins(Player player, double amount){
  PlayerData playerData = MobCoinsAPI.getPlayerData(player);
  if(playerData == null){
    System.out.println("PlayerData for " + player.getName() + " doesn't exist!");
    return;
  }
  
  playerData.addCoins(amount);
  playerData.reduceCoins(amount);
  playerData.setCoins(amount);
  
}

public double getPlayerCoins(Player player){
  PlayerData playerData = MobCoinsAPI.getPlayerData(player);
  if(playerData == null){
    return -1;
  }
  return playerData.getCoins();
}
```
