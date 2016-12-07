package com.cadiducho.fem.lobby.cmds;

import com.cadiducho.fem.core.api.FEMUser;
import com.cadiducho.fem.core.cmds.FEMCmd;
import com.cadiducho.fem.core.util.Metodos;

import java.util.Arrays;

public class SetNvidiaCMD extends FEMCmd{

    public SetNvidiaCMD() {
        super("setnvidia", Grupo.Admin, Arrays.asList());
    }

    @Override
    public void run(FEMUser user, String lbl, String[] args) {
        plugin.getConfig().set("nvidia", Metodos.locationToString(user.getPlayer().getLocation().subtract(0, 1, 0)));
        plugin.saveConfig();
        System.out.println(Metodos.locationToString(user.getPlayer().getLocation().subtract(0, 1, 0)));
    }
}
