package me.deathrealms.shopgui.utils;

import me.deathrealms.realmsapi.XMaterial;
import me.deathrealms.shopgui.Shop;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;

public class SellPrice {
    private static final Map<XMaterial, SellPrice> sellPrices;

    static {
        sellPrices = new HashMap<>();
    }

    private final double sellPrice;

    public SellPrice(double sellPrice) {
        this.sellPrice = sellPrice;
    }

    public static Map<XMaterial, SellPrice> getSellPrices() {
        ConfigurationSection categories = Shop.getShops().getConfigSection("categories");
        for (String shop : categories.getKeys(false)) {
            ConfigurationSection config = Shop.getShops().getConfigSection(shop + ".items");
            if (config != null) {
                for (String str : config.getKeys(false)) {
                    int damage = config.getInt(str + ".item.damage", 0);
                    XMaterial material;
                    if (NumberUtils.isNumber(config.getString(str + ".item.material"))) {
                        material = XMaterial.matchXMaterial(Integer.parseInt(config.getString(str + ".item.material")), (byte) damage);
                    } else {
                        material = XMaterial.matchXMaterial(config.getString(str + ".item.material"), (byte) damage);
                    }
                    int amount = config.getInt(str + ".item.amount", 1);
                    double sellPrice = (config.getDouble(str + ".sell-price") / amount);
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
