package com.cadiducho.fem.lobby.utils;

import com.cadiducho.fem.core.particles.ParticleEffect;
import com.cadiducho.fem.core.util.ItemUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public enum ParticleType {

    SEGUIR1(0, ItemUtil.createItem(Material.WOOL, 1, (short)14, "Corazones", new ArrayList<>()),ParticleEffect.HEART, ParticleID.NONE),
    SEGUIR2(1, ItemUtil.createItem(Material.FURNACE, "Tren"), ParticleEffect.SMOKE_NORMAL, ParticleID.NONE),
    HALO1(2, ItemUtil.createItem(Material.STATIONARY_LAVA, "Anillo de Fuego"), ParticleEffect.DRIP_LAVA, ParticleID.HALO),
    HALO2(3, ItemUtil.createItem(Material.STATIONARY_WATER, "Anillo de Agua"), ParticleEffect.DRIP_WATER, ParticleID.HALO),
    SPIRAL1(4, ItemUtil.createItem(Material.FIREWORK, "Centellas"), ParticleEffect.FIREWORKS_SPARK, ParticleID.SPIRAL);

    private int id;
    private ItemStack i;
    private ParticleEffect pe;
    private ParticleID pid;

    ParticleType(int id, ItemStack i, ParticleEffect pe, ParticleID pid){
        this.id = id;
        this.i = i;
        this.pe = pe;
        this.pid = pid;
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

    public enum ParticleID {
        //TODO: Más
        NONE, HALO, SPIRAL, FOUNTAIN;
    }
}
