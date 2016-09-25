package com.cadiducho.fem.tnt.task;

import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.tnt.TntIsland;
import com.cadiducho.fem.tnt.TntWars;
import com.cadiducho.fem.tnt.manager.GameState;
import java.util.HashMap;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class CountdownTask extends BukkitRunnable {

    private final TntWars plugin;

    public CountdownTask(TntWars instance) {
        plugin = instance;
    }

    private int count = 7;

    @Override
    public void run() {       
        if (count == 7) {            
            plugin.getGm().getPlayersInGame().forEach(p -> plugin.getAm().teleport(p));
        } else if (count > 0 && count <= 5) {
            plugin.getMsg().sendBroadcast("&7El juego empezará en " + count);
            plugin.getGm().getPlayersInGame().forEach(p -> p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1F, 1F)); 
        } else if (count == 0) {
            GameState.state = GameState.GAME;
            for (Player players : plugin.getGm().getPlayersInGame()) {
                players.playSound(players.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1F, 1F);
                TntWars.getPlayer(players).setCleanPlayer(GameMode.SURVIVAL);
                players.setScoreboard(plugin.getServer().getScoreboardManager().getNewScoreboard());
                TntWars.getPlayer(players).setGameScoreboard();
                HashMap<Integer, Integer> plays = TntWars.getPlayer(players).getBase().getUserData().getPlays();
                plays.replace(1, plays.get(1) + 1);
                TntWars.getPlayer(players).getBase().getUserData().setPlays(plays);
                FEMServer.getUser(players).save();
                plugin.getAm().getIslas().forEach(i -> i.destroyCapsule());
            }
            plugin.getGm().setDañoEnCaida(false);
            
            //Iniciar hilo de la fase de esconder
            plugin.getAm().getGeneradores().forEach(gen -> gen.init());
            new GameTask(plugin).runTaskTimer(plugin, 20l, 20l);
            cancel();
        }

        --count;
        plugin.getGm().getPlayersInGame().forEach(pl -> pl.setLevel(count));
    }

}
