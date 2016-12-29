package com.cadiducho.fem.pro.cmd;

import com.cadiducho.fem.core.api.FEMUser;
import com.cadiducho.fem.core.cmds.FEMCmd;
import com.cadiducho.fem.pro.utils.ProType;

import java.util.Arrays;

public class Items extends FEMCmd {

    public Items() {
        super("proBlock", Grupo.Moderador, Arrays.asList());
    }

    @Override
    public void run(FEMUser user, String label, String[] args) {
        user.getPlayer().getInventory().addItem(ProType.getItems());
    }
}
