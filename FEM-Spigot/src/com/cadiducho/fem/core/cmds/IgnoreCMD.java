package com.cadiducho.fem.core.cmds;

import java.util.Arrays;
import com.cadiducho.fem.core.api.FEMUser;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class IgnoreCMD extends FEMCmd {

    public IgnoreCMD() {
        super("ignore", Grupo.Usuario, Arrays.asList("ignora", "ignorar"));
    }

    @Override
    public void run(FEMUser user, String lbl, String[] args) {
        if(args.length == 0){
            user.sendMessage("&eUsa &a/ignore <Usuario>");
            return;
        }
        
        if (args[0].equals(user.getName())) {
            user.sendMessage("&c¡No puedes ignorarte a ti mismo!");
            return;
        }
        
        try {
            ignore(args[0], user.getName());
        } catch (IOException ex) {
            user.sendMessage("Ha ocurrido un error enviando el mensaje");
            plugin.log("Error enviando un mensaje privado: " + ex.toString());
        }
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
    
    public void ignore(String target, String from) throws IOException {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        out.writeUTF("ign");
        out.writeUTF(target);
        out.writeUTF(from);

        Collection<? extends Player> c = Bukkit.getOnlinePlayers(); //Usar jugador falso
        Player p = (Player) c.toArray()[0];
        p.sendPluginMessage(plugin, "FEMChat", b.toByteArray());
    }
    
}
