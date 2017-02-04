package com.cadiducho.fem.lucky.task;

import com.cadiducho.fem.lucky.LuckyWarriors;
import org.bukkit.scheduler.BukkitRunnable;

public class ShutdownTask extends BukkitRunnable{
    
    private final LuckyWarriors plugin;

    public ShutdownTask(LuckyWarriors instance) {
        plugin = instance;
    }
    
    private int game = 10;

    @Override
    public void run() {
        switch (game) {
            case 10:
                plugin.getMsg().sendBroadcast("&aVolverás al lobby en &e10 &asegundos");
                break;
            case 5:
                plugin.getServer().getOnlinePlayers().stream().forEach(p -> LuckyWarriors.getPlayer(p).sendToLobby());
                break;
            case 0:            
                plugin.getServer().shutdown();
                cancel();
                break;
            default:
                break;
        }
        --game;
    } 
}
