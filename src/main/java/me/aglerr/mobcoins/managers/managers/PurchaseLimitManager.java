package me.aglerr.mobcoins.managers.managers;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import me.aglerr.mobcoins.configs.Config;
import me.aglerr.mobcoins.configs.CustomConfig;
import me.aglerr.mobcoins.managers.Manager;
import me.aglerr.mobcoins.shops.items.TypeItem;
import me.aglerr.mobcoins.utils.Common;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.UUID;

public class PurchaseLimitManager implements Manager {

    private final Table<UUID, String, Integer> purchaseTable = HashBasedTable.create();

    public int getPlayerPurchaseLimit(UUID uuid, TypeItem item){
        // Check if player have a purchase limit data for this item
        if(this.purchaseTable.contains(uuid, item.getConfigKey())){
            // Return the purchase limit amount from the data
            return this.purchaseTable.get(uuid, item.getConfigKey());
        }
        // Return '0' because player doesn't have any data for this item
        return 0;
    }

    public void putOrIncreasePlayerPurchaseLimit(UUID uuid, TypeItem item){
        // Check if player have a purchase limit data for this item
        if(this.purchaseTable.contains(uuid, item.getConfigKey())){
            // Getting the current purchase limit for the player
            int currentLimit = this.purchaseTable.get(uuid, item.getConfigKey());

            // Increasing purchase limit by 1
            int increasedLimit = currentLimit + 1;

            // Putting the data on the table
            this.purchaseTable.put(uuid, item.getConfigKey(), increasedLimit);

            Common.debug(true, "Increasing Purchase Limit (uuid: {uuid}, item: {item}, current: {current}, increased: {increased})"
                    .replace("{uuid}", uuid.toString())
                    .replace("{current}", String.valueOf(currentLimit))
                    .replace("{increased}", String.valueOf(increasedLimit))
                    .replace("{item}", item.getConfigKey()));
            return;
        }

        // If player doesn't have a purchase limit data for this item //

        // Putting the data onto the table
        this.purchaseTable.put(uuid, item.getConfigKey(), 1);

        Common.debug(true, "Putting Purchase Limit (uuid: {uuid}, item: {item})"
                .replace("{uuid}", uuid.toString())
                .replace("{item}", item.getConfigKey()));
    }

    @Override
    public void load() {
        Common.log(true, "Trying to load all purchase limit data");

        FileConfiguration config = Config.TEMP_DATA.getConfig();

        if(!config.isConfigurationSection("purchaseLimit")) {
            Common.error(true, "Failed because there is no purchase limit data");
            return;
        }

        for(String uuid : config.getConfigurationSection("purchaseLimit").getKeys(false)){
            for(String key : config.getConfigurationSection("purchaseLimit." + uuid).getKeys(false)){
                int limit = config.getInt("purchaseLimit." + uuid + "." + key);
                this.purchaseTable.put(UUID.fromString(uuid), key, limit);
            }
        }

        Common.success(true, "Successfully loaded all purchase limit data");

    }

    @Override
    public void save() {
        Common.log(true, "Trying to save all purchase limit data");

        CustomConfig temp = Config.TEMP_DATA;
        FileConfiguration config = temp.getConfig();

        for(UUID uuid : this.purchaseTable.rowKeySet()){
            for(String key : this.purchaseTable.columnKeySet()){
                int limit = this.purchaseTable.get(uuid, key);
                config.set("purchaseLimit." + uuid + "." + key, limit);
            }
        }

        temp.saveConfig();

        Common.success(true, "Successfully saved all purchase limit data");
    }

}
