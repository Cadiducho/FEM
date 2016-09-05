package com.cadiducho.fem.ovejas.manager.tasks;

import com.cadiducho.fem.ovejas.SheepQuest;
import org.bukkit.scheduler.BukkitRunnable;

public class WinnerTask extends BukkitRunnable {
    
    private final SheepQuest plugin;
    
    public WinnerTask(SheepQuest instance) {
        plugin = instance;
    }
    
    private int count = 10;
    
    @Override
    public void run() {
        
        if (count == 0) {
            plugin.getServer().getOnlinePlayers().stream().forEach((players) -> {
                //lugin.().sendToServer(players); TODO: BUNGEE
                players.sendMessage("Servidor desconectado");
            });
            plugin.getServer().shutdown();
            this.cancel();
        }
        --count;
    }
    
}
