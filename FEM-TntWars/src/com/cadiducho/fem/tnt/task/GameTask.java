package com.cadiducho.fem.tnt.task;

import com.cadiducho.fem.core.util.Title;
import com.cadiducho.fem.tnt.TntIsland;
import com.cadiducho.fem.tnt.TntPlayer;
import com.cadiducho.fem.tnt.TntWars;
import com.cadiducho.fem.tnt.manager.GameState;
import java.util.HashMap;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class GameTask extends BukkitRunnable {

    private final TntWars plugin;
    public static GameTask instance;
    
    public GameTask(TntWars plugin) {
        this.plugin = plugin;
    }

    private static int count = 0;
    private boolean timePlayed = false;

    @Override
    public void run() {
        instance = this;
        checkWinner();
        if (timePlayed) {
            plugin.getGm().getPlayersInGame().stream().forEach(players -> {
                plugin.getMsg().sendActionBar(players, "&a&lTiempo jugado: " + (count - 3));
            });
        }
        if (count >= 0 && count < 2) {
            plugin.getMsg().sendBroadcast("&7El juego empezará en " + (count == 0 ? "2" : "1") + " segundos");
            plugin.getGm().getPlayersInGame().forEach(p -> p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1F, 1F));  
        } else if (count == 2) {
            for (Player p : plugin.getGm().getPlayersInGame()) {
                TntIsland isla = TntIsland.getIsland(p.getUniqueId());
                new Title("", isla.getColor() + "¡Destruye el resto de islas!", 1, 2, 1).send(p);
                p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1F, 1F);
                p.setScoreboard(plugin.getServer().getScoreboardManager().getNewScoreboard());
                
                final TntPlayer tp = TntWars.getPlayer(p);
                tp.setCleanPlayer(GameMode.SURVIVAL);
                tp.setGameScoreboard();
                HashMap<Integer, Integer> plays = tp.getUserData().getPlays();
                plays.replace(1, plays.get(1) + 1);
                tp.getUserData().setPlays(plays);
                tp.save();
            }
            plugin.getAm().getIslas().forEach(i -> i.destroyCapsule());
            timePlayed = true;
        }
        if (count == 7) { //Desactivar a los 5 segundos la inmunidad por caidas
            plugin.getGm().setDañoEnCaida(true);
            plugin.getAm().getGeneradores().forEach(gen -> gen.init());
        }

        ++count;
    }
    
    public void checkWinner() {
        if (plugin.getGm().getPlayersInGame().size() <= 1) {
            Player winner = plugin.getGm().getPlayersInGame().get(0);
            for (Player p : plugin.getGm().getPlayersInGame()) {
                p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1F, 1F);
                new Title("&a" + p.getName(), "&aha ganado la partida!", 1, 2, 1).send(winner);
            }
            plugin.getMsg().sendBroadcast(winner.getDisplayName() + " ha ganado la partida!");
            HashMap<Integer, Integer> wins = TntWars.getPlayer(winner).getUserData().getWins();
            wins.replace(1, wins.get(1) + 1);
            TntWars.getPlayer(winner).getUserData().setWins(wins);
            TntWars.getPlayer(winner).save();
            end();
            cancel();
        }
    }
    
    public void end() {
        GameState.state = GameState.ENDING;

        //Cuenta atrás para envio a los lobbies y cierre del server
        //Iniciar hilo del juego
        new ShutdownTask(plugin).runTaskTimer(plugin, 20l, 20l);
    }

    public static int getTimeLeft() {
        return count;
    }
}
