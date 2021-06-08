package me.aglerr.mobcoins.shops.items;

import java.util.List;

public class TypeItem {

    private final String configKey;
    private final String type;
    private final String category;
    private final String material;
    private final String name;
    private final boolean glow;
    private final List<Integer> slots;
    private final int amount;
    private final double price;
    private final int buyLimit;
    private final int stock;
    private final List<String> lore;
    private final List<String> commands;

    public TypeItem(String configKey, String type, String category, String material, String name, boolean glow, List<Integer> slots, int amount, double price, int buyLimit, int stock, List<String> lore, List<String> commands) {
        this.configKey = configKey;
        this.type = type;
        this.category = category;
        this.material = material;
        this.name = name;
        this.glow = glow;
        this.slots = slots;
        this.amount = amount;
        this.price = price;
        this.buyLimit = buyLimit;
        this.stock = stock;
        this.lore = lore;
        this.commands = commands;
    }

    public String getConfigKey(){
        return configKey;
    }

    public String getType() {
        return type;
    }

    public String getCategory() {
        return category;
    }

    public String getMaterial() {
        return material;
    }

    public String getName() {
        return name;
    }

    public boolean isGlow() {
        return glow;
    }

    public List<Integer> getSlots() {
        return slots;
    }

    public int getAmount() {
        return amount;
    }

    public double getPrice() {
        return price;
    }

    public int getBuyLimit() {
        return buyLimit;
    }

    public int getStock() {
        return stock;
    }

    public List<String> getLore() {
        return lore;
    }

    public List<String> getCommands() {
        return commands;
    }
}
