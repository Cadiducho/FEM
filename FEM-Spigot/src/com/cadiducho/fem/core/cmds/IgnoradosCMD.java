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

public class IgnoradosCMD extends FEMCmd {

    public IgnoradosCMD() {
        super("ignorados", Grupo.Usuario, Arrays.asList("ignored", "ignorelist"));
    }

    @Override
    public void run(FEMUser user, String lbl, String[] args) {
        try {
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);
            out.writeUTF("ignlist");
            out.writeUTF(user.getName());

            Collection<? extends Player> c = Bukkit.getOnlinePlayers(); //Usar jugador falso
            Player p = (Player) c.toArray()[0];
            p.sendPluginMessage(plugin, "FEMChat", b.toByteArray());
        } catch (IOException ex) {}
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}
