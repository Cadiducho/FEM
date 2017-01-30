package com.cadiducho.fem.royale.listeners;

import com.cadiducho.fem.core.util.Title;
import com.cadiducho.fem.royale.BattlePlayer;
import com.cadiducho.fem.royale.BattleRoyale;
import com.cadiducho.fem.royale.manager.GameState;
import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.Sound;
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
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.server.ServerListPingEvent;

import java.util.ArrayList;
import java.util.HashMap;

public class GameListener implements Listener {

    private final BattleRoyale plugin;

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
            final BattlePlayer bpDead = BattleRoyale.getPlayer(e.getEntity());
            
            if (e.getEntity().getKiller() instanceof Player) {

                //Si lo asesinan, mostrar asesino y dar stats al asesino
                final BattlePlayer bpKiller = BattleRoyale.getPlayer(e.getEntity().getKiller());
                e.getEntity().getWorld().strikeLightningEffect(e.getEntity().getLocation());
                plugin.getMsg().sendMessage(e.getEntity(), "Te ha matado &e" + e.getEntity().getKiller().getDisplayName());
                Title.sendTitle(e.getEntity(), 1, 7, 1, "&b&l¡" + e.getEntity().getKiller().getDisplayName() + "&a te ha asesinado!", "Puedes volver al Lobby cuando quieras");
                bpKiller.addKillToPlayer();
                bpKiller.sendMessage("Has recibido una moneda por matar a &e" + e.getEntity().getName());
                bpKiller.getPlayer().getInventory().addItem(plugin.getMoneda());
                
                //Stats
                HashMap<Integer, Integer> kills = bpKiller.getUserData().getKills();
                kills.replace(5, kills.get(5) + 1);
                bpKiller.getUserData().setKills(kills);
                bpKiller.getUserData().setCoins(bpKiller.getUserData().getCoins() + 1);
                bpKiller.save();
            } else {
                plugin.getMsg().sendMessage(e.getEntity(), "¡Has muerto!");
                Title.sendTitle(e.getEntity(), 1, 7, 1,"&b&l¡Has muerto!", "Puedes volver al Lobby cuando quieras");
            }

            //Stats del muerto
            e.getEntity().getWorld().strikeLightningEffect(e.getEntity().getLocation());
            plugin.getMsg().sendBroadcast("&c&l" + e.getEntity().getDisplayName() + "&a ha sido eliminado de la partida");
            plugin.getGm().getPlayersInGame().remove(e.getEntity());
            bpDead.setSpectator();
            bpDead.sendMessage("Escribe &e/lobby &fpara volver al Lobby");
            bpDead.repeatActionBar("Escribe &e/lobby &fpara volver al Lobby");
            HashMap<Integer, Integer> deaths = bpDead.getUserData().getDeaths();
            deaths.replace(5, deaths.get(5) + 1);
            bpDead.getUserData().setDeaths(deaths);
            bpDead.save();

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
    
    ArrayList<Material> permitidos = Lists.newArrayList(Material.WORKBENCH);

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Block b = e.getBlock();
        if (!permitidos.contains(b.getType())) {
            e.setCancelled(true);
        }
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
                plugin.getWorld().playSound(cofre.getLocation(), Sound.BLAZE_DEATH, 100F, 100F);
                plugin.getMsg().sendBroadcast("&d¡Ha caido un cofre de abastecimiento en alguna parte del mapa!");
            }
        }
    }
}