package com.cadiducho.fem.tnt.task;

import com.cadiducho.fem.tnt.TntWars;
import org.bukkit.scheduler.BukkitRunnable;

public class ShutdownTask extends BukkitRunnable {
    
    private final TntWars plugin;
    private int count = 10;
    
    public ShutdownTask(TntWars instance) {
        plugin = instance;
    }

    @Override
    public void run() {
        
        plugin.getServer().getOnlinePlayers().stream().forEach(pl -> pl.sendMessage("Volverás al lobby en: " + count));
        if (count == 0) {            
            plugin.getServer().getOnlinePlayers().stream().forEach(p -> {
                p.sendMessage("Servidor desconectado");
                TntWars.getPlayer(p).sendToLobby();
            });
            plugin.getServer().unloadWorld(plugin.getServer().getWorlds().get(0), false);
            plugin.getServer().shutdown();
            cancel();
        }
        --count;
    }
}