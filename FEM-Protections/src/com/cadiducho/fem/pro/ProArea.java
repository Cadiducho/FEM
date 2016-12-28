package com.cadiducho.fem.pro;

import com.cadiducho.fem.core.particles.ParticleEffect;
import com.cadiducho.fem.core.util.Metodos;
import com.cadiducho.fem.pro.utils.CuboidRegion;
import com.cadiducho.fem.pro.utils.ProType;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProArea {

    @Getter private int id;
    @Getter private ProType proType;
    @Getter private CuboidRegion cuboidRegion;
    @Getter private ProPlayer proPlayer;
    @Getter private Location location;

    private static final Protections pro = Protections.getInstance();

    public ProArea(Location l, ProType proType, ProPlayer proPlayer){
        this.location = l;
        this.proType = proType;
        this.proPlayer = proPlayer;
    }

    public ProArea(int id){
        this.id = id;
    }

    public void generateArea(){
        generateCuboidRegion();
        BukkitTask bt = Bukkit.getScheduler().runTaskTimer(pro, ()-> showArea(), 0l, 1l);

        pro.getFiles().getAreas().set("area_" + id + ".loc", cuboidRegion.toString());
        pro.getFiles().getAreas().set("area_" + id + ".dueño", proPlayer.getUuid().toString());
        pro.getFiles().getAreas().set("area_" + id + ".tipo", proType.toString());
        pro.getFiles().saveFiles();

        Bukkit.getScheduler().runTaskLater(pro, ()-> bt.cancel(), 100);
    }

    public void generateCuboidRegion(){
        Block b1 = location.getWorld().getBlockAt((int) location.getX() + proType.getArea(), (int)(location.getY() - 1), (int) location.getZ() + proType.getArea());
        Block b2 = location.getWorld().getBlockAt((int) location.getX() - proType.getArea(), (int)(location.getY() - 1), (int) location.getZ() - proType.getArea());

        this.cuboidRegion = new CuboidRegion(b1, b2, (int) location.getY());
    }

    public void showArea(){
        getSquareLocations().forEach(l -> ParticleEffect.REDSTONE.display(new ParticleEffect.OrdinaryColor(255, 0, 255), l, proPlayer.getPlayer()));
    }

    public void putSlabs(){
        getSquareLocations().forEach(l -> l.getWorld().getBlockAt(l.subtract(0, 5, 0)).getRelative(BlockFace.UP).setType(Material.STONE_SLAB2));
    }

    public CuboidRegion getCuboidRegion(){
        Block b1 = stringCuboidBlockToLocation(pro.getFiles().getAreas().getString("area_" + id + ".loc"), 0).getBlock();
        Block b2 = stringCuboidBlockToLocation(pro.getFiles().getAreas().getString("area_" + id + ".loc"), 1).getBlock();
        return new CuboidRegion(b1, b2, b1.getY());
    }

    public ProPlayer getOwner(){
        return new ProPlayer(UUID.fromString(pro.getFiles().getAreas().getString("area_" + id + ".dueño")));
    }

    public ProType getProType(){
        return ProType.valueOf(pro.getFiles().getAreas().getString("area_" + id + ".tipo"));
    }

    public boolean isInsideArea(){
        return getCuboidRegion().contains(proPlayer.getPlayer().getLocation().getBlock());
    }

    public Location stringCuboidBlockToLocation(String string, int block){
        if (string == null) return null;
        return Metodos.stringToLocation(string.split(";")[block]);
    }

    private boolean hit = false;
    public boolean hitOtherArena(){
        generateCuboidRegion();

        getAllRegions().forEach(r -> r.toArray().forEach(b -> {
            if (cuboidRegion.toArray().contains(b)) {
                hit = true;
            }
        }));
        return hit;
    }

    //Utils
    private List<Location> getSquareLocations(){
        List<Location> locations = new ArrayList<>();

        for (int g = 0; g < proType.getArea(); g++){
            locations.add(new Location(cuboidRegion.getCorner1().getWorld(), cuboidRegion.getCorner1().getX() - g, cuboidRegion.getCorner1().getY() + (g / 2), cuboidRegion.getCorner1().getZ()));
        }
        for (int g = 0; g < proType.getArea(); g++){
            locations.add(new Location(cuboidRegion.getCorner1().getWorld(), cuboidRegion.getCorner1().getX(), cuboidRegion.getCorner1().getY() + (g / 2), cuboidRegion.getCorner1().getZ() - g));
        }
        for (int g = 0; g < proType.getArea(); g++){
            locations.add(new Location(cuboidRegion.getCorner2().getWorld(), cuboidRegion.getCorner2().getX() + g, cuboidRegion.getCorner2().getY() + (g / 2), cuboidRegion.getCorner2().getZ()));
        }
        for (int g = 0; g < proType.getArea(); g++){
            locations.add(new Location(cuboidRegion.getCorner2().getWorld(), cuboidRegion.getCorner2().getX(), cuboidRegion.getCorner2().getY() + (g / 2), cuboidRegion.getCorner2().getZ() + g));
        }
        return locations;
    }

    public List<CuboidRegion> getAllRegions(){
        List<CuboidRegion> regions = new ArrayList<>();
        for (int x = 0; x < pro.getFiles().getCurrentID(); x++){
            ProArea area = new ProArea(x);
            regions.add(area.getCuboidRegion());
        }
        return regions;
    }
}
