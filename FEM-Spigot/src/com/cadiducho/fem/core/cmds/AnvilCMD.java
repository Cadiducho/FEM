package com.cadiducho.fem.core.cmds;

import com.cadiducho.fem.core.api.FEMUser;
import java.util.Arrays;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class AnvilCMD extends FEMCmd {

    public AnvilCMD() {
        super("anvil", Grupo.Vip, Arrays.asList("yunque", "anv"));
    }

    @Override
    public void run(FEMUser user, String label, String[] args) {
        Inventory anvil = Bukkit.createInventory(user.getPlayer(), InventoryType.ANVIL);
        user.getPlayer().openInventory(anvil);
    }  
}
