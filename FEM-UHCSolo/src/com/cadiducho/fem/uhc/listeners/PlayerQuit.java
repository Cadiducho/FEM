package com.cadiducho.fem.uhc.listeners;

import com.cadiducho.fem.uhc.Main;
import com.cadiducho.fem.uhc.manager.GameState;
import org.bukkit.Sound;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerQuit implements Listener {

    public Main plugin;

    public PlayerQuit(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        //User deathUser = Core.getUser(e.getPlayer());
        e.setQuitMessage(null);

        if (null != GameState.state) {
            switch (GameState.state) {
                case LOBBY:
                    plugin.lm.addSpawnOnQuit(e.getPlayer());
                    plugin.grm.clear(e.getPlayer().getLocation());
                    plugin.gm.removePlayerFromUHC(e.getPlayer());
                    plugin.msg.sendBroadcast("&e" + e.getPlayer().getDisplayName() + " &7abandonó la partida.");
                    plugin.gm.PLAYERS_IN_GAME.stream()
                            .map(id -> plugin.getServer().getPlayer(id))
                            .forEach(pl -> plugin.msg.sendActionBar(pl, "&7Hay &e" + plugin.gm.PLAYERS_IN_GAME.size() + " &7jugadores de " + "&d/&e40"));

                    break;
                case JUEGO:
                case FIN:
                case ELIMINACION:
                    if (plugin.getServer().getOnlinePlayers().isEmpty()) {
                        plugin.getServer().shutdown();
                    }
                    
                    if (plugin.gm.PLAYERS_IN_GAME.contains(e.getPlayer().getUniqueId())) {
                        // deathUser.addDeaths(User.Modo.UHC, 1);
                        for (ItemStack item : e.getPlayer().getInventory().getContents()) {
                            e.getPlayer().getWorld().dropItem(e.getPlayer().getLocation().add(0.5, 0.5, 0.5), item);
                        }
                    }
                    plugin.gm.removePlayerFromUHC(e.getPlayer());
                    if (plugin.gm.SPECTATORS.contains(e.getPlayer().getUniqueId())) {
                        return;
                    }
                    plugin.gm.checkWinner();
                    if (e.getPlayer().getLastDamageCause().getEntity() instanceof Player) {
                        e.getPlayer().getLocation().getWorld().strikeLightningEffect(e.getPlayer().getLocation());
                        plugin.msg.sendBroadcast("&e" + e.getPlayer().getDisplayName() + " &7ha muerto por &e" + e.getPlayer().getLastDamageCause().getEntity().getName());
                        plugin.gm.PLAYERS_IN_GAME.stream()
                            .map(id -> plugin.getServer().getPlayer(id))
                            .forEach(pl -> pl.playSound(pl.getLocation(), Sound.ENTITY_WITHER_DEATH, 1F, 1F));
                    } else {
                        e.getPlayer().getLocation().getWorld().strikeLightningEffect(e.getPlayer().getLocation());
                        plugin.gm.PLAYERS_IN_GAME.stream()
                            .map(id -> plugin.getServer().getPlayer(id))
                            .forEach(pl -> pl.playSound(pl.getLocation(), Sound.ENTITY_WITHER_DEATH, 1F, 1F));
                        plugin.msg.sendBroadcast("&e" + e.getPlayer().getDisplayName() + " &7ha muerto desconectado.");
                    }
                    break;
                case PVE:
                    if (plugin.getServer().getOnlinePlayers().size() <= 1) {
                        plugin.getServer().getOnlinePlayers().forEach(online -> plugin.up.sendToServer(online, "UHCLOBBY001"));
                        plugin.getServer().shutdown();
                    }
                    plugin.gm.removePlayerFromUHC(e.getPlayer());
                    plugin.gm.DESCONECTADOS.put(e.getPlayer().getUniqueId(), e.getPlayer());
                    break;
                default:
                    break;
            }
        }
    }
}
