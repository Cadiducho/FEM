package com.cadiducho.fem.pro;

import com.cadiducho.fem.core.util.Metodos;
import com.cadiducho.fem.pro.utils.CuboidRegion;
import com.cadiducho.fem.pro.utils.ProType;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.block.Block;

public class ProPlot {

    @Getter private int id;
    @Getter private ProType proType;
    @Getter private CuboidRegion cuboidRegion;
    @Getter private ProPlayer proPlayer;
    @Getter private Location l;

    private static final Protections pro = Protections.getInstance();

    public ProPlot(Location l, ProType proType, ProPlayer proPlayer){
        this.l = l;
        this.proType = proType;
        this.proPlayer = proPlayer;
    }

    public ProPlot(int id){
        this.id = id;
    }

    public void generateArea(){
        Block b1 = l.getWorld().getBlockAt((int)l.getX() + proType.getArea(), (int)(l.getY() - 1), (int)l.getZ() + proType.getArea());
        Block b2 = l.getWorld().getBlockAt((int)l.getX() - proType.getArea(), (int)(l.getY() - 1), (int)l.getZ() - proType.getArea());

        CuboidRegion c = new CuboidRegion(b1, b2, (int)l.getY());
        this.cuboidRegion = c;

        pro.getFiles().getAreas().set("area_" + id + ".loc", cuboidRegion.toString());
        pro.getFiles().getAreas().set("area_" + id + ".dueño", proPlayer.getName());
        pro.getFiles().getAreas().set("area_" + id + ".tipo", proType.toString());
        pro.getFiles().saveFiles();
    }

    public CuboidRegion getCuboidRegion(){
        Block b1 = stringCuboidBlockToLocation(pro.getFiles().getAreas().getString("area_" + id + ".loc"), 0).getBlock();
        Block b2 = stringCuboidBlockToLocation(pro.getFiles().getAreas().getString("area_" + id + ".loc"), 1).getBlock();
        return new CuboidRegion(b1, b2, b1.getY() + 1);
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
}
