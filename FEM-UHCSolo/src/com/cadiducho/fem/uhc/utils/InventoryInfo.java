package com.cadiducho.fem.uhc.utils;

import java.util.Arrays;
import com.cadiducho.fem.uhc.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InventoryInfo {

    public Main plugin;

    public InventoryInfo(Main plugin) {
        this.plugin = plugin;
    }

    public void openInventory(Player player) {
        Inventory inv;
        inv = plugin.getServer().createInventory(null, 9 * 2, "§lInformación");

        ItemStack ON = new ItemStack(Material.INK_SACK, 1, (byte) 10);
        ItemMeta ONM = ON.getItemMeta();
        ONM.setDisplayName("§a§lACTIVADO");
        ONM.setLore(Arrays.asList("§7Esta opción está §a§lACTIVADA"));
        ON.setItemMeta(ONM);

        ItemStack OFF = new ItemStack(Material.INK_SACK, 1, (byte) 8);
        ItemMeta OFFM = OFF.getItemMeta();
        OFFM.setDisplayName("§c§lDESACTIVADO");
        OFFM.setLore(Arrays.asList("§7Esta opción está §c§lDESACTIVADA"));
        OFF.setItemMeta(OFFM);

        ItemStack CABEZAORO = new ItemBuilder(Material.SKULL_ITEM).setDisplayName("§bCabeza de oro").getItem();
        inv.setItem(0, CABEZAORO);
        inv.setItem(9, OFF);

        ItemStack NOCHE = new ItemBuilder(Material.WATCH).setDisplayName("§bNoche").getItem();
        inv.setItem(1, NOCHE);
        inv.setItem(10, OFF);
        
        ItemStack i = new ItemStack(Material.BOW, 1, (short) 2);
        ItemMeta im = i.getItemMeta();
        im.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_UNBREAKABLE});
        im.spigot().setUnbreakable(true);
        i.setItemMeta(im);
        inv.setItem(2, i);

        ItemStack NETHER = new ItemBuilder(Material.NETHERRACK).setDisplayName("§bNether").getItem();
        inv.setItem(6, NETHER);
        ItemStack INFORNETHER = new ItemBuilder(Material.BOOK).setDisplayName("§c§lNether no tiene límite").setLores(Arrays.asList("§7El nether no tiene límite, puedes recorrer todo el nether", "§7pero antes de la fase de §c§lPVP §7tienes que estar en el mundo", "§7sino serás teletransportado")).getItem();
        inv.setItem(15, INFORNETHER);

        ItemStack POCIONES = new ItemBuilder(Material.POTION).setDisplayName("§bPociones").getItem();
        inv.setItem(7, POCIONES);
        ItemStack INFOPOCIONES = new ItemBuilder(Material.BOOK).setDisplayName("§c§lNivel 2 DESACTIVADAS").setLores(Arrays.asList("§7La pociones del nivel se encuentran desactivadas", "§7Sólo las pociones de nivel 1 están permitidas.")).getItem();
        inv.setItem(16, INFOPOCIONES);

        ItemStack BORDE = new ItemBuilder(Material.BARRIER).setDisplayName("§bBorde").getItem();
        inv.setItem(8, BORDE);
        ItemStack INFOBORDE = new ItemBuilder(Material.BOOK).setDisplayName("§c§l750 §d/ §c§§l-750").setLores(Arrays.asList("§7El borde está en un tamaño de 1500", "§7El borde se irá siendo pequeño en la fase de", "§c§lELIMINACIÓN", " §7hasta llegar a un borde de §e50 x 50")).getItem();
        inv.setItem(17, INFOBORDE);

        player.openInventory(inv);
    }

    public void onPlayerClickSVMenu(InventoryClickEvent event) {
        //Player player = (Player) event.getWhoClicked();
        if (event.getInventory().getTitle().equalsIgnoreCase("§lInformación")) {
            if (event.getCurrentItem() != null && event.getCurrentItem().getType() != null) {
                event.setCancelled(true);
                if (event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().hasDisplayName()) {
                    event.setCancelled(true);
                    ItemStack clicked = event.getCurrentItem();
                    if (clicked != null) {

                    }
                }
            }
        }
    }

}
