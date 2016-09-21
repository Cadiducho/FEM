package com.cadiducho.fem.tnt;

import com.cadiducho.fem.core.util.Metodos;
import com.cadiducho.fem.tnt.manager.GameState;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class Generador {
    
    public enum GenType {
        IRON, GOLD, DIAMOND, TNT
    }
    
    @Getter @Setter GenType type;
    @Getter @Setter Location loc;
    @Getter Integer level = 1;
    
    private Generador genAnnon;

    public void init() {
        if (type != GenType.TNT) { //Tnt va sin letrero
            Sign s = (Sign) getSign().getBlock().getState();
            s.setLine(0, "§lGenerador");
            s.setLine(1, "§c" + getName());
            s.setLine(2, "Nivel " + getLevel());
            s.setLine(3, "§7Click Derecho");
            s.update();
        }
        
        genAnnon = this;
        TntWars.getInstance().getServer().getScheduler().runTaskLater(TntWars.getInstance(), new Runnable() {
            @Override
            public void run() {
                if (GameState.state == GameState.GAME) {
                    if (genAnnon.getLevel() != 0) {
                        genAnnon.getSign().getWorld().dropItemNaturally(genAnnon.getItemSpawn(), genAnnon.getItem()).setVelocity(new Vector(0, 0, 0));
                    }
                }
                TntWars.getInstance().getServer().getScheduler().runTaskLater(TntWars.getInstance(), this, (long) (20 * genAnnon.timeForTask()));
            }
        }, 20L);
    }
    
    public final String getName() {
        switch (type) {
            case IRON: return "Hierro";
            case DIAMOND: return "Diamante";
            case GOLD: return "Oro";
            case TNT: return "Tnt";
        }
        return "";
    }
    
    public Location getSign() {
        Location sign = loc.clone();
        return sign.add(0, 1, 0);
    }
    
    public Location getItemSpawn() {
        return Metodos.centre(genAnnon.getSign());
    }
    
    public void setLevel(int i) {
        level = i;
        Sign s = (Sign) getSign().getBlock().getState();
        s.setLine(2, "Nivel " + i);
        s.update();
    }
    
    public ItemStack getItem() {
        switch (type) {
            case TNT: return new ItemStack(Material.TNT);
            case IRON: return new ItemStack(Material.IRON_INGOT);
            case DIAMOND: return new ItemStack(Material.DIAMOND);
            case GOLD: return new ItemStack(Material.GOLD_INGOT);
        }
        return null;
    }
    
    public Double timeForTask() {
        switch (type) {
            case TNT: 
                return 15.0;
            case IRON:
                switch (level) {
                    case 1: return 1.0;
                    case 2: return 0.75;
                    case 3: return 0.5;
                    case 4: return 0.25;
                }
           case GOLD:
               switch (level) {
                   case 1: return 4.0;
                   case 2: return 3.5;
                   case 3: return 2.0;
                   case 4: return 1.5;
               }
            case DIAMOND:
                switch (level) {
                    case 1: return 10.0;
                    case 2: return 8.0;
                    case 3: return 6.0;
                    case 4: return 4.0;
                }
            default: return 1.0;
        }      
    }
    
    public static Generador getGenerador(Location loc) {
        for (Generador gen : TntWars.getInstance().getAm().getGeneradores()) {
            if (gen.getLoc().getBlockX() == loc.getBlockX() &&
                    gen.getLoc().getBlockY() == loc.getBlockY() &&
                    gen.getLoc().getBlockZ() == loc.getBlockZ()) {
                return gen;
            }
        }

        return null;
    }
}
