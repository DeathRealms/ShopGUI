package me.deathrealms.shopgui.utils;

import me.deathrealms.realmsapi.XMaterial;

import java.util.List;

public class ShopItem {
    private XMaterial material;
    private String displayName;
    private int amount;
    private int maxStackSize;
    private boolean stackable;
    private double buyPrice;
    private double sellPrice;
    private boolean extendedPotion;
    private boolean spawner;
    private String mobType;
    private List<String> commands;

    public XMaterial getMaterial() {
        return material;
    }

    public void setMaterial(XMaterial material) {
        this.material = material;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getMaxStackSize() {
        return maxStackSize;
    }

    public void setMaxStackSize(int maxStackSize) {
        this.maxStackSize = maxStackSize;
    }

    public boolean isStackable() {
        return stackable;
    }

    public void setStackable(boolean stackable) {
        this.stackable = stackable;
    }

    public double getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public double getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(double sellPrice) {
        this.sellPrice = sellPrice;
    }

    public boolean isExtendedPotion() {
        return extendedPotion;
    }

    public void setExtendedPotion(boolean extendedPotion) {
        this.extendedPotion = extendedPotion;
    }

    public boolean isSpawner() {
        return spawner;
    }

    public void setSpawner(boolean spawner) {
        this.spawner = spawner;
    }

    public String getMobType() {
        return mobType;
    }

    public void setMobType(String mobType) {
        this.mobType = mobType;
    }

    public List<String> getCommands() {
        return commands;
    }

    public void setCommands(List<String> commands) {
        this.commands = commands;
    }
}
