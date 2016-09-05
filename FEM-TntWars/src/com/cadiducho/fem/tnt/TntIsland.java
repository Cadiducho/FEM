package com.cadiducho.fem.tnt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public class TntIsland {

    @Getter ArrayList<Location> matblocks = new ArrayList<>();
    @Getter @Setter Block bedrockCore;
    @Getter @Setter Player owner;
    @Getter @Setter Location spawn;
    @Getter @Setter String id;
    @Getter @Setter Boolean destroyed = false;
    @Getter @Setter Integer destroyTaskId;
    @Getter HashMap<Location, Material> generadores = new HashMap<>();
    
    public void addBlock(Block block) {
        matblocks.add(block.getLocation());
    }

    public void addBlock(Location block) {
        matblocks.add(block);
    }
    
    public void addGenerator(Location block, Material mat) {
        generadores.put(block, mat);
    }
    
    public void explode() {
        getBedrockCore().getWorld().createExplosion(getBedrockCore().getLocation(), 5F);
        getBedrockCore().getRelative(BlockFace.UP).setType(Material.AIR);
        setDestroyed(true);
        
        Random rand = new Random();
        matblocks.stream()
                .filter(loc -> rand.nextInt(3) == 1) // 1/3 de probabilidad
                .forEach(loc -> loc.getBlock().setType(Material.AIR)); //Simular destruccion
    }

    public static TntIsland getIsland(UUID id) {
        for (TntIsland i :TntWars.getInstance().getAm().getIslas())
            if (i.getOwner().getUniqueId() == id) return i;
        
        return null;
    }
}
