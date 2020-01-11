package me.deathrealms.shopgui.commands;

import me.deathrealms.realmsapi.command.Command;
import me.deathrealms.realmsapi.user.User;
import me.deathrealms.shopgui.Config;
import me.deathrealms.shopgui.Shop;
import me.deathrealms.shopgui.gui.CategoryGUI;
import me.deathrealms.shopgui.gui.ShopGUI;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class ShopCommand extends Command {

    public ShopCommand() {
        super("shop");
    }

    @Override
    public void run(User user, String label, String[] args) {
        if (!user.isAuthorized("shopgui.command.shop")) {
            user.sendMessage(Config.noPermissionMessage);
        } else {
            if (args.length == 0) {
                new ShopGUI().open(user);
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("reload")) {
                    Config.load();
                    Shop.getShops().reloadConfig();
                    user.sendMessage(Config.prefix + Config.reloadConfigMessage);
                    return;
                }
                ConfigurationSection categories = Shop.getShops().getConfigSection("categories");
                String title = categories.getString(args[0].toLowerCase() + ".name");
                int rows = categories.getInt(args[0].toLowerCase() + ".rows", 6);
                if (title == null) {
                    user.sendMessage(Config.prefix + Config.shopNotFoundMessage.replace("%shop%", args[0].toLowerCase()));
                } else {
                    new CategoryGUI(user, args[0].toLowerCase(), title, rows).open(user);
                }
            } else {
                new ShopGUI().open(user);
            }
        }
    }

    @Override
    public List<String> tabCompleteOptions(User user, String[] args) {
        if (args.length == 1 && user.isAuthorized("shopgui.command.shop")) {
            ConfigurationSection categories = Shop.getShops().getConfigSection("categories");
            return new ArrayList<>(categories.getKeys(false));
        } else {
            return emptyList();
        }
    }
}
