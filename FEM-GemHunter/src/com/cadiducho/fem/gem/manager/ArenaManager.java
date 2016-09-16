package com.cadiducho.fem.gem.manager;

import com.cadiducho.fem.gem.GemHunters;
import com.cadiducho.fem.core.util.Metodos;
import java.util.logging.Level;
import lombok.Getter;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;

public class ArenaManager {

    private final GemHunters plugin;
    private int minPlayers = 2;
    private int maxPlayers = 16;
    @Getter private final Location pos1;
    @Getter private final Location pos2;
    @Getter private final Material typeGema;
    @Getter private final Location lobby;
    
    public ArenaManager(GemHunters instance) {
        plugin = instance;
        minPlayers = plugin.getConfig().getInt("GemHunters.Arena.usersMin");
        maxPlayers = plugin.getConfig().getInt("GemHunters.Arena.usersMax");
        
        pos1 = Metodos.stringToLocation(plugin.getConfig().getString("GemHunters.Arena.borderPos1"));
        pos2 = Metodos.stringToLocation(plugin.getConfig().getString("GemHunters.Arena.borderPos2"));
        typeGema = Material.getMaterial(plugin.getConfig().getString("GemHunters.material"));
        lobby = Metodos.stringToLocation(plugin.getConfig().getString("GemHunters.Arena.Lobby"));
    }

    public void prepareWorld(World w) {
        w.setPVP(true);
        w.setGameRuleValue("naturalRegeneration", "false");
        w.setGameRuleValue("doDaylightCycle", "false");
        w.setStorm(false);
        w.setDifficulty(Difficulty.EASY);
        w.setTime(6000);    
        w.getLivingEntities().stream()
                    .filter(e -> !e.getType().equals(EntityType.PLAYER))
                    .forEach(e -> e.damage(e.getMaxHealth()));
        
        muro(w, true);
        
        plugin.getLogger().log(Level.INFO, "Mundo para {0}/{1} preparado", new Object[]{minPlayers, maxPlayers});
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public int getMinPlayers() {
        return minPlayers;
    }
    
    public void muro(World w, boolean poner){  
        int minx = Math.min(pos1.getBlockX(), pos2.getBlockX());
        int miny = Math.min(pos1.getBlockY(), pos2.getBlockY());
        int minz = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
        int maxx = Math.max(pos1.getBlockX(), pos2.getBlockX());
        int maxy = Math.max(pos1.getBlockY(), pos2.getBlockY());
        int maxz = Math.max(pos1.getBlockZ(), pos2.getBlockZ());
        int z;
        Block currentBlock;
        for (int x2 = minx; x2 <= maxx; x2++){
            for (int y = miny; y <= maxy; y++){
                for (z = minz; z <= maxz; z++){
                    currentBlock = w.getBlockAt(x2, y, z);/*
                    if (currentBlock.getType() == Material.BEDROCK){ //Te puesto cambie de aire a un bloque de oro, pero cambialo tu como quieras 
                        currentBlock.setType(Material.AIR);
                    } else {
                        currentBlock.setType(Material.BEDROCK);
                    }*/
                    if (!poner) currentBlock.setType(Material.AIR);
                    if (poner) currentBlock.setType(Material.BEDROCK);
                }
            }
        }
    }
}
