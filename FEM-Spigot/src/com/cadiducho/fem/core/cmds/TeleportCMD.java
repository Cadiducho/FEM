package com.cadiducho.fem.core.cmds;

import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.core.api.FEMUser;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.Arrays;
import java.util.List;

public class TeleportCMD extends FEMCmd {
    
    public TeleportCMD() {
        super("tp", Grupo.Moderador, Arrays.asList("teleport", "tppos", "tploc"));
    }
    
    @Override
    public void run(FEMUser user, String label, String[] args) {
        switch (args.length) {
            case 1: //del sender a otra persona
                FEMUser target = FEMServer.getUser(plugin.getServer().getPlayer(args[0]));
                if (!target.isOnline() || target == null) {
                    user.sendMessage("*userDesconectado");
                    return;
                }
                user.getPlayer().teleport(target.getPlayer().getLocation(), PlayerTeleportEvent.TeleportCause.COMMAND);
                user.sendMessage("*tp.to", target.getName());
                break;
            case 2: //tp de un user a otro
                FEMUser from = FEMServer.getUser(plugin.getServer().getPlayer(args[0]));
                FEMUser to = FEMServer.getUser(plugin.getServer().getPlayer(args[1]));

                if (!from.isOnline() || from == null || !to.isOnline() || to == null) {
                    user.sendMessage("*userDesconectado");
                    return;
                }

                from.getPlayer().teleport(to.getPlayer().getLocation(), PlayerTeleportEvent.TeleportCause.COMMAND);
                from.sendMessage("*tp.to", to.getName());
                user.sendMessage("*tp.toOtro", from.getName(), to.getName());
                break;   
            case 3: //mandar sender a unas coordenadas
                Double x, y, z;
                try {
                    x = Double.parseDouble(args[0]);
                    y = Double.parseDouble(args[1]);
                    z = Double.parseDouble(args[2]);
                } catch (NumberFormatException e) {
                    user.sendMessage("*tp.pos.uso");
                    return;
                }
                Location loc = new Location(user.getPlayer().getWorld(), x, y, z);

                user.getPlayer().teleport(loc, PlayerTeleportEvent.TeleportCause.COMMAND);
                user.sendMessage("*tp.pos.to", x, y, z);
                break;
            default:
                user.sendMessage("*tp.uso");
                user.sendMessage("*tp.pos.uso");
                break;
        }    
    }
    
    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}
