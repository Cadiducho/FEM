package com.cadiducho.fem.core.cmds;

import java.util.Arrays;
import com.cadiducho.fem.core.api.FEMUser;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ReplyCMD extends FEMCmd {

    public ReplyCMD() {
        super("reply", Grupo.Usuario, Arrays.asList("r", "responde", "responder"));
    }

    @Override
    public void run(FEMUser user, String lbl, String[] args) {
        if(args.length == 0){
            user.sendMessage("&eUsa &a/r <mensaje>");
            return;
        }
        
        String mensaje = "";
        for (String arg : args){
            mensaje = mensaje + arg + " ";
        }
        
        try {
            TellCMD.sendPrivateMessage("\u00A8" + "reply", user.getName(), mensaje); //\u00A8 es un char interno para identificar el reply
        } catch (IOException ex) {
            user.sendMessage("Ha ocurrido un error enviando el mensaje");
            plugin.getLogger().log(Level.INFO, "Error enviando un mensaje privado: {0}", ex.toString());
        }
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}
