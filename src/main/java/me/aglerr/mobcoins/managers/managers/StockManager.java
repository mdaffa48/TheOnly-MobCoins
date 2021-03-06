package me.aglerr.mobcoins.managers.managers;

import me.aglerr.mclibs.libs.Common;
import me.aglerr.mclibs.libs.CustomConfig;
import me.aglerr.mclibs.libs.Debug;
import me.aglerr.mobcoins.configs.Config;
import me.aglerr.mobcoins.configs.ConfigValue;
import me.aglerr.mobcoins.managers.Manager;
import me.aglerr.mobcoins.shops.items.TypeItem;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

public class StockManager implements Manager {

    private final Map<String, Integer> stock = new HashMap<>();

    public String getStockInString(TypeItem item){
        // Return unlimited stock placeholder If the item doesn't use stock
        if(item.getStock() < 0){
            return ConfigValue.PLACEHOLDER_UNLIMITED_STOCK;
        }
        // Return the current item stock data
        if(this.stock.containsKey(item.getConfigKey())){
            // Get the stock remaining
            int stock = this.stock.get(item.getConfigKey());
            // Return out of stock placeholder If the item stock is 0
            if(stock == 0){
                return ConfigValue.PLACEHOLDER_OUT_OF_STOCK;
            }
            // Return the stock remaining value
            return String.valueOf(stock);
        }
        // Return the configured stock amount
        return String.valueOf(item.getStock());
    }

    public int getStockInInteger(TypeItem item){
        // If item has a stock data, return the saved amount
        if(this.stock.containsKey(item.getConfigKey())){
            return this.stock.get(item.getConfigKey());
        }
        // Return the configured stock amount
        return item.getStock();
    }

    public void clearStockForItem(TypeItem item){
        this.stock.remove(item.getConfigKey());
    }

    // This method are used when someone bought an item on the shop
    public void putOrDecrease(TypeItem item){

        // Return if the stock is disabled for the item
        if(item.getStock() <= 0) return;

        // Put the item with decreased stock by 1
        if(!this.stock.containsKey(item.getConfigKey())){
            int decreasedStock = item.getStock() - 1;
            this.stock.put(item.getConfigKey(), decreasedStock);

            Debug.send("Putting item stock data (item: {item}, original: {stock}, result: {result})"
                    .replace("{stock}", String.valueOf(item.getStock()))
                    .replace("{result}", String.valueOf(decreasedStock))
                    .replace("{item}", item.getConfigKey()));
            return;
        }

        // If the item exist on the data, decrease the stock by 1
        int currentStock = this.stock.get(item.getConfigKey());
        int decreasedStock = currentStock - 1;

        if(decreasedStock < 0)
            decreasedStock = 0;

        this.stock.put(item.getConfigKey(), decreasedStock);

        Debug.send("Decreasing item stock data (item: {item}, before: {before}, after: {after})"
                .replace("{before}", String.valueOf(currentStock))
                .replace("{after}", String.valueOf(decreasedStock))
                .replace("{item}", item.getConfigKey()));

    }

    public void clearItemStock(){
        this.stock.clear();
    }

    @Override
    public void load() {
        Common.log("&rLoading all item stock data");

        FileConfiguration config = Config.TEMP_DATA.getConfig();

        if(!config.isConfigurationSection("stock")) {
            Common.log("&cFailed, because there is no item stock data");
            return;
        }

        for(String key : config.getConfigurationSection("stock").getKeys(false)){
            int stockRemaining = config.getInt("stock." + key);
            this.stock.put(key, stockRemaining);
        }

        Common.log("&aSuccessfully loaded all item stock data");
    }

    @Override
    public void save() {
        Common.log("&rSaving all item stock data");
        CustomConfig tempData = Config.TEMP_DATA;
        FileConfiguration config = tempData.getConfig();
        for(String key : this.stock.keySet()){
            config.set("stock." + key, this.stock.get(key));
        }
        tempData.saveConfig();
        Common.log("&aSuccessfully saved all item stock data");
    }
}
