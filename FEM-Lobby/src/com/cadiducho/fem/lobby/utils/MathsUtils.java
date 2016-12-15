package com.cadiducho.fem.lobby.utils;

import com.cadiducho.fem.core.particles.ParticleEffect;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class MathsUtils {

    private Player[] players = Bukkit.getOnlinePlayers().toArray(new Player[Bukkit.getOnlinePlayers().size()]);

    public void drawParticles(final Player p, final ParticleType pt){
        Location l = p.getLocation();

        switch (pt.getPid()){
            case NONE:
                drawFollowParticles(l, pt.getPe(), pt.getColor());
                break;
            case HALO:
                drawCircle(l, pt.getPe(), pt.getColor(), 2);
                break;
            case SPIRAL:
                drawSpiral(l, pt.getPe(), pt.getColor());
                break;
            case SPHERE:
                drawSphere(l, pt.getPe(), pt.getColor());
                break;
            case WINGS:
                drawWings(p.getEyeLocation(), pt.getPe(), pt.getColor());
                break;
            case POLYGON:
                drawPolygon(pt.getPoints(), p.getEyeLocation(), pt.getPe(), pt.getColor());
                break;
            case POLYGON_FULL:
                drawFullPolygon(pt.getPoints(), p.getEyeLocation(), pt.getPe(), pt.getColor());
                break;
            case SHIELD:
                drawShield(l, pt.getPe(), pt.getColor());
                break;
            case TORNADO:
                drawTornado(l, pt.getPe(), pt.getColor());
                break;
        }
    }

    private void drawFollowParticles(final Location l, final ParticleEffect pe, final ParticleEffect.OrdinaryColor color){
        if (pe.hasProperty(ParticleEffect.ParticleProperty.COLORABLE)) {
            pe.display(color, l, players);
            return;
        }
        pe.display((long)(l.getX()), (long)(l.getY()), (long)(l.getZ()), 0, 1, l, players);
    }

    private void drawSpiral(final Location l, final ParticleEffect pe, final ParticleEffect.OrdinaryColor color){
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

    private void drawCircle(final Location l, final ParticleEffect pe, final ParticleEffect.OrdinaryColor color, int radius) {
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

    private void drawShield(final Location l, final ParticleEffect pe, final ParticleEffect.OrdinaryColor color){
        int particles = 120;
        int particlesPerIteration = 8;
        float size = 1.0F;
        float xFactor = 1.4F;
        float yFactor = 1.4F;
        float zFactor = 1.4F;
        float yOffset = 1.0F;
        double xRotation = 0.0D;
        double yRotation = 0.0D;
        double zRotation = 0.0D;
        int step = 0;
        Vector v = new Vector();

        for (int i = 0; i < particlesPerIteration; i++) {
            step += 1;

            float f1 = 3.1415927F / particles * step;
            float f2 = (float) (Math.sin(f1) * size);
            float f3 = f2 * 3.1415927F * f1;

            v.setX(xFactor * f2 * Math.cos(f3));
            v.setZ(zFactor * f2 * Math.sin(f3));
            v.setY(yFactor * Math.cos(f1) + yOffset);

            Utils.rotateVector(v, xRotation, yRotation, zRotation);

            if (pe.hasProperty(ParticleEffect.ParticleProperty.COLORABLE)) {
                pe.display(color, l, players);
            } else {
                pe.display(0.0F, 0.0F, 0.0F, 1.0F, 1, l, players);
            }
            l.subtract(l);
        }
    }

    private void drawTornado(final Location l, final ParticleEffect pe, final ParticleEffect.OrdinaryColor color){
        float lineNumber = 3.0F;
        float j = 0.0F;
        float radius = 0.3F;
        float heightEcart = 0.01F;

        l.setY(l.getY() + 0.5D);

        for (int i = 0; i < lineNumber; i++) {
            l.add(Math.cos(j) * radius, j * heightEcart,Math.sin(j) * radius);

            if (pe.hasProperty(ParticleEffect.ParticleProperty.COLORABLE)) {
                pe.display(color, l, players);
            } else {
                pe.display(0.0F, 0.0F, 0.0F, 1.0F, 1, l, players);
            }
        }
        j += 0.2F;
        if (j > 50.0F) {
            j = 0.0F;
        }
    }

    private void drawSphere(final Location l, final ParticleEffect pe, final ParticleEffect.OrdinaryColor color){
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

    private void drawPolygon(final int points, final Location l, final ParticleEffect pe, final ParticleEffect.OrdinaryColor color){
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

    private void drawFullPolygon(final int points, final Location l, final ParticleEffect pe, final ParticleEffect.OrdinaryColor color){
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

    //Location = EyeLocation
    public void drawWings(final Location l, final ParticleEffect pe, final ParticleEffect.OrdinaryColor color){
        Location loc = l.subtract(0.0D, 0.3D, 0.0D);
        loc.setPitch(0.0F);
        loc.setYaw(l.getYaw());
        Vector v1 = loc.getDirection().normalize().multiply(-0.3D);
        v1.setY(0);
        loc.add(v1);
        for (double i = -10.0D; i < 6.2D; i += 0.2) {
            double var = Math.sin(i / 12.0D);
            double x = Math.sin(i)* (Math.exp(Math.cos(i)) - 2.0D * Math.cos(4.0D * i) - Math.pow(var, 5.0D)) / 2.0D;
            double z = Math.cos(i)* (Math.exp(Math.cos(i)) - 2.0D * Math.cos(4.0D * i) - Math.pow(var, 5.0D)) / 2.0D;
            Vector v = new Vector(-x, 0.0D, -z);
            Utils.rotateAroundAxisX(v, (loc.getPitch() + 90.0F) * 0.017453292F);
            Utils.rotateAroundAxisY(v, -loc.getYaw() * 0.017453292F);

            if (pe.hasProperty(ParticleEffect.ParticleProperty.COLORABLE)) {
                pe.display(color, l, players);
            } else {
                pe.display(0.0F, 0.0F, 0.0F, 1.0F, 1, l, players);
            }
        }
    }
}
