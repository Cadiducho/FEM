package com.cadiducho.fem.pro.utils;

import com.cadiducho.fem.core.util.ItemBuilder;
import com.cadiducho.fem.pro.Protections;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public enum ProType {

    BASIC(Protections.getInstance().getFiles().getConfig().getInt("Area.Basico"), Material.LAPIS_BLOCK, "Protección Básica"),
    MEDIUM(Protections.getInstance().getFiles().getConfig().getInt("Area.Medio"), Material.REDSTONE_ORE, "Protección Media"),
    BIG(Protections.getInstance().getFiles().getConfig().getInt("Area.Grande"), Material.IRON_ORE, "Protección Grande");

    @Getter private int area;
    @Getter private Material mat;
    @Getter private String name;

    private static ProType type;

    ProType(int area, Material mat, String name){
        this.area = area;
        this.mat = mat;
        this.name = name;
    }

    public static ProType parseMaterial(Material m){
        Arrays.asList(ProType.values()).forEach(t -> {
            if (t.getMat() == m) type = t;
        });
        return type;
    }

    public static ItemStack[] getItems(){
        ItemStack[] items = null;

        items[0] = new ItemBuilder().setType(BASIC.getMat()).setDisplayName(BASIC.getName()).build();
        items[1] = new ItemBuilder().setType(MEDIUM.getMat()).setDisplayName(MEDIUM.getName()).build();
        items[0] = new ItemBuilder().setType(BIG.getMat()).setDisplayName(BIG.getName()).build();

        return items;
    }
}
