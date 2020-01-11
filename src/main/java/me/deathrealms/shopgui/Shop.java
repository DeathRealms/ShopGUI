package me.deathrealms.shopgui;

import me.deathrealms.realmsapi.RealmsAPI;
import me.deathrealms.realmsapi.files.CustomFile;
import me.deathrealms.shopgui.commands.SellAllCommand;
import me.deathrealms.shopgui.commands.SellCommand;
import me.deathrealms.shopgui.commands.ShopCommand;
import me.deathrealms.shopgui.listeners.SellGUIListener;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;

public class Shop extends RealmsAPI {
    private static CustomFile shops;
    private static Economy economy;

    public static Economy getEconomy() {
        return economy;
    }

    public static CustomFile getShops() {
        return shops;
    }

    @Override
    public void onEnable() {
        if (!setupEconomy()) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        super.onEnable();
        setupFiles();
        getServer().getPluginManager().registerEvents(new SellGUIListener(), this);
        new ShopCommand();
        new SellCommand();
        new SellAllCommand();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) return false;
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) return false;
        economy = rsp.getProvider();
        return economy != null;
    }

    private void setupFiles() {
        shops = new CustomFile("shops");
        Config.load();
        shops.createFile(true);
    }
}
