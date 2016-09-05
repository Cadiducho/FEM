package com.cadiducho.fem.uhc.listeners;

import com.cadiducho.fem.uhc.Main;
import com.cadiducho.fem.uhc.manager.GameState;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;

import org.bukkit.entity.Player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamage implements Listener {

    public Main plugin;

    public EntityDamage(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntiyDamage(EntityDamageEvent event) {
        if (GameState.state == GameState.LOBBY || GameState.state == GameState.TELETRANSPORTE || GameState.state == GameState.PVE || GameState.state == GameState.FIN) {
            if(event.getEntityType() == EntityType.PLAYER && event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK){
                event.setCancelled(true);
            }
        } else {
            event.setCancelled(false);
        }

    }

    @EventHandler
    public void onEntityDamageByBlock(EntityDamageByBlockEvent e) {
        if (e.getEntity() instanceof Player) {
            if (GameState.state == GameState.LOBBY || GameState.state == GameState.TELETRANSPORTE) {
                e.setCancelled(true);
            } else if (GameState.state == GameState.PVE || GameState.state == GameState.JUEGO || GameState.state == GameState.ELIMINACION) {
                e.setCancelled(false);
            }
        }
    }

    @EventHandler
    public void onEntiyDamageByEntity(EntityDamageByEntityEvent e) {
        if (GameState.state == GameState.LOBBY || GameState.state == GameState.TELETRANSPORTE || GameState.state == GameState.PVE || GameState.state == GameState.FIN) {
            if (e.getEntity() instanceof Player) {
                if (e.getDamager() instanceof Player) {
                    Player p = (Player) e.getDamager();
                    e.setCancelled(true);
                } else if (e.getDamager() instanceof Arrow) {
                    Arrow arrow = (Arrow) e.getDamager();
                    if (arrow.getShooter() instanceof Player) {
                        Player p = (Player) arrow.getShooter();
                        arrow.remove();
                        e.setCancelled(true);
                    }
                }
            }
        }
    }
}
