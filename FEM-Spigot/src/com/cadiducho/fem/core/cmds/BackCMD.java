package com.cadiducho.fem.core.cmds;

import com.cadiducho.fem.core.api.FEMUser;
import java.util.Arrays;
import org.bukkit.event.player.PlayerTeleportEvent;

public class BackCMD extends FEMCmd {
    
    public BackCMD() {
        super("back", Grupo.Owner, Arrays.asList("tpback"));
    }
    
    @Override
    public void run(FEMUser user, String label, String[] args) {
        if (user.getUserData().getLastLocation() == null) {
            user.sendMessage("*back.notFound");
            return;
        }
        
        user.getPlayer().teleport(user.getUserData().getLastLocation(), PlayerTeleportEvent.TeleportCause.COMMAND);
        user.sendMessage("*back.mensaje");
    }  
}
