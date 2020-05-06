package me.deathrealms.shopgui.gui;

import me.deathrealms.realmsapi.RealmsAPI;
import me.deathrealms.realmsapi.XMaterial;
import me.deathrealms.realmsapi.inventories.GUI;
import me.deathrealms.realmsapi.items.ItemBuilder;
import me.deathrealms.realmsapi.user.User;
import me.deathrealms.realmsapi.utils.ChatUtils;
import me.deathrealms.shopgui.Config;
import me.deathrealms.shopgui.Shop;
import me.deathrealms.shopgui.utils.ShopItem;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class BuyStacksGUI extends GUI {
    private final Plugin silkSpawners;
    private final Plugin epicSpawners;
    private final String shop;
    private final String title;
    private final int rows;
    private final XMaterial material;
    private final ShopItem shopItem;
    private final int maxStackSize;
    private final boolean stackable;
    private final double buyPrice;
    private final boolean extendedPotion;
    private final boolean spawner;
    private final String mobType;
    private final List<String> commands;

    public BuyStacksGUI(String shop, String title, int rows, XMaterial material, ShopItem shopItem) {
        super("&cBuying Stacks of " + material.toWord(), 2);
        this.silkSpawners = Bukkit.getPluginManager().getPlugin("SilkSpawners");
        this.epicSpawners = Bukkit.getPluginManager().getPlugin("EpicSpawners");
        this.shop = shop;
        this.title = title;
        this.rows = rows;
        this.material = material;
        this.shopItem = shopItem;
        this.maxStackSize = shopItem.getMaxStackSize();
        this.stackable = shopItem.isStackable();
        this.buyPrice = shopItem.getBuyPrice();
        this.extendedPotion = shopItem.isExtendedPotion();
        this.spawner = shopItem.isSpawner();
        this.mobType = shopItem.getMobType();
        this.commands = shopItem.getCommands();
    }

    private void buyStacks(User user, int stacks) {
        int amount = (stacks * maxStackSize);
        ItemBuilder builder = new ItemBuilder(material);
        if (stackable) builder.setAmount(amount);
        ItemStack item = builder.build();
        if (spawner) item = null;
        if (!(Shop.getEconomy().getBalance(user.getOfflineBase()) >= (buyPrice * amount))) {
            user.sendMessage(Config.prefix + Config.cannotAffordPurchase);
        } else {
            Shop.getEconomy().withdrawPlayer(user.getOfflineBase(), (buyPrice * amount));
            if (item != null) {
                if (!stackable) {
                    for (int i = 0; i < amount; i++) {
                        if (!user.getInventory().addItem(item).isEmpty()) {
                            user.getLocation().getWorld().dropItemNaturally(user.getLocation(), item);
                        }
                    }
                } else {
                    if (!user.getInventory().addItem(item).isEmpty()) {
                        user.getLocation().getWorld().dropItemNaturally(user.getLocation(), item);
                    }
                }
            }
            if (spawner) {
                if (silkSpawners != null) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "silkspawners give " + user.getName() + " " + mobType + " " + amount);
                } else if (epicSpawners != null) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "epicspawners give " + user.getName() + " " + mobType + " " + amount);
                } else return;
            }
            user.sendMessage(Config.itemPurchased
                    .replace("%amount%", stacks + (stacks == 1 ? "x Stack of" : "x Stacks of"))
                    .replace("%item%", spawner ? WordUtils.capitalizeFully(mobType) + " Spawners" : material.toWord())
                    .replace("%money%", String.valueOf((buyPrice * amount))));
            for (String command : commands) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ChatUtils.parsePlaceholders(user, command));
            }
            new CategoryGUI(user, shop, title, rows).open(user);
        }
    }

    @Override
    public void build() {
        set(0, item(material, meta -> {
            if (extendedPotion) meta.setPotionData(extendedPotion);
            meta.setDisplayName("&e1 Stack");
            meta.setLore("&7Buy Price: &c$" + (buyPrice * maxStackSize));
        }), event -> {
            User user = RealmsAPI.getUser(event.getWhoClicked().getUniqueId());
            buyStacks(user, 1);
        });

        set(1, item(material, meta -> {
            if (extendedPotion) meta.setPotionData(extendedPotion);
            meta.setAmount(2);
            meta.setDisplayName("&e2 Stacks");
            meta.setLore("&7Buy Price: &c$" + (buyPrice * (maxStackSize * 2)));
        }), event -> buyStacks(RealmsAPI.getUser(event.getWhoClicked().getUniqueId()), 2));

        set(2, item(material, meta -> {
            if (extendedPotion) meta.setPotionData(extendedPotion);
            meta.setAmount(3);
            meta.setDisplayName("&e3 Stacks");
            meta.setLore("&7Buy Price: &c$" + (buyPrice * (maxStackSize * 3)));
        }), event -> buyStacks(RealmsAPI.getUser(event.getWhoClicked().getUniqueId()), 3));

        set(3, item(material, meta -> {
            if (extendedPotion) meta.setPotionData(extendedPotion);
            meta.setAmount(4);
            meta.setDisplayName("&e4 Stacks");
            meta.setLore("&7Buy Price: &c$" + (buyPrice * (maxStackSize * 4)));
        }), event -> buyStacks(RealmsAPI.getUser(event.getWhoClicked().getUniqueId()), 4));

        set(4, item(material, meta -> {
            if (extendedPotion) meta.setPotionData(extendedPotion);
            meta.setAmount(5);
            meta.setDisplayName("&e5 Stacks");
            meta.setLore("&7Buy Price: &c$" + (buyPrice * (maxStackSize * 5)));
        }), event -> buyStacks(RealmsAPI.getUser(event.getWhoClicked().getUniqueId()), 5));

        set(5, item(material, meta -> {
            if (extendedPotion) meta.setPotionData(extendedPotion);
            meta.setAmount(6);
            meta.setDisplayName("&e6 Stacks");
            meta.setLore("&7Buy Price: &c$" + (buyPrice * (maxStackSize * 6)));
        }), event -> buyStacks(RealmsAPI.getUser(event.getWhoClicked().getUniqueId()), 6));

        set(6, item(material, meta -> {
            if (extendedPotion) meta.setPotionData(extendedPotion);
            meta.setAmount(7);
            meta.setDisplayName("&e7 Stacks");
            meta.setLore("&7Buy Price: &c$" + (buyPrice * (maxStackSize * 7)));
        }), event -> buyStacks(RealmsAPI.getUser(event.getWhoClicked().getUniqueId()), 7));

        set(7, item(material, meta -> {
            if (extendedPotion) meta.setPotionData(extendedPotion);
            meta.setAmount(8);
            meta.setDisplayName("&e8 Stacks");
            meta.setLore("&7Buy Price: &c$" + (buyPrice * (maxStackSize * 8)));
        }), event -> buyStacks(RealmsAPI.getUser(event.getWhoClicked().getUniqueId()), 8));

        set(8, item(material, meta -> {
            if (extendedPotion) meta.setPotionData(extendedPotion);
            meta.setAmount(9);
            meta.setDisplayName("&e9 Stacks");
            meta.setLore("&7Buy Price: &c$" + (buyPrice * (maxStackSize * 9)));
        }), event -> buyStacks(RealmsAPI.getUser(event.getWhoClicked().getUniqueId()), 9));

        set(13, item(XMaterial.ARROW, meta -> meta.setDisplayName("&cBack")), event -> {
            User user = RealmsAPI.getUser(event.getWhoClicked().getUniqueId());
            new BuyGUI(shop, title, rows, material, shopItem).open(user);
        });
    }
}
