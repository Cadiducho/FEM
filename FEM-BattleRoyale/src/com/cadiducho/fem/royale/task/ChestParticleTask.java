package com.cadiducho.fem.royale.task;

import com.cadiducho.fem.core.util.ReflectionUtils;
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
    int count = 10 * 2;
    @Override
    public void run() {
        int radio = 1;
        for (double y = 0; y <= 100; y += 0.05) {
            double x = radio * Math.cos(y);
            double z = radio * Math.sin(y);
       
            PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.REDSTONE, false, (float) (loc.getX() + x), (float) (loc.getY() + y), (float) (loc.getZ() + z), (float) 0, (float) 0, (float) 255, (float) 0, 0, 1);
            try {
                ReflectionUtils.setValue(packet, true, "j", true);
            } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException ex) {}
            plugin.getServer().getOnlinePlayers().forEach(p -> ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet));
        }
        if (count == 0) {
            cancel();
        }
        --count;
    }
}
            
            