package com.cadiducho.fem.lucky.manager;

import com.cadiducho.fem.core.util.Title;
import com.cadiducho.fem.lucky.LuckyPlayer;
import com.cadiducho.fem.lucky.LuckyWarriors;
import com.cadiducho.fem.lucky.task.DeathMatchTask;
import com.cadiducho.fem.lucky.task.LobbyTask;
import com.cadiducho.fem.lucky.task.ShutdownTask;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class GameManager {

    private final LuckyWarriors plugin;

    public GameManager(LuckyWarriors instance) {
        plugin = instance;
    }

    @Getter private final ArrayList<Player> playersInGame = new ArrayList<>();
    @Getter private final ArrayList<Player> spectators = new ArrayList<>();
    @Getter private final HashMap<UUID, Integer> kills = new HashMap<>();
    @Getter public boolean dm = false;

    //¿Ha de comprobar el inicio del juego?
    @Getter @Setter private boolean checkStart = true;

    public void checkStart() {
        if (checkStart == true && playersInGame.size() >= plugin.getAm().getMinPlayers()) {
            checkStart = false;
            new LobbyTask(plugin).runTaskTimer(plugin, 1l, 20l);
        }
    }
    
    public void checkDm() {
        if (playersInGame.size() <= 2 && dm == false) {
            dm = true;
            plugin.getAm().loadSpawn();
            GameState.state = GameState.DEATHMATCH;
            new DeathMatchTask(plugin).runTaskTimer(plugin, 20l, 20l);
        } else if (playersInGame.size() == 1 || playersInGame.isEmpty()) {
            plugin.getServer().shutdown();
        }
    }

    public boolean checkWinner() {
        if (GameState.state == GameState.GAME || GameState.state == GameState.DEATHMATCH) {
            if (playersInGame.size() < 2) {
                Player winner = playersInGame.get(0);

                plugin.getMsg().sendBroadcast("&e"+winner.getDisplayName() + " &aha ganado la partida!");
                plugin.getServer().getOnlinePlayers().forEach(p -> { 
                    new Title("&a" + winner.getName(), "&aha ganado la partida!").send(p);
                    p.playSound(p.getLocation(), Sound.LEVEL_UP, 1F, 1F);
                });
                
                final LuckyPlayer lp = LuckyWarriors.getPlayer(winner);
                HashMap<Integer, Integer> wins = lp.getUserData().getWins();
                wins.replace(6, wins.get(6) + 1);
                lp.getUserData().setWins(wins);
                lp.getUserData().setCoins(lp.getUserData().getCoins() + 10);
                lp.save();
                        
                new ShutdownTask(plugin).runTaskTimer(plugin, 20l, 20l);
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
