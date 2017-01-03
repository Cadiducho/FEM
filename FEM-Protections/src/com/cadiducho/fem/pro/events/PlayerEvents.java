package com.cadiducho.fem.pro.events;

import com.cadiducho.fem.core.cmds.FEMCmd;
import com.cadiducho.fem.pro.*;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

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
            ProBlock block = new ProBlock(e.getClickedBlock());
            if (block.getAllTypesToProtect().contains(e.getClickedBlock())) {
                if (block.isProtected()) {
                    if (!block.getProtectionPlayers().contains(player)) {
                        e.setCancelled(true);
                        return;
                    }
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
                if (a.getCuboidRegion().toArray().contains(damaged.getLocation().getBlock()) || a.getCuboidRegion().toArray().contains(damager.getLocation().getBlock())){
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
                if (a.getCuboidRegion().toArray().contains(damaged.getLocation().getBlock()) || a.getCuboidRegion().toArray().contains(damager.getLocation().getBlock())){
                    if (!a.getSetting("pve")) {
                        e.setCancelled(true);
                        ((Player) damaged).setLastDamage(0);
                        return;
                    }
                }
            });
        }
    }

    @EventHandler
    public void inventoryClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) {
            return;
        }

        Player p = (Player) e.getWhoClicked();
        ProPlayer player = new ProPlayer(p.getUniqueId());

        switch (e.getInventory().getTitle()) {
            case "Todas tus areas":
                e.setCancelled(true);
                switch (e.getSlot()){
                    case 50:
                        //TODO: Pages
                        break;
                    default:
                        ProMenu.setArea(e.getSlot());
                        ProMenu.openMenu(player, ProMenu.MenuType.SETTINGS, ProMenu.getArea());
                        break;
                }
                break;

            case "Configuracion":
                e.setCancelled(true);
                ProArea area = new ProArea(ProMenu.getArea());
                switch (e.getSlot()) {
                    case 1:
                    case 10:
                        area.setSetting("join", !area.getSetting("join"));
                        ProMenu.openMenu(player, ProMenu.MenuType.SETTINGS, ProMenu.getArea());
                        player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ORB_PICKUP, 1F, 1F);
                        player.sendMessage("&eAjuste cambiado");
                        break;
                    case 2:
                    case 11:
                        area.setSetting("pvp", !area.getSetting("pvp"));
                        ProMenu.openMenu(player, ProMenu.MenuType.SETTINGS, ProMenu.getArea());
                        player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ORB_PICKUP, 1F, 1F);
                        player.sendMessage("&eAjuste cambiado");
                        break;
                    case 3:
                    case 12:
                        area.setSetting("pve", !area.getSetting("pve"));
                        ProMenu.openMenu(player, ProMenu.MenuType.SETTINGS, ProMenu.getArea());
                        player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ORB_PICKUP, 1F, 1F);
                        player.sendMessage("&eAjuste cambiado");
                        break;
                    case 4:
                    case 13:
                        area.setSetting("explosion", !area.getSetting("explosion"));
                        ProMenu.openMenu(player, ProMenu.MenuType.SETTINGS, ProMenu.getArea());
                        player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ORB_PICKUP, 1F, 1F);
                        player.sendMessage("&eAjuste cambiado");
                        break;
                }
                break;

            default:
                break;
        }

        if (player.isOnRank(FEMCmd.Grupo.Moderador)) { //Staff poder usar inventarios
            e.setCancelled(false);
            return;
        }
        e.setCancelled(true); //Prevenir que muevan / oculten / tiren objetos de la interfaz del Lobby
    }
}
