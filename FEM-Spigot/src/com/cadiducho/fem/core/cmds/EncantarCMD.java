package com.cadiducho.fem.core.cmds;

import com.cadiducho.fem.core.api.FEMUser;
import java.util.Arrays;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class EncantarCMD extends FEMCmd {

    public EncantarCMD() {
        super("encantar", Grupo.Vip, Arrays.asList("encantamiento", "enchantingtable"));
    }

    @Override
    public void run(FEMUser user, String label, String[] args) {
        Inventory enchantingTable = Bukkit.createInventory(user.getPlayer(), InventoryType.ENCHANTING);
        user.getPlayer().openInventory(enchantingTable);
    }
}
