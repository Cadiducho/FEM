package com.cadiducho.fem.dropper;

import com.cadiducho.fem.core.util.ItemUtil;
import com.cadiducho.fem.core.util.Metodos;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

public class DropMenu {

    //TODO: Cambiar
    private static final Dropper plugin = Dropper.getInstance();

    public static void openIngInv(DropPlayer u){
        Inventory inv = Bukkit.createInventory(null, 36, Metodos.colorizar("&6Under&eGames &a- &l&eInsignias"));

        if (plugin.getConfig().getString("Dropper.mapas").length() == u.getUserData().getDropperInsignias().size()) {
            u.getPlayer().getInventory().addItem(ItemUtil.createItem(Material.NETHER_STAR, "&l&6Insignia de Todos los mapas"));
        }
        u.getUserData().getDropperInsignias().forEach(m -> {
            inv.addItem(ItemUtil.createItem(Material.EMERALD, "&aInsignia oculta del mapa &e" + m));
        });

        u.getPlayer().openInventory(inv);
    }

    public static void openMapsInv(DropPlayer u){
        Inventory inv = Bukkit.createInventory(null, 36, Metodos.colorizar("&6Under&eGames &a- &l&aMapas superados"));

        u.getUserData().getDropper().forEach((m, v) -> {
            inv.addItem(ItemUtil.createItem(Material.DIAMOND, v, "&d" + m, "&a" + v + " &eveces superado"));
        });

        u.getPlayer().openInventory(inv);
    }
}
