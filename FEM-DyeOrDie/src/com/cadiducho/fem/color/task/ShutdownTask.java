package com.cadiducho.fem.color.task;

import com.cadiducho.fem.color.DyeOrDie;
import com.cadiducho.fem.core.api.FEMServer;
import org.bukkit.scheduler.BukkitRunnable;

public class ShutdownTask extends BukkitRunnable {
    
    private final DyeOrDie plugin;
    private int count = 10;
    
    public ShutdownTask(DyeOrDie instance) {
        plugin = instance;
    }

    @Override
    public void run() {
        
        plugin.getMsg().sendBroadcast("Volverás al lobby en: " + (count - 1));
        if (count == 1) {            
            plugin.getServer().getOnlinePlayers().stream().forEach((players) -> {
                FEMServer.getUser(players).sendToLobby();
                players.sendMessage("Servidor desconectado");
            });
        }
        if (count == 0) {
            plugin.getServer().spigot().restart();
            cancel();
        }
        --count;
    }
}