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
        plugin.getGm().getPlayersInGame().forEach(pl ->  pl.setLevel(count));
        
        switch (count) {
            case 75:
                gameInstance = this;
                System.out.println("instance ajustado");
                break;
            case 45:
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
        System.out.println("Termiando ronda");
        gameInstance.cancel();
        Pictograma.getPlayer(plugin.getGm().builder).setCleanPlayer(GameMode.ADVENTURE);
        Pictograma.getPlayer(plugin.getGm().builder).spawn();
        plugin.getGm().getBarraCheta().removePlayer(plugin.getGm().builder);
        plugin.getGm().builder = null;
        plugin.getGm().getBarra().setTitle(plugin.getGm().word.toUpperCase());
        if (plugin.getAm().getColaPintar().isEmpty()) {
            plugin.getGm().endGame();
            return;
        }
        plugin.getMsg().sendBroadcast("&aSe acabó el tiempo! La siguiente ronda comenzará en 5 segundos"); 
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> plugin.getGm().startRound(), 100L);
    }
}