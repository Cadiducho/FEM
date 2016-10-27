package com.cadiducho.fem.core.cmds;

import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.core.api.FEMUser;
import java.util.Arrays;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

public class TeleportAllCMD extends FEMCmd {
    
    public TeleportAllCMD() {
        super("tpall", Grupo.Admin, Arrays.asList("teleportall"));
    }
    
    @Override
    public void run(FEMUser user, String label, String[] args) {
        FEMUser target = user;
        if (args.length != 0) {
            target = FEMServer.getUser(plugin.getServer().getPlayer(args[0]));
            if (!target.isOnline()) {
                user.sendMessage("*userDesconectado");
                return;
            }
        }
        
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            p.teleport(target.getPlayer().getLocation(), PlayerTeleportEvent.TeleportCause.COMMAND);
        }

        user.sendMessage("*tp.all", target == user ? "ti" : ("&e"+target.getName()));
    }
    
    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}
