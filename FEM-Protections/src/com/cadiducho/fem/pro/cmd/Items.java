package com.cadiducho.fem.pro.cmd;

import com.cadiducho.fem.core.api.FEMUser;
import com.cadiducho.fem.core.cmds.FEMCmd;
import com.cadiducho.fem.pro.utils.ProType;

import java.util.Arrays;

public class Items extends FEMCmd {

    public Items() {
        super("problock", Grupo.Moderador, Arrays.asList());
    }

    @Override
    public void run(FEMUser user, String label, String[] args) {
        ProType.getItems().forEach(i -> user.getPlayer().getInventory().addItem(i));
    }
}
