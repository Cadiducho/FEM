package com.cadiducho.fem.uhc.manager;

import com.cadiducho.fem.core.api.FEMServer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

import com.cadiducho.fem.uhc.Main;
import com.cadiducho.fem.uhc.countdown.LobbyCountdown;
import com.cadiducho.fem.uhc.countdown.WinnerCountdown;
import org.bukkit.GameMode;

public class GameManager {

    public Main plugin;

    public GameManager(Main plugin) {
        this.plugin = plugin;
    }
    public ArrayList<UUID> SPECTATORS = new ArrayList<>();

    public ArrayList<UUID> PLAYERS_IN_GAME = new ArrayList<>();
    public Map<UUID, Player> DESCONECTADOS = new HashMap<>();

    public int ESPERA_LOBBY = 60;
    public int TELETRANSPORTE = 10;
    public int PVE = 1200;
    public int JUEGO = 2400;

    public void addPlayerToUHC(Player player) {
        PLAYERS_IN_GAME.add(player.getUniqueId());
    }

    public void removePlayerFromUHC(Player player) {
        PLAYERS_IN_GAME.remove(player.getUniqueId());
    }

    public void startGame() {
        new LobbyCountdown(plugin).runTaskTimer(plugin, 0, 20);
    }

    public ArrayList<UUID> getPlayers() {
        return PLAYERS_IN_GAME;
    }

    public void checkWinner() {
        if (PLAYERS_IN_GAME.size() < 2) {
            for (UUID id : PLAYERS_IN_GAME) {
                Player winner = plugin.getServer().getPlayer(id);
                winner.setGameMode(GameMode.CREATIVE);
                GameState.state = GameState.FIN;
                new WinnerCountdown(plugin).runTaskTimer(plugin, 0, 20);
                plugin.msg.sendBroadcast("&a&lHA GANADO EL &6&lUHC &e&l" + winner.getDisplayName().toUpperCase());
                plugin.msg.sendActionBar(winner, "&6+50 puntos");      
                FEMServer.getUser(winner).addWon(1, 1);
                for (Player player : plugin.getServer().getOnlinePlayers()) {
                    plugin.msg.sendTitle(player, "&6" + winner.getDisplayName(), "&b&lHa ganado la partida", 0, 10, 5);
                    player.setScoreboard(plugin.getServer().getScoreboardManager().getNewScoreboard());
                }
            }
        }
    }

    public void addSpectator(Player player) {
        if (SPECTATORS.contains(player.getUniqueId())) {
            SPECTATORS.remove(player.getUniqueId());
        }
        SPECTATORS.add(player.getUniqueId());
    }

    public void removeSpectator(Player player) {
        SPECTATORS.remove(player.getUniqueId());
    }

}
