package com.cadiducho.fem.gem.manager;

import com.cadiducho.fem.core.api.FEMServer.GameID;
import com.cadiducho.fem.core.util.Title;
import com.cadiducho.fem.gem.GemHunters;
import com.cadiducho.fem.gem.GemPlayer;
import com.cadiducho.fem.gem.task.GameTask;
import com.cadiducho.fem.gem.task.LobbyTask;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.HashMap;

public class GameManager {

    private final GemHunters plugin;

    public GameManager(GemHunters instance) {
        plugin = instance;
    }

    @Getter private final ArrayList<Player> playersInGame = new ArrayList<>();
    @Getter private final HashMap<Team, ArrayList<Location>> gemas = new HashMap<>();

    //¿Ha de comprobar el inicio del juego?
    @Getter @Setter private boolean checkStart = true;

    public void checkStart() {
        if (checkStart == true && playersInGame.size() >= plugin.getAm().getMinPlayers()) {
            checkStart = false;
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
            p.playSound(p.getLocation(), Sound.LEVEL_UP, 1F, 1F);
            Title.sendTitle(p, 1, 7, 1, "&a&lVICTORIA", "¡Tu equipo ha ganado :D!");
            
            final GemPlayer gp = GemHunters.getPlayer(p);
            gp.getUserData().addWin(GameID.GEMHUNTERS);
            gp.getUserData().setCoins(gp.getUserData().getCoins() + 10);
            gp.save();
        }
        plugin.getTm().getJugadores().get(loser).forEach(p -> {
            p.playSound(p.getLocation(), Sound.LEVEL_UP, 1F, 1F);
            Title.sendTitle(p, 1, 7, 1, "&c&lDERROTA", "¡Tu equipo ha perdido :C!");
        });
        
        GameTask.end();
        return winner;
    }

    public void addPlayerToGame(Player player) {
        if (playersInGame.contains(player)) {
            playersInGame.remove(player);
        }
        playersInGame.add(player);
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
