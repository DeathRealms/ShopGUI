package me.deathrealms.shopgui.listeners;

import me.deathrealms.realmsapi.RealmsAPI;
import me.deathrealms.realmsapi.XMaterial;
import me.deathrealms.realmsapi.items.ItemBuilder;
import me.deathrealms.realmsapi.user.User;
import me.deathrealms.shopgui.Config;
import me.deathrealms.shopgui.Shop;
import me.deathrealms.shopgui.utils.SellPrice;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SellGUIListener implements Listener {
    private final Inventory inventory = Bukkit.createInventory(null, 5 * 9, Config.sellTitle);
    private final ItemStack sellAll = new ItemBuilder(XMaterial.NETHER_STAR).setDisplayName("&cSell All").build();

    public void open(User user) {
        inventory.setItem(40, sellAll);
        user.openInventory(inventory);
    }

    @EventHandler
    public void onInvClose(InventoryCloseEvent event) {
        if (event.getView().getTitle().equals(Config.sellTitle)) {
            User user = RealmsAPI.getUser(event.getPlayer().getUniqueId());
            double total = 0.0;
            boolean couldNotSell = false;
            int amountNotSold = 0;
            for (ItemStack item : user.getBase().getOpenInventory().getTopInventory().getContents()) {
                double sellPrice;
                if (item != null && !item.isSimilar(sellAll)) {
                    XMaterial material = XMaterial.matchXMaterial(item);
                    if (!SellPrice.getSellPrices().containsKey(material) || material.isSpawner()) {
                        ++amountNotSold;
                        couldNotSell = true;
                        user.getInventory().addItem(item);
                        continue;
                    }
                    sellPrice = SellPrice.getSellPrices().get(material).getSellPrice();
                    int quantity = item.getAmount();
                    total += quantity * sellPrice;
                }
            }
            if (couldNotSell) {
                if (amountNotSold == 1) {
                    user.sendMessage(Config.prefix + "&cCould not sell &f" + amountNotSold + " &citem");
                } else {
                    user.sendMessage(Config.prefix + "&cCould not sell &f" + amountNotSold + " &citems");
                }
            }
            double totalRounded = Math.round(total * 100.0) / 100.0;
            Shop.getEconomy().depositPlayer(user.getOfflineBase(), totalRounded);
            if (totalRounded > 0.0) {
                user.sendMessage(Config.prefix + Config.sellAllMessage.replace("%money%", String.valueOf(totalRounded)));
            }
        }
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(Config.sellTitle)) {
            if (event.getCurrentItem() == null) return;
            if (event.getRawSlot() == 40) {
                event.setCancelled(true);
                Player player = (Player) event.getWhoClicked();
                player.performCommand("sellall");
                player.closeInventory();
            }
        }
    }
}
