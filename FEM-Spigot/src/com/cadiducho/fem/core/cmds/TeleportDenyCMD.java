package com.cadiducho.fem.core.cmds;

import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.core.api.FEMUser;
import java.util.Arrays;

public class TeleportDenyCMD extends FEMCmd {
    
    public TeleportDenyCMD() {
        super("tpadeny", Grupo.Usuario, Arrays.asList("teleportdeny"));
    }
    
    @Override
    public void run(FEMUser user, String label, String[] args) {
        if (FEMServer.getTeleportRequests().containsKey(user.getUuid()) || FEMServer.getTeleportHereRequests().containsKey(user.getUuid())) {
            FEMUser t1 = FEMServer.getUser(FEMServer.getTeleportRequests().get(user.getUuid()));
            FEMUser t2 = FEMServer.getUser(FEMServer.getTeleportHereRequests().get(user.getUuid()));
            user.sendMessage("*tp.tpa.denegada");
            if (t1 != null) t1.sendMessage("*tp.tpa.denegadaOtro", user.getName());
            if (t2 != null) t2.sendMessage("*tp.tpa.denegadaOtro", user.getName());

            FEMServer.removeTeleportRequest(user.getUuid());
            FEMServer.removeTeleportHereRequest(user.getUuid());
        } else {
            user.sendMessage("*tp.tpa.noSolicitud");
        }
    }  
}
