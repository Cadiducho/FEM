package com.cadiducho.fem.skywars.task;

import com.cadiducho.fem.skywars.SkyWars;
import com.cadiducho.fem.skywars.manager.GameState;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

public class LobbyTask extends BukkitRunnable {

    private final SkyWars plugin;

    public LobbyTask(SkyWars instance) {
        plugin = instance;
    }

    private int count = 30;

    @Override
    public void run() {
        //Comprobar si sigue habiendo suficientes jugadores o cancelar
        if (plugin.getGm().getPlayersInGame().size() < plugin.getAm().getMinPlayers()) {
            plugin.getGm().setCheckStart(true);
            GameState.state = GameState.LOBBY;
            cancel();
            return;
        }
        
        plugin.getGm().getPlayersInGame().stream().forEach(players -> {
            plugin.getMsg().sendActionBar(players, "&a&lEl juego empieza en: " + count);
        });
        if (count == 30) {
            plugin.getMsg().sendBroadcast("&7El juego empezará en 30 segundos");
        } else if (count == 6) {
            plugin.getGm().getPlayersInGame().forEach(p -> plugin.getAm().teleport(p));
        } else if (count > 0 && count <= 5) {
            plugin.getMsg().sendBroadcast("&7El juego empezará en " + count + " segundos");
            plugin.getGm().getPlayersInGame().forEach(p -> p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1F, 1F));
        } else if (count == 0) {
            GameState.state = GameState.GAME;
            plugin.getGm().setDañoEnCaida(false);
            plugin.getAm().fillChests();
            
            //Iniciar hilo de la fase de esconder
            new GameTask(plugin).runTaskTimer(plugin, 20l, 20l);
            cancel();
        }

        --count;      
    }

}
