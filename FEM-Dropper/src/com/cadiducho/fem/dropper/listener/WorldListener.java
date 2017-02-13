package com.cadiducho.fem.dropper.listener;

import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.core.cmds.FEMCmd;
import com.cadiducho.fem.dropper.Dropper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.server.ServerListPingEvent;

public class WorldListener implements Listener {

    private final Dropper plugin;

    public WorldListener(Dropper instnace) {
        plugin = instnace;
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerBreak(BlockBreakEvent e) {
        if (!FEMServer.getUser(e.getPlayer()).isOnRank(FEMCmd.Grupo.Admin)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerPlace(BlockPlaceEvent e) {
        if (!FEMServer.getUser(e.getPlayer()).isOnRank(FEMCmd.Grupo.Admin)) {
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onMotdChange(ServerListPingEvent e){
        e.setMotd("WAITING_FOR_PLAYERS");
    }
}
