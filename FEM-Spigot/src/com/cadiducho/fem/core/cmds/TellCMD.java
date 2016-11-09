package com.cadiducho.fem.core.cmds;

import java.util.Arrays;
import com.cadiducho.fem.core.api.FEMUser;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TellCMD extends FEMCmd {

    public TellCMD() {
        super("tell", Grupo.Usuario, Arrays.asList("w", "m", "msg", "mensaje"));
    }

    @Override
    public void run(FEMUser user, String lbl, String[] args) {
        String target = "";
        try {
            target = args[0];
        } catch (Exception ex) {}
        if (target.equals(user.getName())) {
            user.sendMessage("&c¡No puedes enviarte mensajes a ti mismo!");
            return;
        }

        String mensaje = "";
        for (int i = 1; i < args.length; i++) {
            mensaje = mensaje + args[i] + " ";
        }
        
        try {
            sendPrivateMessage(target, user.getName(), mensaje); 
        } catch (IOException ex) {
            user.sendMessage("Ha ocurrido un error enviando el mensaje");
            plugin.getLogger().log(Level.INFO, "Error enviando un mensaje privado: {0}", ex.toString());
        }
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
    
    public static void sendPrivateMessage(String target, String from, String mensaje) throws IOException {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        out.writeUTF("priv");
        out.writeUTF(target);
        out.writeUTF(from);
        out.writeUTF(mensaje);

        Collection<? extends Player> c = Bukkit.getOnlinePlayers(); //Usar jugador falso
        Player p = (Player) c.toArray()[0];
        p.sendPluginMessage(plugin, "FEMChat", b.toByteArray());
    }
}
