package me.deathrealms.shopgui.commands;

import me.deathrealms.realmsapi.XSound;
import me.deathrealms.realmsapi.command.SubCommand;
import me.deathrealms.realmsapi.source.CommandSource;
import me.deathrealms.shopgui.Config;
import me.deathrealms.shopgui.Shop;

public class ReloadCommand extends SubCommand {

    public ReloadCommand() {
        super("reload", "shopgui.command.reload", true);
    }

    @Override
    protected void run(CommandSource source, String cmd, String label, String[] args) {
        Config.load();
        Shop.getShops().reloadConfig();
        source.sendMessage(Config.prefix + Config.reloadConfigMessage);
        if (source.isPlayer()) {
            source.getUser().playSound(XSound.BLOCK_NOTE_BLOCK_PLING);
        }
    }
}
