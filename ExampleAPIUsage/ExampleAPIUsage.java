// Always null-check the PlayerData, just incase if the plugin failed
// to create a PlayerData for specific players and we don't want
// unexpected result to happen.

// Getting player's mobcoins with formatted amount
public String getPlayerFormattedCoins(Player player){
  PlayerData playerData = MobCoinsAPI.getPlayerData(player);
  if(playerData == null){
    return "Player data not exists!"; 
  }
  return playerData.getCoinsFormatted();
}

// Adding coins to player data
public void addPlayerMobCoins(Player player){
  PlayerData playerData = MobCoinsAPI.getPlayerData(player);
  if(playerData == null){
    System.out.println(player.getName() + " doesn't have player data.");
    return; 
  }
  playerData.addCoins(50.25);
  player.sendMessage("You have received 50.25 coins");
}
