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

    //Vuelta por si acaso se cambia de versión...
    S(Protections.getInstance().getFiles().getConfig().getInt("Area.S"), Material.COAL_ORE, "Protección Básica"),
    M(Protections.getInstance().getFiles().getConfig().getInt("Area.M"), Material.IRON_ORE, "Protección Media"),
    L(Protections.getInstance().getFiles().getConfig().getInt("Area.L"), Material.LAPIS_ORE, "Protección Grande"),
    XL(Protections.getInstance().getFiles().getConfig().getInt("Area.XL"), Material.REDSTONE_ORE, "Protección Gigante"),
    GRANJAS(Protections.getInstance().getFiles().getConfig().getInt("Area.Granjas"), Material.QUARTZ_ORE, "Protección Granjas");

    @Getter private int area;
    @Getter private Material material;
    @Getter private String name;

    private static ProType type;

    ProType(int area, Material mat, String name){
        this.area = area;
        this.material = mat;
        this.name = name;
    }

    public static ProType parseMaterial(Material m){
        Arrays.asList(ProType.values()).forEach(t -> {
            if (t.getMaterial() == m) type = t;
        });
        return type;
    }

    public static ItemStack generateItemStack(ProType pt){
        return new ItemBuilder().setType(pt.getMaterial()).setDisplayName(pt.getName()).addUnsafeEnchant(Enchantment.ARROW_DAMAGE, 1).addItemFlag(ItemFlag.HIDE_ENCHANTS).build();
    }

    public static List<ItemStack> getItems(){
        List<ItemStack> items = new ArrayList<>();

        items.add(new ItemBuilder().setType(S.getMaterial()).setDisplayName(S.getName()).addUnsafeEnchant(Enchantment.ARROW_DAMAGE, 1).addItemFlag(ItemFlag.HIDE_ENCHANTS).build());
        items.add(new ItemBuilder().setType(M.getMaterial()).setDisplayName(M.getName()).addUnsafeEnchant(Enchantment.ARROW_DAMAGE, 1).addItemFlag(ItemFlag.HIDE_ENCHANTS).build());
        items.add(new ItemBuilder().setType(L.getMaterial()).setDisplayName(L.getName()).addUnsafeEnchant(Enchantment.ARROW_DAMAGE, 1).addItemFlag(ItemFlag.HIDE_ENCHANTS).build());
        items.add(new ItemBuilder().setType(XL.getMaterial()).setDisplayName(XL.getName()).addUnsafeEnchant(Enchantment.ARROW_DAMAGE, 1).addItemFlag(ItemFlag.HIDE_ENCHANTS).build());
        items.add(new ItemBuilder().setType(GRANJAS.getMaterial()).setDisplayName(GRANJAS.getName()).addUnsafeEnchant(Enchantment.ARROW_DAMAGE, 1).addItemFlag(ItemFlag.HIDE_ENCHANTS).build());

        return items;
    }
}
