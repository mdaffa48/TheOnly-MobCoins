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
    private final int purchaseLimit;
    private final int stock;
    private final List<String> lore;
    private final List<String> commands;
    private final boolean rotatingShop;
    private final boolean isSpecial;

    public TypeItem(String configKey, String type, String category, String material, String name, boolean glow, List<Integer> slots, int amount, double price, int purchaseLimit, int stock, List<String> lore, List<String> commands, boolean rotatingShop, boolean isSpecial) {
        this.configKey = configKey;
        this.type = type;
        this.category = category;
        this.material = material;
        this.name = name;
        this.glow = glow;
        this.slots = slots;
        this.amount = amount;
        this.price = price;
        this.purchaseLimit = purchaseLimit;
        this.stock = stock;
        this.lore = lore;
        this.commands = commands;
        this.rotatingShop = rotatingShop;
        this.isSpecial = isSpecial;
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

    public int getPurchaseLimit(){
        return purchaseLimit;
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

    public boolean isRotatingShop() {
        return rotatingShop;
    }

    public boolean isSpecial() {
        return isSpecial;
    }
}
