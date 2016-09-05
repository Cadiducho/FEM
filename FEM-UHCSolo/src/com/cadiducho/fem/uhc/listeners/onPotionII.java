package com.cadiducho.fem.uhc.listeners;

import com.cadiducho.fem.uhc.Main;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;

public class onPotionII implements Listener {

    public Main plugin;

    public onPotionII(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void Brew(BrewEvent e) {
        if (e.getContents().getIngredient() != null) {
            if (e.getContents().getIngredient().getType().equals(Material.GLOWSTONE_DUST)) {
                BrewerInventory i = e.getContents();
                ItemStack i1 = i.getItem(1);
                ItemStack i2 = i.getItem(1);
                ItemStack i3 = i.getItem(3);
                if ((i1 != null && !i1.getType().equals(Material.AIR) && (!i1.getType().equals(Material.POTION)
                        || i1.getData().getData() != 0))) {
                    if ((i2 != null && !i2.getType().equals(Material.AIR) && (!i2.getType().equals(Material.POTION)
                            || i2.getData().getData() != 0))) {
                        if ((i3 != null && !i3.getType().equals(Material.AIR) && (!i3.getType().equals(Material.POTION)
                                || i3.getData().getData() != 0))) {
                            e.setCancelled(true);
                        }
                    }
                }
            }
        }
    }

}
