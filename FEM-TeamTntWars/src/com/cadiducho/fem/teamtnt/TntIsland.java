package com.cadiducho.fem.teamtnt;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class TntIsland {

    @Getter ArrayList<Location> blocks = new ArrayList<>();
    @Getter @Setter Block bedrockCore;
    @Getter @Setter Team team;
    @Getter @Setter Location spawn;
    @Getter @Setter String id;
    @Getter @Setter Boolean destroyed = false;
    @Getter @Setter Integer destroyTaskId;
    @Getter @Setter ChatColor color;
    @Getter HashMap<Location, Material> generadores = new HashMap<>();
    
    public void addBlock(Block block) {
        blocks.add(block.getLocation());
    }

    public void addBlock(Location block) {
        blocks.add(block);
    }
    
    public void addGenerator(Location block, Material mat) {
        generadores.put(block, mat);
    }
    
    public void explode() {
        getBedrockCore().getWorld().createExplosion(getBedrockCore().getLocation(), 5F);
        getBedrockCore().getRelative(BlockFace.UP).setType(Material.AIR);
        setDestroyed(true);
        
        Random rand = new Random();
        blocks.stream()
                .filter(loc -> rand.nextInt(9) < 7) // 7/10 de probabilidad
                .forEach(loc -> loc.getBlock().setType(Material.AIR)); //Simular destruccion
    }
    
    public void destroyCapsule() {
        //Eliminar todos los barrier invisibles dentro de la isla, dejar caer al jugador
        blocks.stream()
                .filter(loc -> loc.getBlock().getType() == Material.BARRIER)
                .forEach(loc -> loc.getBlock().setType(Material.AIR));
    }

    public static TntIsland getIsland(Team team) {
        if (TeamTntWars.getInstance().getAm().getIslas() != null || !TeamTntWars.getInstance().getAm().getIslas().isEmpty()) {
            for (TntIsland i : TeamTntWars.getInstance().getAm().getIslas()) {
                if (i.getTeam().equals(team)) {
                    return i;
                }
            }
        }
        return null;
    }
    
    public static TntIsland getIsland(String id) {
        if (TeamTntWars.getInstance().getAm().getIslas() != null || !TeamTntWars.getInstance().getAm().getIslas().isEmpty()) {
            for (TntIsland i : TeamTntWars.getInstance().getAm().getIslas()) {
                if (i.getId() == null ? id == null : i.getId().equals(id)) {
                    return i;
                }
            }
        }
        return null;
    }
}
