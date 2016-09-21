package com.cadiducho.fem.tnt.listener;

import com.cadiducho.fem.tnt.TntIsland;
import com.cadiducho.fem.tnt.TntPlayer;
import com.cadiducho.fem.tnt.TntWars;
import com.cadiducho.fem.tnt.manager.GameState;
import com.cadiducho.fem.tnt.task.TntExplodeTask;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.scheduler.BukkitTask;

public class WorldListener implements Listener {

    private final TntWars plugin;

    public WorldListener(TntWars instnace) {
        plugin = instnace;
    }
    
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        if (GameState.state == GameState.GAME) {
            Block placed = e.getBlock();
            TntPlayer pl = TntWars.getPlayer(e.getPlayer());
            if (placed.getType() == Material.TNT) {
                TntIsland isla = checkBedrock(placed.getRelative(BlockFace.DOWN)); 
                if (isla == null || isla.getOwner() == null) {
                    e.getPlayer().sendMessage("Sólo puedes poner TNT en el núcleo de la isla de otros jugadores conectados");
                    e.setCancelled(true);
                    return;
                }
                if (isla.getDestroyed()) {
                    e.setCancelled(true);
                    return;
                }
                
                if (isla.getOwner().getUniqueId().equals(e.getPlayer().getUniqueId())) {
                    e.getPlayer().sendMessage("No puedes poner TNT en tu isla");
                    e.setCancelled(true);
                    return;
                }
                
                BukkitTask bt = new TntExplodeTask(isla, e.getPlayer()).runTaskTimer(plugin, 1L, 20L);
                isla.setDestroyTaskId(bt.getTaskId());
            }
        }
    }
    
    @EventHandler
    public void onBlockDestroy(BlockBreakEvent e) {
        if (GameState.state == GameState.GAME) {
            Block broken = e.getBlock();
            TntPlayer pl = TntWars.getPlayer(e.getPlayer());
            if (broken.getType() == Material.TNT) {
                TntIsland isla = checkBedrock(broken.getRelative(BlockFace.DOWN));
                if (isla == null) {
                    e.setCancelled(true);
                    return;
                }
                
                if (isla.getOwner().getUniqueId().equals(e.getPlayer().getUniqueId())) {
                    plugin.getServer().getScheduler().cancelTask(isla.getDestroyTaskId());
                    plugin.getMsg().sendBroadcast(pl.getBase().getDisplayName() + " ha evitado la explosión de su isla!");
                    pl.getBase().getUserData().setTntQuitadas(pl.getBase().getUserData().getTntQuitadas() + 1);
                    pl.getBase().save();
                }
            }
        }
    }
    
    public TntIsland checkBedrock(Block b) {
        for (TntIsland i : plugin.getAm().getIslas()) {
            if (i.getBedrockCore().getLocation().getBlockX() == b.getLocation().getBlockX() &&
                    i.getBedrockCore().getLocation().getBlockY() == b.getLocation().getBlockY() &&
                    i.getBedrockCore().getLocation().getBlockZ() == b.getLocation().getBlockZ()) {
                return i;
            }
        }
        return null;
    }
    
    @EventHandler
    public void onMotdChange(ServerListPingEvent e){
        e.setMotd(GameState.getParsedStatus());
    }

}
