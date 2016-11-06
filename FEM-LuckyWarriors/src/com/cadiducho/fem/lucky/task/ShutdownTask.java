package com.cadiducho.fem.lucky.task;

import com.cadiducho.fem.lucky.LuckyWarriors;
import org.bukkit.scheduler.BukkitRunnable;

public class ShutdownTask extends BukkitRunnable{
    
    private final LuckyWarriors plugin;

    public ShutdownTask(LuckyWarriors instance) {
        plugin = instance;
    }
    
    private int game = 9;

    @Override
    public void run() {
        if(game == 5){
            plugin.getServer().getOnlinePlayers().stream().forEach(p -> LuckyWarriors.getPlayer(p).sendToLobby());
        } else if(game == 0){
            plugin.getServer().shutdown();
            cancel();            
        }
        -- game;
    } 
}
