package me.deathrealms.shopgui.gui;

import me.deathrealms.realmsapi.RealmsAPI;
import me.deathrealms.realmsapi.XMaterial;
import me.deathrealms.realmsapi.inventories.GUI;
import me.deathrealms.realmsapi.user.User;
import me.deathrealms.shopgui.Config;
import me.deathrealms.shopgui.Shop;
import me.deathrealms.shopgui.utils.ShopItem;
import org.bukkit.inventory.ItemStack;

public class SellStacksGUI extends GUI {
    private final String shop;
    private final String title;
    private final int rows;
    private final XMaterial material;
    private final ShopItem shopItem;
    private final int maxStackSize;
    private final double sellPrice;
    private final boolean extendedPotion;

    public SellStacksGUI(String shop, String title, int rows, XMaterial material, ShopItem shopItem) {
        super("&aSelling Stacks of " + material.toWord(), 2);
        this.shop = shop;
        this.title = title;
        this.rows = rows;
        this.material = material;
        this.shopItem = shopItem;
        this.maxStackSize = shopItem.getMaxStackSize();
        this.sellPrice = shopItem.getSellPrice();
        this.extendedPotion = shopItem.isExtendedPotion();
    }

    private void sellStacks(User user, int stacks) {
        int amount = (stacks * maxStackSize);
        ItemStack item = new ItemStack(material.parseMaterial(), amount);
        if (!user.getInventory().containsAtLeast(item, amount)) {
            user.sendMessage(Config.prefix + Config.notEnoughItemsToSell);
        } else {
            Shop.getEconomy().withdrawPlayer(user.getOfflineBase(), (sellPrice * amount));
            user.sendMessage(Config.itemSold
                    .replace("%amount%", stacks + (stacks == 1 ? "x Stack of" : "x Stacks of"))
                    .replace("%item%", material.toWord())
                    .replace("%money%", String.valueOf((Math.round((sellPrice * amount) * 100.0) / 100.0))));
            user.getInventory().removeItem(item);
            new CategoryGUI(user, shop, title, rows).open(user);
        }
    }

    @Override
    public void build() {
        set(0, item(material, meta -> {
            if (extendedPotion) meta.setPotionData(extendedPotion);
            meta.setDisplayName("&e1 Stack");
            meta.setLore("&7Sell Price: &a$" + (Math.round((sellPrice * maxStackSize) * 100.0) / 100.0));
        }), event -> {
            User user = RealmsAPI.getUser(event.getWhoClicked().getUniqueId());
            sellStacks(user, 1);
        });

        set(1, item(material, meta -> {
            if (extendedPotion) meta.setPotionData(extendedPotion);
            meta.setAmount(2);
            meta.setDisplayName("&e2 Stacks");
            meta.setLore("&7Sell Price: &a$" + (Math.round((sellPrice * (maxStackSize * 2) * 100.0) / 100.0)));
        }), event -> sellStacks(RealmsAPI.getUser(event.getWhoClicked().getUniqueId()), 2));

        set(2, item(material, meta -> {
            if (extendedPotion) meta.setPotionData(extendedPotion);
            meta.setAmount(3);
            meta.setDisplayName("&e3 Stacks");
            meta.setLore("&7Sell Price: &a$" + (Math.round((sellPrice * (maxStackSize * 3) * 100.0) / 100.0)));
        }), event -> sellStacks(RealmsAPI.getUser(event.getWhoClicked().getUniqueId()), 3));

        set(3, item(material, meta -> {
            if (extendedPotion) meta.setPotionData(extendedPotion);
            meta.setAmount(4);
            meta.setDisplayName("&e4 Stacks");
            meta.setLore("&7Sell Price: &a$" + (Math.round((sellPrice * (maxStackSize * 4) * 100.0) / 100.0)));
        }), event -> sellStacks(RealmsAPI.getUser(event.getWhoClicked().getUniqueId()), 4));

        set(4, item(material, meta -> {
            if (extendedPotion) meta.setPotionData(extendedPotion);
            meta.setAmount(5);
            meta.setDisplayName("&e5 Stacks");
            meta.setLore("&7Sell Price: &a$" + (Math.round((sellPrice * (maxStackSize * 5) * 100.0) / 100.0)));
        }), event -> sellStacks(RealmsAPI.getUser(event.getWhoClicked().getUniqueId()), 5));

        set(5, item(material, meta -> {
            if (extendedPotion) meta.setPotionData(extendedPotion);
            meta.setAmount(6);
            meta.setDisplayName("&e6 Stacks");
            meta.setLore("&7Sell Price: &a$" + (Math.round((sellPrice * (maxStackSize * 6) * 100.0) / 100.0)));
        }), event -> sellStacks(RealmsAPI.getUser(event.getWhoClicked().getUniqueId()), 6));

        set(6, item(material, meta -> {
            if (extendedPotion) meta.setPotionData(extendedPotion);
            meta.setAmount(7);
            meta.setDisplayName("&e7 Stacks");
            meta.setLore("&7Sell Price: &a$" + (Math.round((sellPrice * (maxStackSize * 7) * 100.0) / 100.0)));
        }), event -> sellStacks(RealmsAPI.getUser(event.getWhoClicked().getUniqueId()), 7));

        set(7, item(material, meta -> {
            if (extendedPotion) meta.setPotionData(extendedPotion);
            meta.setAmount(8);
            meta.setDisplayName("&e8 Stacks");
            meta.setLore("&7Sell Price: &a$" + (Math.round((sellPrice * (maxStackSize * 8) * 100.0) / 100.0)));
        }), event -> sellStacks(RealmsAPI.getUser(event.getWhoClicked().getUniqueId()), 8));

        set(8, item(material, meta -> {
            if (extendedPotion) meta.setPotionData(extendedPotion);
            meta.setAmount(9);
            meta.setDisplayName("&e9 Stacks");
            meta.setLore("&7Sell Price: &a$" + (Math.round((sellPrice * (maxStackSize * 9) * 100.0) / 100.0)));
        }), event -> sellStacks(RealmsAPI.getUser(event.getWhoClicked().getUniqueId()), 9));

        set(13, item(XMaterial.ARROW, meta -> meta.setDisplayName("&cBack")), event -> {
            User user = RealmsAPI.getUser(event.getWhoClicked().getUniqueId());
            new SellGUI(shop, title, rows, material, shopItem).open(user);
        });
    }
}
