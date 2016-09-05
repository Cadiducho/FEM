package com.cadiducho.fem.ovejas.listener;

import com.cadiducho.fem.ovejas.SheepQuest;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

public class WorldListener implements Listener {

    private final SheepQuest plugin;

    public WorldListener(SheepQuest instnace) {
        plugin = instnace;
    }

    public void init() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent e) {
        if (!e.getEntityType().equals(EntityType.SHEEP)) {
            e.setCancelled(true);
        }
    }
}
