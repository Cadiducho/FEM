package com.cadiducho.fem.color.task;

import com.cadiducho.fem.core.util.Title;
import com.cadiducho.fem.color.DyeOrDie;
import com.cadiducho.fem.color.manager.GameState;
import com.cadiducho.fem.core.api.FEMServer;
import java.util.HashMap;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class CountdownTask extends BukkitRunnable {

    private final DyeOrDie plugin;

    public CountdownTask(DyeOrDie instance) {
        plugin = instance;
    }

    private int count = 30;

    @Override
    public void run() {       
        if (count == 7) {            
            //Colocar jugadores
            plugin.getGm().getPlayersInGame().forEach(p -> DyeOrDie.getPlayer(p).spawn());
            
        } else if (count > 0 && count <= 5) {
            plugin.getMsg().sendBroadcast("&7El juego empezará en " + count);

        } else if (count == 0) {
            GameState.state = GameState.GAME;
            for (Player players : plugin.getGm().getPlayersInGame()) {
                players.playSound(players.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1F, 1F);
                DyeOrDie.getPlayer(players).setCleanPlayer(GameMode.SURVIVAL);
                new Title("&b&l¡Comienza a correr!", "", 1, 2, 1).send(players);
                players.setScoreboard(plugin.getServer().getScoreboardManager().getNewScoreboard());
                HashMap<Integer, Integer> plays = FEMServer.getUser(players).getUserData().getPlays();
                plays.replace(2, plays.get(1) + 1);
                FEMServer.getUser(players).getUserData().setPlays(plays);
                FEMServer.getUser(players).save();
            }
            
            //Iniciar hilo de la fase de esconder
            plugin.getGm().startGame();
            cancel();
        }

        --count;
        plugin.getGm().getPlayersInGame().forEach(pl -> pl.setLevel(count));
    }

}
