package me.deathrealms.shopgui.gui;

import me.deathrealms.realmsapi.RealmsAPI;
import me.deathrealms.realmsapi.XMaterial;
import me.deathrealms.realmsapi.inventories.Button;
import me.deathrealms.realmsapi.inventories.PagedGUI;
import me.deathrealms.realmsapi.user.User;
import me.deathrealms.shopgui.Config;
import me.deathrealms.shopgui.Shop;
import me.deathrealms.shopgui.utils.ShopItem;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CategoryGUI extends PagedGUI {
    private String category;
    private String title;
    private int rows;

    public CategoryGUI(User user, String shop, String title, int rows) {
        super(title, rows, new Button(XMaterial.BOOK.parseItem(), meta -> meta.setDisplayName("&7Return to Main Menu"), event -> new ShopGUI().open(user)),
                true);
        this.category = shop;
        this.title = title;
        this.rows = rows;
    }

    private int removeItem(Inventory inventory, Material material) {
        final ItemStack toRemove = new ItemStack(material, Integer.MAX_VALUE);
        final ItemStack notRemoved = inventory.removeItem(toRemove).get(0);
        return notRemoved == null ? Integer.MAX_VALUE : Integer.MAX_VALUE - notRemoved.getAmount();
    }

    @Override
    public void build() {
        ConfigurationSection categories = Shop.getShops().getConfigSection(category);
        if (categories != null) {
            ConfigurationSection items = Shop.getShops().getConfigSection(category + ".items");
            if (items != null) {
                for (String item : items.getKeys(false)) {
                    XMaterial material;
                    int damage = items.getInt(item + ".item.damage", 0);
                    if (NumberUtils.isNumber(items.getString(item + ".item.material"))) {
                        material = XMaterial.matchXMaterial(Integer.parseInt(items.getString(item + ".item.material")), (byte) damage);
                    } else {
                        material = XMaterial.matchXMaterial(items.getString(item + ".item.material"), (byte) damage);
                    }
                    String displayName = items.getString(item + ".item.name");
                    int amount = items.getInt(item + ".item.amount", 1);
                    int maxStackSize = items.getInt(item + ".item.max-stack", 64);
                    boolean stackable = items.getBoolean(item + ".stackable", true);
                    double buyPrice = (items.getDouble(item + ".buy-price") / amount);
                    double sellPrice = (items.getDouble(item + ".sell-price") / amount);
                    boolean extendedPotion = items.getBoolean(item + ".extended", false);
                    boolean spawner = items.getBoolean(item + ".item.spawner");
                    String mobType = items.getString(item + ".item.mob");
                    List<String> commands = items.getStringList(item + ".commands");

                    ShopItem shopItem = new ShopItem();
                    shopItem.setMaterial(material);
                    shopItem.setDisplayName(displayName);
                    shopItem.setAmount(amount);
                    shopItem.setMaxStackSize(maxStackSize);
                    shopItem.setStackable(stackable);
                    shopItem.setBuyPrice(buyPrice);
                    shopItem.setSellPrice(sellPrice);
                    shopItem.setExtendedPotion(extendedPotion);
                    shopItem.setSpawner(spawner);
                    shopItem.setMobType(mobType);
                    shopItem.setCommands(commands);

                    add(material, meta -> {
                        if (displayName != null) meta.setDisplayName(displayName);
                        if (items.get(item + ".buy-price") != null) {
                            for (String lore : Config.buyLore) {
                                meta.addLoreLine(lore.replace("%buyprice%", String.valueOf((buyPrice * amount))));
                            }
                        }
                        if (items.get(item + ".sell-price") != null) {
                            for (String lore : Config.sellLore) {
                                meta.addLoreLine(lore.replace("%sellprice%", String.valueOf((sellPrice * amount))));
                            }
                        }
                        meta.setAmount(amount);
                        meta.setDamage(damage);
                        if (extendedPotion) meta.setPotionData(extendedPotion);
                    }, event -> {
                        User user = RealmsAPI.getUser(event.getWhoClicked().getUniqueId());
                        switch (event.getClick()) {
                            case LEFT: {
                                if (items.get(item + ".buy-price") == null) {
                                    user.sendMessage(Config.prefix + Config.cannotBuyItem);
                                } else {
                                    new BuyGUI(category, title, rows, material, shopItem).open(user);
                                }
                                return;
                            }
                            case RIGHT: {
                                if (items.get(item + ".sell-price") == null) {
                                    user.sendMessage(Config.prefix + Config.cannotSellItem);
                                } else {
                                    new SellGUI(category, title, rows, material, shopItem).open(user);
                                }
                                return;
                            }
                            case MIDDLE: {
                                if (items.get(item + ".sell-price") == null) {
                                    user.sendMessage(Config.prefix + Config.cannotSellItem);
                                } else {
                                    int amountSold = removeItem(user.getInventory(), material.parseMaterial());
                                    if (amountSold > 0) {
                                        double total = Math.round((sellPrice * amountSold) * 100.0) / 100.0;
                                        user.sendMessage(Config.prefix + Config.middleClickSellAll
                                                .replace("%amount%", String.valueOf(amountSold))
                                                .replace("%item%", material.toWord().trim())
                                                .replace("%money%", String.valueOf(total)));
                                        Shop.getEconomy().depositPlayer(user.getOfflineBase(), (sellPrice * amountSold));
                                    } else {
                                        user.sendMessage(Config.prefix + Config.noItemsToSell);
                                    }
                                }
                            }
                        }
                    });
                }
            }
        }
    }
}
