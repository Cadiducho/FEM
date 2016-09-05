package com.cadiducho.fem.uhc.listeners;

import com.cadiducho.fem.uhc.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;

public class ChunkListener implements Listener{

    public Main plugin;

    public ChunkListener(Main plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onLoadChunk(ChunkUnloadEvent e){
        e.setCancelled(false);
    }

}
