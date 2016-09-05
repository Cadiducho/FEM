package com.cadiducho.fem.color;

import java.util.ArrayList;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.block.Block;

public class DyeMiniArea {

    ArrayList<Location> matblocks = new ArrayList<>();
    public DyeColor color = DyeColor.GRAY;

    public void addBlock(Block block) {
        matblocks.add(block.getLocation());
    }

    public void addBlock(Location block) {
        matblocks.add(block);
    }

    public ArrayList<Location> getBlocks() {
        return this.matblocks;
    }

    public void setColor(DyeColor color) {
        this.color = color;
        for (Location loc : this.matblocks) {
            loc.getBlock().setData(color.getWoolData());
        }
    }
}
