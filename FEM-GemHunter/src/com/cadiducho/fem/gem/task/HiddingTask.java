package com.cadiducho.fem.gem.task;

import com.cadiducho.fem.gem.GemHunters;
import com.cadiducho.fem.gem.manager.GameState;
import com.cadiducho.fem.core.util.Title;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;

public class HiddingTask extends BukkitRunnable {

    private final GemHunters plugin;
    
    public HiddingTask(GemHunters instance) {
        plugin = instance;
    }

    private static int count = 40;

    @Override
    public void run() {
        //Por tiempos
        switch (count) {
            case 180:
                plugin.getGm().getPlayersInGame().forEach((players) -> {
                    players.playSound(players.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
                }); break;
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
        plugin.getGm().getPlayersInGame().forEach(pl -> pl.setLevel(count));
    }
    
    public void end() {
        
        //Si alguno no ha puesto gemas, terminar aquí
        if (plugin.getTm().getPuntos(plugin.getTm().azul) == 0) {
            plugin.getMsg().sendBroadcast("El equipo morado ha ganado ya que el amarillo no ha escondido gemas!");
            GameState.state = GameState.ENDING;
            new ShutdownTask(plugin).runTaskTimer(plugin, 20l, 20l);
        } else if (plugin.getTm().getPuntos(plugin.getTm().rojo) == 0) {
            plugin.getMsg().sendBroadcast("El equipo amarillo ha ganado ya que el morado no ha escondido gema!");
            GameState.state = GameState.ENDING;
            new ShutdownTask(plugin).runTaskTimer(plugin, 20l, 20l);
        }
        
        GameState.state = GameState.GAME;
        for (Player players : plugin.getGm().getPlayersInGame()) {
            players.playSound(players.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1F, 1F);
            GemHunters.getPlayer(players).setCleanPlayer(GameMode.SURVIVAL);
            GemHunters.getPlayer(players).dressPlayer();
            new Title("&b&lBusca y destruye las Gemas del equipo contrario", "", 1, 2, 1).send(players);
            players.setScoreboard(plugin.getServer().getScoreboardManager().getNewScoreboard());
            GemHunters.getPlayer(players).setGameScoreboard();
        }

        //Iniciar hilo del juego
        new GameTask(plugin).runTaskTimer(plugin, 20l, 20l);
        cancel();
    }

    public static int getTimeLeft() {
        return count;
    }
}
