package com.cadiducho.fem.core.listeners;

import com.cadiducho.fem.core.FEMCore;
import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.core.api.FEMUser;
import com.cadiducho.fem.core.util.Metodos;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryListener implements Listener {
    
    private final FEMCore plugin;
    
    public InventoryListener(FEMCore instance) {
        plugin = instance;
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {	
        Player p = (Player) e.getWhoClicked();
        FEMUser u = FEMServer.getUser(p);

        if (e.getInventory().getName().equalsIgnoreCase(Metodos.colorizar("&aLista de Staff &7>> &6FEM"))) {
            e.setCancelled(true);
        }
        
        if (e.getInventory().getName().equalsIgnoreCase(Metodos.colorizar("&aLista de Warps &7>> &6FEM"))) {
            e.setCancelled(true);
            String itemName = e.getCurrentItem().getItemMeta().getDisplayName();
            FEMServer.getWarp(itemName).teleport(u);
        }
    }
}
