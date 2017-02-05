package com.cadiducho.fem.gem.task;

import com.cadiducho.fem.core.api.FEMServer.GameID;
import com.cadiducho.fem.gem.GemPlayer;
import org.bukkit.GameMode;
import org.bukkit.scheduler.BukkitRunnable;

public class RespawnTask extends BukkitRunnable {
    
    private final GemPlayer player;
    
    public RespawnTask(GemPlayer instance) {
        player = instance;
    }
    
    private int count = 5;
    
    @Override
    public void run() {
        if (player.getPlayer() == null) cancel();
        
        if (count == 5) {
            player.sendMessage("&aRespawnearás en 5 segundos");
            player.getUserData().addDeath(GameID.GEMHUNTERS);
            player.save();
        } else if (count == 0) {
            player.spawn();
            player.setCleanPlayer(GameMode.SURVIVAL);
            player.dressPlayer();
        }
        
        count--;
    }
    
}
