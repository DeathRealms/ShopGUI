package me.deathrealms.shopgui.commands;

import me.deathrealms.realmsapi.command.Command;
import me.deathrealms.realmsapi.user.User;
import me.deathrealms.shopgui.Config;
import me.deathrealms.shopgui.listeners.SellGUIListener;

public class SellCommand extends Command {

    public SellCommand() {
        super("sell", false);
    }

    @Override
    public void run(User user, String label, String[] args) {
        if (!user.isAuthorized("shopgui.command.sell")) {
            user.sendMessage(Config.noPermissionMessage);
        } else {
            new SellGUIListener().open(user);
        }
    }
}
