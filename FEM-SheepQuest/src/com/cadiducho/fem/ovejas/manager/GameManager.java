package com.cadiducho.fem.ovejas.manager;

import com.cadiducho.fem.ovejas.SheepQuest;
import com.cadiducho.fem.ovejas.manager.tasks.LobbyTask;
import java.util.ArrayList;
import org.bukkit.entity.Player;

public class GameManager {

    private final SheepQuest plugin;

    public GameManager(SheepQuest instance) {
        plugin = instance;
    }

    private final ArrayList<Player> playersInGame = new ArrayList<>();

    private boolean checkStart = false;

    public void init() {
        playersInGame.clear();
    }

    public void checkStart() {
        if (checkStart == false && playersInGame.size() >= plugin.getAm().getMinPlayers()) {
            checkStart = true;
            new LobbyTask(plugin).runTaskTimer(plugin, 20l, 20l);
        }
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

    public ArrayList<Player> getPlayersInGame() {
        return playersInGame;
    }
}
