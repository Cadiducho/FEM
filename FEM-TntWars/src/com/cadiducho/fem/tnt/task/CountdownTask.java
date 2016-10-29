package com.cadiducho.fem.tnt.task;

import com.cadiducho.fem.tnt.TntWars;
import com.cadiducho.fem.tnt.manager.GameState;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

public class CountdownTask extends BukkitRunnable {

    private final TntWars plugin;

    public CountdownTask(TntWars instance) {
        plugin = instance;
    }

    private int count = 36;

    @Override
    public void run() {
        plugin.getGm().getPlayersInGame().stream().forEach(players -> {
            plugin.getMsg().sendActionBar(players, "&a&lEl juego empieza en: " + (count + 4));
        });
        if (count == 30) {
            plugin.getMsg().sendBroadcast("&7El juego empezará en 30 segundos");
        } else if (count > 0 && count <= 2) {
            plugin.getMsg().sendBroadcast("&7El juego empezará en " + (count + 3) + " segundos");
            plugin.getGm().getPlayersInGame().forEach(p -> p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1F, 1F));
        } else if (count == 0) {
            GameState.state = GameState.GAME;
            plugin.getMsg().sendBroadcast("&7El juego empezará en 3 segundos");
            plugin.getGm().getPlayersInGame().forEach(p -> p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1F, 1F));
            plugin.getGm().getPlayersInGame().forEach(p -> plugin.getAm().teleport(p)); 
            plugin.getGm().setDañoEnCaida(false);
            
            //Iniciar hilo de la fase de esconder
            new GameTask(plugin).runTaskTimer(plugin, 20l, 20l);
            cancel();
        }

        --count;      
    }

}
