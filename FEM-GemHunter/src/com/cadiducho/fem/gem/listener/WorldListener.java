package com.cadiducho.fem.gem.listener;

import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.gem.GemHunters;
import com.cadiducho.fem.gem.manager.GameState;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.server.ServerListPingEvent;

public class WorldListener implements Listener {

    private final GemHunters plugin;

    public WorldListener(GemHunters instnace) {
        plugin = instnace;
    }
    
    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent e) {
        if (!e.getEntityType().equals(EntityType.SHEEP)) {
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onGemPlace(BlockPlaceEvent e) {
        Block b = e.getBlock();
        if (b.getType() == plugin.getAm().getTypeGema()) {
            if (plugin.getGm().isHidding()) {
                e.getPlayer().sendMessage("Has puesto tu gema");
                plugin.getMsg().sendBroadcast(e.getPlayer().getName() + " ha puesto su gema");
                FEMServer.getUser(e.getPlayer()).getUserData().setGemPlanted(FEMServer.getUser(e.getPlayer()).getUserData().getGemPlanted() + 1);
                FEMServer.getUser(e.getPlayer());
                plugin.getTm().addPunto(plugin.getTm().getTeam(e.getPlayer()), b.getLocation());   
                return;
            }
        }
        //Cancelar los que no son gemas validas
        e.setCancelled(true);
    }
    
    @EventHandler
    public void onGemBreak(BlockBreakEvent e) {
        Block b = e.getBlock();
        if (b.getType() == plugin.getAm().getTypeGema()) {
            if (plugin.getGm().isInGame()) {
                e.setCancelled(!plugin.getTm().tryRemovePunto(e.getPlayer(), b.getLocation()));
                return;
            }
        }
        //Cancelar los que no son gemas validas
        e.setCancelled(true);
    }
    
    @EventHandler
    public void onMotdChange(ServerListPingEvent e){
        e.setMotd(GameState.getParsedStatus());
    }
}
