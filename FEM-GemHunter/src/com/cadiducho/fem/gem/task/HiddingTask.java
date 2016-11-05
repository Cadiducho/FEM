package com.cadiducho.fem.gem.task;

import com.cadiducho.fem.gem.GemHunters;
import com.cadiducho.fem.gem.manager.GameState;
import com.cadiducho.fem.core.util.Title;
import com.cadiducho.fem.gem.GemPlayer;
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
        //Por tiempos
        switch (count) {
            case 30:
                plugin.getMsg().sendBroadcast("&7Solo quedan 30 segundos para esconder tu gema!");
                break;
            case 0:
                end();
                break;
            default:
                break;
        }
        
        //Comprobar si se han puesto todos las gemas
        Team t = plugin.getTm().azul;
        if (plugin.getTm().getPuntos(t) == plugin.getTm().getJugadores().get(t).size()) { //Amarillos totales
            t = plugin.getTm().rojo;
            if (plugin.getTm().getPuntos(t) == plugin.getTm().getJugadores().get(t).size()) { //Morado tambien
                end();
            }
        }
        --count;
        plugin.getGm().getPlayersInGame().stream().forEach(pl -> pl.setLevel(count));
    }
    
    public void end() {
        
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
            GemPlayer gp = GemHunters.getPlayer(p);
            p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1F, 1F);
            gp.setCleanPlayer(GameMode.SURVIVAL);
            gp.dressPlayer();
            new Title("&b&lBusca y destruye las Gemas del equipo contrario", "", 1, 2, 1).send(p);
            p.setScoreboard(plugin.getServer().getScoreboardManager().getNewScoreboard());
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
