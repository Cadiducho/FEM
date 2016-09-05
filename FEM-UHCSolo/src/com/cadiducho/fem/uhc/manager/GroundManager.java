package com.cadiducho.fem.uhc.manager;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.cadiducho.fem.uhc.Main;
import org.bukkit.Location;

public class GroundManager {

    public Main plugin;

    public GroundManager(Main plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("deprecation")
    public void clear(Location loc) {
        ItemStack item = new ItemStack(Material.AIR);
        int a = loc.getBlockX();
        int b = loc.getBlockY();
        int c = loc.getBlockZ();
        //CENTRO Y ARRIBA
        loc.getWorld().getBlockAt(a, b - 1, c).setType(item.getType());
        loc.getWorld().getBlockAt(a, b + 3, c).setType(item.getType());

        //BORDE
        loc.getWorld().getBlockAt(a + 1, b - 1, c).setType(item.getType());
        loc.getWorld().getBlockAt(a, b - 1, c + 1).setType(item.getType());
        loc.getWorld().getBlockAt(a + 1, b - 1, c + 1).setType(item.getType());
        loc.getWorld().getBlockAt(a, b - 1, c - 1).setType(item.getType());
        loc.getWorld().getBlockAt(a - 1, b - 1, c).setType(item.getType());
        loc.getWorld().getBlockAt(a - 1, b - 1, c - 1).setType(item.getType());
        loc.getWorld().getBlockAt(a + 1, b - 1, c - 1).setType(item.getType());
        loc.getWorld().getBlockAt(a - 1, b - 1, c + 1).setType(item.getType());
        loc.getWorld().getBlockAt(a - 1, b - 1, c - 1).setType(item.getType());

        //ALREDEDOR
        for (int i = 0; i < 4; i++) {
            loc.getWorld().getBlockAt(a + 1, b + i, c).setType(item.getType());
            loc.getWorld().getBlockAt(a, b + i, c + 1).setType(item.getType());
            loc.getWorld().getBlockAt(a + 1, b + i, c + 1).setType(item.getType());
            loc.getWorld().getBlockAt(a, b + i, c - 1).setType(item.getType());
            loc.getWorld().getBlockAt(a - 1, b + i, c).setType(item.getType());
            loc.getWorld().getBlockAt(a - 1, b + i, c - 1).setType(item.getType());
            loc.getWorld().getBlockAt(a + 1, b + i, c - 1).setType(item.getType());
            loc.getWorld().getBlockAt(a - 1, b + i, c + 1).setType(item.getType());
            loc.getWorld().getBlockAt(a - 1, b + i, c - 1).setType(item.getType());
        }
        loc.getWorld().getBlockAt(a + 1, b - 1, c).setType(item.getType());
        loc.getWorld().getBlockAt(a, b - 1, c + 1).setType(item.getType());
        loc.getWorld().getBlockAt(a + 1, b - 1, c + 1).setType(item.getType());
        loc.getWorld().getBlockAt(a, b - 1, c - 1).setType(item.getType());
        loc.getWorld().getBlockAt(a - 1, b - 1, c).setType(item.getType());
        loc.getWorld().getBlockAt(a - 1, b - 1, c - 1).setType(item.getType());
        loc.getWorld().getBlockAt(a + 1, b - 1, c - 1).setType(item.getType());
        loc.getWorld().getBlockAt(a - 1, b - 1, c + 1).setType(item.getType());
        loc.getWorld().getBlockAt(a - 1, b - 1, c - 1).setType(item.getType());
    }

    
    public void setBuildGround(Location loc) {
        Random rand = new Random();
        int randomint = rand.nextInt(15);
        switch (randomint) {
            case 1:
                buildGround(loc, (byte) randomint);
                break;
            case 2:
                buildGround(loc, (byte) randomint);
                break;
            case 3:
                buildGround(loc, (byte) randomint);
                break;
            case 4:
                buildGround(loc, (byte) randomint);
                break;
            case 5:
                buildGround(loc, (byte) randomint);
                break;
            case 6:
                buildGround(loc, (byte) randomint);
                break;
            case 8:
                buildGround(loc, (byte) randomint);
                break;
            case 9:
                buildGround(loc, (byte) randomint);
                break;
            case 10:
                buildGround(loc, (byte) randomint);
                break;
            case 11:
                buildGround(loc, (byte) randomint);
                break;
            case 12:
                buildGround(loc, (byte) randomint);
                break;
            case 13:
                buildGround(loc, (byte) randomint);
                break;
            case 14:
                buildGround(loc, (byte) randomint);
                break;
            case 7:
            default:
                buildGround(loc, (byte) 0);
        }
    }

    public enum cageType {
        VERDE, MORADO, NARANJO, AZUL, ROJO, RUSTICO, DIAMANTE, GRANJA, CARCEL;
        //---------- VIP ------- //------VIP+ --------- //----------- DONOR ----------
    }

    public void setVIPGround(Location loc, cageType type) {
        if (null != type) {
            switch (type) {
                case VERDE:
                    buildVIPGround(loc, (byte) 5);
                    break;
                case MORADO:
                    buildVIPGround(loc, (byte) 10);
                    break;
                case NARANJO:
                    buildVIPGround(loc, (byte) 1);
                    break;
                case AZUL:
                    buildVIPGround(loc, (byte) 11);
                    break;
                case ROJO:
                    buildVIPGround(loc, (byte) 14);
                    break;
                case RUSTICO:
                    buildsukulentoCage(loc, (byte) 12, Material.LOG, Material.WOOD, Material.WOOD, Material.STAINED_GLASS);
                    break;
                case DIAMANTE:
                    buildsukulentoCage(loc, (byte) 3, Material.STAINED_GLASS, Material.STAINED_CLAY, Material.DIAMOND_BLOCK, Material.STAINED_GLASS_PANE);
                    break;
                case GRANJA:
                    buildsukulentoCage(loc, (byte) 3, Material.GRASS, Material.GRASS, Material.GLASS, Material.FENCE);
                    break;
                case CARCEL:
                    buildsukulentoCage(loc, (byte) 3, Material.IRON_BLOCK, Material.IRON_BLOCK, Material.IRON_BLOCK, Material.IRON_FENCE);
                    break;
                default:
                    break;
            }
        }
    }

    public void buildsukulentoCage(Location loc, byte color, Material alrededorm, Material centrom, Material arribam, Material bordem) {
        int a = loc.getBlockX();
        int b = loc.getBlockY();
        int c = loc.getBlockZ();
        ItemStack alrededor = new ItemStack(alrededorm);
        ItemStack centro = new ItemStack(centrom);
        ItemStack arriba = new ItemStack(arribam);
        ItemStack borde = new ItemStack(bordem);
        if (color == 333) {
            alrededor = new ItemStack(alrededorm);
            borde = new ItemStack(bordem);
        }
        //CENTRO Y ARRIBA
        loc.getWorld().getBlockAt(a, b - 1, c).setTypeIdAndData(centro.getTypeId(), color, true);//ABAJO
        loc.getWorld().getBlockAt(a, b + 3, c).setTypeIdAndData(arriba.getTypeId(), color, true);//ARRIBA

        //BORDE
        loc.getWorld().getBlockAt(a + 1, b - 1, c).setTypeIdAndData(alrededor.getTypeId(), color, true);
        loc.getWorld().getBlockAt(a, b - 1, c + 1).setTypeIdAndData(alrededor.getTypeId(), color, true);
        loc.getWorld().getBlockAt(a + 1, b - 1, c + 1).setTypeIdAndData(alrededor.getTypeId(), color, true);
        loc.getWorld().getBlockAt(a, b - 1, c - 1).setTypeIdAndData(alrededor.getTypeId(), color, true);
        loc.getWorld().getBlockAt(a - 1, b - 1, c).setTypeIdAndData(alrededor.getTypeId(), color, true);
        loc.getWorld().getBlockAt(a - 1, b - 1, c - 1).setTypeIdAndData(alrededor.getTypeId(), color, true);
        loc.getWorld().getBlockAt(a + 1, b - 1, c - 1).setTypeIdAndData(alrededor.getTypeId(), color, true);
        loc.getWorld().getBlockAt(a - 1, b - 1, c + 1).setTypeIdAndData(alrededor.getTypeId(), color, true);
        loc.getWorld().getBlockAt(a - 1, b - 1, c - 1).setTypeIdAndData(alrededor.getTypeId(), color, true);

        //ALREDEDOR
        for (int i = 0; i < 4; i++) {
            loc.getWorld().getBlockAt(a + 1, b + i, c).setTypeIdAndData(borde.getTypeId(), color, true);
            loc.getWorld().getBlockAt(a, b + i, c + 1).setTypeIdAndData(borde.getTypeId(), color, true);
            loc.getWorld().getBlockAt(a + 1, b + i, c + 1).setTypeIdAndData(borde.getTypeId(), color, true);
            loc.getWorld().getBlockAt(a, b + i, c - 1).setTypeIdAndData(borde.getTypeId(), color, true);
            loc.getWorld().getBlockAt(a - 1, b + i, c).setTypeIdAndData(borde.getTypeId(), color, true);
            loc.getWorld().getBlockAt(a - 1, b + i, c - 1).setTypeIdAndData(borde.getTypeId(), color, true);
            loc.getWorld().getBlockAt(a + 1, b + i, c - 1).setTypeIdAndData(borde.getTypeId(), color, true);
            loc.getWorld().getBlockAt(a - 1, b + i, c + 1).setTypeIdAndData(borde.getTypeId(), color, true);
            loc.getWorld().getBlockAt(a - 1, b + i, c - 1).setTypeIdAndData(borde.getTypeId(), color, true);
        }
        loc.getWorld().getBlockAt(a + 1, b - 1, c).setTypeIdAndData(alrededor.getTypeId(), color, true);
        loc.getWorld().getBlockAt(a, b - 1, c + 1).setTypeIdAndData(alrededor.getTypeId(), color, true);
        loc.getWorld().getBlockAt(a + 1, b - 1, c + 1).setTypeIdAndData(alrededor.getTypeId(), color, true);
        loc.getWorld().getBlockAt(a, b - 1, c - 1).setTypeIdAndData(alrededor.getTypeId(), color, true);
        loc.getWorld().getBlockAt(a - 1, b - 1, c).setTypeIdAndData(alrededor.getTypeId(), color, true);
        loc.getWorld().getBlockAt(a - 1, b - 1, c - 1).setTypeIdAndData(alrededor.getTypeId(), color, true);
        loc.getWorld().getBlockAt(a + 1, b - 1, c - 1).setTypeIdAndData(alrededor.getTypeId(), color, true);
        loc.getWorld().getBlockAt(a - 1, b - 1, c + 1).setTypeIdAndData(alrededor.getTypeId(), color, true);
        loc.getWorld().getBlockAt(a - 1, b - 1, c - 1).setTypeIdAndData(alrededor.getTypeId(), color, true);
    }

    public void buildVIPGround(Location loc, byte color) {
        int a = loc.getBlockX();
        int b = loc.getBlockY();
        int c = loc.getBlockZ();
        ItemStack alrededor = new ItemStack(Material.STAINED_GLASS);
        ItemStack centro = new ItemStack(Material.STAINED_CLAY);
        ItemStack arriba = new ItemStack(Material.STAINED_GLASS);
        ItemStack borde = new ItemStack(Material.STAINED_GLASS_PANE);
        if (color == 333) {
            alrededor = new ItemStack(Material.STAINED_GLASS);
            centro = new ItemStack(Material.STAINED_CLAY);
            arriba = new ItemStack(Material.STAINED_GLASS);
            borde = new ItemStack(Material.STAINED_GLASS_PANE);
        }
        //CENTRO Y ARRIBA
        loc.getWorld().getBlockAt(a, b - 1, c).setTypeIdAndData(centro.getTypeId(), color, true);//ABAJO
        loc.getWorld().getBlockAt(a, b + 3, c).setTypeIdAndData(arriba.getTypeId(), color, true);//ARRIBA

        //BORDE
        loc.getWorld().getBlockAt(a + 1, b - 1, c).setTypeIdAndData(alrededor.getTypeId(), color, true);
        loc.getWorld().getBlockAt(a, b - 1, c + 1).setTypeIdAndData(alrededor.getTypeId(), color, true);
        loc.getWorld().getBlockAt(a + 1, b - 1, c + 1).setTypeIdAndData(alrededor.getTypeId(), color, true);
        loc.getWorld().getBlockAt(a, b - 1, c - 1).setTypeIdAndData(alrededor.getTypeId(), color, true);
        loc.getWorld().getBlockAt(a - 1, b - 1, c).setTypeIdAndData(alrededor.getTypeId(), color, true);
        loc.getWorld().getBlockAt(a - 1, b - 1, c - 1).setTypeIdAndData(alrededor.getTypeId(), color, true);
        loc.getWorld().getBlockAt(a + 1, b - 1, c - 1).setTypeIdAndData(alrededor.getTypeId(), color, true);
        loc.getWorld().getBlockAt(a - 1, b - 1, c + 1).setTypeIdAndData(alrededor.getTypeId(), color, true);
        loc.getWorld().getBlockAt(a - 1, b - 1, c - 1).setTypeIdAndData(alrededor.getTypeId(), color, true);

        //ALREDEDOR
        for (int i = 0; i < 4; i++) {
            loc.getWorld().getBlockAt(a + 1, b + i, c).setTypeIdAndData(borde.getTypeId(), color, true);
            loc.getWorld().getBlockAt(a, b + i, c + 1).setTypeIdAndData(borde.getTypeId(), color, true);
            loc.getWorld().getBlockAt(a + 1, b + i, c + 1).setTypeIdAndData(borde.getTypeId(), color, true);
            loc.getWorld().getBlockAt(a, b + i, c - 1).setTypeIdAndData(borde.getTypeId(), color, true);
            loc.getWorld().getBlockAt(a - 1, b + i, c).setTypeIdAndData(borde.getTypeId(), color, true);
            loc.getWorld().getBlockAt(a - 1, b + i, c - 1).setTypeIdAndData(borde.getTypeId(), color, true);
            loc.getWorld().getBlockAt(a + 1, b + i, c - 1).setTypeIdAndData(borde.getTypeId(), color, true);
            loc.getWorld().getBlockAt(a - 1, b + i, c + 1).setTypeIdAndData(borde.getTypeId(), color, true);
            loc.getWorld().getBlockAt(a - 1, b + i, c - 1).setTypeIdAndData(borde.getTypeId(), color, true);
        }
        loc.getWorld().getBlockAt(a + 1, b - 1, c).setTypeIdAndData(alrededor.getTypeId(), color, true);
        loc.getWorld().getBlockAt(a, b - 1, c + 1).setTypeIdAndData(alrededor.getTypeId(), color, true);
        loc.getWorld().getBlockAt(a + 1, b - 1, c + 1).setTypeIdAndData(alrededor.getTypeId(), color, true);
        loc.getWorld().getBlockAt(a, b - 1, c - 1).setTypeIdAndData(alrededor.getTypeId(), color, true);
        loc.getWorld().getBlockAt(a - 1, b - 1, c).setTypeIdAndData(alrededor.getTypeId(), color, true);
        loc.getWorld().getBlockAt(a - 1, b - 1, c - 1).setTypeIdAndData(alrededor.getTypeId(), color, true);
        loc.getWorld().getBlockAt(a + 1, b - 1, c - 1).setTypeIdAndData(alrededor.getTypeId(), color, true);
        loc.getWorld().getBlockAt(a - 1, b - 1, c + 1).setTypeIdAndData(alrededor.getTypeId(), color, true);
        loc.getWorld().getBlockAt(a - 1, b - 1, c - 1).setTypeIdAndData(alrededor.getTypeId(), color, true);
    }

    public void buildGround(Location loc, byte dataByte) {
        ItemStack i = new ItemStack(Material.STAINED_GLASS);
        ItemStack i2 = new ItemStack(Material.STAINED_CLAY);
        if (dataByte == 333) {
            i = new ItemStack(Material.GLASS);
            i2 = new ItemStack(Material.STAINED_CLAY);
            dataByte = 0;
        }
        int a = loc.getBlockX();
        int b = loc.getBlockY();
        int c = loc.getBlockZ();

        loc.getWorld().getBlockAt(a, b - 1, c).setTypeIdAndData(i2.getTypeId(), dataByte, true);
        loc.getWorld().getBlockAt(a + 1, b - 1, c).setTypeIdAndData(i.getTypeId(), dataByte, true);
        loc.getWorld().getBlockAt(a, b - 1, c + 1).setTypeIdAndData(i.getTypeId(), dataByte, true);
        loc.getWorld().getBlockAt(a + 1, b - 1, c + 1).setTypeIdAndData(i.getTypeId(), dataByte, true);
        loc.getWorld().getBlockAt(a, b - 1, c - 1).setTypeIdAndData(i.getTypeId(), dataByte, true);
        loc.getWorld().getBlockAt(a - 1, b - 1, c).setTypeIdAndData(i.getTypeId(), dataByte, true);
        loc.getWorld().getBlockAt(a - 1, b - 1, c - 1).setTypeIdAndData(i.getTypeId(), dataByte, true);
        loc.getWorld().getBlockAt(a + 1, b - 1, c - 1).setTypeIdAndData(i.getTypeId(), dataByte, true);
        loc.getWorld().getBlockAt(a - 1, b - 1, c + 1).setTypeIdAndData(i.getTypeId(), dataByte, true);
        loc.getWorld().getBlockAt(a - 1, b - 1, c - 1).setTypeIdAndData(i.getTypeId(), dataByte, true);
    }


}
