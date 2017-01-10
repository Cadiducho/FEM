package com.cadiducho.fem.pro.events;

import com.cadiducho.fem.core.cmds.FEMCmd;
import com.cadiducho.fem.pro.ProArea;
import com.cadiducho.fem.pro.ProBlock;
import com.cadiducho.fem.pro.ProPlayer;
import com.cadiducho.fem.pro.Protections;
import com.cadiducho.fem.pro.utils.ProType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.Arrays;

public class WorldEvents implements Listener {

    private Protections plugin;

    public WorldEvents(Protections Main) {
        this.plugin = Main;
    }

    //TODO: Block Flags

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Block b = e.getBlock();
        ProPlayer player = new ProPlayer(e.getPlayer().getUniqueId());

        switch (b.getType()) {
            case FURNACE:
            case CHEST:
                ProBlock block = new ProBlock(b, player);
                block.protectBlock();
                break;
        }

        if (b.getType() == Material.HOPPER){
            Block bUp = b.getRelative(BlockFace.UP);
            ProBlock block = new ProBlock(bUp);

            if (block.getAllTypesToProtect().contains(bUp)){
                if (block.isProtected()){
                    if (block.getFlag("useHoppers")) {
                        if (!player.isOnRank(FEMCmd.Grupo.Moderador) || !block.getProtectionOwners().equals(player) || !block.getProtectionPlayers().contains(player)) {
                            player.sendMessage("&cNo se puede poner un hopper debajo de este cofre");
                            e.setCancelled(true);
                            return;
                        }
                    }
                }
            }
        }

        if (b.getWorld().getName().equalsIgnoreCase(plugin.getFiles().getConfig().getString("Mundo"))) {
            if (new ProArea().getAllAreas().size() == 0) return;
            new ProArea().getAllAreas().forEach(a -> {
                if (a.getCuboidRegion().contains(b)) {
                    if (!player.isOnRank(FEMCmd.Grupo.Moderador) || !a.getAreaOwners().equals(player) || !a.getAreaUsers().contains(player)) {
                        e.setCancelled(true);
                        player.getPlayer().sendMessage(ChatColor.RED + "No puedes poner bloques en una zona que no es tuya");
                        return;
                    }
                }
            });
        }
        Arrays.asList(ProType.values()).forEach(t -> {
            if (b.getType() == t.getMaterial()) {
                ProArea area = new ProArea(player.getPlayer().getLocation(), ProType.parseMaterial(b.getType()), player);
                area.generateCuboidRegion();
                if (area.hitOtherArena()) {
                    player.getPlayer().sendMessage(ChatColor.RED + "El nuevo arena esta chocando con otro area. Pon el bloque en otro lugar");
                    e.setCancelled(true);
                    return;
                }
                area.generateArea(Material.DOUBLE_STONE_SLAB2);
                BukkitTask bt = Bukkit.getScheduler().runTaskTimer(this.plugin, ()-> area.showArea(), 0l, 1l);

                Bukkit.getScheduler().runTaskLater(this.plugin, ()-> bt.cancel(), 100);
            }
        });
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Block b = e.getBlock();
        ProPlayer player = new ProPlayer(e.getPlayer().getUniqueId());

        switch (b.getType()) {
            case FURNACE:
            case CHEST:
                ProBlock block = new ProBlock(b);
                if (!player.isOnRank(FEMCmd.Grupo.Moderador) || !block.getDueño().equals(player)) {
                    player.sendMessage("No puedes romper este bloque ya que no es tuyo");
                    return;
                }
                block.removeProtection();
                break;
        }

        if (b.getWorld().getName().equalsIgnoreCase(plugin.getFiles().getConfig().getString("Mundo"))) {
            if (new ProArea().getAllAreas().size() == 0) return;
            new ProArea().getAllAreas().forEach(a -> {
                if (a.getCuboidRegion().contains(b)) {
                    if (!player.isOnRank(FEMCmd.Grupo.Moderador) || !a.getAreaOwners().equals(player) || !a.getAreaUsers().contains(player)) {
                        e.setCancelled(true);
                        player.getPlayer().sendMessage(ChatColor.RED + "No puedes romper bloques en una zona que no es tuya");
                        return;
                    }
                    if (a.getDueño().equals(player) || player.isOnRank(FEMCmd.Grupo.Moderador) || b.getLocation().equals(a.getLocation())) {
                        a.removeArena(Material.AIR);
                    }
                }
            });
        }
    }

    @EventHandler
    public void onExplosion(EntityExplodeEvent e){
        e.blockList().forEach(b -> new ProArea().getAllAreas().forEach(a ->{
            if (a.getCuboidRegion().toArray().contains(b)){
                if (!a.getFlags("explosion")){
                    e.setCancelled(true);
                }
            }
        }));
    }

    @EventHandler
    public void onExplosion(BlockExplodeEvent e){
        e.blockList().forEach(b -> new ProArea().getAllAreas().forEach(a ->{
            if (a.getCuboidRegion().toArray().contains(b)){
                if (!a.getFlags("explosion")){
                    e.setCancelled(true);
                }
            }
        }));
    }
}
