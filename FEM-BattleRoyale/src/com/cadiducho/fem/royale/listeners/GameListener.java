package com.cadiducho.fem.royale.listeners;

import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.royale.BattleRoyale;
import com.cadiducho.fem.royale.manager.GameState;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.bukkit.event.player.PlayerMoveEvent;
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
                plugin.getMsg().sendMessage(e.getEntity(), "Te ha matado " + e.getEntity().getKiller().getDisplayName());
                plugin.getMsg().sendBroadcast(e.getEntity().getDisplayName() + " ha sido eliminado de la partida");
                plugin.getGm().getPlayersInGame().remove(e.getEntity());
                plugin.getPm().setSpectator(e.getEntity());
                FEMServer.getUser(e.getEntity()).sendMessage("Escribe &e/lobby &fpara volver al Lobby");
                FEMServer.getUser(e.getEntity()).repeatActionBar("Escribe &e/lobby &fpara volver al Lobby");
                plugin.getPm().addKillToPlayer(e.getEntity().getKiller());
                
                //Stats
                HashMap<Integer, Integer> kills = FEMServer.getUser(e.getEntity().getKiller()).getUserData().getKills();
                kills.replace(5, kills.get(5) + 1);
                FEMServer.getUser(e.getEntity().getKiller()).getUserData().setKills(kills);
                FEMServer.getUser(e.getEntity().getKiller()).save();
                HashMap<Integer, Integer> deaths = FEMServer.getUser(e.getEntity()).getUserData().getDeaths();
                deaths.replace(5, deaths.get(5) + 1);
                FEMServer.getUser(e.getEntity()).getUserData().setDeaths(deaths);
                FEMServer.getUser(e.getEntity()).save();
            } else {
                plugin.getMsg().sendMessage(e.getEntity(), "Has muerto");
                e.getEntity().getWorld().strikeLightningEffect(e.getEntity().getLocation());
                plugin.getMsg().sendBroadcast(e.getEntity().getDisplayName() + " ha sido eliminado de la partida");
                plugin.getGm().getPlayersInGame().remove(e.getEntity());
                plugin.getPm().setSpectator(e.getEntity());
                FEMServer.getUser(e.getEntity()).sendMessage("Escribe &e/lobby &fpara volver al Lobby");
                FEMServer.getUser(e.getEntity()).repeatActionBar("Escribe &e/lobby &fpara volver al Lobby");
                HashMap<Integer, Integer> deaths = FEMServer.getUser(e.getEntity()).getUserData().getDeaths();
                deaths.replace(5, deaths.get(5) + 1);
                FEMServer.getUser(e.getEntity()).getUserData().setDeaths(deaths);
                FEMServer.getUser(e.getEntity()).save();
            }
            if (!plugin.getGm().checkWinner()) {
                plugin.getGm().checkDm();
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {  
            if (plugin.getGm().acceptPlayers() || GameState.state == GameState.PVE) {
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
            if (plugin.getGm().acceptPlayers() || GameState.state == GameState.PVE) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityDamageByBlock(EntityDamageByBlockEvent e) {
        if (e.getEntity() instanceof Player) {
            if (plugin.getGm().acceptPlayers() || GameState.state == GameState.PVE) {
                e.setCancelled(true);
            }
        }
    }
    
    ArrayList<Material> permitidos = Lists.newArrayList(Material.WORKBENCH, Material.TNT);

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        e.setCancelled(true);  
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Block b = e.getBlock();
        if (!permitidos.contains(b.getType())) {
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if (GameState.state == GameState.COUNTDOWN) {
            if (!(e.getFrom().getBlockZ() == e.getTo().getBlockZ()) || !(e.getFrom().getBlockX() == e.getTo().getBlockX())) {
                e.setTo(e.getFrom());
            }
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
                plugin.getAm().fillChest(cofre.getInventory(), true);
                plugin.getWorld().strikeLightningEffect(cofre.getLocation());
                plugin.getMsg().sendBroadcast("&6¡Ha caido un cofre de abastecimiento en alguna parte del mapa!");
            }
        }
    }
}