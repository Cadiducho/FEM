package com.cadiducho.fem.skywars.task;

import com.cadiducho.fem.skywars.SkyWars;
import org.bukkit.scheduler.BukkitRunnable;

public class ShutdownTask extends BukkitRunnable {
    
    private final SkyWars plugin;
    private int count = 10;
    
    public ShutdownTask(SkyWars instance) {
        plugin = instance;
    }

    @Override
    public void run() {
        
        plugin.getServer().getOnlinePlayers().stream().forEach(pl -> pl.sendMessage("Volverás al lobby en: " + count));
        if (count == 0) {            
            plugin.getServer().getOnlinePlayers().stream().forEach(p -> {
                p.sendMessage("Servidor desconectado");
                SkyWars.getPlayer(p).sendToLobby();
            });
            plugin.getServer().unloadWorld(plugin.getServer().getWorlds().get(0), false);
            plugin.getServer().spigot().restart();
            cancel();
        }
        --count;
    }
}