package com.cadiducho.fem.uhc.utils;

import com.cadiducho.fem.uhc.Main;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class Transformador {

    public Main plugin;

    public Transformador(Main plugin) {
        this.plugin = plugin;
    }

    public String getCentro(Player player) {
        if (player.getWorld().getEnvironment() == World.Environment.NORMAL) {
            Location l = plugin.wm.getArenaWorld().getSpawnLocation().clone();
            l.setY(player.getLocation().getY());
            String CENTRO = " " + Math.floor(player.getLocation().distance(l));
            return CENTRO;
        } else {
            Location l = plugin.wm.getNetherWorld().getSpawnLocation().clone();
            l.setY(player.getLocation().getY());
            String CENTRO = " " + Math.floor(player.getLocation().distance(l));
            return CENTRO;
        }
    }
    
    public String tiempoPVE(int pve) {
        int remaingSec = pve % 60;
        int remaingMin = (pve - remaingSec) / 60;
        String remaingTimeFormated = remaingMin + ":" + remaingSec;
        if (remaingMin == 0) {
            remaingTimeFormated = "" + remaingSec;
        } else if (remaingSec < 10) {
            remaingTimeFormated = remaingMin + ":0" + remaingSec;
        }
        return remaingTimeFormated;
    }

    public String tiempoPVP(int pvp) {
        int remaingSec = pvp % 60;
        int remaingMin = (pvp - remaingSec) / 60;
        String remaingTimeFormated = remaingMin + ":" + remaingSec;
        if (remaingMin == 0) {
            remaingTimeFormated = "" + remaingSec;
        } else if (remaingSec < 10) {
            remaingTimeFormated = remaingMin + ":0" + remaingSec;
        }
        return remaingTimeFormated;
    }

}
