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
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Arrays;

public class WorldEvents implements Listener {

    private Protections plugin;

    public WorldEvents(Protections Main) {
        this.plugin = Main;
    }

    private ArrayList<ProPlayer> checking = new ArrayList<>();

    private ProArea area;
    private BukkitTask bt;

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

        if (b.getWorld().getName().equalsIgnoreCase(plugin.getFiles().getConfig().getString("Mundo"))) {
            if (plugin.getFiles().getCurrentID("areas") != 0) {
                for (int x = 0; x < plugin.getFiles().getCurrentID("areas"); x++) {
                    ProArea area = new ProArea(x);
                    if (area.getCuboidRegion().contains(b)) {
                        if (!player.isOnRank(FEMCmd.Grupo.Moderador) && !area.getAreaOwners().equals(player) || !area.getAreaUsers().contains(player)) {
                            e.setCancelled(true);
                            player.getPlayer().sendMessage(ChatColor.RED + "No puedes poner bloques en una zona que no es tuya");
                            return;
                        }
                    }
                }
            }
            Arrays.asList(ProType.values()).forEach(t -> {
                if (b.getType() == t.getMaterial()) {
                    if (checking.contains(player)) return;
                    this.area = new ProArea(player.getPlayer().getLocation(), ProType.parseMaterial(b.getType()), player);
                    this.area.generateCuboidRegion();
                    if (this.area.hitOtherArena()) {
                        player.getPlayer().sendMessage(ChatColor.RED + "El nuevo arena esta chocando con otro area. Pon el bloque en otro lugar");
                        e.setCancelled(true);
                        return;
                    }
                    checking.add(player);
                    //Método temporar, no estará así en el futuro (Espero...)
                    player.sendMessage("&2Si quieres poner el area aquí, escribe &cSI &2en el chat, en el caso contrario escribe &cNO");
                    this.bt = Bukkit.getScheduler().runTaskTimer(this.plugin, ()-> area.showArea(), 0l, 1l);
                }
            });
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e){
        Player p = e.getPlayer();
        ProPlayer player = new ProPlayer(p.getUniqueId());

        if (checking.contains(player)){
            switch (e.getMessage().toLowerCase()){
                case "si":
                    this.area.generateArea(Material.DOUBLE_STONE_SLAB2);
                    checking.remove(player);
                    this.bt.cancel();
                    break;
                case "no":
                    p.getInventory().addItem(ProType.generateItemStack(this.area.getProType()));
                    checking.remove(player);
                    this.bt.cancel();
                    break;
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Block b = e.getBlock();
        ProPlayer player = new ProPlayer(e.getPlayer().getUniqueId());

        switch (b.getType()) {
            case FURNACE:
            case CHEST:
                ProBlock block = new ProBlock(b);
                if (!player.isOnRank(FEMCmd.Grupo.Moderador) && !block.getProtectionOwners().contains(player)) {
                    player.sendMessage("No puedes romper este bloque ya que no es tuyo");
                    return;
                }
                block.removeProtection();
                break;
        }

        if (b.getWorld().getName().equalsIgnoreCase(plugin.getFiles().getConfig().getString("Mundo"))) {
            for (int x = 0; x < plugin.getFiles().getCurrentID("areas"); x++) {
                ProArea area = new ProArea(x);
                if (area.getCuboidRegion().contains(b)) {
                    if (!player.isOnRank(FEMCmd.Grupo.Moderador) && !area.getAreaOwners().equals(player) || !area.getAreaUsers().contains(player)) {
                        e.setCancelled(true);
                        player.getPlayer().sendMessage(ChatColor.RED + "No puedes romper bloques en una zona que no es tuya");
                        return;
                    }
                    if (area.getDueño().equals(player) || player.isOnRank(FEMCmd.Grupo.Moderador) || b.getLocation().equals(area.getLocation())) {
                        area.removeArena(Material.AIR);
                    }
                }
            }
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
