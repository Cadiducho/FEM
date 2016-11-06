package com.cadiducho.fem.royale.manager;

import com.cadiducho.fem.core.util.Title;
import com.cadiducho.fem.royale.BattlePlayer;
import java.util.ArrayList;
import com.cadiducho.fem.royale.BattleRoyale;
import com.cadiducho.fem.royale.task.DeathMatchCountdown;
import com.cadiducho.fem.royale.task.WinnerCountdown;
import java.util.HashMap;
import java.util.UUID;
import lombok.Getter;
import org.bukkit.entity.Player;

public class GameManager {

    private final BattleRoyale plugin;

    public GameManager(BattleRoyale instance) {
        plugin = instance;
    }

    @Getter private final ArrayList<Player> playersInGame = new ArrayList<>();
    @Getter private final ArrayList<Player> spectators = new ArrayList<>();
    @Getter private final HashMap<UUID, Integer> kills = new HashMap<>();
    @Getter public boolean start;
    @Getter public boolean dm;

    public void init() {
        playersInGame.clear();
        spectators.clear();
        start = false;
        dm = false;
    }

    public void checkDm() {
        if (playersInGame.size() <= 4 && dm == false) {
            dm = true;
            plugin.getAm().loadSpawn();
            GameState.state = GameState.DEATHMATCH;
            new DeathMatchCountdown(plugin).runTaskTimer(plugin, 20l, 20l);
        } else if (playersInGame.size() == 1 || playersInGame.isEmpty()) {
            plugin.getServer().shutdown();
        }
    }

    public boolean checkWinner() {
        if (isInGame()) {
            if (playersInGame.size() < 2) {
                playersInGame.stream().forEach((winner) -> {
                    plugin.getMsg().sendBroadcast(winner.getDisplayName() + " ha ganado la partida!");
                    
                    final BattlePlayer bp = BattleRoyale.getPlayer(winner);
                    HashMap<Integer, Integer> wins = bp.getUserData().getWins();
                    wins.replace(5, wins.get(5) + 1);
                    new Title("&b&l¡Has ganado!", "", 1, 2, 1).send(winner);
                    bp.getUserData().setWins(wins);
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
