package com.cadiducho.fem.gem.manager;

import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.core.util.Title;
import com.cadiducho.fem.gem.GemHunters;
import com.cadiducho.fem.gem.task.GameTask;
import com.cadiducho.fem.gem.task.LobbyTask;
import java.util.ArrayList;
import java.util.HashMap;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

public class GameManager {

    private final GemHunters plugin;

    public GameManager(GemHunters instance) {
        plugin = instance;
    }

    @Getter private final ArrayList<Player> playersInGame = new ArrayList<>();
    @Getter private final HashMap<Team, ArrayList<Location>> gemas = new HashMap<>();

    @Getter @Setter private boolean checkStart = false;

    public void checkStart() {
        System.out.println("CheckStart: " + checkStart);
        if (checkStart == false && playersInGame.size() >= plugin.getAm().getMinPlayers()) {
            checkStart = true;
            System.out.println("Comenzando");
            new LobbyTask(plugin).runTaskTimer(plugin, 1l, 20l);
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
        
        Team loser = plugin.getTm().getOpositeTeam(winner);
        for (Player p : plugin.getTm().getJugadores().get(winner)) {
            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1F, 1F);
            new Title("&a&lVICTORIA", "¡Tu equipo ha ganado :D!", 1, 2, 1).send(p);
            HashMap<Integer, Integer> wins = FEMServer.getUser(p).getUserData().getWins();
            wins.replace(3, wins.get(3) + 1);
            FEMServer.getUser(p).getUserData().setWins(wins);
            FEMServer.getUser(p).save();
        }
        for (Player p : plugin.getTm().getJugadores().get(loser)) {
            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1F, 1F);
            new Title("&c&lDERROTA", "¡Tu equipo ha perdido :C!", 1, 2, 1).send(p);
        }
        GameTask.instance.end();
        return winner;
    }

    public void addPlayerToGame(Player player) {
        if (playersInGame.contains(player)) {
            playersInGame.remove(player);
        }
        playersInGame.add(player);
        System.out.println("Añadido " + player.getName());
    }

    public void removePlayerFromGame(Player player) {
        playersInGame.remove(player);
    }

    public boolean acceptPlayers() {
        return (GameState.state == GameState.PREPARING || GameState.state == GameState.LOBBY);
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

    public boolean isInGame() {
        return GameState.state == GameState.GAME;
    }
}
