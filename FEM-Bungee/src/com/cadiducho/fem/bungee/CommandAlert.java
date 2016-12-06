package com.cadiducho.fem.bungee;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;

public class CommandAlert extends Command{

    public CommandAlert(){
        super("gritar", "bungee.alert", new String[0]);
    }

    public void execute(CommandSender sender, String[] args){
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Tienes que añadir un mensaje!");
        } else {
            StringBuilder builder = new StringBuilder();
            String[] arrayOfString;
            int j = (arrayOfString = args).length;
            for (int i = 0; i < j; i++)
            {
                String s = arrayOfString[i];
                builder.append(ChatColor.translateAlternateColorCodes('&', s));
                builder.append(" ");
            }
            String message = builder.substring(0, builder.length() - 1);

            ProxyServer.getInstance().getPlayers().forEach(p -> p.sendMessage(ChatColor.AQUA + sender.getName() + ChatColor.RESET + ": " + message));
        }
    }
}
