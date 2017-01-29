package com.cadiducho.fem.gem.listener;

import com.cadiducho.fem.gem.GemHunters;
import com.cadiducho.fem.gem.GemPlayer;
import com.cadiducho.fem.gem.manager.GameState;
import org.bukkit.block.Block;
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
        e.setCancelled(true);
    }
    
    @EventHandler
    public void onGemPlace(BlockPlaceEvent e) {
        Block b = e.getBlock();
        if (b.getType() == plugin.getAm().getTypeGema()) {
            if (plugin.getGm().isHidding()) {
                e.getPlayer().sendMessage("Has puesto tu gema");
                plugin.getMsg().sendBroadcast("&c" + e.getPlayer().getName() + " &eha puesto su gema");
                
                final GemPlayer gp = GemHunters.getPlayer(e.getPlayer());
                gp.getUserData().setGemPlanted(gp.getUserData().getGemPlanted() + 1);
                gp.save();
                
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
                e.setCancelled(!plugin.getTm().tryRemoveGema(e.getPlayer(), b.getLocation()));
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
