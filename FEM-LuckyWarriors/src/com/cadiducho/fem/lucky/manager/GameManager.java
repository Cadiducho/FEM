package com.cadiducho.fem.lucky.manager;

import com.cadiducho.fem.core.api.FEMServer;
import java.util.ArrayList;
import com.cadiducho.fem.lucky.LuckyWarriors;
import com.cadiducho.fem.lucky.task.DeathMatchCountdown;
import com.cadiducho.fem.lucky.task.WinnerCountdown;
import java.util.HashMap;
import lombok.Getter;
import org.bukkit.entity.Player;

public class GameManager {

    private final LuckyWarriors plugin;

    public GameManager(LuckyWarriors instance) {
        plugin = instance;
    }

    @Getter private final ArrayList<Player> playersInGame = new ArrayList<>();
    @Getter private final ArrayList<Player> spectators = new ArrayList<>();
    @Getter public boolean start;
    @Getter public boolean dm;

    public void init() {
        playersInGame.clear();
        spectators.clear();
        start = false;
        dm = false;
    }

    public void checkDm() {
        if (playersInGame.size() <= 2 && dm == false) {
            dm = true;
            plugin.getAm().loadSpawn();
            GameState.state = GameState.DEATHMATCH;
            new DeathMatchCountdown(plugin).runTaskTimer(plugin, 20l, 20l);
        } else if (playersInGame.size() == 1 || playersInGame.isEmpty()) {
            plugin.getServer().shutdown();
        }
    }

    public boolean checkWinner() {
        if (GameState.state == GameState.GAME || GameState.state == GameState.DEATHMATCH) {
            if (playersInGame.size() < 2) {
                playersInGame.stream().forEach((winner) -> {
                    plugin.getMsg().sendBroadcast(winner.getDisplayName() + " ha ganado la partida!");
                    HashMap<Integer, Integer> wins = FEMServer.getUser(winner).getUserData().getWins();
                    wins.replace(6, wins.get(6) + 1);
                    FEMServer.getUser(winner).getUserData().setWins(wins);
                    FEMServer.getUser(winner).save();
                });
                new WinnerCountdown(plugin).runTaskTimer(plugin, 20l, 20l);
                GameState.state = GameState.ENDING;
                return true;
            }
        }
        return true;
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
        if (playersInGame.contains(player)) {
            playersInGame.remove(player);
        }
    }
    
    public boolean acceptPlayers() {
        return (GameState.state == GameState.PREPARING || GameState.state == GameState.LOBBY);
    }
    
    public boolean isInGame() {
        return (GameState.state == GameState.LUCKY || GameState.state == GameState.CRAFT || GameState.state == GameState.GAME || GameState.state == GameState.DEATHMATCH);
    }
}
