package com.cadiducho.fem.pic.task;

import com.cadiducho.fem.core.api.FEMServer.GameID;
import com.cadiducho.fem.pic.PicPlayer;
import com.cadiducho.fem.pic.Pictograma;
import com.cadiducho.fem.pic.manager.GameState;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class CountdownTask extends BukkitRunnable {

    private final Pictograma plugin;

    public CountdownTask(Pictograma instance) {
        plugin = instance;
    }

    private int count = 7;

    @Override
    public void run() {
        plugin.getGm().getPlayersInGame().forEach(pl -> pl.setLevel(count));

        switch (count){
            case 7:
                //Colocar jugadores
                plugin.getGm().getPlayersInGame().forEach(p -> Pictograma.getPlayer(p).spawn());

                //Si son 6 jugadores, jugar dos rondas por cabeza. Si son más solo una:
                plugin.getAm().setColaPintar((ArrayList<Player>) plugin.getGm().getPlayersInGame().clone());

                if (plugin.getGm().getPlayersInGame().size() < 7) {
                    plugin.getAm().getColaPintar().addAll(plugin.getGm().getPlayersInGame()); //Dos veces, dos rondas por cabeza
                }
                break;
            case 5:
            case 4:
            case 3:
            case 2:
            case 1:
                plugin.getMsg().sendBroadcast("&7El juego empezará en " + count);
                break;
            case 0:
                GameState.state = GameState.GAME;
                for (Player p : plugin.getGm().getPlayersInGame()) {
                    p.playSound(p.getLocation(), Sound.EXPLODE, 1F, 1F);
                    Pictograma.getPlayer(p).setCleanPlayer(GameMode.SURVIVAL);

                    //Ajustar puntuaciones y scoreboard
                    plugin.getGm().getScore().put(p, 0);
                    plugin.getGm().increaseScore(p, 0);

                    final PicPlayer pp = Pictograma.getPlayer(p);
                    pp.setGameScoreboard();
                    pp.getUserData().addPlay(GameID.PICTOGRAMA);
                    pp.save();
                }

                //Iniciar hilo de la fase de esconder (Ja, no, empezar ronda)
                plugin.getGm().startRound();
                cancel();
            break;
        }
        --count;
    }
}