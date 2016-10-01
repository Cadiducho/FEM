package com.cadiducho.fem.royale.task;

import com.cadiducho.fem.royale.BattleRoyale;
import net.minecraft.server.v1_10_R1.EnumParticle;
import net.minecraft.server.v1_10_R1.PacketPlayOutWorldParticles;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.scheduler.BukkitRunnable;

public class ChestParticleTask extends BukkitRunnable {
    
    private final BattleRoyale plugin;
    private final Location loc;

    public ChestParticleTask(BattleRoyale instance, Location loc) {
        plugin = instance;
        this.loc = loc;
    }
    
    //Task para mostar partículas durante 10 segundos
    int count = 15;
    @Override
    public void run() {
        int radio = 1;
        for (double y = 0; y <= 50; y += 0.05) {
            double x = radio * Math.cos(y);
            double z = radio * Math.sin(y);
            PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.FIREWORKS_SPARK, false, (float) (loc.getX() + x), (float) (loc.getY() + y), (float) (loc.getZ() + z), 0, 0, 0, 0, 1);
            plugin.getServer().getOnlinePlayers().forEach(p -> ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet));
        }
        if (count == 0) {
            cancel();
        }
        --count;
    } 
}
            
            