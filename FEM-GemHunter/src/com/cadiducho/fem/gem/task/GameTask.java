package com.cadiducho.fem.gem.task;

import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.gem.GemHunters;
import com.cadiducho.fem.gem.manager.GameState;
import java.util.HashMap;
import lombok.Getter;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;

public class GameTask extends BukkitRunnable {

    private final GemHunters plugin;
    public static GameTask instance;
    
    public GameTask(GemHunters plugin) {
        this.plugin = plugin;
    }

    private static int count = 100;

    @Override
    public void run() {
        instance = this;
        
        switch (count) {
            case 100:
                plugin.getAm().muro(plugin.getServer().getWorlds().get(0), false);
                plugin.getGm().getPlayersInGame().forEach((players) -> {
                    players.playSound(players.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
                }); break;
            case 30:
                plugin.getMsg().sendBroadcast("&7Solo quedan 30 segundos! ");
                break;
            case 0:
                if (checkMinWinner() == null) {
                    plugin.getMsg().sendBroadcast("no hay ganador...");
                }
                end();
                break;
            default:
                break;
        }

        --count;
        plugin.getGm().getPlayersInGame().forEach(pl -> pl.setLevel(count));
    }
    
    public Team checkMinWinner() {
        Team winner = null;
        Team a = plugin.getTm().azul;
        Team m = plugin.getTm().rojo;
        
        if (plugin.getTm().getPuntos(a) > plugin.getTm().getPuntos(m)) winner = a;
        if (plugin.getTm().getPuntos(a) < plugin.getTm().getPuntos(m)) winner = m;
        
        if (winner == null) return null;
        
        //Hay un ganador
        plugin.getMsg().sendBroadcast("Ha ganado el equipo " + winner.getDisplayName());
        for (Player p : plugin.getTm().getJugadores().get(winner)) {
            HashMap<Integer, Integer> wins = FEMServer.getUser(p).getUserData().getWins();
            wins.replace(3, wins.get(1) + 1);
            FEMServer.getUser(p).getUserData().setWins(wins);
            FEMServer.getUser(p).save();
        }
        return winner;
    }
    
    public void end() {
        GameState.state = GameState.ENDING;

        //Cuenta atrás para envio a los lobbies y cierre del server
        //Iniciar hilo del juego
        new ShutdownTask(plugin).runTaskTimer(plugin, 20l, 20l);
        this.cancel();
    }


    public static int getTimeLeft() {
        return count;
    }
}
