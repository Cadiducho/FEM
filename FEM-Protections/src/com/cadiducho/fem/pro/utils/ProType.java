package com.cadiducho.fem.pro.utils;

import lombok.Getter;
import org.bukkit.Material;

public enum ProType {

    BASIC(10, Material.LAPIS_BLOCK, "Protección Básica"),
    MEDIUM(20, Material.REDSTONE_ORE, "Protección Media"),
    BIG(30, Material.IRON_ORE, "Protección Grande");

    @Getter private int area;
    @Getter private Material mat;
    @Getter private String name;

    ProType(int area, Material mat, String name){
        this.area = area;
        this.mat = mat;
        this.name = name;
    }
}
