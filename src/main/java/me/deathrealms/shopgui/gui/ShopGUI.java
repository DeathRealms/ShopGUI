package me.deathrealms.shopgui.gui;

import me.deathrealms.realmsapi.XMaterial;
import me.deathrealms.realmsapi.inventories.PagedGUI;
import me.deathrealms.realmsapi.items.ItemUtils;
import me.deathrealms.realmsapi.user.User;
import me.deathrealms.shopgui.Config;
import me.deathrealms.shopgui.Shop;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.configuration.ConfigurationSection;

public class ShopGUI extends PagedGUI {
    private final User user;

    public ShopGUI(User user) {
        super(Config.shopTitle, Config.shopRows, true);
        this.user = user;
    }

    @Override
    public void build() {
        ConfigurationSection categories = Shop.getShops().getConfigSection("categories");
        for (String category : categories.getKeys(false)) {
            ConfigurationSection shops = Shop.getShops().getConfigSection(category);
            String materialName = categories.getString(category + ".material");
            int damage = categories.getInt(category + ".damage", 0);
            String name = categories.getString(category + ".name");
            XMaterial material;
            if (NumberUtils.isNumber(materialName)) {
                material = XMaterial.matchXMaterial(Integer.parseInt(materialName), (byte) damage);
            } else {
                material = XMaterial.matchXMaterial(materialName, (byte) damage);
            }
            if (Config.perShopPermissions) {
                if (!user.isAuthorized("shopgui.shop." + category.toLowerCase())) {
                    material = XMaterial.RED_STAINED_GLASS_PANE;
                }
            }
            add(material, meta -> {
                meta.addNBTString("category", category);
                meta.setDisplayName(name);
                meta.setDamage(damage);
                if (Config.perShopPermissions) {
                    if (!user.isAuthorized("shopgui.shop." + category.toLowerCase())) {
                        meta.setLore(Config.noPermissionForShop);
                    }
                }
            }, event -> {
                String title;
                if (shops != null) {
                    title = shops.getString("name", categories.getString(ItemUtils.getNBTString(event.getCurrentItem(), "category") + ".name"));
                } else {
                    title = categories.getString(ItemUtils.getNBTString(event.getCurrentItem(), "category") + ".name");
                }
                int rows = categories.getInt(ItemUtils.getNBTString(event.getCurrentItem(), "category") + ".rows", 6);
                if (rows == 1) rows = 2;
                if (Config.perShopPermissions) {
                    if (!user.isAuthorized("shopgui.shop." + category.toLowerCase())) {
                        user.sendMessage(Config.prefix + Config.noPermissionForShop);
                    } else {
                        new CategoryGUI(user, category, title, rows).open(user);
                    }
                } else {
                    new CategoryGUI(user, category, title, rows).open(user);
                }
            });
        }
    }
}
