package com.cadiducho.fem.uhc.utils;

import com.cadiducho.fem.uhc.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class LocationUtil {

    public Main plugin;

    public LocationUtil(Main plugin) {
        this.plugin = plugin;
    }

    public String locationToString(Location l) {
        return String.valueOf(new StringBuilder(String.valueOf(l.getWorld().getName())).append(":").append(l.getBlockX()).toString()) + ":" + String.valueOf(l.getBlockY()) + ":" + String.valueOf(l.getBlockZ());
    }

    public Location stringToLoc(String s) {
        Location l = null;
        try {
            World world = Bukkit.getWorld(s.split(":")[0]);
            Double x = Double.parseDouble(s.split(":")[1]);
            Double y = Double.parseDouble(s.split(":")[2]);
            Double z = Double.parseDouble(s.split(":")[3]);

            l = new Location(world, x + 0.5, y.doubleValue(), z + 0.5);
        } catch (Exception ex) {
            
        }
        return l;
    }
}
