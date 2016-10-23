package com.cadiducho.fem.royale.task;

import com.cadiducho.fem.royale.BattleRoyale;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.FallingBlock;
import org.bukkit.scheduler.BukkitRunnable;

public class ChestTask extends BukkitRunnable {
    
    private final BattleRoyale plugin;

    public ChestTask(BattleRoyale instance) {
        plugin = instance;
    }
    
    //Hilo para spawnear cofres cada dos minutos en posiciones aleatorias
    @Override
    public void run() {
        Location loc = plugin.getAm().spawnRandomChest();
        FallingBlock chest = plugin.getWorld().spawnFallingBlock(loc, Material.TRAPPED_CHEST, (byte)0);
        System.out.println("Cofre caido en " + loc.getBlockX() + "/"+ loc.getBlockY() + "/"+ loc.getBlockZ() + "/");
        // En GameListener sonidos y mensajes en el chat
        
        new ChestParticleTask(plugin, loc).runTaskTimer(plugin, 1l, 10l);
    } 
}
