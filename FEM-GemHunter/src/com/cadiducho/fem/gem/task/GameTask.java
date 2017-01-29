package com.cadiducho.fem.gem.task;

import com.cadiducho.fem.core.util.Title;
import com.cadiducho.fem.gem.GemHunters;
import com.cadiducho.fem.gem.GemPlayer;
import com.cadiducho.fem.gem.manager.GameState;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;

public class GameTask extends BukkitRunnable {

    private final GemHunters plugin;
    public static GameTask instance;
    
    public GameTask(GemHunters instance) {
        plugin = instance;
    }

    private static int count = 300;

    @Override
    public void run() {
        instance = this;
        
        switch (count) {
            case 300:
                plugin.getAm().muro(plugin.getServer().getWorlds().get(0));
                plugin.getGm().getPlayersInGame().stream().forEach(p -> p.playSound(p.getLocation(), Sound.CLICK, 1f, 1f));
                break;
            case 30:
                plugin.getMsg().sendBroadcast("&7Sólo quedan 30 segundos!");
                plugin.getGm().getPlayersInGame().stream().forEach(p -> {
                    new Title("&b&lSólo quedan 30 segundos", "¡Dáte prisa!", 1, 2, 1).send(p);
                });
                break;
            case 0:
                if (checkMinWinner() == null) {
                    plugin.getMsg().sendBroadcast("¡Empate! ¡No hay ningún ganador!");
                    plugin.getGm().getPlayersInGame().stream().forEach(p -> {
                        new Title("&b&lEMPATE", "", 1, 2, 1).send(p);
                    });
                }
                end();
                break;
            default:
                break;
        }

        --count;
        plugin.getGm().getPlayersInGame().stream().forEach(pl -> pl.setLevel(count));
        noPlayers();
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
        
        Team loser = plugin.getTm().getOpositeTeam(winner);
        for (Player p : plugin.getTm().getJugadores().get(winner)) {
            p.playSound(p.getLocation(), Sound.LEVEL_UP, 1F, 1F);
            new Title("&a&lVICTORIA", "¡Tu equipo ha ganado :D!", 1, 2, 1).send(p);
            
            final GemPlayer gp = GemHunters.getPlayer(p);
            HashMap<Integer, Integer> wins = gp.getUserData().getWins();
            wins.replace(3, wins.get(3) + 1);
            gp.getUserData().setWins(wins);
            gp.save();
        }
        plugin.getTm().getJugadores().get(loser).forEach(p -> {
            p.playSound(p.getLocation(), Sound.LEVEL_UP, 1F, 1F);
            new Title("&c&lDERROTA", "¡Tu equipo ha perdido :C!", 1, 2, 1).send(p);
        });
        return winner;
    }
    
    public static void end() {
        GameState.state = GameState.ENDING;

        //Cuenta atrás para envio a los lobbies y cierre del server
        //Iniciar hilo del juego
        new ShutdownTask(GemHunters.getInstance()).runTaskTimer(GemHunters.getInstance(), 1l, 20l);
        if (GameTask.instance != null) GameTask.instance.cancel();
    }

    public static int getTimeLeft() {
        return count;
    }

    private void noPlayers(){
        if (plugin.getGm().getPlayersInGame().isEmpty()){
            plugin.getServer().shutdown();
        }
    }
}
