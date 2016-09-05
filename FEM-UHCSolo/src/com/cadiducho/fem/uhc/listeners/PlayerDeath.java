package com.cadiducho.fem.uhc.listeners;

import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.core.api.FEMUser;
import com.cadiducho.fem.uhc.Main;
import com.cadiducho.fem.uhc.manager.GameState;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.entity.Player;

public class PlayerDeath implements Listener {

    public Main plugin;

    public PlayerDeath(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player killer = (Player) e.getEntity().getKiller();
        Player death = (Player) e.getEntity();
        FEMUser deathUser = FEMServer.getUser(death);
        FEMUser killerUser = FEMServer.getUser(death);
        e.setDeathMessage(null);
        plugin.up.setSpectator(death);
        plugin.gm.PLAYERS_IN_GAME.remove(death.getUniqueId());
        plugin.gm.checkWinner();
        if (GameState.state == GameState.PVE || GameState.state == GameState.JUEGO || GameState.state == GameState.FIN) {
            if (killer instanceof Player) {
                death.getLocation().getWorld().strikeLightningEffect(death.getLocation());
                plugin.msg.sendBroadcast("&e" + death.getDisplayName() + " &7ha muerto por &e" + killer.getDisplayName());
                deathUser.addDied(1, 1);
                killerUser.addKilled(1, 1);
                plugin.msg.sendActionBar(killer, "&6+5 puntos");
                plugin.gm.PLAYERS_IN_GAME.stream()
                        .map(id -> plugin.getServer().getPlayer(id))
                        .forEach((pl) ->  pl.playSound(pl.getLocation(), Sound.ENTITY_WITHER_DEATH, 1F, 1F));
            } else {
                death.getLocation().getWorld().strikeLightningEffect(death.getLocation());
                plugin.msg.sendBroadcast("&e" + death.getDisplayName() + " &7ha muerto");
                deathUser.addDied(1, 1);
                plugin.gm.PLAYERS_IN_GAME.stream()
                        .map(id -> plugin.getServer().getPlayer(id))
                        .forEach((pl) ->  pl.playSound(pl.getLocation(), Sound.ENTITY_WITHER_DEATH, 1F, 1F));
            }
        } else if (GameState.state == GameState.ELIMINACION || GameState.state == GameState.FIN) {
            if (killer instanceof Player) {
                death.getLocation().getWorld().strikeLightningEffect(death.getLocation());
                plugin.msg.sendBroadcast("&e" + death.getDisplayName() + " &7ha muerto por &e" + killer.getDisplayName());
                deathUser.addDied(1, 1);
                killerUser.addKilled(1, 1);
                plugin.msg.sendActionBar(killer, "&6+50 puntos");
                plugin.gm.PLAYERS_IN_GAME.stream()
                        .map(id -> plugin.getServer().getPlayer(id))
                        .forEach((pl) ->  pl.playSound(pl.getLocation(), Sound.ENTITY_WITHER_DEATH, 1F, 1F));
            } else {
                death.getLocation().getWorld().strikeLightningEffect(death.getLocation());
                plugin.msg.sendBroadcast("&e" + death.getDisplayName() + " &7ha muerto");
                deathUser.addDied(1, 1);
                plugin.gm.PLAYERS_IN_GAME.stream()
                        .map(id -> plugin.getServer().getPlayer(id))
                        .forEach((pl) ->  pl.playSound(pl.getLocation(), Sound.ENTITY_WITHER_DEATH, 1F, 1F));
            }
        }
    }

}
