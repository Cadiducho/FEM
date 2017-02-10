package com.cadiducho.fem.lobby.utils;

import com.cadiducho.fem.core.api.FEMUser;
import lombok.Getter;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.inventivetalent.particle.ParticleEffect;

import java.util.Collection;
import java.util.HashMap;

public class MathsUtils {

    @Getter private HashMap<FEMUser, BukkitRunnable> particles;

    private Collection<? extends Player> players = Bukkit.getOnlinePlayers();

    private World world;
    private double x;
    private double y;
    private double z;

    public MathsUtils(){
        particles = new HashMap<>();
    }

    public void drawParticles(final Player p, final ParticleType pt){
        world = p.getWorld();
        x = p.getLocation().getX();
        y = p.getLocation().getY();
        z = p.getLocation().getZ();

        switch (pt.getPid()){
            case NONE:
                drawFollowParticles(pt.getPe(), pt.getColor());
                break;
            case HALO:
                drawCircle(pt.getPe(), pt.getColor(), 2);
                break;
            case SPIRAL:
                drawSpiral(pt.getPe(), pt.getColor());
                break;
            case SPHERE:
                drawSphere(pt.getPe(), pt.getColor());
                break;
            case WINGS:
                x = p.getEyeLocation().getX();
                y = p.getEyeLocation().getY();
                z = p.getEyeLocation().getZ();
                drawWings(pt.getPe(), pt.getColor());
                break;
            case POLYGON:
                x = p.getEyeLocation().getX();
                y = p.getEyeLocation().getY();
                z = p.getEyeLocation().getZ();
                drawPolygon(pt.getPoints(), pt.getPe(), pt.getColor());
                break;
            case POLYGON_FULL:
                x = p.getEyeLocation().getX();
                y = p.getEyeLocation().getY();
                z = p.getEyeLocation().getZ();
                drawFullPolygon(pt.getPoints(), pt.getPe(), pt.getColor());
                break;
            case SHIELD:
                drawShield(pt.getPe(), pt.getColor());
                break;
            case TORNADO:
                drawTornado(pt.getPe(), pt.getColor());
                break;
        }
    }

    private void drawFollowParticles(final ParticleEffect pe, final Color color){
        pe.sendColor(players, x, y, z, color);
    }

    private void drawSpiral(final ParticleEffect pe, final Color color){
        final int radius = 2;

        for (double y = 0; y <= 5; y+=0.05) {
            double x = radius * Math.cos(y);
            double z = radius * Math.sin(y);

            pe.sendColor(players, x, y, z, color);
        }
    }

    private void drawCircle(final ParticleEffect pe, final Color color, int radius) {
        final int amount = 8;
        final double increment = (2 * Math.PI) / amount;

        for (int i = 0; i < amount; i++) {
            double angle = i * increment;
            double x = this.x + (radius * Math.cos(angle));
            double z = this.z + (radius * Math.sin(angle));

            pe.sendColor(players, x, y, z, color);
        }
    }

    private void drawShield(final ParticleEffect pe, final Color color){
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

            pe.sendColor(players, x, y, z, color);
        }
    }

    private void drawTornado(final ParticleEffect pe, final Color color){
        float lineNumber = 3.0F;
        float j = 0.0F;
        float radius = 0.3F;
        float heightEcart = 0.01F;
        Location l = new Location(world, x, y, z);

        l.setY(l.getY() + 0.5D);

        for (int i = 0; i < lineNumber; i++) {
            l.add(Math.cos(j) * radius, j * heightEcart,Math.sin(j) * radius);

            pe.sendColor(players, x, y, z, color);
        }
        j += 0.2F;
        if (j > 50.0F) {
            j = 0.0F;
        }
    }

    private void drawSphere(final ParticleEffect pe, final Color color){
        for(double i = 0; i <= Math.PI; i += Math.PI / 10){
            double radius = Math.sin(i);
            double y = Math.cos(i);
            for(double a = 0; a < Math.PI * 2; a += Math.PI / 10) {
                double x = Math.cos(a) * radius;
                double z = Math.sin(a) * radius;
                Location l = new Location(world, x, y, z);

                l.add(x, y, z);

                pe.sendColor(players, x, y, z, color);

                l.subtract(x, y, z);
            }
        }
    }

    private void drawPolygon(final int points, final ParticleEffect pe, final Color color){
        int t = 0;
        for(int iteration = 0; iteration < points; iteration++){
            double angle = 360.0 / points * iteration;

            angle = Math.toRadians(angle);

            double x = Math.cos(angle);
            double z = Math.sin(angle);

            Location l = new Location(world, this.x, y, this.z);

            l.add(x, 0, z);

            pe.sendColor(players, x, y, z, color);

            l.subtract(x, 0, z);

            t++;

            if (t == 6){
                for (int r = 0; r < 6; r++) {
                    drawCircle(pe, color, r);
                    Bukkit.getOnlinePlayers().forEach(p -> p.playSound(l, Sound.FUSE, 1F, 1F));

                    if (r == 6)
                        Bukkit.getOnlinePlayers().forEach(p -> p.playSound(l, Sound.EXPLODE, 1F, 1F));
                }
            }
        }
    }

    private void drawFullPolygon(final int points, final ParticleEffect pe, final Color color){
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

            Location l = new Location(world, this.x, y, this.z);

            for(double d = 0; d < distance - .1; d += .1){
                l.add(x + deltaX * d, 0, z + deltaZ * d);

                pe.sendColor(players, x, y, z, color);

                l.subtract(x + deltaX * d, 0, z + deltaZ * d);
            }

            t++;

            if (t == 6){
                for (int r = 0; r < 6; r++) {
                    drawCircle(pe, color, r);
                    Bukkit.getOnlinePlayers().forEach(p -> p.playSound(l, Sound.FUSE, 1F, 1F));

                    if (r == 6)
                        Bukkit.getOnlinePlayers().forEach(p -> p.playSound(l, Sound.EXPLODE, 1F, 1F));
                }
            }
        }
    }

    //Location = EyeLocation
    public void drawWings(final ParticleEffect pe, final Color color){
        Location l = new Location(world, this.x, y, this.z);
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

            pe.sendColor(players, x, y, z, color);
        }
    }
}
