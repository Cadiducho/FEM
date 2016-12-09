package com.cadiducho.fem.lobby.utils;

import com.cadiducho.fem.core.particles.ParticleEffect;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class MathsUtils {

    private Player[] players = Bukkit.getOnlinePlayers().toArray(new Player[Bukkit.getOnlinePlayers().size()]);

    public void drawParticles(final Player p, final ParticleType pt){
        Location l = p.getLocation();

        switch (pt.getPID()){
            case NONE:
                drawFollowParticles(l, pt.getPE(), pt.getColor());
                break;
            case HALO:
                drawCircle(l, pt.getPE(), pt.getColor(), 2);
                break;
            case SPIRAL:
                drawSpiral(l, pt.getPE(), pt.getColor());
                break;
            case SPHERE:
                drawSphere(l, pt.getPE(), pt.getColor());
                break;
        }
    }

    private void drawFollowParticles(final Location l, final ParticleEffect pe, ParticleEffect.OrdinaryColor color){
        if (pe.hasProperty(ParticleEffect.ParticleProperty.COLORABLE)) {
            pe.display(color, l, players);
            return;
        }
        pe.display((long)(l.getX()), (long)(l.getY()), (long)(l.getZ()), 0, 1, l, players);
    }

    private void drawSpiral(final Location l, final ParticleEffect pe, ParticleEffect.OrdinaryColor color){
        final int radius = 2;

        for (double y = 0; y <= 5; y+=0.05) {
            double x = radius * Math.cos(y);
            double z = radius * Math.sin(y);

            if (pe.hasProperty(ParticleEffect.ParticleProperty.COLORABLE)) {
                pe.display(color, l, players);
            } else {
                pe.display((long) (l.getX() + x), (long) (l.getY() + y), (long) (l.getZ() + z), 0, 1, l, players);
            }
        }
    }

    private void drawCircle(final Location l, final ParticleEffect pe, ParticleEffect.OrdinaryColor color, int radius) {
        final int amount = 8;
        final double increment = (2 * Math.PI) / amount;

        for (int i = 0; i < amount; i++) {
            double angle = i * increment;
            double x = l.getX() + (radius * Math.cos(angle));
            double z = l.getZ() + (radius * Math.sin(angle));

            if (pe.hasProperty(ParticleEffect.ParticleProperty.COLORABLE)) {
                pe.display(color, l, players);
            } else {
            pe.display((long) (l.getX() + x), (long) (l.getY()), (long) (l.getZ() + z), 0, 1, l, players);
            }
        }
    }

    private void rotationEffect(final Location l, final ParticleEffect pe, ParticleEffect.OrdinaryColor color){
        final double radialsPerStep = Math.PI / 30;
        float step = 0;

        for (int y = 0; y < 5; y++) {
            l.add(0, y, 0);
            l.add(Math.cos(radialsPerStep * step) * 2, 0, Math.sin(radialsPerStep * step) * 2);

            if (pe.hasProperty(ParticleEffect.ParticleProperty.COLORABLE)) {
                pe.display(color, l, players);
            } else {
                pe.display(0.0F, 0.0F, 0.0F, 1.0F, 1, l, players);
            }

            step++;
        }

    }

    private void radarEffect(final Location l, final ParticleEffect pe, ParticleEffect.OrdinaryColor color){
        final float radius = 0.2f;
        final double radialsPerStep = Math.PI / 18;
        float j = 0.0F;

        l.setY(l.getY() + 2.0D);

        for (int k = 0; k < 5F; k++){
            l.setX(l.getX() + Math.sin(j * radialsPerStep) * radius);
            l.setY(l.getY());
            l.setZ(l.getZ() + Math.cos(j * radialsPerStep) * radius);

            if (pe.hasProperty(ParticleEffect.ParticleProperty.COLORABLE)) {
                pe.display(color, l, players);
            } else {
                pe.display(0.0F, 0.0F, 0.0F, 1.0F, 1, l, players);
            }
            j += 0.3F;
        }
        if(j >= 360.0F){
            j = 0.0F;
        }
    }

    private void tornadoEffect(final Location l, final ParticleEffect pe, ParticleEffect.OrdinaryColor color){
        final float LineNumber = 3f;
        float j = 0.0f;
        final float radius = 0.3f;
        final float heightEcart = 0.01f;

        l.setY(l.getY() + 2);

        for(int k = 0; k < LineNumber; k++){
            l.add(Math.cos(j) * radius, j * heightEcart, Math.sin(j) * radius);

            if (pe.hasProperty(ParticleEffect.ParticleProperty.COLORABLE)) {
                pe.display(color, l, players);
            } else {
                pe.display(0.0F, 0.0F, 0.0F, 1.0F, 1, l, players);
            }
        }
        j += 0.2f;
        if(j > 50){
            j = 0;
        }
    }

    private void drawSphere(final Location l, final ParticleEffect pe, ParticleEffect.OrdinaryColor color){
        for(double i = 0; i <= Math.PI; i += Math.PI / 10){
            double radius = Math.sin(i);
            double y = Math.cos(i);
            for(double a = 0; a < Math.PI * 2; a += Math.PI / 10) {
                double x = Math.cos(a) * radius;
                double z = Math.sin(a) * radius;

                l.add(x, y, z);

                if (pe.hasProperty(ParticleEffect.ParticleProperty.COLORABLE)) {
                    pe.display(color, l, players);
                } else {
                pe.display(0.0F, 0.0F, 0.0F, 1.0F, 1, l, players);
                }

                l.subtract(x, y, z);
            }
        }
    }

    private void createPolygon(final int points, final Location l, final ParticleEffect pe, ParticleEffect.OrdinaryColor color){
        int t = 0;
        for(int iteration = 0; iteration < points; iteration++){
            double angle = 360.0 / points * iteration;

            angle = Math.toRadians(angle);

            double x = Math.cos(angle);
            double z = Math.sin(angle);

            l.add(x, 0, z);

            if (pe.hasProperty(ParticleEffect.ParticleProperty.COLORABLE)) {
                pe.display(color, l, players);
            } else {
                pe.display(0.0F, 0.0F, 0.0F, 1.0F, 1, l, players);
            }

            l.subtract(x, 0, z);

            t++;

            if (t == 6){
                for (int r = 0; r < 6; r++) {
                    drawCircle(l, pe, color, r);
                    Bukkit.getOnlinePlayers().forEach(p -> p.playSound(l, Sound.BLOCK_REDSTONE_TORCH_BURNOUT, 1F, 1F));

                    if (r == 6)
                        Bukkit.getOnlinePlayers().forEach(p -> p.playSound(l, Sound.ENTITY_GENERIC_EXPLODE, 1F, 1F));
                }
            }
        }
    }

    private void createFullPolygon(final int points, final Location l, final ParticleEffect pe, ParticleEffect.OrdinaryColor color){
        int t = 0;
        for(int iteration = 0; iteration < points; iteration++){
            double angle = 360.0 / points * iteration;
            double nextAngle = 360.0 / points * (iteration + 1);

            angle = Math.toRadians(angle);
            nextAngle = Math.toRadians(nextAngle);

            double x = Math.cos(angle);
            double z = Math.sin(angle);
            double x2 = Math.cos(nextAngle);
            double z2 = Math.sin(nextAngle);
            double deltaX = x2 - x;
            double deltaZ = z2 - z;
            double distance = Math.sqrt((deltaX - x) * (deltaX - x) + (deltaZ - z) * (deltaZ - z));

            for(double d = 0; d < distance - .1; d += .1){
                l.add(x + deltaX * d, 0, z + deltaZ * d);

                if (pe.hasProperty(ParticleEffect.ParticleProperty.COLORABLE)) {
                    pe.display(color, l, players);
                } else {
                    pe.display(0.0F, 0.0F, 0.0F, 1.0F, 1, l, players);
                }

                l.subtract(x + deltaX * d, 0, z + deltaZ * d);
            }

            t++;

            if (t == 6){
                for (int r = 0; r < 6; r++) {
                    drawCircle(l, pe, color, r);
                    Bukkit.getOnlinePlayers().forEach(p -> p.playSound(l, Sound.BLOCK_REDSTONE_TORCH_BURNOUT, 1F, 1F));

                    if (r == 6)
                        Bukkit.getOnlinePlayers().forEach(p -> p.playSound(l, Sound.ENTITY_GENERIC_EXPLODE, 1F, 1F));
                }
            }
        }
    }
}
