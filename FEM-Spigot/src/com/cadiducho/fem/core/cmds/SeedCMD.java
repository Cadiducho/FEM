package com.cadiducho.fem.core.cmds;

import com.cadiducho.fem.core.api.FEMUser;
import java.util.Arrays;

public class SeedCMD extends FEMCmd {

    public SeedCMD() {
        super("seed", Grupo.Admin, Arrays.asList("semilla", "seedmapa", "idmundo"));
    }

    @Override
    public void run(FEMUser user, String lbl, String[] args) {
        user.sendMessage("*seed.mensaje", user.getPlayer().getWorld().getSeed());
    }
}
