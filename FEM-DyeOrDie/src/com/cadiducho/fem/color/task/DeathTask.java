package com.cadiducho.fem.color.task;

import com.cadiducho.fem.color.DyePlayer;
import com.cadiducho.fem.color.DyeOrDie;
import org.bukkit.scheduler.BukkitRunnable;

public class DeathTask extends BukkitRunnable {
    
    private final DyePlayer player;
    private final DyeOrDie plugin = DyeOrDie.getInstance();
    
    public DeathTask(DyePlayer instance) {
        player = instance;
    }
    
    private int count = 5;
    
    @Override
    public void run() {
        if (player.getBase().getPlayer() == null) cancel();
        
        if (count == 5) {
            player.getBase().sendMessage("En 5 segundos serás enviado al Lobby");
            plugin.getGm().getPlayersInGame().remove(player.getBase().getPlayer());
        } else if (count == 0) {
            player.getBase().sendToServer("lobby");
            cancel();
        }

        player.getBase().getPlayer().setLevel(count);
        count--;
    }
    
}
