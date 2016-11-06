package com.cadiducho.fem.lobby.cmds;

import com.cadiducho.fem.core.api.FEMUser;
import com.cadiducho.fem.core.cmds.FEMCmd;
import java.util.Arrays;

public class SpawnCMD extends FEMCmd {
    
    public SpawnCMD() {
        super("spawn", Grupo.Usuario, Arrays.asList());
    }
    
    @Override
    public void run(FEMUser user, String label, String[] args) {
        user.getPlayer().teleport(user.getPlayer().getWorld().getSpawnLocation());
    }
}
