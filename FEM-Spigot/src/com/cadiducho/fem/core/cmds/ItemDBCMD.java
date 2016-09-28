package com.cadiducho.fem.core.cmds;

import com.cadiducho.fem.core.api.FEMUser;
import java.util.Arrays;
import org.bukkit.inventory.ItemStack;

public class ItemDBCMD extends FEMCmd {

    public ItemDBCMD() {
        super("itemdb", Grupo.Owner, Arrays.asList("itemid"));
    }

    @Override
    public void run(FEMUser user, String lbl, String[] args) {
        ItemStack item = user.getPlayer().getInventory().getItemInMainHand();
        user.sendMessage("*itemdb.mensaje", item.getTypeId(), item.getType().toString());
    }
}
