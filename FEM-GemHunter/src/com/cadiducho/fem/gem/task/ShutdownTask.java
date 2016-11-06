package com.cadiducho.fem.gem.task;

import com.cadiducho.fem.gem.GemHunters;
import java.util.ArrayList;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

public class ShutdownTask extends BukkitRunnable {
    
    private final GemHunters plugin;
    private int count = 10;
    
    public ShutdownTask(GemHunters instance) {
        plugin = instance;
    }

    @Override
    public void run() {
        plugin.getMsg().sendBroadcast("Volverás al lobby en: " + count);
        
        if (count == 0) {
            ArrayList<Location> gemas = plugin.getGm().getGemas().get(plugin.getTm().azul);
            gemas.addAll(plugin.getGm().getGemas().get(plugin.getTm().rojo));
            gemas.forEach(loc -> loc.getBlock().setType(Material.AIR));
            
            plugin.getServer().getOnlinePlayers().forEach(p -> {
                p.sendMessage("Servidor desconectado");
                GemHunters.getPlayer(p).sendToLobby();
            });
            plugin.getServer().spigot().restart();
            cancel();
        }
        --count;
    }
}