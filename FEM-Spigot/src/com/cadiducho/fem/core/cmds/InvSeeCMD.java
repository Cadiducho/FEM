package com.cadiducho.fem.core.cmds;

import java.util.Arrays;

import org.bukkit.inventory.PlayerInventory;

import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.core.api.FEMUser;

public class InvSeeCMD extends FEMCmd {

    public InvSeeCMD() {
        super("invsee", Grupo.Owner, Arrays.asList("verinv"));
    }

    @Override
    public void run(FEMUser user, String lbl, String[] args) {
        if (args.length < 1) {
            user.sendMessage("*invsee.uso");
            return;
        }

        FEMUser target = FEMServer.getUser(plugin.getServer().getPlayer(args[0]));
        if (target == null) {
            user.sendMessage("*userDesconectado");
            return;
        }

        PlayerInventory inv = target.getPlayer().getInventory();
        user.getPlayer().openInventory(inv);
    }
}
