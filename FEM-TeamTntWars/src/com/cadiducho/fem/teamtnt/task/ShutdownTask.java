package com.cadiducho.fem.teamtnt.task;

import com.cadiducho.fem.teamtnt.TeamTntWars;
import org.bukkit.scheduler.BukkitRunnable;

public class ShutdownTask extends BukkitRunnable {
    
    private final TeamTntWars plugin;
    private int count = 10;
    
    public ShutdownTask(TeamTntWars instance) {
        plugin = instance;
    }

    @Override
    public void run() {
        
        plugin.getServer().getOnlinePlayers().stream().forEach(pl -> pl.sendMessage("Volverás al lobby en: " + count));
        if (count == 0) {            
            plugin.getServer().getOnlinePlayers().stream().forEach(p -> {
                p.sendMessage("Servidor desconectado");
                TeamTntWars.getPlayer(p).sendToLobby();
            });
            plugin.getServer().unloadWorld(plugin.getServer().getWorlds().get(0), false);
            plugin.getServer().spigot().restart();
            cancel();
        }
        --count;
    }
}