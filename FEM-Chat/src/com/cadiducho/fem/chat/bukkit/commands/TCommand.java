package com.cadiducho.fem.chat.bukkit.commands;

import com.cadiducho.fem.chat.bukkit.BukkitPlugin;
import java.io.IOException;
import java.util.logging.Level;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class TCommand implements CommandExecutor {

    private static BukkitPlugin plugin;

    public TCommand(BukkitPlugin instance) {
        plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        String target = "";
        try {
            target = args[0];
        } catch (Exception ex) {}
        if (target.equals(sender.getName())) {
            sender.sendMessage(ChatColor.RED + "No puedes enviarte mensajes a ti mismo!");
            return true;
        }

        String mensaje = "";
        for (int i = 1; i < args.length; i++) {
            mensaje = mensaje + args[i] + " ";
        }
        
        try {
            plugin.sendPrivateMessage(target, sender.getName(), mensaje); 
        } catch (IOException ex) {
            sender.sendMessage("Ha ocurrido un error enviando el mensaje");
            plugin.getLogger().log(Level.INFO, "Error enviando un mensaje privado: {0}", ex.toString());
        }
        return true;
    }
}
