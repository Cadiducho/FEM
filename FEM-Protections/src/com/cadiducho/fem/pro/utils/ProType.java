package com.cadiducho.fem.pro.utils;

import com.cadiducho.fem.core.util.ItemBuilder;
import com.cadiducho.fem.pro.Protections;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum ProType {

    BASIC(Protections.getInstance().getFiles().getConfig().getInt("Area.Basico"), Material.LAPIS_ORE, "Protección Básica"),
    MEDIUM(Protections.getInstance().getFiles().getConfig().getInt("Area.Medio"), Material.IRON_ORE, "Protección Media"),
    BIG(Protections.getInstance().getFiles().getConfig().getInt("Area.Grande"), Material.REDSTONE_ORE, "Protección Grande");

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

    public static List<ItemStack> getItems(){
        List<ItemStack> items = new ArrayList<>();

        items.add(new ItemBuilder().setType(BASIC.getMat()).setDisplayName(BASIC.getName()).addUnsafeEnchant(Enchantment.ARROW_DAMAGE, 1).addItemFlag(ItemFlag.HIDE_ENCHANTS).build());
        items.add(new ItemBuilder().setType(MEDIUM.getMat()).setDisplayName(MEDIUM.getName()).addUnsafeEnchant(Enchantment.ARROW_DAMAGE, 1).addItemFlag(ItemFlag.HIDE_ENCHANTS).build());
        items.add(new ItemBuilder().setType(BIG.getMat()).setDisplayName(BIG.getName()).addUnsafeEnchant(Enchantment.ARROW_DAMAGE, 1).addItemFlag(ItemFlag.HIDE_ENCHANTS).build());

        return items;
    }
}
