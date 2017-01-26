package com.cadiducho.fem.royale.manager;

import com.cadiducho.fem.core.util.Title;
import com.cadiducho.fem.royale.BattlePlayer;
import com.cadiducho.fem.royale.BattleRoyale;
import com.cadiducho.fem.royale.task.DeathMatchCountdown;
import com.cadiducho.fem.royale.task.LobbyTask;
import com.cadiducho.fem.royale.task.WinnerCountdown;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class GameManager {

    private final BattleRoyale plugin;

    public GameManager(BattleRoyale instance) {
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
        if (playersInGame.size() <= 4 && dm == false) {
            dm = true;
            plugin.getAm().loadSpawn();
            GameState.state = GameState.DEATHMATCH;
            new DeathMatchCountdown(plugin).runTaskTimer(plugin, 20l, 20l);
        }
    }

    public boolean checkWinner() {
        if (isInGame()) {
            if (playersInGame.size() < 2) {
                playersInGame.stream().forEach((winner) -> {
                    plugin.getMsg().sendBroadcast("&a&l" + winner.getDisplayName() + "&2 ha ganado la partida!");
                    
                    final BattlePlayer bp = BattleRoyale.getPlayer(winner);
                    HashMap<Integer, Integer> wins = bp.getUserData().getWins();
                    wins.replace(5, wins.get(5) + 1);
                    new Title("&b&l¡Has ganado!", "", 1, 2, 1).send(winner);
                    bp.getUserData().setWins(wins);
                    bp.getUserData().setCoins(bp.getUserData().getCoins() + 5);
                    bp.save();
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
    
    public boolean acceptPlayers() {
        return (GameState.state == GameState.PREPARING || GameState.state == GameState.LOBBY);
    }

    public boolean isInGame() {
        return (GameState.state == GameState.GAME || GameState.state == GameState.PVE || GameState.state == GameState.DEATHMATCH);
    }

    public boolean isFinished() {
        return GameState.state == GameState.ENDING;
    }
    
    public void removePlayerFromGame(Player player) {
        if (playersInGame.contains(player)) {
            playersInGame.remove(player);
        }
    }
}
