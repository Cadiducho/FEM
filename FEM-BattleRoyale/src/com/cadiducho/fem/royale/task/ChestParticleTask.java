package com.cadiducho.fem.royale.task;

import com.cadiducho.fem.core.particles.ParticleEffect;
import com.cadiducho.fem.royale.BattleRoyale;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
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

            ParticleEffect.REDSTONE.display(new ParticleEffect.OrdinaryColor(0, 0, 255), new Location(loc.getWorld(), x, y, z), Bukkit.getOnlinePlayers().toArray(new Player[Bukkit.getOnlinePlayers().size()]));
        }
        if (count == 0) {
            cancel();
        }
        --count;
    }
}
            
            