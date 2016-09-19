package com.cadiducho.fem.gem.manager;

import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.gem.GemHunters;
import com.cadiducho.fem.gem.task.GameTask;
import com.cadiducho.fem.gem.task.LobbyTask;
import java.util.ArrayList;
import java.util.HashMap;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

public class GameManager {

    private final GemHunters plugin;

    public GameManager(GemHunters instance) {
        plugin = instance;
    }

    private final ArrayList<Player> playersInGame = new ArrayList<>();
    @Getter private final HashMap<Team, ArrayList<Location>> gemas = new HashMap<>();

    private boolean checkStart = false;

    public void checkStart() {
        if (checkStart == false && playersInGame.size() >= plugin.getAm().getMinPlayers()) {
            checkStart = true;
            new LobbyTask(plugin).runTaskTimer(plugin, 20l, 20l);
        }
    }
    
    public Team checkWinner() {
        if (!isInGame()) return null;
        Team winner = null;
        
        if (plugin.getTm().getPuntos(plugin.getTm().azul) == 0) winner = plugin.getTm().rojo;
        if (plugin.getTm().getPuntos(plugin.getTm().rojo) == 0) winner = plugin.getTm().azul;
        
        if (winner == null) return null;
        
        //Hay un ganador
        plugin.getMsg().sendBroadcast("Ha ganado el equipo " + winner.getDisplayName());
        for (Player p : plugin.getTm().getJugadores().get(winner)) {
            HashMap<Integer, Integer> wins = FEMServer.getUser(p).getUserData().getWins();
            wins.replace(3, wins.get(3) + 1);
            FEMServer.getUser(p).getUserData().setWins(wins); 
            FEMServer.getUser(p).save();
        }
        GameTask.instance.end();
        return winner;
    }

    public void addPlayerToGame(Player player) {
        if (playersInGame.contains(player)) {
            playersInGame.remove(player);
            playersInGame.add(player);
        } else {
            playersInGame.add(player);
        }
    }

    public void removePlayerFromGame(Player player) {
        playersInGame.remove(player);
    }

    public boolean isEnding() {
        return GameState.state == GameState.ENDING;
    }

    public boolean isInLobby() {
        return GameState.state == GameState.LOBBY;
    }
    
    public boolean isInCountdown() {
        return GameState.state == GameState.COUNTDOWN;
    }
    
    public boolean isHidding() {
        return GameState.state == GameState.HIDDING;
    }
    
    public boolean isInExtension() {
        return GameState.state == GameState.EXTENSION;
    }
    
    public boolean isInGame() {
        return GameState.state == GameState.GAME;
    }

    public ArrayList<Player> getPlayersInGame() {
        return playersInGame;
    }
}
