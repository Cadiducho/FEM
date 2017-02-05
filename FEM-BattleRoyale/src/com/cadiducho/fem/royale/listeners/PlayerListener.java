package com.cadiducho.fem.royale.listeners;

import com.cadiducho.fem.royale.BattlePlayer;
import com.cadiducho.fem.royale.BattleRoyale;
import com.cadiducho.fem.royale.manager.GameState;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
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
        final Player player = e.getPlayer();
        final BattlePlayer bp = BattleRoyale.getPlayer(player);
        e.setJoinMessage(null);
        if (!plugin.getGm().acceptPlayers() || plugin.getGm().getPlayersInGame().size() >= plugin.getAm().getMaxPlayers()) {
            bp.setSpectator();
        } else {
            player.teleport(plugin.getAm().getLobby());
            bp.setLobbyPlayer();
            plugin.getMsg().sendBroadcast("&7Ha entrado al juego &e" + player.getDisplayName() + " &3(&b" + plugin.getGm().getPlayersInGame().size() + "&d/&b" + plugin.getAm().getMaxPlayers() + "&3)");
            plugin.getGm().checkStart();
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
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (plugin.getGm().acceptPlayers()) {
            e.setCancelled(true);
            if (e.getItem() != null) {
                if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    switch (e.getItem().getType()){
                        case COMPASS:
                            BattleRoyale.getPlayer(e.getPlayer()).sendToLobby();
                            break;
                    }
                }
            }
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
    public void onEntityFire(EntityCombustEvent e) {
        if (!(GameState.state == GameState.GAME || GameState.state == GameState.PVE || GameState.state == GameState.DEATHMATCH)) {
            e.setCancelled(true);
        }
    }
}
