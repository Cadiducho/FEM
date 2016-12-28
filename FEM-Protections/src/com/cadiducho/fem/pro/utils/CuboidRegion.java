package com.cadiducho.fem.pro.utils;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public class CuboidRegion {

    @Getter private Block corner1;
    @Getter private Block corner2;
    @Getter private World world;

    @Getter private int y;

    public CuboidRegion(Block corner1, Block corner2, int y) {
        if (corner1.getWorld().equals(corner2.getWorld())) {
            this.corner1 = corner1;
            this.corner2 = corner2;
            this.y = y;
            world = corner1.getWorld();
        } else {
            throw new IllegalArgumentException("Corners must be in same World");
        }
    }

    public List<Block> toArray() {
        List<Block> result = new ArrayList();

        int minX = Math.min(corner1.getX(), corner2.getX());
        int minZ = Math.min(corner1.getZ(), corner2.getZ());
        int maxX = Math.max(corner1.getX(), corner2.getX());
        int maxZ = Math.max(corner1.getZ(), corner2.getZ());

        for (int x = minX; x <= maxX; x++) {
            for (int y = corner1.getY() - (this.y / 2); y <= corner1.getY() + (this.y / 2); y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    result.add(world.getBlockAt(new Location(world, x, y, z)));
                }
            }
        }
        return result;
    }

    public boolean contains(Block b) {
        return toArray().contains(b);
    }

    public Location getCenter() {
        int minX = Math.min(corner1.getX(), corner2.getX());
        int minZ = Math.min(corner1.getZ(), corner2.getZ());
        int maxX = Math.max(corner1.getX(), corner2.getX());
        int maxZ = Math.max(corner1.getZ(), corner2.getZ());

        return new Location(world, minX + (maxX - minX) / 2, y + 1, minZ + (maxZ - minZ) / 2);
    }

    @Override
    public String toString() {
        Location l = corner1.getLocation();
        String s = String.valueOf(new StringBuilder(String.valueOf(world.getName())).append("%").append(l.getBlockX()).toString()) + "%" + String.valueOf(l.getBlockY()) + "%" + String.valueOf(l.getBlockZ());
        Location l1 = corner2.getLocation();
        String s1 = String.valueOf(new StringBuilder(String.valueOf(world.getName())).append("%").append(l1.getBlockX()).toString()) + "%" + String.valueOf(l1.getBlockY()) + "%" + String.valueOf(l1.getBlockZ());
        String result = s + ";" + s1;
        return result;
    }
}
