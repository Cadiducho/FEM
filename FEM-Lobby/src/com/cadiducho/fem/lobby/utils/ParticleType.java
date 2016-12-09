package com.cadiducho.fem.lobby.utils;

import com.cadiducho.fem.core.particles.ParticleEffect;
import com.cadiducho.fem.core.util.ItemUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public enum ParticleType {

    //NORMAL
    P(0, ItemUtil.createItem(Material.WOOL, 1, (short)14, "Corazones", new ArrayList<>()),ParticleEffect.HEART, ParticleID.NONE),
    P1(1, ItemUtil.createItem(Material.FURNACE, "Tren"), ParticleEffect.SMOKE_NORMAL, ParticleID.NONE),
    //HALO
    P2(2, ItemUtil.createItem(Material.STATIONARY_LAVA, "Anillo de Fuego"), ParticleEffect.DRIP_LAVA, ParticleID.HALO),
    P3(3, ItemUtil.createItem(Material.STATIONARY_WATER, "Anillo de Agua"), ParticleEffect.DRIP_WATER, ParticleID.HALO),
    //SPIRAL
    P4(4, ItemUtil.createItem(Material.FIREWORK, "Centellas"), ParticleEffect.FIREWORKS_SPARK, ParticleID.SPIRAL),
    P5(5, ItemUtil.createItem(Material.RECORD_11, "Música"), ParticleEffect.NOTE, ParticleID.SPIRAL, new ParticleEffect.OrdinaryColor(50, 70, 100)),
    //SPHERE
    P6(6, ItemUtil.createItem(Material.PORTAL, "Area Venenosa"), ParticleEffect.REDSTONE, ParticleID.SPHERE, new ParticleEffect.OrdinaryColor(255, 0, 255)),
    //POLYGON
    P7(7, ItemUtil.createItem(Material.ENCHANTMENT_TABLE, "Magia Arcana"), ParticleEffect.ENCHANTMENT_TABLE, ParticleID.POLYGON);

    private int id;
    private ItemStack i;
    private ParticleEffect pe;
    private ParticleID pid;
    private ParticleEffect.OrdinaryColor color;

    ParticleType(int id, ItemStack i, ParticleEffect pe, ParticleID pid){
        this.id = id;
        this.i = i;
        this.pe = pe;
        this.pid = pid;
    }

    ParticleType(int id, ItemStack i, ParticleEffect pe, ParticleID pid, ParticleEffect.OrdinaryColor color){
        this.id = id;
        this.i = i;
        this.pe = pe;
        this.pid = pid;
        this.color = color;
    }

    public int getID(){
        return id;
    }
    public ItemStack getItem(){
        return i;
    }
    public ParticleEffect getPE(){
        return pe;
    }
    public ParticleID getPID(){
        return pid;
    }
    public ParticleEffect.OrdinaryColor getColor(){
        return color;
    }

    public enum ParticleID {
        //TODO: Más
        NONE, HALO, SPIRAL, FOUNTAIN, WINGS, SPHERE, POLYGON, POLYGON_FULL;
    }
}
