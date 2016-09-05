package com.cadiducho.fem.tnt.task;

import com.cadiducho.fem.tnt.TntPlayer;
import java.util.HashMap;
import org.bukkit.GameMode;
import org.bukkit.scheduler.BukkitRunnable;

public class RespawnTask extends BukkitRunnable {
    
    private final TntPlayer player;
    
    public RespawnTask(TntPlayer instance) {
        player = instance;
    }
    
    private int count = 5;
    
    @Override
    public void run() {
        if (player.getBase().getPlayer() == null) cancel();
        
        if (count == 5) {
            HashMap<Integer, Integer> deaths = player.getBase().getUserData().getDeaths();
            deaths.replace(1, deaths.get(1) + 1);
            player.getBase().getUserData().setDeaths(deaths);
            player.getBase().save();
            player.getBase().sendMessage("Respawnearás en 5 segundos");
        } else if (count == 0) {
            player.spawn();
            player.setCleanPlayer(GameMode.SURVIVAL);
        }
        
        count--;
    }
    
}
