package com.cadiducho.fem.color.listener;

import com.cadiducho.fem.color.DyeOrDie;
import com.cadiducho.fem.color.manager.GameState;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.server.ServerListPingEvent;

public class WorldListener implements Listener {

    private final DyeOrDie plugin;

    public WorldListener(DyeOrDie instnace) {
        plugin = instnace;
    }
    
    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent e) {
        if (!e.getEntityType().equals(EntityType.FALLING_BLOCK)) {
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onFallingBlockLand(EntityChangeBlockEvent event) {
        if ((event.getEntity() instanceof FallingBlock)) {
            FallingBlock fallingBlock = (FallingBlock) event.getEntity();
            if ((fallingBlock.getMaterial() == Material.WOOL) && (event.getBlock().getType() == Material.AIR)) {
                event.setCancelled(true);
                fallingBlock.remove();
            }
        }
    }
    
    @EventHandler
    public void onMotdChange(ServerListPingEvent e){
        e.setMotd(GameState.getParsedStatus() + "#" + plugin.getAm().getAreaBorder1().getWorld().getName());
    }
}
