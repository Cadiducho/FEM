package com.cadiducho.fem.core.cmds;

import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.core.api.FEMUser;
import java.util.Arrays;
import org.bukkit.entity.Player;

public class NearCMD extends FEMCmd {

    public NearCMD() {
        super("near", Grupo.Usuario, Arrays.asList("cercanos", "jugadorescercanos"));
    }

    @Override
    public void run(FEMUser user, String lbl, String[] args) {
        String usuarios = "";
        usuarios = user.getPlayer().getNearbyEntities(10.0D, 10.0D, 10.0D).stream()
                .filter(e -> e instanceof Player)
                .map(e -> FEMServer.getUser((Player) e).getDisplayName() + ", ")
                .reduce(usuarios, String::concat);
        
        user.sendMessage("*near.mensaje", usuarios);
    }
}
