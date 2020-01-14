package me.deathrealms.shopgui.utils;

import me.deathrealms.realmsapi.XMaterial;
import me.deathrealms.shopgui.Shop;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;

public class SellPrice {
    private static Map<XMaterial, SellPrice> sellPrices;

    static {
        sellPrices = new HashMap<>();
    }

    private double sellPrice;

    public SellPrice(double sellPrice) {
        this.sellPrice = sellPrice;
    }

    public static Map<XMaterial, SellPrice> getSellPrices() {
        ConfigurationSection categories = Shop.getShops().getConfigSection("categories");
        for (String shop : categories.getKeys(false)) {
            ConfigurationSection config = Shop.getShops().getConfigSection(shop + ".items");
            if (config != null) {
                for (String str : config.getKeys(false)) {
                    int damage = config.getInt(str + ".damage", 0);
                    XMaterial material;
                    if (NumberUtils.isNumber(str)) {
                        material = XMaterial.matchXMaterial(Integer.parseInt(str), (byte) damage);
                    } else {
                        material = XMaterial.matchXMaterial(str, (byte) damage);
                    }
                    int amount = config.getInt(material.name() + ".amount", 1);
                    double sellPrice = (config.getDouble(material.name() + ".sell-price") / amount);
                    sellPrices.put(material, new SellPrice(sellPrice));
                }
            }
        }
        return sellPrices;
    }

    public double getSellPrice() {
        return this.sellPrice;
    }
}
