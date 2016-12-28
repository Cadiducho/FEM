package com.cadiducho.fem.pro.events;

import com.cadiducho.fem.pro.ProArea;
import com.cadiducho.fem.pro.ProPlayer;
import com.cadiducho.fem.pro.Protections;
import com.cadiducho.fem.pro.utils.ProType;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.Arrays;

public class WorldEvents implements Listener{

    private Protections plugin;

    public WorldEvents(Protections Main){
        this.plugin = Main;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e){
        ProPlayer player = new ProPlayer(e.getPlayer().getUniqueId());
        for (int x = 0; x < plugin.getFiles().getCurrentID(); x++){
            ProArea area = new ProArea(x);
            if (area.getCuboidRegion().contains(e.getBlock())) {
                if (!area.getOwner().equals(player)) {
                    e.setCancelled(true);
                    e.getPlayer().sendMessage(ChatColor.RED + "No puedes poner bloques en una zona que no es tuya");
                    return;
                }
                return;
            }

            Arrays.asList(ProType.values()).forEach(t -> {
                if (e.getBlock().getType() == t.getMat()) {
                    ProArea newArea = new ProArea(player.getPlayer().getLocation(), ProType.parseMaterial(e.getBlock().getType()), player);
                    if (newArea.hitOtherArena()) return;
                    newArea.generateArea();
                    newArea.showArea();
                    //newArea.putSlabs(); ?
                }
            });
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        for (int x = 0; x < plugin.getFiles().getCurrentID(); x++){
            ProArea area = new ProArea(x);
            if (area.getCuboidRegion().contains(e.getBlock())) {
                if (!area.getOwner().equals(new ProPlayer(e.getPlayer().getUniqueId()))) {
                    e.setCancelled(true);
                    e.getPlayer().sendMessage(ChatColor.RED + "No puedes romper bloques en una zona que no es tuya");
                }
            }
        }
    }
}
