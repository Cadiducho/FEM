package com.cadiducho.fem.gem.task;

import com.cadiducho.fem.core.util.Title;
import com.cadiducho.fem.gem.GemHunters;
import com.cadiducho.fem.gem.GemPlayer;
import com.cadiducho.fem.gem.manager.GameState;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;

public class HiddingTask extends BukkitRunnable {

    private final GemHunters plugin;
    
    public HiddingTask(GemHunters instance) {
        plugin = instance;
    }

    private static int count = 60;

    @Override
    public void run() {
        plugin.getGm().getPlayersInGame().stream().forEach(players -> {
            plugin.getMsg().sendActionBar(players, "&a&lTiempo para esconder: " + count);
        });
        
        //Por tiempos
        switch (count) {
            case 30:
                plugin.getMsg().sendBroadcast("&7Solo quedan 30 segundos para esconder tu gema!");
                break;
            case 0:
                endHiding();
                break;
            default:
                break;
        }
        
        //Comprobar si se han puesto todos las gemas
        Team t = plugin.getTm().azul;
        if (plugin.getTm().getPuntos(t) == plugin.getTm().getJugadores().get(t).size()) { //Amarillos totales
            t = plugin.getTm().rojo;
            if (plugin.getTm().getPuntos(t) == plugin.getTm().getJugadores().get(t).size()) { //Morado tambien
                endHiding();
            }
        }
        --count; 
    }
    
    public void endHiding() {
        
        //Si alguno no ha puesto gemas, terminar aquí
        if (plugin.getTm().getPuntos(plugin.getTm().azul) == 0) {
            plugin.getMsg().sendBroadcast("El equipo morado ha ganado ya que el amarillo no ha escondido gemas!");
            GameState.state = GameState.ENDING;
            new ShutdownTask(plugin).runTaskTimer(plugin, 1l, 20l);
        } else if (plugin.getTm().getPuntos(plugin.getTm().rojo) == 0) {
            plugin.getMsg().sendBroadcast("El equipo amarillo ha ganado ya que el morado no ha escondido gema!");
            GameState.state = GameState.ENDING;
            new ShutdownTask(plugin).runTaskTimer(plugin, 1l, 20l);
        }
        
        GameState.state = GameState.GAME;
        plugin.getGm().getPlayersInGame().stream().forEach(p -> {
            p.playSound(p.getLocation(), Sound.EXPLODE, 1F, 1F);
            p.setScoreboard(plugin.getServer().getScoreboardManager().getNewScoreboard());
            Title.sendTitle(p, 1, 7, 1, "&b&lBusca y destruye las Gemas del equipo contrario", "");
            
            final GemPlayer gp = GemHunters.getPlayer(p);
            gp.setCleanPlayer(GameMode.SURVIVAL);
            gp.dressPlayer();
            gp.setGameScoreboard();
            gp.spawn();
        });

        //Iniciar hilo del juego
        new GameTask(plugin).runTaskTimer(plugin, 1l, 20l);
        cancel();
    }

    public static int getTimeLeft() {
        return count;
    }
}
