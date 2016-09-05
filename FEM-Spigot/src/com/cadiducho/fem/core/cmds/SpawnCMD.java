package com.cadiducho.fem.core.cmds;

import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.core.api.FEMUser;
import com.cadiducho.fem.core.util.FEMFileLoader;
import com.cadiducho.fem.core.util.Metodos;
import java.util.Arrays;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class SpawnCMD extends FEMCmd {
    
    public SpawnCMD() {
        super("spawn", Grupo.Usuario, Arrays.asList("espaun", "hespaun", "lobby"));
    }
    
    @Override
    public void run(FEMUser user, String label, String[] args) {
        Location spawn = metodos.stringToLocation(plugin.getConfig().getString("teleports.spawn"));
        if (args.length == 0) {
            user.sendMessage("*spawn.mensaje");
            user.getPlayer().teleport(spawn, TeleportCause.COMMAND);
        }

        FEMUser target = FEMServer.getUser(plugin.getServer().getPlayer(args[0]));
        if (target == null) {
            user.sendMessage("*userDesconectado");
            return;
        }
        target.getPlayer().teleport(spawn, TeleportCause.COMMAND);
    }

    @Override
    public void run(CommandSender sender, String label, String[] args) {
        Location spawn = metodos.stringToLocation(plugin.getConfig().getString("teleports.spawn"));
        if (args.length < 1) {
            sender.sendMessage(Metodos.colorizar(FEMFileLoader.getLang().getString("*spawn.limiteConsola")));
        }

        FEMUser target = FEMServer.getUser(plugin.getServer().getPlayer(args[0]));
        if (target == null) {
            sender.sendMessage(Metodos.colorizar(FEMFileLoader.getLang().getString("*userDesconectado")));
            return;
        }
        target.getPlayer().teleport(spawn, TeleportCause.COMMAND);
    }
}
