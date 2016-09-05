package com.cadiducho.fem.chat.bukkit.commands;

import com.cadiducho.fem.chat.FEMChat;
import com.cadiducho.fem.chat.bukkit.BukkitPlugin;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class IgnoradosCommand implements CommandExecutor {

    private static BukkitPlugin plugin;
    
    public IgnoradosCommand(BukkitPlugin instance) {
        plugin = instance;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage("No puedes ignorar desde consola!");
            return true;
        }
        
        try {
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);
            out.writeUTF(FEMChat.IGNLIST_SUBCHANNEL);
            out.writeUTF(sender.getName());

            Collection<? extends Player> c = Bukkit.getOnlinePlayers(); //Usar jugador falso
            Player p = (Player) c.toArray()[0];
            p.sendPluginMessage(plugin, FEMChat.MAIN_CHANNEL, b.toByteArray());
        } catch (IOException ex) {}
        return true;
    }
}
