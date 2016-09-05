package com.cadiducho.fem.chat.bukkit.commands;

import com.cadiducho.fem.chat.FEMChat;
import com.cadiducho.fem.chat.bukkit.BukkitPlugin;
import java.io.IOException;
import java.util.logging.Level;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RCommand implements CommandExecutor {

    private static BukkitPlugin plugin;
    
    public RCommand(BukkitPlugin instance) {
        plugin = instance;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage("No puedes responder desde consola!");
            return true;
        }
        
        Player p = (Player) sender;
        
        if(args.length == 0){
            sender.sendMessage("Usa /r <mensaje>");
            return true;
        }
        
        String mensaje = "";
        for (String arg : args){
            mensaje = mensaje + arg + " ";
        }
        
        try {
            plugin.sendPrivateMessage(FEMChat.CHAR + "reply", p.getName(), mensaje);
        } catch (IOException ex) {
            sender.sendMessage("Ha ocurrido un error enviando el mensaje");
            plugin.getLogger().log(Level.INFO, "Error enviando un mensaje privado: {0}", ex.toString());
        }
        return true;
    }
}
