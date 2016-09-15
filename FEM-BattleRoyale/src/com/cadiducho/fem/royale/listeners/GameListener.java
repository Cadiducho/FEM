package com.cadiducho.fem.royale.listeners;

import com.cadiducho.fem.royale.BattleRoyale;
import com.cadiducho.fem.royale.manager.GameState;
import java.util.HashMap;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.server.ServerListPingEvent;

public class GameListener implements Listener {

    private final BattleRoyale plugin;
    private final HashMap<Location, Block> blocksPlaced = new HashMap<>();

    public GameListener(BattleRoyale instance) {
        plugin = instance;
    }

    @EventHandler
    public void onMotd(ServerListPingEvent e) {
        e.setMotd(GameState.getParsedStatus());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEntityEvent e) {
        /*if (plugin.getGm().isInGame()) {
            if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) { //destruir?
                if (e.getClickedBlock() != null) {
                    if (blocksPlaced.containsKey(e.getClickedBlock().getLocation()) && blocksPlaced.containsValue(e.getClickedBlock())) {
                        if (e.getClickedBlock().getType() == Material.WOOD) {
                            e.getClickedBlock().setType(Material.AIR);
                            e.getClickedBlock().getDrops().clear();
                            e.getClickedBlock().getWorld().dropItem(e.getClickedBlock().getLocation().add(0.5, 0.5, 0.5), new ItemStack(Material.WOOD, 1)).setVelocity(new Vector(0, 0, 0));
                        } else if (e.getClickedBlock().getType() == Material.COBBLESTONE) {
                            e.getClickedBlock().setType(Material.AIR);
                            e.getClickedBlock().getDrops().clear();
                            e.getClickedBlock().getWorld().dropItem(e.getClickedBlock().getLocation().add(0.5, 0.5, 0.5), new ItemStack(Material.COBBLESTONE, 1)).setVelocity(new Vector(0, 0, 0));
                        }
                    }
                }
            }
        }*/
        if (e.getRightClicked() instanceof Villager) {
            if (plugin.getGm().isInGame()) {
                e.setCancelled(true);
                Villager v = (Villager) e.getRightClicked();
                if (v.getName().toLowerCase().contains("compro")) {
                    plugin.getAm().getCompro().addCustomer(e.getPlayer());
                } else if (v.getName().toLowerCase().contains("vendo")) {
                    plugin.getAm().getVendo().addCustomer(e.getPlayer());
                }
            }
        } else {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        e.setDeathMessage(null);
        if (plugin.getGm().isInGame()) {
            if (e.getEntity().getKiller() instanceof Player) {
                e.getEntity().getWorld().strikeLightningEffect(e.getEntity().getLocation());
                plugin.getMsg().sendMessage(e.getEntity(), "Has matado a " + e.getEntity().getKiller().getDisplayName());
                plugin.getMsg().sendBroadcast(e.getEntity().getDisplayName() + " ha sido eliminado de la partida");
                plugin.getGm().getPlayersInGame().remove(e.getEntity());
                plugin.getPm().setSpectator(e.getEntity());
                plugin.getPm().addKillToPlayer(e.getEntity().getKiller());
            } else {
                plugin.getMsg().sendMessage(e.getEntity(), "Has muerto");
                e.getEntity().getWorld().strikeLightningEffect(e.getEntity().getLocation());
                plugin.getMsg().sendBroadcast(e.getEntity().getDisplayName() + " ha sido eliminado de la partida");
                plugin.getGm().getPlayersInGame().remove(e.getEntity());
                plugin.getPm().setSpectator(e.getEntity());
            }
            if (!plugin.getGm().checkWinner()) {
                plugin.getGm().checkDm();
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            if (plugin.getGm().isWaiting() || GameState.state == GameState.PVE) {
                e.setCancelled(true);
            }
        }
        if (e.getEntity() instanceof Villager) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player) {
            if (plugin.getGm().isWaiting() || GameState.state == GameState.PVE) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityDamageByBlock(EntityDamageByBlockEvent e) {
        if (e.getEntity() instanceof Player) {
            if (plugin.getGm().isWaiting() || GameState.state == GameState.PVE) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (plugin.getGm().isInGame() || plugin.getGm().isDm() || GameState.state == GameState.PVE) {
            if (blocksPlaced.containsKey(e.getBlock().getLocation()) && blocksPlaced.containsValue(e.getBlock())) {
                blocksPlaced.remove(e.getBlock().getLocation(), e.getBlock());
            }
        } else {
            e.setCancelled(true);
        }
        
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        if (plugin.getGm().isInGame() || plugin.getGm().isDm() || GameState.state == GameState.PVE) {
            if (blocksPlaced.containsKey(e.getBlock().getLocation()) && blocksPlaced.containsValue(e.getBlock())) {
                blocksPlaced.put(e.getBlock().getLocation(), e.getBlock());
            }
        } else {
            e.setCancelled(true);
        }  
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onFallingChest(EntityChangeBlockEvent e) {
        if(e.getEntity() instanceof FallingBlock) {
            FallingBlock chest = (FallingBlock) e.getEntity();
            if (chest.getMaterial() == Material.TRAPPED_CHEST) {
                e.setCancelled(true);
                e.getBlock().setType(Material.TRAPPED_CHEST);
                Chest cofre = (Chest)e.getBlock().getState();
                plugin.getAm().fillChest(cofre.getInventory());
                plugin.getWorld().strikeLightningEffect(cofre.getLocation());
            }
        }
    }
}