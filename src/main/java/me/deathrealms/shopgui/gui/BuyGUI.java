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

public class BuyGUI extends GUI {
    private final Plugin silkSpawners;
    private final Plugin epicSpawners;
    private final String shop;
    private final String title;
    private final int rows;
    private final XMaterial material;
    private final ShopItem shopItem;
    private final String displayName;
    private final int maxStackSize;
    private final boolean stackable;
    private final double buyPrice;
    private final boolean extendedPotion;
    private final boolean spawner;
    private final String mobType;
    private final List<String> commands;
    private int amount;

    public BuyGUI(String shop, String title, int rows, XMaterial material, ShopItem shopItem) {
        super("&cBuying " + material.toWord(), 6);
        this.silkSpawners = Bukkit.getPluginManager().getPlugin("SilkSpawners");
        this.epicSpawners = Bukkit.getPluginManager().getPlugin("EpicSpawners");
        this.shop = shop;
        this.title = title;
        this.rows = rows;
        this.material = material;
        this.shopItem = shopItem;
        this.displayName = shopItem.getDisplayName();
        this.amount = shopItem.getAmount();
        this.maxStackSize = shopItem.getMaxStackSize();
        this.stackable = shopItem.isStackable();
        this.buyPrice = shopItem.getBuyPrice();
        this.extendedPotion = shopItem.isExtendedPotion();
        this.spawner = shopItem.isSpawner();
        this.mobType = shopItem.getMobType();
        this.commands = shopItem.getCommands();
    }

    private void amountAddCheck() {
        if ((amount + 1) > maxStackSize) {
            set(24, null, clickEvent -> clickEvent.setCancelled(true));
        }
        if ((amount + 10) > maxStackSize) {
            set(25, null, clickEvent -> clickEvent.setCancelled(true));
        }
        if (amount == maxStackSize) {
            set(26, null, clickEvent -> clickEvent.setCancelled(true));
        }
    }

    private void amountTakeCheck() {
        if ((amount - 1) < 1) {
            set(20, null, clickEvent -> clickEvent.setCancelled(true));
        }
        if ((amount - 10) < 1) {
            set(19, null, clickEvent -> clickEvent.setCancelled(true));
        }
        if (amount == 1) {
            set(18, null, clickEvent -> clickEvent.setCancelled(true));
        }
    }

    private void updateItem(User user) {
        set(22, item(material, meta -> {
            if (displayName != null) meta.setDisplayName(displayName);
            meta.setAmount(amount);
        }), clickEvent -> clickEvent.setCancelled(true));
        open(user);
        amountAddCheck();
        amountTakeCheck();
    }

    @Override
    public void build() {
        set(18, item(XMaterial.RED_STAINED_GLASS_PANE, meta -> meta.setDisplayName("&cSet to 1").setAmount(1)), event -> {
            amount = 1;
            updateItem(RealmsAPI.getUser(event.getWhoClicked().getUniqueId()));
        });

        set(19, item(XMaterial.RED_STAINED_GLASS_PANE, meta -> meta.setDisplayName("&cRemove 10").setAmount(10)), event -> {
            if ((amount - 10) >= 1) {
                for (int i = 0; i < 10; i++) {
                    --amount;
                }
            }
            updateItem(RealmsAPI.getUser(event.getWhoClicked().getUniqueId()));
        });

        set(20, item(XMaterial.RED_STAINED_GLASS_PANE, meta -> meta.setDisplayName("&cRemove 1")), event -> {
            if (amount > 1) --amount;
            updateItem(RealmsAPI.getUser(event.getWhoClicked().getUniqueId()));
        });

        set(22, item(material, meta -> {
            if (extendedPotion) meta.setPotionData(extendedPotion);
            if (displayName != null) meta.setDisplayName(displayName);
            meta.setAmount(amount);
            meta.setLore("&7Buy Price: &c$" + (buyPrice * amount));
        }), event -> event.setCancelled(true));

        set(24, item(XMaterial.LIME_STAINED_GLASS_PANE, meta -> meta.setDisplayName("&aAdd 1")), event -> {
            if (amount < maxStackSize) ++amount;
            updateItem(RealmsAPI.getUser(event.getWhoClicked().getUniqueId()));
        });

        set(25, item(XMaterial.LIME_STAINED_GLASS_PANE, meta -> meta.setDisplayName("&aAdd 10").setAmount(10)), event -> {
            if ((amount + 10) <= maxStackSize) {
                for (int i = 0; i < 10; i++) {
                    ++amount;
                }
            }
            updateItem(RealmsAPI.getUser(event.getWhoClicked().getUniqueId()));
        });

        set(26, item(XMaterial.LIME_STAINED_GLASS_PANE, meta -> {
            meta.setDisplayName("&aSet to " + maxStackSize);
            meta.setAmount(maxStackSize);
        }), event -> {
            amount = maxStackSize;
            updateItem(RealmsAPI.getUser(event.getWhoClicked().getUniqueId()));
        });

        set(39, item(XMaterial.LIME_STAINED_GLASS_PANE, meta -> meta.setDisplayName("&aPurchase")), event -> {
            User user = RealmsAPI.getUser(event.getWhoClicked().getUniqueId());
            ItemBuilder builder = new ItemBuilder(material);
            if (stackable) builder.setAmount(amount);
            if (extendedPotion) builder.setPotionData(extendedPotion);
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
                        .replace("%amount%", amount + "x")
                        .replace("%item%", spawner ? WordUtils.capitalizeFully(mobType.replace("_", " ")) + (amount == 1 ? " Spawner" : " Spawners")
                                : material.toWord())
                        .replace("%money%", String.valueOf((buyPrice * amount))));
                for (String command : commands) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ChatUtils.parsePlaceholders(user, command));
                }
            }
            new CategoryGUI(user, shop, title, rows).open(user);
        });

        set(41, item(XMaterial.RED_STAINED_GLASS_PANE, meta -> meta.setDisplayName("&cCancel")), event -> {
            User user = RealmsAPI.getUser(event.getWhoClicked().getUniqueId());
            new CategoryGUI(user, shop, title, rows).open(user);
        });

        set(49, item(XMaterial.HOPPER, meta -> meta.setDisplayName("&eBuy Stacks")), event -> {
            User user = RealmsAPI.getUser(event.getWhoClicked().getUniqueId());
            new BuyStacksGUI(shop, title, rows, material, shopItem).open(user);
        });
        amountAddCheck();
        amountTakeCheck();
    }
}
