package me.deathrealms.shopgui.commands;

import me.deathrealms.realmsapi.XMaterial;
import me.deathrealms.realmsapi.command.Command;
import me.deathrealms.realmsapi.user.User;
import me.deathrealms.shopgui.Config;
import me.deathrealms.shopgui.Shop;
import me.deathrealms.shopgui.utils.SellPrice;
import org.bukkit.inventory.ItemStack;

public class SellAllCommand extends Command {

    public SellAllCommand() {
        super("sellall");
    }

    @Override
    public void run(User user, String label, String[] args) {
        if (!user.isAuthorized("shopgui.command.sellall")) {
            user.sendMessage(Config.noPermissionMessage);
        } else {
            double total = 0.0;
            for (ItemStack item : user.getInventory().getContents()) {
                double sellPrice;
                if (item != null) {
                    XMaterial material = XMaterial.matchXMaterial(item);
                    if (!SellPrice.getSellPrices().containsKey(material) || material.isSpawner()) continue;
                    sellPrice = SellPrice.getSellPrices().get(material).getSellPrice();
                    int quantity = item.getAmount();
                    total += quantity * sellPrice;
                    user.getInventory().removeItem(item);
                }
            }
            double totalRounded = Math.round(total * 100.0) / 100.0;
            Shop.getEconomy().depositPlayer(user.getOfflineBase(), totalRounded);
            if (totalRounded > 0.0) {
                user.sendMessage(Config.prefix + Config.sellAllMessage.replace("%money%", String.valueOf(totalRounded)));
            } else {
                user.sendMessage(Config.prefix + Config.noItemsToSell);
            }
        }
    }
}
