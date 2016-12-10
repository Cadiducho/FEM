package com.cadiducho.fem.skywars;

import java.util.ArrayList;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class SkyIsland {

    @Getter ArrayList<Location> blocks = new ArrayList<>();
    @Getter @Setter UUID owner;
    @Getter @Setter Location spawn;
    @Getter @Setter String id;
    
    public void addBlock(Block block) {
        blocks.add(block.getLocation());
    }

    public void addBlock(Location block) {
        blocks.add(block);
    }
    
    public void destroyCapsule() {
        //Eliminar todos los barrier invisibles dentro de la isla, dejar caer al jugador
        blocks.stream()
                .filter(loc -> loc.getBlock().getType() == Material.BARRIER)
                .forEach(loc -> loc.getBlock().setType(Material.AIR));
    }

    public static SkyIsland getIsland(UUID uuid) {
        if (SkyWars.getInstance().getAm().getIslas() != null || !SkyWars.getInstance().getAm().getIslas().isEmpty()) {
            for (SkyIsland i : SkyWars.getInstance().getAm().getIslas()) {
                if (i.getOwner() != null) {
                    if (i.getOwner() == uuid) {
                        return i;
                    }
                }
            }
        }
        return null;
    }
    
    public static SkyIsland getIsland(String id) {
        if (SkyWars.getInstance().getAm().getIslas() != null || !SkyWars.getInstance().getAm().getIslas().isEmpty()) {
            for (SkyIsland i : SkyWars.getInstance().getAm().getIslas()) {
                if (i.getId() == null ? id == null : i.getId().equals(id)) {
                    return i;
                }
            }
        }
        return null;
    }
}
