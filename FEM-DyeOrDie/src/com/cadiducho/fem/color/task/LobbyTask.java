package com.cadiducho.fem.color.task;

import com.cadiducho.fem.color.DyeOrDie;
import com.cadiducho.fem.color.DyePlayer;
import com.cadiducho.fem.color.manager.GameState;
import com.cadiducho.fem.core.util.Title;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

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
        
        plugin.getGm().getPlayersInGame().forEach(pl -> {
            pl.setLevel(count);
            pl.setFireTicks(0);
        });
        if (count == 7) {            
            //Colocar jugadores
            plugin.getGm().getPlayersInGame().forEach(p -> DyeOrDie.getPlayer(p).spawn());       
        } else if (count > 0 && count <= 5) {
            plugin.getMsg().sendBroadcast("&7El juego empezará en " + count);
            plugin.getGm().getPlayersInGame().forEach(p -> p.playSound(p.getLocation(), Sound.NOTE_PLING, 1F, 1F));
        } else if (count == 0) {
            GameState.state = GameState.GAME;
            for (Player p : plugin.getGm().getPlayersInGame()) {
                p.playSound(p.getLocation(), Sound.EXPLODE, 1F, 1F);
                DyeOrDie.getPlayer(p).setCleanPlayer(GameMode.SURVIVAL);
                Title.sendTitle(p, 1, 7, 1,"&b&l¡Comienza a correr!", "");
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