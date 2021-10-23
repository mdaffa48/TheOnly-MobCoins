package me.aglerr.mobcoins.shops.inventory;

import me.aglerr.lazylibs.inventory.LazyInventory;
import me.aglerr.lazylibs.libs.ItemBuilder;
import me.aglerr.lazylibs.libs.XMaterial;
import me.aglerr.mobcoins.MobCoins;
import me.aglerr.mobcoins.configs.Config;
import me.aglerr.mobcoins.objects.NotificationUser;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ToggleInventory extends LazyInventory {

    private final NotificationUser notificationUser;

    public ToggleInventory(MobCoins plugin, Player player, int size, String title) {
        super(size, title);

        this.notificationUser = plugin.getManagerHandler().getNotificationManager().getNotificationUser(player);

        setAllItems();
        fillInventory();
    }

    private void fillInventory(){
        for (int i = 0; i < this.getInventory().getSize(); i++) {
            ItemStack item = this.getInventory().getItem(i);

            if(item == null || item.getType() == Material.AIR){
                this.setItem(i, getItem("fill"));
            }

        }
    }

    private void setAllItems(){
        this.setItem(getSlot("sound"), getItem("sound"), event -> {
            notificationUser.setSound(!notificationUser.isSound());
            setAllItems();
        });
        this.setItem(getSlot("title"), getItem("title"), event -> {
            notificationUser.setTitle(!notificationUser.isTitle());
            setAllItems();
        });
        this.setItem(getSlot("actionbar"), getItem("actionbar"), event -> {
            notificationUser.setActionBar(!notificationUser.isActionBar());
            setAllItems();
        });
        this.setItem(getSlot("message"), getItem("message"), event -> {
            notificationUser.setMessage(!notificationUser.isMessage());
            setAllItems();
        });
    }

    private ItemStack getItem(String type){
        switch(type){
            case "fill":{
                String material = getString("fillInventory.material");
                String name = getString("fillInventory.name");

                if(!XMaterial.matchXMaterial(material).isPresent()){
                    return getErrorItems();
                }

                return new ItemBuilder(XMaterial.matchXMaterial(material).get().parseItem())
                        .name(name)
                        .build();
            }
            case "sound":{
                String material = getString("soundNotification.material");
                String name = getString("soundNotification.name")
                        .replace("{status}", notificationUser.isSound() ? getString("placeholders.statusEnabled") : getString("placeholders.statusDisabled"));
                List<String> lore = new ArrayList<>();
                for(String line : getStringList("soundNotification.lore")){
                    lore.add(line.replace("{status}", notificationUser.isSound() ? getString("placeholders.statusEnabled") : getString("placeholders.statusDisabled")));
                }

                if(!XMaterial.matchXMaterial(material).isPresent()){
                    return getErrorItems();
                }

                return new ItemBuilder(XMaterial.matchXMaterial(material).get().parseItem())
                        .name(name)
                        .lore(lore)
                        .build();
            }
            case "title":{
                String material = getString("titleNotification.material");
                String name = getString("titleNotification.name")
                        .replace("{status}", notificationUser.isTitle() ? getString("placeholders.statusEnabled") : getString("placeholders.statusDisabled"));
                List<String> lore = new ArrayList<>();
                for(String line : getStringList("titleNotification.lore")){
                    lore.add(line.replace("{status}", notificationUser.isTitle() ? getString("placeholders.statusEnabled") : getString("placeholders.statusDisabled")));
                }

                if(!XMaterial.matchXMaterial(material).isPresent()){
                    return getErrorItems();
                }

                return new ItemBuilder(XMaterial.matchXMaterial(material).get().parseItem())
                        .name(name)
                        .lore(lore)
                        .build();
            }
            case "actionbar":{
                String material = getString("actionBarNotification.material");
                String name = getString("actionBarNotification.name")
                        .replace("{status}", notificationUser.isActionBar() ? getString("placeholders.statusEnabled") : getString("placeholders.statusDisabled"));
                List<String> lore = new ArrayList<>();
                for(String line : getStringList("actionBarNotification.lore")){
                    lore.add(line.replace("{status}", notificationUser.isActionBar() ? getString("placeholders.statusEnabled") : getString("placeholders.statusDisabled")));
                }

                if(!XMaterial.matchXMaterial(material).isPresent()){
                    return getErrorItems();
                }

                return new ItemBuilder(XMaterial.matchXMaterial(material).get().parseItem())
                        .name(name)
                        .lore(lore)
                        .build();
            }
            case "message":{
                String material = getString("messageNotification.material");
                String name = getString("messageNotification.name")
                        .replace("{status}", notificationUser.isMessage() ? getString("placeholders.statusEnabled") : getString("placeholders.statusDisabled"));
                List<String> lore = new ArrayList<>();
                for(String line : getStringList("messageNotification.lore")){
                    lore.add(line.replace("{status}", notificationUser.isMessage() ? getString("placeholders.statusEnabled") : getString("placeholders.statusDisabled")));
                }

                if(!XMaterial.matchXMaterial(material).isPresent()){
                    return getErrorItems();
                }

                return new ItemBuilder(XMaterial.matchXMaterial(material).get().parseItem())
                        .name(name)
                        .lore(lore)
                        .build();
            }
            default:
                return new ItemStack(Material.DIRT);
        }
    }

    private int getSlot(String type){
        FileConfiguration config = Config.TOGGLE_INVENTORY_CONFIG.getConfig();
        switch(type){
            case "sound":{
                return config.getInt("soundNotification.slot");
            }
            case "title":{
                return config.getInt("titleNotification.slot");
            }
            case "actionbar":{
                return config.getInt("actionBarNotification.slot");
            }
            case "message":{
                return config.getInt("messageNotification.slot");
            }
            default:
                return 0;
        }
    }

    private ItemStack getErrorItems(){
        return new ItemBuilder(Material.BARRIER)
                .name("&cInvalid Material! Check your config please.")
                .build();
    }

    private String getString(String path){
        FileConfiguration config = Config.TOGGLE_INVENTORY_CONFIG.getConfig();
        return config.getString(path);
    }

    private int getInt(String path){
        FileConfiguration config = Config.TOGGLE_INVENTORY_CONFIG.getConfig();
        return config.getInt(path);
    }

    private List<String> getStringList(String path){
        FileConfiguration config = Config.TOGGLE_INVENTORY_CONFIG.getConfig();
        return config.getStringList(path);
    }

    private boolean getBoolean(String path){
        FileConfiguration config = Config.TOGGLE_INVENTORY_CONFIG.getConfig();
        return config.getBoolean(path);
    }

}
