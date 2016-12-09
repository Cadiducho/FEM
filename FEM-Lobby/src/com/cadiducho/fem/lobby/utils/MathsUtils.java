package com.cadiducho.fem.lobby.utils;

import com.cadiducho.fem.core.particles.ParticleEffect;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class MathsUtils {

    private Player[] players = Bukkit.getOnlinePlayers().toArray(new Player[Bukkit.getOnlinePlayers().size()]);

    public void drawParticles(final Player p, final ParticleType pt){
        Location l = p.getLocation();

        switch (pt.getPID()){
            case NONE:
                drawFollowParticles(l, pt.getPE());
            case HALO:
                drawCircle(l, pt.getPE());
            case SPIRAL:
                drawSpiral(l, pt.getPE());
        }
    }

    private void drawFollowParticles(final Location l, final ParticleEffect pe){
        pe.display((long)(l.getX()), (long)(l.getY()), (long)(l.getZ()), 0, 1, l, players);
    }

    private void drawSpiral(final Location l, final ParticleEffect pe){
        final int radius = 2;

            for (double y = 0; y <= 5; y+=0.05) {
                double x = radius * Math.cos(y);
                double z = radius * Math.sin(y);

                pe.display((long)(l.getX() + x), (long)(l.getY() + y), (long)(l.getZ() + z), 0, 1, l, players);
            }
    }

    private void drawCircle(final Location l, final ParticleEffect pe){
        final World w = l.getWorld();

        final int amount = 8;
        final int radius = 2;
        final double increment = (2 * Math.PI) / amount;

            for (int i = 0;i < amount; i++){
                double angle = i * increment;
                double x = l.getX() + (radius * Math.cos(angle));
                double z = l.getZ() + (radius * Math.sin(angle));

                pe.display((long)(l.getX() + x), (long)(l.getY()), (long)(l.getZ() + z), 0, 1, l, players);
            }
    }
}
