package me.deathrealms.shopgui;

import com.google.common.collect.Lists;
import me.deathrealms.realmsapi.files.Serializer;

import java.util.List;

import static me.deathrealms.realmsapi.utils.ChatUtils.format;

public class Config {
    private static final transient Config instance = new Config();
    public static String shopTitle = format("&cShop");
    public static String sellTitle = format("&cSell");
    public static int shopRows = 2;
    public static boolean perShopPermissions = true;
    public static String prefix = format("&8[&aShopGUI&8] ");
    public static String noPermissionMessage = format("&cYou do not have permission to do this.");
    public static String reloadConfigMessage = format("&7You have reloaded all configuration files.");
    public static String noPermissionForShop = format("&cYou do not have permission for this shop.");
    public static String shopNotFoundMessage = format("&cShop not found.");
    public static String cannotAffordPurchase = format("&cYou cannot afford to purchase this.");
    public static String cannotBuyItem = format("&cYou cannot buy this item.");
    public static String cannotSellItem = format("&cYou cannot sell this item.");
    public static String noItemsToSell = format("&cYou do not have any items to sell.");
    public static String notEnoughItemsToSell = format("&cYou do not have enough items to sell.");
    public static String itemPurchased = format("&aYou have purchased &f%amount% %item% &afor &f$%money%");
    public static String itemSold = format("&aYou have sold &f%amount% %item% &afor &f$%money%");
    public static String middleClickSellAll = format("&aYou sold &f%amount% %item% &afor &f$%money%");
    public static String sellAllMessage = format("&aSold items for &f$%money%");
    public static List<String> buyLore = format(Lists.newArrayList("&7Buy Price: &c$%buyprice%"));
    public static List<String> sellLore = format(Lists.newArrayList("&7Sell Price: &a$%sellprice%", "&9Middle click to sell all"));

    public static void load() {
        new Serializer().load(instance, Config.class, "config");
    }
}
