package com.cadiducho.fem.gem.task;

import com.cadiducho.fem.gem.GemPlayer;
import java.util.HashMap;
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
            player.sendMessage("Respawnearás en 5 segundos");
            player.sendMessage("En 5 segundos serás enviado al Lobby");
            HashMap<Integer, Integer> deaths = player.getUserData().getDeaths();
            deaths.replace(3, deaths.get(3) + 1);
            player.getUserData().setDeaths(deaths);
            player.save();
        } else if (count == 0) {
            player.spawn();
            player.setCleanPlayer(GameMode.SURVIVAL);
            player.dressPlayer();
        }
        
        count--;
    }
    
}
