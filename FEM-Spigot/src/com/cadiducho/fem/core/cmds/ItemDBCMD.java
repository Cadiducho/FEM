package com.cadiducho.fem.core.cmds;

import com.cadiducho.fem.core.api.FEMUser;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class ItemDBCMD extends FEMCmd {

    public ItemDBCMD() {
        super("itemdb", Grupo.Admin, Arrays.asList("itemid"));
    }

    @Override
    public void run(FEMUser user, String lbl, String[] args) {
        ItemStack item = user.getPlayer().getInventory().getItemInHand();
        user.sendMessage("*itemdb.mensaje", item.getTypeId(), item.getType().toString());
    }
}
