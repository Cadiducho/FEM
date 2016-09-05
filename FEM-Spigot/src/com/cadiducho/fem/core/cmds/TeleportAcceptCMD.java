package com.cadiducho.fem.core.cmds;

import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.core.api.FEMUser;
import java.util.Arrays;
import org.bukkit.event.player.PlayerTeleportEvent;

public class TeleportAcceptCMD extends FEMCmd {
    
    public TeleportAcceptCMD() {
        super("tpaccept", Grupo.Usuario, Arrays.asList("teleportaccept", "tpaacept"));
    }
    
    @Override
    public void run(FEMUser user, String label, String[] args) {
        if (FEMServer.getTeleportHereRequests().containsKey(user.getUuid())) {
            FEMUser target = FEMServer.getUser(FEMServer.getTeleportHereRequests().get(user.getUuid()));
            if (target == null) {
                user.sendMessage("*tp.tpa.noSolicitud");
            } else {
                user.getPlayer().teleport(target.getPlayer(), PlayerTeleportEvent.TeleportCause.COMMAND);
                target.sendMessage("*tp.here", user.getName());
                user.sendMessage("*tp.tpa.aceptada", target.getName());
                FEMServer.removeTeleportHereRequest(target.getUuid());
            }
        } else if (!FEMServer.getTeleportRequests().containsKey(user.getUuid())) {
            user.sendMessage("*tp.tpa.noSolicitud");
        } else {
            FEMUser target = FEMServer.getUser(FEMServer.getTeleportRequests().get(user.getUuid()));
            if (target == null) {
                user.sendMessage("*tp.tpa.noSolicitud");
            } else {
                target.getPlayer().teleport(user.getPlayer(), PlayerTeleportEvent.TeleportCause.COMMAND);
                target.sendMessage("*tp.to", user.getName());
                user.sendMessage("*tp.tpa.aceptada", target.getName());
                FEMServer.removeTeleportRequest(user.getUuid());
            }
        }
    }  
}
