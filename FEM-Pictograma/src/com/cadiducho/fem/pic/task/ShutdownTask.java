package com.cadiducho.fem.pic.task;

import com.cadiducho.fem.pic.Pictograma;
import org.bukkit.Bukkit;
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
            plugin.getServer().getOnlinePlayers().stream().forEach(p -> {
                p.sendMessage("Servidor desconectado");
                Pictograma.getPlayer(p).sendToLobby();
            });
            Bukkit.shutdown();
            cancel();
        }
        --count;
    }
}