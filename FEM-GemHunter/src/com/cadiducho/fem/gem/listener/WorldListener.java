package com.cadiducho.fem.gem.listener;

import com.cadiducho.fem.core.util.Metodos;
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
                if (e.getPlayer().getLocation().getBlockY() == e.getPlayer().getLocation().getY()) {
                    e.getPlayer().sendMessage(Metodos.colorizar("&c¡No puedes colocar tu gema saltando!"));
                    e.setCancelled(true);
                    return;
                }
                e.getPlayer().sendMessage(Metodos.colorizar("&aHas colocado tu gema"));
                plugin.getMsg().sendBroadcast(plugin.getTm().getTeam(e.getPlayer()).getPrefix() + e.getPlayer().getName() + " &eha colocado su gema");
                
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
        e.setMotd(GameState.getParsedStatus() + "#" + plugin.getAm().getPos1().getWorld().getName());
    }
}
