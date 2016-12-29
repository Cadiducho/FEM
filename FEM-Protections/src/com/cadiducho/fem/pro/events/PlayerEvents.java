package com.cadiducho.fem.pro.events;

import com.cadiducho.fem.pro.ProArea;
import com.cadiducho.fem.pro.ProPlayer;
import com.cadiducho.fem.pro.Protections;
import org.bukkit.ChatColor;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerEvents implements Listener{

    private Protections plugin;

    public PlayerEvents(Protections Main) {
        this.plugin = Main;
    }

    @EventHandler
    public void onJoinArea(PlayerMoveEvent e){
        Player p = e.getPlayer();
        ProPlayer player = new ProPlayer(p.getUniqueId());

        new ProArea().getAllAreas().forEach(a ->{
            if (a.getCuboidRegion().toArray().contains(p.getLocation().getWorld())){
                if (a.getOwner().equals(player)) return;
                if (!a.getSetting("join")) p.setVelocity(p.getLocation().getDirection().normalize().multiply(-2));
            }
        });
    }

    @EventHandler
    public void onPvPInArea(EntityDamageByEntityEvent e){
        Entity damaged = e.getEntity();
        Entity damager = e.getDamager();

        if (damaged instanceof Player && damager instanceof Player) {
            new ProArea().getAllAreas().forEach(a ->{
                if (a.getCuboidRegion().toArray().contains(damaged.getLocation()) || a.getCuboidRegion().toArray().contains(damager.getLocation())){
                    if (!a.getSetting("pvp")) {
                        e.setCancelled(true);
                        ((Player) damaged).setLastDamage(0);
                        damager.sendMessage(ChatColor.RED + "Este area es un area no-pvp");
                        return;
                    }
                }
            });
        }

        if (damaged instanceof Player || damager instanceof Monster || damager instanceof Animals || damager instanceof Wolf) {
            new ProArea().getAllAreas().forEach(a ->{
                if (a.getCuboidRegion().toArray().contains(damaged.getLocation()) || a.getCuboidRegion().toArray().contains(damager.getLocation())){
                    if (!a.getSetting("pve")) {
                        e.setCancelled(true);
                        ((Player) damaged).setLastDamage(0);
                        return;
                    }
                }
            });
        }
    }
}
