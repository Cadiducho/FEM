package com.cadiducho.fem.tnt.listener;

import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.tnt.Generador;
import com.cadiducho.fem.tnt.TntIsland;
import com.cadiducho.fem.tnt.TntPlayer;
import com.cadiducho.fem.tnt.TntWars;
import com.cadiducho.fem.tnt.manager.GameState;
import com.cadiducho.fem.tnt.task.TntExplodeTask;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.ItemStack;
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
                TntIsland isla = checkBedrock(placed.getLocation().add(0, -1, 0).getBlock()); 
                if (isla == null || isla.getOwner() == null) {
                    pl.sendMessage("&eSólo puedes poner TNT en el núcleo de la isla de otros jugadores conectados");
                    e.setCancelled(true);
                    return;
                }
                if (isla.getDestroyed()) {
                    e.setCancelled(true);
                    return;
                }
                
                if (isla.getOwner().equals(e.getPlayer().getUniqueId())) {
                    pl.sendMessage("&cNo puedes poner TNT en tu isla");
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
            
            //Bedrock
            if (broken.getType() == Material.BEDROCK) {
                e.setCancelled(true); //Para creativos o así
            }
            
            //Procesar el desactivado de TNT
            if (broken.getType() == Material.TNT) {
                TntIsland isla = checkBedrock(broken.getLocation().add(0, -1, 0).getBlock());
                if (isla == null) {
                    e.setCancelled(true);
                    return;
                }
                
                if (isla.getOwner().equals(e.getPlayer().getUniqueId())) {
                    plugin.getServer().getScheduler().cancelTask(isla.getDestroyTaskId());
                    plugin.getMsg().sendBroadcast(pl.getDisplayName() + " ha evitado la explosión de su isla!");
                    for (Player p : plugin.getGm().getPlayersInGame()) {
                        p.playSound(isla.getBedrockCore().getLocation(), Sound.BLOCK_ANVIL_USE, 10F, 1F);
                    }
                    pl.getUserData().setTntQuitadas(pl.getUserData().getTntQuitadas() + 1);
                    pl.save();
                }
                return;
            }
            
            //No romper bloques en la isla del centro, salvo los que han puesto
            TntIsland centro = TntIsland.getIsland("centro");
            if (centro != null) { //Nunca debería ser null...
                if (centro.getBlocks().contains(broken.getLocation())) {
                    e.setCancelled(true);
                }
            }
            
            //No romper generadores
            Generador gen = Generador.getGenerador(broken.getLocation());
            if (gen != null) {
                e.setCancelled(true);
                pl.sendMessage("&c¡No puedes romper generadores!");
            }
        }
    }
    
    public TntIsland checkBedrock(Block b) {
        if (TntWars.getInstance().getAm().getIslas() != null || !TntWars.getInstance().getAm().getIslas().isEmpty()) {
            for (TntIsland i : TntWars.getInstance().getAm().getIslas()) {
                if (i.getOwner() != null) {
                    if (i.getBedrockCore() != null) {
                        if (i.getBedrockCore().getLocation().getBlockX() == b.getLocation().getBlockX() &&
                                i.getBedrockCore().getLocation().getBlockY() == b.getLocation().getBlockY() &&
                                i.getBedrockCore().getLocation().getBlockZ() == b.getLocation().getBlockZ()) {
                            return i;
                        }
                    }
                }
            }
        }
        return null;
    }
    
    @EventHandler
    public void onMotdChange(ServerListPingEvent e){
        e.setMotd(GameState.getParsedStatus());
    }
    
    @EventHandler
    public void onCraftItem(PrepareItemCraftEvent e) {
        e.getInventory().setResult(new ItemStack(Material.AIR));
    }
}