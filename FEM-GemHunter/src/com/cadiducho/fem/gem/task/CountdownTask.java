package com.cadiducho.fem.gem.task;

import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.core.util.Title;
import com.cadiducho.fem.gem.GemHunters;
import com.cadiducho.fem.gem.GemPlayer;
import com.cadiducho.fem.gem.manager.GameState;
import java.util.HashMap;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

public class CountdownTask extends BukkitRunnable {

    private final GemHunters plugin;

    public CountdownTask(GemHunters instance) {
        plugin = instance;
    }

    private int count = 10;

    @Override
    public void run() {
        if (count == 10) {            
            //Colocar jugadores
            plugin.getTm().cleanTeams();
            plugin.getTm().drawTeams(plugin.getGm().getPlayersInGame());
            plugin.getGm().getPlayersInGame().stream().forEach(p -> GemHunters.getPlayer(p).spawn());  
        } else if (count > 0 && count <= 5) {
            plugin.getMsg().sendBroadcast("&7El juego empezará en " + count);
            plugin.getGm().getPlayersInGame().stream().forEach(p -> p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1F, 1F));
        } else if (count == 0) {
            GameState.state = GameState.HIDDING;
            plugin.getGm().getPlayersInGame().stream().forEach(p -> {
                GemPlayer gp = GemHunters.getPlayer(p);
                p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1F, 1F);
                gp.setCleanPlayer(GameMode.SURVIVAL);
                gp.dressPlayer();
                new Title("&b&l¡Esconde tu gema!", "", 1, 2, 1).send(p);
                p.setScoreboard(plugin.getServer().getScoreboardManager().getNewScoreboard());
                gp.setHiddingScoreboard();
                HashMap<Integer, Integer> plays = FEMServer.getUser(p).getUserData().getPlays();
                plays.replace(3, plays.get(3) + 1);
                gp.getUserData().setPlays(plays);
                gp.save();
            });
            
            //Iniciar hilo de la fase de esconder
            new HiddingTask(plugin).runTaskTimer(plugin, 1l, 20l);
            cancel();
        }

        --count;
        plugin.getGm().getPlayersInGame().stream().forEach(pl -> pl.setLevel(count));
    }

}
