package com.cadiducho.fem.tnt.task;

import com.cadiducho.fem.tnt.TntWars;
import com.cadiducho.fem.tnt.manager.GameState;
import java.util.HashMap;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class GameTask extends BukkitRunnable {

    private final TntWars plugin;
    public static GameTask instance;
    
    public GameTask(TntWars plugin) {
        this.plugin = plugin;
    }

    private static int count = 0;

    @Override
    public void run() {
        instance = this;
        checkWinner();
        
        if (count == 5) { //Desactivar a los 5 segundos la inmunidad por caidas
            plugin.getGm().setDañoEnCaida(false);
        }

        ++count;
        plugin.getGm().getPlayersInGame().forEach(pl -> pl.setLevel(count));
    }
    
    public void checkWinner() {
        if (plugin.getGm().getPlayersInGame().size() <= 1) {
            Player winner = plugin.getGm().getPlayersInGame().get(0);
            plugin.getMsg().sendBroadcast(winner.getDisplayName() + " ha ganado la partida!");
            HashMap<Integer, Integer> wins = TntWars.getPlayer(winner).getBase().getUserData().getWins();
            wins.replace(1, wins.get(1) + 1);
            TntWars.getPlayer(winner).getBase().getUserData().setWins(wins);
            TntWars.getPlayer(winner).getBase().save();
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
