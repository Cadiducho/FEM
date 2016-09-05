package com.cadiducho.fem.ovejas.manager.tasks;

import com.cadiducho.fem.ovejas.SheepPlayer;
import org.bukkit.GameMode;
import org.bukkit.scheduler.BukkitRunnable;

public class RespawnTask extends BukkitRunnable {
    
    private final SheepPlayer player;
    
    public RespawnTask(SheepPlayer instance) {
        player = instance;
    }
    
    private int count = 5;
     
    @Override
    public void run() {
        if (player.getBase().getPlayer() == null) cancel();
        
        if (count == 5) {
            player.getBase().sendMessage("Respawnearás en 5 segundos");
        } else if (count == 0) {
            player.spawn();
            player.setCleanPlayer(GameMode.ADVENTURE);
            player.dressPlayer();
        }
        
        count--;
    }
    
}
