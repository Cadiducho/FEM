package com.cadiducho.fem.royale.listeners;

import com.cadiducho.fem.royale.BattleRoyale;
import com.cadiducho.fem.royale.task.LobbyCountdown;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    private final BattleRoyale plugin;

    public PlayerListener(BattleRoyale instance) {
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
        if (!plugin.getGm().acceptPlayers() || plugin.getGm().getPlayersInGame().size() >= plugin.getAm().getMaxPlayers()) {
            plugin.getPm().setSpectator(player);
        } else { 
            player.teleport(plugin.getAm().getLobby());
            plugin.getPm().setLobbyPlayer(player);
            plugin.getMsg().sendBroadcast("&7Ha entrado al juego &e" + player.getDisplayName() + " &3(&b" + plugin.getGm().getPlayersInGame().size() + "&d/&b" + plugin.getAm().getMaxPlayers() + "&3)");
            if (plugin.getGm().getPlayersInGame().size() == plugin.getAm().getMinPlayers() && plugin.getGm().start == false) {
                new LobbyCountdown(plugin).runTaskTimer(plugin, 20l, 20l);
                plugin.getGm().start = true;
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        e.setQuitMessage(null);
        if (plugin.getGm().acceptPlayers()) {
            plugin.getGm().removePlayerFromGame(player);
        } else if (plugin.getGm().isInGame() || plugin.getGm().isFinished()) {
            plugin.getGm().removePlayerFromGame(player);
            plugin.getGm().checkWinner();
            plugin.getGm().checkDm();
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
