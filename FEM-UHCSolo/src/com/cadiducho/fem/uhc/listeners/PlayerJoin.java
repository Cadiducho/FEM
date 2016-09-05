package com.cadiducho.fem.uhc.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.EventPriority;

import com.cadiducho.fem.uhc.Main;
import com.cadiducho.fem.uhc.manager.GameState;
import org.bukkit.scoreboard.Criterias;
import org.bukkit.scoreboard.DisplaySlot;

public class PlayerJoin implements Listener {

    public Main plugin;

    public PlayerJoin(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        e.setJoinMessage(null);
        plugin.msg.sendHeaderAndFooter(player, "&c&lDONKEY&b&lUHC", "&bEncuentra rangos, beneficios, y más en &e&lDONKEYCRAFT.NET");
        if (GameState.state == GameState.LOBBY) {
            plugin.gm.addPlayerToUHC(player);
            plugin.up.setLobbyPlayer(player);
            plugin.lm.loadSpawn(player);
            plugin.ub.setScoreboard(player);
            plugin.up.esperan2(player);
            player.getScoreboard().registerNewObjective("vida", Criterias.HEALTH).setDisplaySlot(DisplaySlot.PLAYER_LIST);
            plugin.gm.PLAYERS_IN_GAME.stream()
                    .map(id -> plugin.getServer().getPlayer(id))
                    .forEach(pl -> plugin.msg.sendActionBar(pl, "&7Hay &e" + plugin.gm.PLAYERS_IN_GAME.size() + " &7jugadores de " + "&e40"));
            plugin.msg.sendBroadcast("&e" + player.getDisplayName() + " &7ha entrado al juego. &b(&e" + plugin.gm.getPlayers().size() + "&3/&e40&b)");
        } else if (GameState.state == GameState.LOBBY && plugin.gm.PLAYERS_IN_GAME.size() >= 20) {
            plugin.gm.startGame();
        } else if (GameState.state == GameState.TELETRANSPORTE || GameState.state == GameState.JUEGO || GameState.state == GameState.ELIMINACION) {
            plugin.up.setSpectator(player);
        } else if (GameState.state == GameState.PVE) {
            if (plugin.gm.DESCONECTADOS.containsKey(player.getUniqueId())) {
                plugin.gm.addPlayerToUHC(player);
               
            }
        }
    }

}
