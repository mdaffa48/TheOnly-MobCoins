package me.aglerr.mobcoins.shops.items;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TypeItem {

    private final @NotNull String configKey;
    private final String type;
    private final String category;
    private final @NotNull String material;
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
    private final String fileName;

    public TypeItem(String configKey, String type, String category, String material, String name, boolean glow, List<Integer> slots, int amount, double price, int purchaseLimit, int stock, List<String> lore, List<String> commands, boolean rotatingShop, boolean isSpecial, String fileName) {
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
        this.fileName = fileName;
    }

    public @NotNull String getConfigKey(){
        return configKey;
    }

    public String getType() {
        return type;
    }

    public String getCategory() {
        return category;
    }

    public @NotNull String getMaterial() {
        return material;
    }

    public String getName() {
        if(name == null){
            return "&cThis is the default name, check your configuration!";
        }
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

    public String getFileName() {
        return fileName;
    }
}
