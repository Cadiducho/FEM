package com.cadiducho.fem.color.task;

import com.cadiducho.fem.color.DyeOrDie;
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
            plugin.getServer().getOnlinePlayers().stream().forEach(p -> {
                DyeOrDie.getPlayer(p).sendToLobby();
                p.sendMessage("Servidor desconectado");
            });
        }
        if (count == 0) {
            plugin.getServer().shutdown();
            cancel();
        }
        --count;
    }
}