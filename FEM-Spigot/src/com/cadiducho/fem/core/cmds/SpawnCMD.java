package com.cadiducho.fem.core.cmds;

import com.cadiducho.fem.core.api.FEMUser;
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
