package com.cadiducho.fem.core.cmds;

import java.util.Arrays;

import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.core.api.FEMUser;

public class ClearCMD extends FEMCmd {

    public ClearCMD() {
        super("clear", Grupo.Owner, Arrays.asList("limpiar", "limpia"));
    }

    @Override
    public void run(FEMUser user, String lbl, String[] args) {
        if (args.length < 1) {
            user.sendMessage("*clear.mensaje");
            int invsize = user.getPlayer().getInventory().getSize() - 5;
            for (int i = 0; i < invsize; i++) {
                user.getPlayer().getInventory().clear(i);
            }
            return;
        }
        FEMUser target = FEMServer.getUser(plugin.getServer().getPlayer(args[0]));
        target.sendMessage("*clear.mensaje");
        user.sendMessage("*clear.otro", target.getName());
        int invsize = user.getPlayer().getInventory().getSize() - 5;
        for (int i = 0; i < invsize; i++) {
            target.getPlayer().getInventory().clear(i);
        }
    }
}
