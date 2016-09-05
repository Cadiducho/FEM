package com.cadiducho.fem.core.cmds;

import com.cadiducho.fem.core.api.FEMUser;
import java.util.Arrays;

public class RepairCMD extends FEMCmd {

    public RepairCMD() {
        super("repair", Grupo.Admin, Arrays.asList("reparar", "arreglar"));
    }

    @Override
    public void run(FEMUser user, String lbl, String[] args) {
        user.getPlayer().getInventory().getItemInMainHand().setDurability((short) 0);
        user.sendMessage("*repair.mensaje");
    }

}
