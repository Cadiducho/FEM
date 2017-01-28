package com.cadiducho.fem.lucky.listeners;

import com.cadiducho.fem.lucky.LuckyPlayer;
import com.cadiducho.fem.lucky.LuckyWarriors;
import com.cadiducho.fem.lucky.manager.GameState;
import com.cadiducho.fem.lucky.utils.LuckyPacks;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.HashMap;
import java.util.Random;

public class GameListener implements Listener {

    private final LuckyWarriors plugin;

    public GameListener(LuckyWarriors instance) {
        plugin = instance;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        e.setDeathMessage(null);
        if (plugin.getGm().isInGame()) {
            final LuckyPlayer lpDead = LuckyWarriors.getPlayer(e.getEntity());
            
            if (e.getEntity().getKiller() instanceof Player) {
                e.getEntity().getWorld().strikeLightningEffect(e.getEntity().getLocation());
                plugin.getMsg().sendMessage(e.getEntity(), "Has sido asesinado por " + e.getEntity().getKiller().getDisplayName());
                plugin.getMsg().sendBroadcast(e.getEntity().getDisplayName() + " ha sido eliminado de la partida");
                plugin.getGm().getPlayersInGame().remove(e.getEntity());
                
                final LuckyPlayer lpKiller = LuckyWarriors.getPlayer(e.getEntity());
                lpDead.setSpectator();
                lpDead.sendMessage("Escribe &e/lobby &fpara volver al Lobby");
                lpDead.repeatActionBar("Escribe &e/lobby &fpara volver al Lobby");
                lpDead.addKillToPlayer();
                
                //Stats
                HashMap<Integer, Integer> kills = lpKiller.getUserData().getKills();
                kills.replace(6, kills.get(6) + 1);
                lpKiller.getUserData().setKills(kills);
                lpKiller.getUserData().setCoins(lpKiller.getUserData().getCoins() + 1);
                lpKiller.save();
                HashMap<Integer, Integer> deaths = lpKiller.getUserData().getDeaths();
                deaths.replace(6, deaths.get(6) + 1);
                lpDead.getUserData().setDeaths(deaths);
                lpDead.save();
            } else {
                plugin.getMsg().sendMessage(e.getEntity(), "Has muerto");
                e.getEntity().getWorld().strikeLightningEffect(e.getEntity().getLocation());
                plugin.getMsg().sendBroadcast(e.getEntity().getDisplayName() + " ha sido eliminado de la partida");
                plugin.getGm().getPlayersInGame().remove(e.getEntity());
                lpDead.setSpectator();
                lpDead.sendMessage("Escribe &e/lobby &fpara volver al Lobby");
                lpDead.repeatActionBar("Escribe &e/lobby &fpara volver al Lobby");
                HashMap<Integer, Integer> deaths = lpDead.getUserData().getDeaths();
                deaths.replace(6, deaths.get(6) + 1);
                lpDead.getUserData().setDeaths(deaths);
                lpDead.save();
            }
            if (!plugin.getGm().checkWinner()) {
                plugin.getGm().checkDm();
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            if (GameState.state == GameState.LUCKY || GameState.state == GameState.CRAFT) {
                e.getEntity().setFireTicks(0);
                e.setCancelled(true);
                return;
            }
            if (!plugin.getGm().isInGame()) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (!plugin.getGm().isInGame() || GameState.state == GameState.LUCKY || GameState.state == GameState.CRAFT) {
            e.getEntity().setFireTicks(0);
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamageByBlock(EntityDamageByBlockEvent e) {
        if (e.getEntity() instanceof Player) {
            if (!plugin.getGm().isInGame()) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        if (GameState.state == GameState.DEATHMATCH || GameState.state == GameState.GAME || GameState.state == GameState.CRAFT) {
            plugin.getAm().getBlocksPlaced().put(e.getBlock().getLocation(), e.getBlock());
            e.setCancelled(false);
        } else {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if ((GameState.state == GameState.CRAFT || GameState.state == GameState.GAME || GameState.state == GameState.DEATHMATCH) && plugin.getAm().getBlocksPlaced().containsKey(e.getBlock().getLocation())) {
            e.setCancelled(false);
            return;
        }
        
        //Ejecutar sistemas de luckies si es esa fase y se rompe la esponja
        if (GameState.state == GameState.LUCKY) {
            Block block = e.getBlock();
            block.getDrops();
            Player p = e.getPlayer();
            Location loc = block.getLocation();
            if (block.getType() == Material.SPONGE) {
                
                //Sustituimos por aire y calculamos probabilidad
                e.getBlock().setType(Material.AIR);
                int proporcion = new Random().nextInt(100) + 1;
                loc.add(0, 1.3, 0); // Añadir cierta altura para crear efecto de caida
                
                /*
                * Normales: 30%
                * Util: 35%
                * Cheto: 30%
                * Mierda: 5%
                */
                
                if (proporcion <= 100 && proporcion > 70) { // 30%
                    LuckyPacks.getRandomPackGenericos(loc);
                } else if (proporcion <= 70  && proporcion > 35) { // 35%
                    LuckyPacks.getRandomPackUtil(loc);
                } else if (proporcion <= 35 && proporcion > 5) { // 30%
                    LuckyPacks.getRandomPackChetos(loc);
                } else if (proporcion <= 5) { // 5%
                    LuckyPacks.getRandomPackMierda(loc);
                } else {
                    System.out.println("Proporciones mal configuradas!");
                }
                
                // Dar un nivel para poder encantar en la fase de crafteo algo
                p.giveExpLevels(1);
                p.playSound(loc, Sound.ORB_PICKUP, 1f, 1f);

                // Actualizar stats
                final LuckyPlayer lp = LuckyWarriors.getPlayer(p);
                lp.getUserData().setLuckyRotos(lp.getUserData().getLuckyRotos() + 1);
                lp.save();
            } 
        }
        e.setCancelled(true);
    }
}
