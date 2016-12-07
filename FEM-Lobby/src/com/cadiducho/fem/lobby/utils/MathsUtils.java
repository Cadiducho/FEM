package com.cadiducho.fem.lobby.utils;

import com.cadiducho.fem.core.util.Metodos;
import com.cadiducho.fem.lobby.Lobby;
import com.cadiducho.fem.lobby.particles.ParticleEffect;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class MathsUtils {

    private HashMap<UUID, Integer> effects = new HashMap<>();

    public void drawSpiral(final Player p, final ParticleEffect pe){
        final Location l = p.getLocation();
        final int radius = 2;

        effects.put(p.getUniqueId(), Bukkit.getScheduler().runTaskTimer(Lobby.getInstance(), ()-> {
            for (double y = 0; y <= 5; y+=0.05) {
                double x = radius * Math.cos(y);
                double z = radius * Math.sin(y);

                pe.display( (long)(l.getX() + x), (long)(l.getY() + y), (long)(l.getZ() + z), 0, 1, l, Bukkit.getOnlinePlayers().toArray(new Player[Bukkit.getOnlinePlayers().size()]));
            }
        }, 1, 20).getTaskId());
    }

    public void drawCircle(final Player p, final ParticleEffect pe, final Color color){
        final Location l = p.getLocation();
        final World w = l.getWorld();

        final int amount = 8;
        final int radius = 2;
        final double increment = (2 * Math.PI) / amount;

        effects.put(p.getUniqueId(), Bukkit.getScheduler().runTaskTimer(Lobby.getInstance(), ()-> {
            for (int i = 0;i < amount; i++){
                double angle = i * increment;
                double x = l.getX() + (radius * Math.cos(angle));
                double z = l.getZ() + (radius * Math.sin(angle));

                pe.display( (long)(l.getX() + x), (long)(l.getY()), (long)(l.getZ() + z), 0, 1, l, Bukkit.getOnlinePlayers().toArray(new Player[Bukkit.getOnlinePlayers().size()]));
            }
        }, 1, 20).getTaskId());
    }

    public void stopEffects(final Player p){
        UUID uuid = p.getUniqueId();
        if (checkIfEffectEnabled(uuid)){
            Bukkit.getScheduler().cancelTask(effects.get(uuid));
            effects.remove(uuid);
            return;
        }
        p.sendMessage(Metodos.colorizar("&cNo tienes ningún efecto activado"));
    }

    private boolean checkIfEffectEnabled(final UUID uuid){
        return effects.containsKey(uuid);
    }
}
