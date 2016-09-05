package com.cadiducho.fem.tnt.task;

import com.cadiducho.fem.tnt.TntPlayer;
import com.cadiducho.fem.tnt.TntWars;
import java.util.HashMap;
import org.bukkit.scheduler.BukkitRunnable;

public class DeathTask extends BukkitRunnable {
    
    private final TntPlayer player;
    private final TntWars plugin = TntWars.getInstance();
    
    public DeathTask(TntPlayer instance) {
        player = instance;
    }
    
    private int count = 5;
    
    @Override
    public void run() {
        if (player.getBase().getPlayer() == null) cancel();
        
        if (count == 5) {
            plugin.getGm().removePlayerFromGame(player.getBase().getPlayer());
            player.getBase().sendMessage("En 5 segundos serás enviado al Lobby");
            HashMap<Integer, Integer> deaths = player.getBase().getUserData().getDeaths();
            deaths.replace(1, deaths.get(1) + 1);
            player.getBase().getUserData().setDeaths(deaths);
            player.getBase().save();
        } else if (count == 0) {
            player.getBase().sendToServer("lobby");
            cancel();
        }

        player.getBase().getPlayer().setLevel(count);
        count--;
    }
    
}
