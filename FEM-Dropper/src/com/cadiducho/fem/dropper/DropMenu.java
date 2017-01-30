package com.cadiducho.fem.dropper;

import com.cadiducho.fem.core.util.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

public class DropMenu {

    //TODO: Cambiar

    public static void openIngInv(DropPlayer u){
        Inventory inv = Bukkit.createInventory(null, 36, "Insignias");

        u.getUserData().getDropperInsignias().forEach(m -> {
            inv.addItem(ItemUtil.createItem(Material.EMERALD, "&aInsignia oculta del mapa &e" + m));
        });

        u.getPlayer().openInventory(inv);
    }

    public static void openMapsInv(DropPlayer u){
        Inventory inv = Bukkit.createInventory(null, 36, "Mapas");

        u.getUserData().getDropper().forEach((m, v) -> {
            inv.addItem(ItemUtil.createItem(Material.DIAMOND, v, "&d" + m, "&a" + v + " &eveces superado"));
        });

        u.getPlayer().openInventory(inv);
    }
}
