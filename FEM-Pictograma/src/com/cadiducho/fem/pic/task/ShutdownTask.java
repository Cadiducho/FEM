package com.cadiducho.fem.pic.task;

import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.pic.Pictograma;
import org.bukkit.scheduler.BukkitRunnable;

public class ShutdownTask extends BukkitRunnable {
    
    private final Pictograma plugin;
    private int count = 10;
    
    public ShutdownTask(Pictograma instance) {
        plugin = instance;
    }

    @Override
    public void run() {
        
        plugin.getMsg().sendBroadcast("Volverás al lobby en: " + count);
        if (count == 0) {
            plugin.getServer().getOnlinePlayers().stream().forEach((players) -> {
                players.sendMessage("Servidor desconectado");
                FEMServer.getUser(players).sendToLobby();
            });
            plugin.getServer().spigot().restart();
            cancel();
        }
        --count;
    }
}