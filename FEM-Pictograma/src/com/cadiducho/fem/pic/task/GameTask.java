package com.cadiducho.fem.pic.task;

import com.cadiducho.fem.pic.Pictograma;
import lombok.Getter;
import org.bukkit.GameMode;
import org.bukkit.scheduler.BukkitRunnable;

public class GameTask extends BukkitRunnable {

    private final Pictograma plugin;
    @Getter private static GameTask gameInstance;

    public GameTask(Pictograma instance) {
        plugin = instance;
    }

    private int count = 75;

    @Override
    public void run() {
        //Mostrar tiempo restante
        plugin.getGm().getPlayersInGame().forEach(pl ->  pl.setLevel(count));
        
        //Mostrar la palabra a los jugadores. Entera a quien acerto y contructor, en proceso al resto
        plugin.getGm().getPlayersInGame().stream().forEach(p -> {
            if (p.getUniqueId() == plugin.getGm().builder.getUniqueId()) {
                plugin.getMsg().sendActionBar(p, "&aPalabra: &b" + plugin.getGm().word.toUpperCase());
            } else if (plugin.getGm().getHasFound().contains(p.getUniqueId())) {
                plugin.getMsg().sendActionBar(p, "&e¡Acertada!: &b" + plugin.getGm().word.toUpperCase());
            } else {
                plugin.getMsg().sendActionBar(p, "&a" + plugin.getGm().wordf.toString().toUpperCase());
            }
        });
        switch (count) {
            case 75:
                gameInstance = this;
                break;
            case 30:
                plugin.getMsg().sendBroadcast("&630 segundos para terminar la ronda!");
                break;
            case 35:
            case 25:
            case 15:
                plugin.getGm().addRandomLetter();
                break;
            case 10:
                plugin.getMsg().sendBroadcast("&610 segundos para terminar la ronda!");
                break;
            case 0:
                prepareNextRound();
                break;
        }
        --count;
    }
    
    public void prepareNextRound() {
        gameInstance.cancel();
        
        //Informar a los usuarios
        plugin.getMsg().sendEmptyLine();
        if (plugin.getGm().builder != null || !plugin.getGm().builder.isOnline()) {
            plugin.getMsg().sendBroadcast("&aSe acabó el tiempo! La siguiente ronda comenzará en 5 segundos");
        }
        plugin.getMsg().sendBroadcast("&aLa palabra era: &e&l" + plugin.getGm().word);
        plugin.getMsg().sendEmptyLine();
        
        //No escribir palabras entre rondas
        plugin.getGm().word = "";
        plugin.getGm().acceptWords = false;
        
        //Mover al constructor y asignar valor null para buscar uno nuevo
        if (plugin.getGm().builder != null || !plugin.getGm().builder.isOnline()) {
            Pictograma.getPlayer(plugin.getGm().builder).setCleanPlayer(GameMode.ADVENTURE);
            Pictograma.getPlayer(plugin.getGm().builder).spawn();
        }
        plugin.getGm().builder = null;

        //Comenzar nueva ronda o terminar juego
        if (plugin.getAm().getColaPintar().isEmpty()) {
            plugin.getGm().endGame();
            return;
        }
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> plugin.getGm().startRound(), 100L);
    }
}