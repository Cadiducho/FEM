package com.cadiducho.fem.pic.task;

import com.cadiducho.fem.core.util.Title;
import com.cadiducho.fem.pic.Pictograma;
import com.cadiducho.fem.pic.manager.GameState;
import java.util.ArrayList;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class CountdownTask extends BukkitRunnable {

    private final Pictograma plugin;

    public CountdownTask(Pictograma instance) {
        plugin = instance;
    }

    private int count = 7;

    @Override
    public void run() {       
        if (count == 7) {            
            //Colocar jugadores
            plugin.getGm().getPlayersInGame().forEach(p -> Pictograma.getPlayer(p).spawn());
            plugin.getAm().setColaPintar((ArrayList<Player>) plugin.getGm().getPlayersInGame().clone());
        } else if (count > 0 && count <= 5) {
            plugin.getMsg().sendBroadcast("&7El juego empezará en " + count);

        } else if (count == 0) {
            GameState.state = GameState.GAME;
            for (Player p : plugin.getGm().getPlayersInGame()) {
                p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1F, 1F);
                Pictograma.getPlayer(p).setCleanPlayer(GameMode.SURVIVAL);
                
                //Ajustar puntuaciones y scoreboard
                plugin.getGm().getScore().put(p, 0);
                plugin.getGm().increaseScore(p, 0);
                p.setScoreboard(plugin.getGm().getBoard());  
                /*Pictograma.getPlayer(players).setHiddingScoreboard();
                HashMap<Integer, Integer> plays = FEMServer.getUser(players).getUserData().getPlays();
                plays.replace(3, plays.get(1) + 1);
                FEMServer.getUser(players).getUserData().setPlays(plays);
                FEMServer.getUser(players).save();*/
            }
            
            //Iniciar hilo de la fase de esconder
            plugin.getGm().startRound();
            cancel();
        }

        --count;
        plugin.getGm().getPlayersInGame().forEach(pl -> pl.setLevel(count));
    }

}
