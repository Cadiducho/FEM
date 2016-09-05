package com.cadiducho.fem.ovejas.listener;

import com.cadiducho.fem.core.util.Metodos;
import com.cadiducho.fem.ovejas.SheepPlayer;
import com.cadiducho.fem.ovejas.SheepQuest;
import com.cadiducho.fem.ovejas.manager.GameState;
import com.cadiducho.fem.ovejas.manager.tasks.RespawnTask;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class PlayerListener implements Listener {

    private final SheepQuest plugin;

    public PlayerListener(SheepQuest instnace) {
        plugin = instnace;
    }

    public void init() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent e) {
        Player player = e.getPlayer();
        if (GameState.state == null) {
            e.setKickMessage("No puedes entrar todavía");
        } else if (plugin.getGm().isInLobby() && plugin.getGm().getPlayersInGame().size() <= plugin.getAm().getMaxPlayers()) {
            e.allow();
        } else {
            e.setKickMessage("No tienes acceso a entrar aquí.");
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        e.setJoinMessage(null);
        if (plugin.getGm().isInLobby()) {
            SheepQuest.getPlayer(player).setLobbyPlayer();
            plugin.getMsg().sendBroadcast("&7Ha entrado al juego &e" + player.getDisplayName() + " &3(&b" + plugin.getGm().getPlayersInGame().size() + "&d/&b" + plugin.getAm().getMaxPlayers() + "&3)");
            plugin.getGm().checkStart();
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        e.setQuitMessage(null);
        if (plugin.getGm().isInLobby() || plugin.getGm().isInCountdown()) {
            plugin.getGm().removePlayerFromGame(player);
            plugin.getMsg().sendBroadcast("&7abandonó el juego &e" + player.getDisplayName() + " &3(&b" + plugin.getGm().getPlayersInGame().size() + "&d/&b" + plugin.getAm().getMaxPlayers() + "&3)");
        } else if (plugin.getGm().isInGame()) {
            plugin.getGm().removePlayerFromGame(player);
        } else if (plugin.getGm().isEnding()) {
            plugin.getGm().removePlayerFromGame(player);
        }
        
        SheepQuest.players.remove(SheepQuest.getPlayer(player));
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (plugin.getGm().isInLobby()) {
            e.setCancelled(true);
        }
    }
    
    public void onPlayerInteract(PlayerInteractEntityEvent e) {
        if (plugin.getGm().isInGame()) {
            if (e.getRightClicked() instanceof Sheep) {
                Sheep sheep = (Sheep) e.getRightClicked();
                if (e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.SADDLE)) {
                    SheepPlayer pl = SheepQuest.getPlayer(e.getPlayer());
                    pl.addSheep(sheep);
                }
            }
        }
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent e) {
        if (e instanceof LivingEntity) {
            LivingEntity le = (LivingEntity) e;
            if (le.hasAI()) { //No spawnear salvo las ovejas tontas del juego
                e.setCancelled(true);
            }
        }
    }
    
    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (plugin.getGm().isInLobby() || plugin.getGm().isEnding()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (plugin.getGm().isInLobby() || plugin.getGm().isInCountdown() || plugin.getGm().isEnding()) {
            e.setCancelled(true);
        } else if (plugin.getGm().isInGame()) {
            if (e.getEntity() instanceof Sheep && e.getDamager() instanceof Player) {
                Sheep sheep = (Sheep) e.getEntity();
                Player p = (Player) e.getDamager();
                if (p.getInventory().getItemInMainHand().getType().equals(Material.SADDLE)) {
                    SheepPlayer pl = SheepQuest.getPlayer(p);
                    pl.addSheep(sheep);
                }
                e.setCancelled(true);
            }
            if (e.getEntity() instanceof Player) {
                Player p = (Player) e.getEntity();
                SheepPlayer pl = SheepQuest.getPlayer(p);
                
                //Dropear ovejas
                pl.removeAllSheeps();
                
                //Simular muerte
                if (p.getHealth() - e.getDamage() < 1) { 
                    e.getEntity().getWorld().strikeLightningEffect(e.getEntity().getLocation());
                    plugin.getMsg().sendBroadcast("&e" + p.getDisplayName() + " &7ha muerto" + (e.getDamager() instanceof Player ? " por &e" + ((Player)e.getDamager()).getDisplayName() : "."));            
                    
                    //Limpiar jugador y respawn
                    pl.setCleanPlayer(GameMode.SPECTATOR);
                    new RespawnTask(pl).runTaskTimer(plugin, 20L, 20L);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerToggleSneakEvent(PlayerToggleSneakEvent e) {
        e.setCancelled(true);
    }
    
    @EventHandler
    public void onEntityDamageByBlock(EntityDamageByBlockEvent e) {
        /*if (plugin.getGm().isInLobby() || plugin.getGm().isTeleporting() || plugin.getGm().isEnding()) {
            e.setCancelled(true);
        } else if (plugin.getGm().isInPVE()) {
            e.setCancelled(false);
        } else if (plugin.getGm().isInPVP() || plugin.getGm().isInDeathMatch()) {
            e.setCancelled(false);
        }*/
    }

    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent e) {
        if (plugin.getGm().isInLobby() || plugin.getGm().isInCountdown() || plugin.getGm().isEnding()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerFood(FoodLevelChangeEvent e) {
        if (plugin.getGm().isInLobby() || plugin.getGm().isInCountdown()|| plugin.getGm().isEnding()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerPickUp(PlayerPickupItemEvent e) {
        if (plugin.getGm().isInLobby() || plugin.getGm().isInCountdown()|| plugin.getGm().isEnding()) {
            e.setCancelled(true);
        }
    }

}
