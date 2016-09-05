package com.cadiducho.fem.ovejas.manager.util;

import lombok.Getter;
import org.bukkit.util.Vector;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

public class Area {

    @Getter private final Vector vec1;
    @Getter private final Vector vec2;
    @Getter private final World world;
    @Getter private final Location centro;

    public Area(Location pos1, Location pos2) {
        vec1 = new Vector(pos1.getBlockX(), 0, pos1.getBlockZ());
        vec2 = new Vector(pos2.getBlockX(), 0, pos2.getBlockZ());
        world = pos1.getWorld();
        int radio = Math.abs(pos1.getBlockX() - pos2.getBlockX()) / 2;
        centro = new Location(world, pos1.getBlockX() - radio, 0, pos2.getBlockZ() - radio);
    }

    public Area(Location cen, int radio) {
        centro = cen;
        Location pos1 = new Location(cen.getWorld(), cen.getBlockX() + radio, 0, cen.getBlockZ() + radio);
        Location pos2 = new Location(cen.getWorld(), cen.getBlockX() - radio, 0, cen.getBlockZ() - radio);

        vec1 = new Vector(pos1.getBlockX(), 0, pos1.getBlockZ());
        vec2 = new Vector(pos2.getBlockX(), 0, pos2.getBlockZ());
        world = pos1.getWorld();
        
        pos1.add(0, 10, 0).getBlock().setType(Material.DIAMOND_BLOCK);
        pos2.add(0, 10, 0).getBlock().setType(Material.GOLD_BLOCK);
        
        
    }

    public boolean isOnArea(Location loc) {
        Vector v = new Vector(loc.getBlockX(), 0, loc.getBlockZ());
        return v.isInAABB(Vector.getMinimum(vec1, vec2), Vector.getMaximum(vec1, vec2));
    }

}
