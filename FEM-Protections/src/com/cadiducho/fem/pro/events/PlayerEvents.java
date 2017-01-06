package com.cadiducho.fem.pro.events;

import com.cadiducho.fem.core.cmds.FEMCmd;
import com.cadiducho.fem.pro.ProArea;
import com.cadiducho.fem.pro.ProBlock;
import com.cadiducho.fem.pro.ProPlayer;
import com.cadiducho.fem.pro.Protections;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.material.Door;

import java.util.Set;

public class PlayerEvents implements Listener{

    private Protections plugin;

    public PlayerEvents(Protections Main) {
        this.plugin = Main;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        Player p = e.getPlayer();
        ProPlayer player = new ProPlayer(p.getUniqueId());

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (p.isSneaking()) {
                Block b = player.getPlayer().getTargetBlock((Set<Material>) null, 4);
                ProBlock block = new ProBlock(b, player);

                if (block.getAllTypesToProtect().contains(b.getType())) {
                    if (!block.isProtected()) {
                        block.protectBlock();
                        player.sendMessage("&aBloque protegido");
                        return;
                    } else {
                        player.sendMessage("&cEste bloque ya está protegido");
                        return;
                    }
                } else {
                    player.sendMessage("&cEste bloque no se puede proteger");
                    return;
                }
            }

            Block b = e.getClickedBlock();
            ProBlock block = new ProBlock(e.getClickedBlock());
            if (block.getAllTypesToProtect().contains(b)) {
                if (block.isProtected()) {
                    if (!player.isOnRank(FEMCmd.Grupo.Moderador) && !block.getProtectionPlayers().contains(player) || !block.getProtectionOwners().contains(player)) {
                        e.setCancelled(true);
                        return;
                    }
                }
            }

            if (block.getFlag("autoCloseDoors")) {
                if (b.getType().equals(Material.WOODEN_DOOR) || b.getType().equals(Material.SPRUCE_DOOR) || b.getType().equals(Material.DARK_OAK_DOOR) || b.getType().equals(Material.BIRCH_DOOR) || b.getType().equals(Material.ACACIA_DOOR)) {
                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        BlockState state = b.getState();
                        Door door = (Door) state.getData();
                        if (! door.isOpen()) return;
                        door.setOpen(false);
                        state.update();
                    }, 43);
                }
            }
        }
    }

    @EventHandler
    public void onJoinArea(PlayerMoveEvent e){
        Player p = e.getPlayer();
        ProPlayer player = new ProPlayer(p.getUniqueId());

        if (new ProArea().getAllAreas().size() == 0) return;

        new ProArea().getAllAreas().forEach(a ->{
            if (a.getCuboidRegion().toArray().contains(p.getLocation().getBlock())){
                if (a.getAreaOwners().equals(player) || a.getAreaUsers().contains(player)) return;
                if (!a.getFlags("join")) p.setVelocity(p.getLocation().getDirection().normalize().multiply(-2));
            }
        });
    }

    @EventHandler
    public void onPvPInArea(EntityDamageByEntityEvent e){
        Entity damaged = e.getEntity();
        Entity damager = e.getDamager();

        if (damaged instanceof Player && damager instanceof Player) {
            new ProArea().getAllAreas().forEach(a ->{
                if (a.getCuboidRegion().toArray().contains(damaged.getLocation().getBlock()) || a.getCuboidRegion().toArray().contains(damager.getLocation().getBlock())){
                    if (!a.getFlags("pvp")) {
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
                if (a.getCuboidRegion().toArray().contains(damaged.getLocation().getBlock()) || a.getCuboidRegion().toArray().contains(damager.getLocation().getBlock())){
                    if (!a.getFlags("pve")) {
                        e.setCancelled(true);
                        ((Player) damaged).setLastDamage(0);
                        damager.sendMessage(ChatColor.RED + "Este area es un area no-pve");
                        return;
                    }
                }
            });
        }
    }
}
