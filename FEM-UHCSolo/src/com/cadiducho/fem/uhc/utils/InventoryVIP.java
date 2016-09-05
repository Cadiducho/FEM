package com.cadiducho.fem.uhc.utils;

import com.cadiducho.fem.uhc.Main;
import java.util.Arrays;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryVIP {

    public Main plugin;

    public InventoryVIP(Main plugin) {
        this.plugin = plugin;
    }

    public void openVIPMenu(Player player) {
        Inventory inv;
        inv = plugin.getServer().createInventory(null, 9, "§lOpciones VIP");

        ItemStack ARROW = new ItemBuilder(Material.ARROW).setDisplayName("§bPartículas de flechas").setLores(Arrays.asList("§7Click para abrir")).getItem();
        inv.setItem(0, ARROW);

        ItemStack CAGAS = new ItemBuilder(Material.GRASS).setDisplayName("§aTipo de spawn").setLores(Arrays.asList("§7Click para abrir")).getItem();
        inv.setItem(4, CAGAS);

        player.openInventory(inv);
    }

    public void openMenuVIP(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getInventory().getTitle().equalsIgnoreCase("§lOpciones VIP")) {
            if (event.getCurrentItem() != null && event.getCurrentItem().getType() != null) {
                event.setCancelled(true);
                if (event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().hasDisplayName()) {
                    event.setCancelled(true);
                    ItemStack clicked = event.getCurrentItem();
                    if (clicked != null) {
                        if (event.getSlot() == 0) {
                            player.performCommand("arrows");
                        }
                        if (event.getSlot() == 4) {
                            plugin.ic.openCage(player);
                        }
                    }
                }
            }
        }
    }
}
