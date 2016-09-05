package com.cadiducho.fem.gem.task;

import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.core.util.Title;
import com.cadiducho.fem.gem.GemHunters;
import com.cadiducho.fem.gem.manager.GameState;
import java.util.HashMap;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class CountdownTask extends BukkitRunnable {

    private final GemHunters plugin;

    public CountdownTask(GemHunters instance) {
        plugin = instance;
    }

    private int count = 7;

    @Override
    public void run() {       
        if (count == 7) {            
            //Colocar jugadores
            plugin.getTm().cleanTeams();
            plugin.getTm().drawTeams(plugin.getGm().getPlayersInGame());
            plugin.getGm().getPlayersInGame().forEach(p -> GemHunters.getPlayer(p).spawn());
            
        } else if (count > 0 && count <= 5) {
            plugin.getMsg().sendBroadcast("&7El juego empezará en " + count);

        } else if (count == 0) {
            GameState.state = GameState.HIDDING;
            for (Player players : plugin.getGm().getPlayersInGame()) {
                players.playSound(players.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1F, 1F);
                GemHunters.getPlayer(players).setCleanPlayer(GameMode.SURVIVAL);
                GemHunters.getPlayer(players).dressPlayer();
                new Title("&b&l¡Esconde tu gema!", "", 1, 2, 1).send(players);
                players.setScoreboard(plugin.getServer().getScoreboardManager().getNewScoreboard());
                GemHunters.getPlayer(players).setHiddingScoreboard();
                HashMap<Integer, Integer> plays = FEMServer.getUser(players).getUserData().getPlays();
                plays.replace(3, plays.get(1) + 1);
                FEMServer.getUser(players).getUserData().setPlays(plays);
                FEMServer.getUser(players).save();
            }
            
            //Iniciar hilo de la fase de esconder
            new HiddingTask(plugin).runTaskTimer(plugin, 20l, 20l);
            this.cancel();
        }

        --count;
        plugin.getGm().getPlayersInGame().forEach(pl -> pl.setLevel(count));
    }

}
