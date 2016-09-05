package com.cadiducho.fem.tnt.manager;

import com.cadiducho.fem.tnt.TntWars;
import com.cadiducho.fem.tnt.task.CountdownTask;
import java.util.ArrayList;
import java.util.HashMap;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

public class GameManager {

    private final TntWars plugin;

    public GameManager(TntWars instance) {
        plugin = instance;
    }

    @Getter private final ArrayList<Player> playersInGame = new ArrayList<>();
    @Getter private final HashMap<Team, ArrayList<Location>> gemas = new HashMap<>();

    private boolean checkStart = false;

    public void init() {
        playersInGame.clear();
    }

    public void checkStart() {
        if (checkStart == false && playersInGame.size() >= plugin.getAm().getMinPlayers()) {
            checkStart = true;
            new CountdownTask(plugin).runTaskTimer(plugin, 20l, 20l);
        }
    }
    
    public Player checkWinner() {
        if (!isInGame()) return null;
        Player winner = null;
        /*
        if (plugin.getTm().getPuntos(plugin.getTm().amarillo) == 0) winner = plugin.getTm().morado;
        if (plugin.getTm().getPuntos(plugin.getTm().morado) == 0) winner = plugin.getTm().amarillo;
        
        if (winner == null) return null;
        
        //Hay un ganador
        plugin.getMsg().sendBroadcast("Ha ganado el equipo " + winner.getDisplayName());
        GameTask.instance.end();*/
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
    
    public boolean isInGame() {
        return GameState.state == GameState.GAME;
    }
}
