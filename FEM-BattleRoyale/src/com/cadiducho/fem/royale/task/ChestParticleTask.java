package com.cadiducho.fem.royale.task;

import com.cadiducho.fem.royale.BattleRoyale;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

public class ChestParticleTask extends BukkitRunnable {
    
    private final BattleRoyale plugin;
    private final Location loc;

    public ChestParticleTask(BattleRoyale instance, Location loc) {
        plugin = instance;
        this.loc = loc;
    }
    
    //Task para mostar partículas durante 10 segundos
    int count = 10 * 2;
    @Override
    public void run() {
        int radio = 1;
        for (double y = 0; y <= 100; y += 0.05) {
            double x = radio * Math.cos(y);
            double z = radio * Math.sin(y);

            org.inventivetalent.particle.ParticleEffect.REDSTONE.sendColor(plugin.getServer().getOnlinePlayers(), 
                    new Location(loc.getWorld(), (loc.getX() + x), (loc.getY() + y), (loc.getZ() + z)), 
                    Color.fromBGR(0, 0, 255));
        }
        if (count == 0) {
            cancel();
        }
        --count;
    }
}
            
            