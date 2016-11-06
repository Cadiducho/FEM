package com.cadiducho.fem.color.task;

import com.cadiducho.fem.core.util.Title;
import com.cadiducho.fem.color.DyeOrDie;
import com.cadiducho.fem.color.DyePlayer;
import com.cadiducho.fem.color.manager.GameState;
import java.util.HashMap;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class LobbyTask extends BukkitRunnable {

    private final DyeOrDie plugin;

    public LobbyTask(DyeOrDie instance) {
        plugin = instance;
    }

    private int count = 30;

    @Override
    public void run() {
        //Comprobar si sigue habiendo suficientes jugadores o cancelar
        if (plugin.getGm().getPlayersInGame().size() < plugin.getAm().getMinPlayers()) {
            plugin.getGm().setCheckStart(true);
            plugin.getServer().getOnlinePlayers().forEach(pl ->  pl.setLevel(0));
            GameState.state = GameState.LOBBY;
            cancel();
            return;
        }
        
        plugin.getGm().getPlayersInGame().forEach(pl -> pl.setLevel(count));
        if (count == 7) {            
            //Colocar jugadores
            plugin.getGm().getPlayersInGame().forEach(p -> DyeOrDie.getPlayer(p).spawn());       
        } else if (count > 0 && count <= 5) {
            plugin.getMsg().sendBroadcast("&7El juego empezará en " + count);
            plugin.getGm().getPlayersInGame().forEach(p -> p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1F, 1F));
        } else if (count == 0) {
            GameState.state = GameState.GAME;
            for (Player p : plugin.getGm().getPlayersInGame()) {
                p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1F, 1F);
                DyeOrDie.getPlayer(p).setCleanPlayer(GameMode.SURVIVAL);
                new Title("&b&l¡Comienza a correr!", "", 1, 2, 1).send(p);
                p.setScoreboard(plugin.getServer().getScoreboardManager().getNewScoreboard());
                
                final DyePlayer dp = DyeOrDie.getPlayer(p);
                HashMap<Integer, Integer> plays = dp.getUserData().getPlays();
                plays.replace(2, plays.get(2) + 1);
                dp.getUserData().setPlays(plays);
                dp.save();
            }
            
            //Iniciar hilo de la fase de esconder
            plugin.getGm().startGame();
            cancel();
        }

        --count; 
    }
}