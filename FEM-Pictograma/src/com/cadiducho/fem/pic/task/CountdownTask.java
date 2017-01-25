package com.cadiducho.fem.pic.task;

import com.cadiducho.fem.pic.PicPlayer;
import com.cadiducho.fem.pic.Pictograma;
import com.cadiducho.fem.pic.manager.GameState;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;

public class CountdownTask extends BukkitRunnable {

    private final Pictograma plugin;

    public CountdownTask(Pictograma instance) {
        plugin = instance;
    }

    private int count = 7;

    @Override
    public void run() {
        plugin.getGm().getPlayersInGame().forEach(pl -> pl.setLevel(count));
        if (count == 7) {            
            //Colocar jugadores
            plugin.getGm().getPlayersInGame().forEach(p -> Pictograma.getPlayer(p).spawn());
            
            //Si son 6 jugadores, jugar dos rondas por cabeza. Si son más solo una:
            plugin.getAm().setColaPintar((ArrayList<Player>) plugin.getGm().getPlayersInGame().clone());
            
            if (plugin.getGm().getPlayersInGame().size() < 7) {
                plugin.getAm().getColaPintar().addAll(plugin.getGm().getPlayersInGame()); //Dos veces, dos rondas por cabeza
            }
        } else if (count > 0 && count <= 5) {
            plugin.getMsg().sendBroadcast("&7El juego empezará en " + count);
        } else if (count == 0) {
            GameState.state = GameState.GAME;
            for (Player p : plugin.getGm().getPlayersInGame()) {
                p.playSound(p.getLocation(), Sound.EXPLODE, 1F, 1F);
                Pictograma.getPlayer(p).setCleanPlayer(GameMode.SURVIVAL);
                
                //Ajustar puntuaciones y scoreboard
                plugin.getGm().getScore().put(p, 0);
                plugin.getGm().increaseScore(p, 0);
                p.setScoreboard(plugin.getGm().getBoard());
                
                final PicPlayer pp = Pictograma.getPlayer(p);
                HashMap<Integer, Integer> plays = pp.getUserData().getPlays();
                plays.replace(4, plays.get(4) + 1);
                pp.getUserData().setPlays(plays);
                pp.save();
            }
            
            //Iniciar hilo de la fase de esconder
            plugin.getGm().startRound();
            cancel();
        }

        --count;
    }
}