package com.cadiducho.fem.gem.listener;

import com.cadiducho.fem.core.api.FEMServer.GameID;
import com.cadiducho.fem.gem.GemHunters;
import com.cadiducho.fem.gem.GemPlayer;
import com.cadiducho.fem.gem.task.GameTask;
import com.cadiducho.fem.gem.task.RespawnTask;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.*;


public class PlayerListener implements Listener {

    private final GemHunters plugin;

    public PlayerListener(GemHunters instance) {
        plugin = instance;
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent e) {
        if (plugin.getGm().acceptPlayers() && plugin.getGm().getPlayersInGame().size() < plugin.getAm().getMaxPlayers()) {
            e.allow();
        } else {
            e.setResult(PlayerLoginEvent.Result.KICK_OTHER);
            e.setKickMessage("No tienes acceso a entrar aquí.");
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        e.setJoinMessage(null);
        if (plugin.getGm().acceptPlayers()) {
            player.teleport(plugin.getAm().getLobby());
            GemHunters.getPlayer(player).setLobbyPlayer();
            plugin.getMsg().sendBroadcast("&7Ha entrado al juego &e" + player.getDisplayName() + " &3(&b" + plugin.getGm().getPlayersInGame().size() + "&d/&b" + plugin.getAm().getMaxPlayers() + "&3)");
            plugin.getGm().checkStart();
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        e.setQuitMessage(null);
        
        plugin.getGm().removePlayerFromGame(player);
        plugin.getMsg().sendBroadcast("&7abandonó el juego &e" + player.getDisplayName() + " &3(&b" + plugin.getGm().getPlayersInGame().size() + "&d/&b" + plugin.getAm().getMaxPlayers() + "&3)");
 
        GemHunters.players.remove(GemHunters.getPlayer(player));
        
        if (plugin.getGm().getPlayersInGame().isEmpty()) {
            GameTask.end();
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (e.getClickedBlock() != null) {
            if (e.getClickedBlock().getType().equals(Material.CHEST) || e.getClickedBlock().getType().equals(Material.TRAPPED_CHEST)) {
                e.setCancelled(true);
                return;
            }
        }
        
        if (plugin.getGm().isInLobby()) {
            e.setCancelled(true);
            if (e.getItem() != null) {
                if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    switch (e.getItem().getType()){
                        case COMPASS:
                            GemHunters.getPlayer(e.getPlayer()).sendToLobby();
                            break;
                    }
                }
            }
        }
    }
        
    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent e) {
        if (e instanceof LivingEntity) {
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (!plugin.getGm().isInGame()) {
            e.setCancelled(true);
            return;
        }
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            final GemPlayer pl = GemHunters.getPlayer(p);

            //Simular muerte
            if (p.getHealth() - e.getDamage() < 1) {
                e.getEntity().getWorld().strikeLightningEffect(e.getEntity().getLocation());
                plugin.getMsg().sendBroadcast("&e" + p.getDisplayName() + " &7ha muerto!");
                
                //Limpiar jugador y respawn
                pl.setCleanPlayer(GameMode.SPECTATOR);
                new RespawnTask(pl).runTaskTimer(plugin, 1L, 20L);
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (!plugin.getGm().isInGame()) {
            e.setCancelled(true);
        } else {
            if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
                Player damager = (Player) e.getDamager();
                Player p = (Player) e.getEntity();
                final GemPlayer pl = GemHunters.getPlayer(p);

                if (plugin.getTm().getTeam(p) == plugin.getTm().getTeam(damager)) {
                    e.setCancelled(true);
                    return;
                }
                //Simular muerte
                if (p.getHealth() - e.getDamage() < 1) { 
                    e.getEntity().getWorld().strikeLightningEffect(e.getEntity().getLocation());
                    plugin.getMsg().sendBroadcast("&e" + p.getDisplayName() + " &7ha muerto a manos de &e" + damager.getDisplayName());
                    
                    //Stats
                    final GemPlayer gp = GemHunters.getPlayer(p);
                    gp.getUserData().addKill(GameID.GEMHUNTERS);
                    gp.save();
                    final GemPlayer gp2 = GemHunters.getPlayer(damager);
                    gp2.getUserData().setCoins(gp2.getUserData().getCoins() + 1);
                    gp2.save();
                    
                    //Limpiar jugador y respawn
                    pl.setCleanPlayer(GameMode.SPECTATOR);
                    new RespawnTask(pl).runTaskTimer(plugin, 1L, 20L);
                }
            }
        }
    }
    
    @EventHandler
    public void inventoryClickEvent(InventoryClickEvent e){
        //Cancelar quitarse armadura
         if (e.getSlotType().equals(SlotType.ARMOR)) {
             e.setCancelled(true);
         }
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
    public void onEntityFire(EntityCombustEvent e) {
        if (plugin.getGm().acceptPlayers()) {
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        if (e.getPlayer().getGameMode().equals(GameMode.SPECTATOR)) {
            e.setCancelled(true);
        }
        e.setFormat(plugin.getTm().getTeam(e.getPlayer()).getPrefix() + e.getPlayer().getDisplayName() + ChatColor.WHITE + ": " + ChatColor.GRAY + e.getMessage());
    }
}
