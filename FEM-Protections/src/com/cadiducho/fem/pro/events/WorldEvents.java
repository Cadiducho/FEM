package com.cadiducho.fem.pro.events;

import com.cadiducho.fem.core.util.Metodos;
import com.cadiducho.fem.pro.ProArea;
import com.cadiducho.fem.pro.ProPlayer;
import com.cadiducho.fem.pro.Protections;
import com.cadiducho.fem.pro.utils.ProType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.Arrays;

public class WorldEvents implements Listener {

    private Protections plugin;

    public WorldEvents(Protections Main) {
        this.plugin = Main;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Block b = e.getBlock();
        ProPlayer player = new ProPlayer(e.getPlayer().getUniqueId());

        for (int x = 0; x < plugin.getFiles().getCurrentID(); x++) {
            ProArea area = new ProArea(x);
            if (area.getCuboidRegion().contains(b)) {
                if (!area.getOwner().equals(player)) {
                    e.setCancelled(true);
                    player.getPlayer().sendMessage(ChatColor.RED + "No puedes poner bloques en una zona que no es tuya");
                    return;
                }
            }
        }
        Arrays.asList(ProType.values()).forEach(t -> {
            if (player.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase(t.getName())) {
                if (b.getType() == t.getMat()) {
                    ProArea newArea = new ProArea(player.getPlayer().getLocation(), ProType.parseMaterial(b.getType()), player);
                    if (newArea.hitOtherArena()) {
                        player.getPlayer().sendMessage(ChatColor.RED + "El nuevo arena esta chocando con otro area. Pon el bloque en otro lugar");
                        return;
                    }
                    newArea.generateArea();
                    newArea.showArea();
                    newArea.setBorderMaterial(Material.DOUBLE_STONE_SLAB2);
                }
            }
        });
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Block b = e.getBlock();
        ProPlayer player = new ProPlayer(e.getPlayer().getUniqueId());

        for (int x = 0; x < plugin.getFiles().getCurrentID(); x++) {
            ProArea area = new ProArea(x);
            if (area.getCuboidRegion().contains(b)) {
                if (!area.getOwner().equals(player)) {
                    e.setCancelled(true);
                    player.getPlayer().sendMessage(ChatColor.RED + "No puedes romper bloques en una zona que no es tuya");
                    return;
                }
            }
            if (plugin.getFiles().getAreas().getString("area_" + x + ".block").equalsIgnoreCase(Metodos.locationToString(b.getLocation()))){
                area.removeArena(Material.AIR);
            }
        }
    }
}
