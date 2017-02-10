package com.cadiducho.fem.dropper;

import com.cadiducho.fem.core.util.ItemUtil;
import com.cadiducho.fem.core.util.Metodos;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

public class DropMenu {

    private static final Dropper plugin = Dropper.getInstance();

    public static void openIngInv(DropPlayer u) {
        Inventory inv = Bukkit.createInventory(null, 36, Metodos.colorizar("&6Under&eGames &2- &l&2Insignias"));

        if (plugin.getConfig().getStringList("Dropper.mapas").size() == u.getUserData().getDropperInsignias().size()) {
            inv.addItem(ItemUtil.createItem(Material.NETHER_STAR, "&l&6Insignia de Todos los mapas"));
        }

        u.getUserData().getDropperInsignias().forEach(m -> {
            inv.addItem(ItemUtil.createItem(Material.EMERALD, "&aInsignia oculta del mapa &e" + m));
        });
        
        inv.setItem(35, ItemUtil.createItem(Material.BARRIER, "&cBorrar insignias", "Selecciona las insignias de mapa que quieras borrar"));

        u.getPlayer().openInventory(inv);
    }
    
    public static void openBorarInsig(DropPlayer u) {
        Inventory inv = Bukkit.createInventory(null, 36, Metodos.colorizar("&l&cBorrar &l&2Insignias"));

        u.getUserData().getDropperInsignias().forEach(m -> {
            inv.addItem(ItemUtil.createItem(Material.EMERALD, "&aInsignia oculta del mapa &e" + m));
        });
        
        u.getPlayer().openInventory(inv);
    }

    public static void openMapsInv(DropPlayer u) {
        Inventory inv = Bukkit.createInventory(null, 36, Metodos.colorizar("&6Under&eGames &2- &l&2Mapas superados"));

        u.getUserData().getDropper().forEach((m, v) -> {
            inv.addItem(ItemUtil.createItem(Material.DIAMOND, v, "&d" + m, "&a" + v + " &eveces superado"));
        });
        
        inv.setItem(35, ItemUtil.createItem(Material.BARRIER, "&cBorrar mapas", "Selecciona los registros de mapas que quieras borrar"));

        u.getPlayer().openInventory(inv);
    }
    
    public static void openBorarMapa(DropPlayer u) {
        Inventory inv = Bukkit.createInventory(null, 36, Metodos.colorizar("&l&cBorrar &l&2Mapas"));

        u.getUserData().getDropper().forEach((m, v) -> {
            inv.addItem(ItemUtil.createItem(Material.DIAMOND, v, "&d" + m, "&a" + v + " &eveces superado"));
        });
        
        u.getPlayer().openInventory(inv);
    }
}
