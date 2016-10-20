package com.cadiducho.fem.tnt.listener;

import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.core.util.ItemUtil;
import com.cadiducho.fem.core.util.Metodos;
import com.cadiducho.fem.tnt.TntIsland;
import com.cadiducho.fem.tnt.TntPlayer;
import com.cadiducho.fem.tnt.TntWars;
import com.cadiducho.fem.tnt.manager.GameState;
import com.cadiducho.fem.tnt.task.RespawnTask;
import java.util.HashMap;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class PlayerListener implements Listener {

    private final TntWars plugin;

    public PlayerListener(TntWars instance) {
        plugin = instance;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEntityEvent e) {
        if (e.getRightClicked() instanceof Villager) {
            if (GameState.state == GameState.GAME) {
                e.setCancelled(true);
                Villager v = (Villager) e.getRightClicked();
                if (v.getName().equalsIgnoreCase(Metodos.colorizar("&6Tienda TNTWars"))) {
                    Inventory inv = plugin.getServer().createInventory(null, 9, "§6Tienda TntWars");
                    inv.setItem(0, ItemUtil.createItem(Material.BRICK_STAIRS, "Construcción"));
                    inv.setItem(1, ItemUtil.createItem(Material.GOLD_SWORD, "Armas"));
                    inv.setItem(2, ItemUtil.createItem(Material.IRON_CHESTPLATE, "Armaduras"));
                    inv.setItem(3, ItemUtil.createItem(Material.BEETROOT_SOUP, "Comida"));
                    inv.setItem(4, ItemUtil.createItem(Material.DIAMOND_PICKAXE, "Herramientas"));
                    inv.setItem(5, ItemUtil.createItem(Material.BOW, "Tiro"));
                    inv.setItem(6, ItemUtil.createItem(Material.CHEST, "Misceláneo"));
                    e.getPlayer().openInventory(inv);
                }
            }
        } else {
            e.setCancelled(true);
        }
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
            TntWars.getPlayer(player).setLobbyPlayer();
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
        
        TntWars.players.remove(TntWars.getPlayer(player));
    }
    
    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (plugin.getGm().acceptPlayers() || GameState.state == GameState.COUNTDOWN || GameState.state == GameState.ENDING) {
            e.setCancelled(true);
            return;
        }
        if (e.getEntity() instanceof Villager) {
            e.setCancelled(true);
            return;
        }
        if (e.getEntity() instanceof Player) {
            if (e.getCause() == DamageCause.BLOCK_EXPLOSION) {
                e.setCancelled(true);
                return;
            }
            if (e.getCause() == DamageCause.FALL) {
                if (!plugin.getGm().isDañoEnCaida()) {
                    e.setCancelled(true);
                    return;
                }
            }
            Player p = (Player) e.getEntity();
            TntPlayer pl = TntWars.getPlayer(p);
            //Simular muerte
            if (p.getHealth() - e.getDamage() < 1) {
                for (ItemStack i : pl.getBase().getPlayer().getInventory().getContents()) {
                    if (i != null) {
                        pl.getBase().getPlayer().getWorld().dropItemNaturally(pl.getBase().getPlayer().getLocation(), i);
                        pl.getBase().getPlayer().getInventory().remove(i);
                    }
                }
                pl.setCleanPlayer(GameMode.SPECTATOR);
                Location tploc = pl.getBase().getPlayer().getLocation();
                tploc.setY(25D);
                pl.getBase().getPlayer().teleport(tploc);
                e.getEntity().getWorld().strikeLightningEffect(e.getEntity().getLocation());
                plugin.getMsg().sendBroadcast("&e" + p.getDisplayName() + " &7ha muerto!");
                if (!TntIsland.getIsland(p.getUniqueId()).getDestroyed()) {
                    new RespawnTask(pl).runTaskTimer(plugin, 20L, 20L);
                } else {
                    pl.death();
                }
            }
        }      
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (plugin.getGm().acceptPlayers() || GameState.state == GameState.COUNTDOWN || GameState.state == GameState.ENDING) {
            e.setCancelled(true);
            return;
        } else if (GameState.state == GameState.GAME) {
            if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
                Player damager = (Player) e.getDamager();
                Player p = (Player) e.getEntity();
                TntPlayer pl = TntWars.getPlayer(p);

                //Simular muerte
                if (p.getHealth() - e.getDamage() < 1) {
                    for (ItemStack i : pl.getBase().getPlayer().getInventory().getContents()) {
                        if (i != null) {
                            pl.getBase().getPlayer().getWorld().dropItemNaturally(pl.getBase().getPlayer().getLocation(), i);
                            pl.getBase().getPlayer().getInventory().remove(i);
                        }
                    }
                    pl.setCleanPlayer(GameMode.SPECTATOR);
                    Location tploc = pl.getBase().getPlayer().getLocation();
                    tploc.setY(25D);
                    pl.getBase().getPlayer().teleport(tploc);
                    e.getEntity().getWorld().strikeLightningEffect(e.getEntity().getLocation());
                    plugin.getMsg().sendBroadcast("&e" + p.getDisplayName() + " &7ha muerto a manos de &e" + damager.getDisplayName());
                    HashMap<Integer, Integer> kills = FEMServer.getUser(damager).getUserData().getKills();
                    kills.replace(1, kills.get(1) + 1);
                    FEMServer.getUser(damager).getUserData().setKills(kills);
                    FEMServer.getUser(damager).save();
                    if (!TntIsland.getIsland(p.getUniqueId()).getDestroyed()) {
                        new RespawnTask(pl).runTaskTimer(plugin, 20L, 20L);
                    } else {
                        pl.death();
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
    
    @EventHandler
    public void onBed(PlayerBedEnterEvent e) {
        e.setCancelled(true);
    }
}
