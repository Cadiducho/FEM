package com.cadiducho.fem.dropper.listener;

import com.cadiducho.fem.dropper.DropPlayer;
import com.cadiducho.fem.dropper.Dropper;
import com.cadiducho.fem.dropper.task.RespawnTask;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.*;

public class PlayerListener implements Listener {

    private final Dropper plugin;

    public PlayerListener(Dropper instance) {
        plugin = instance;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        e.setJoinMessage(null);
        Dropper.getPlayer(e.getPlayer()).setLobbyInventory();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        e.setQuitMessage(null);

        Dropper.players.remove(Dropper.getPlayer(player));
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (e.getClickedBlock() != null && (e.getClickedBlock().getType() == Material.WALL_SIGN || e.getClickedBlock().getType() == Material.SIGN_POST)) {
            Sign sign = (Sign) e.getClickedBlock().getState();

            if (ChatColor.stripColor(sign.getLine(0)).equalsIgnoreCase("[dropper]")) {
                String mapa = sign.getLine(1);
                Dropper.getPlayer(e.getPlayer()).sendToDropper(mapa);
            }
        }

        if (e.getAction() == Action.PHYSICAL && e.getClickedBlock().getType() == Material.GOLD_PLATE) {
            Dropper.getPlayer(e.getPlayer()).endMap();
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent e) {
        if (e instanceof LivingEntity) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (e.getCause() != EntityDamageEvent.DamageCause.FALL) {
            e.setCancelled(true);
            return;
        }

        if (e.getEntity() instanceof Player) {
            if (e.getDamage() > 1) {
                Player p = (Player) e.getEntity();
                final DropPlayer pl = Dropper.getPlayer(p);

                //Limpiar jugador y respawn
                pl.setCleanPlayer(GameMode.SPECTATOR);
                new RespawnTask(pl).runTaskTimer(plugin, 1L, 20L);
                e.setCancelled(true);
                return;
            }
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerFood(FoodLevelChangeEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerPickUp(PlayerPickupItemEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        e.setFormat(ChatColor.GREEN + e.getPlayer().getDisplayName() + ChatColor.WHITE + ": " + ChatColor.GRAY + e.getMessage());
    }
}
