package com.cadiducho.fem.skywars.listener;

import com.cadiducho.fem.skywars.SkyPlayer;
import com.cadiducho.fem.skywars.SkyWars;
import com.cadiducho.fem.skywars.manager.GameState;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class PlayerListener implements Listener {

    private final SkyWars plugin;

    public PlayerListener(SkyWars instance) {
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
        if (plugin.getGm().isInLobby()) {
            plugin.getServer().getOnlinePlayers().stream().forEach(p -> player.showPlayer(p)); // Mostrar todos los jugadores a todos
            plugin.getServer().getOnlinePlayers().stream().forEach(p -> p.showPlayer(player));
            player.teleport(plugin.getAm().getLobby());
            SkyWars.getPlayer(player).setLobbyPlayer();
            plugin.getMsg().sendBroadcast("&7Ha entrado al juego &e" + player.getDisplayName() + " &3(&b" + plugin.getGm().getPlayersInGame().size() + "&d/&b" + plugin.getAm().getMaxPlayers() + "&3)");
            plugin.getGm().checkStart();
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        e.setQuitMessage(null);
        if (plugin.getGm().isInLobby() || GameState.state == GameState.COUNTDOWN) {
            plugin.getGm().removePlayerFromGame(player);
            plugin.getMsg().sendBroadcast("&7abandonó el juego &e" + player.getDisplayName() + " &3(&b" + plugin.getGm().getPlayersInGame().size() + "&d/&b" + plugin.getAm().getMaxPlayers() + "&3)");
        } else if (GameState.state == GameState.GAME) {
            plugin.getGm().removePlayerFromGame(player);
        } else if (GameState.state == GameState.ENDING) {
            plugin.getGm().removePlayerFromGame(player);
        }
        
        SkyWars.players.remove(SkyWars.getPlayer(player));
    }
    
    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (plugin.getGm().acceptPlayers() || GameState.state == GameState.COUNTDOWN || GameState.state == GameState.ENDING) {
            e.setCancelled(true);
            return;
        }
        if (e.getEntity() instanceof Player) {
            if (e.getCause() == DamageCause.FALL) {
                if (!plugin.getGm().isDañoEnCaida()) {
                    e.setCancelled(true);
                    return;
                }
            }
            Player p = (Player) e.getEntity();
            SkyPlayer pl = SkyWars.getPlayer(p);
            //Simular muerte
            if (p.getHealth() - e.getDamage() < 1) {
                for (ItemStack i : pl.getPlayer().getInventory().getContents()) {
                    if (i != null) {
                        pl.getPlayer().getWorld().dropItemNaturally(pl.getPlayer().getLocation(), i);
                        pl.getPlayer().getInventory().remove(i);
                    }
                }
                pl.setCleanPlayer(GameMode.SPECTATOR);
                Location tploc = pl.getPlayer().getLocation();
                tploc.setY(25D);
                pl.getPlayer().teleport(tploc);
                e.getEntity().getWorld().strikeLightningEffect(e.getEntity().getLocation());
                plugin.getMsg().sendBroadcast("&e" + p.getDisplayName() + " &7ha muerto!");
                
                pl.death();          
            }
        }      
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (plugin.getGm().acceptPlayers() || GameState.state == GameState.COUNTDOWN || GameState.state == GameState.ENDING) {
            e.setCancelled(true);
        } else if (GameState.state == GameState.GAME) {
            if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
                Player damager = (Player) e.getDamager();
                Player p = (Player) e.getEntity();
                SkyPlayer pl = SkyWars.getPlayer(p);

                //Simular muerte
                if (p.getHealth() - e.getDamage() < 1) {
                    for (ItemStack i : pl.getPlayer().getInventory().getContents()) {
                        if (i != null) {
                            pl.getPlayer().getWorld().dropItemNaturally(pl.getPlayer().getLocation(), i);
                            pl.getPlayer().getInventory().remove(i);
                        }
                    }
                    pl.setCleanPlayer(GameMode.SPECTATOR);
                    Location tploc = pl.getPlayer().getLocation();
                    tploc.setY(25D);
                    pl.getPlayer().teleport(tploc);
                    e.getEntity().getWorld().strikeLightningEffect(e.getEntity().getLocation());
                    plugin.getMsg().sendBroadcast("&e" + p.getDisplayName() + " &7ha muerto a manos de &e" + damager.getDisplayName());
                    
                    final SkyPlayer sp = SkyWars.getPlayer(p);
                    HashMap<Integer, Integer> kills = sp.getUserData().getKills();
                    kills.replace(7, kills.get(7) + 1);
                    sp.getUserData().setKills(kills);
                    sp.save();
                    
                    pl.death(); 
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e){
        Player p = e.getPlayer();

        if (e.getItem() != null){
            if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (e.getHand() == EquipmentSlot.HAND) {
                    if (e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasDisplayName() && e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("Vida")) {
                        e.setCancelled(true);
                        p.getInventory().getItemInMainHand().setType(Material.AIR);
                        if (p.getHealth() + (p.getHealth() * 0.5) >= p.getMaxHealth()) {
                            p.setHealth(p.getMaxHealth());
                            return;
                        }
                        p.setHealth(p.getHealth() + (p.getHealth() * 0.5));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent e) {
        if (plugin.getGm().isInLobby() || GameState.state == GameState.COUNTDOWN || GameState.state == GameState.ENDING) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerFood(FoodLevelChangeEvent e) {
        if (plugin.getGm().isInLobby() || GameState.state == GameState.COUNTDOWN || GameState.state == GameState.ENDING) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerPickUp(PlayerPickupItemEvent e) {
        if (plugin.getGm().isInLobby() || GameState.state == GameState.COUNTDOWN || GameState.state == GameState.ENDING) {
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        if (e.getPlayer().getGameMode().equals(GameMode.SPECTATOR)) {
            e.setCancelled(true);
        }
        e.setFormat(ChatColor.GREEN + e.getPlayer().getDisplayName() + ChatColor.WHITE + ": " + ChatColor.GRAY + e.getMessage());
    }
}
