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

    S(Protections.getInstance().getFiles().getConfig().getInt("Area.S"), (short) 0, "Protección Básica"),
    M(Protections.getInstance().getFiles().getConfig().getInt("Area.M"), (short) 1, "Protección Media"),
    L(Protections.getInstance().getFiles().getConfig().getInt("Area.L"), (short) 2, "Protección Grande"),
    XL(Protections.getInstance().getFiles().getConfig().getInt("Area.XL"), (short) 3, "Protección Gigante");

    @Getter private int area;
    @Getter private Material mat;
    @Getter private short data;
    @Getter private String name;

    private static ProType type;

    ProType(int area, short data, String name){
        this.area = area;
        this.mat = Material.STRUCTURE_BLOCK;
        this.data = data;
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

        items.add(new ItemBuilder().setType(S.getMat()).setDisplayName(S.getName()).setAmount(1).setDurability(S.getData()).addUnsafeEnchant(Enchantment.ARROW_DAMAGE, 1).addItemFlag(ItemFlag.HIDE_ENCHANTS).build());
        items.add(new ItemBuilder().setType(M.getMat()).setDisplayName(M.getName()).setAmount(1).setDurability(M.getData()).addUnsafeEnchant(Enchantment.ARROW_DAMAGE, 1).addItemFlag(ItemFlag.HIDE_ENCHANTS).build());
        items.add(new ItemBuilder().setType(L.getMat()).setDisplayName(L.getName()).setAmount(1).setDurability(L.getData()).addUnsafeEnchant(Enchantment.ARROW_DAMAGE, 1).addItemFlag(ItemFlag.HIDE_ENCHANTS).build());
        items.add(new ItemBuilder().setType(XL.getMat()).setDisplayName(XL.getName()).setAmount(1).setDurability(XL.getData()).addUnsafeEnchant(Enchantment.ARROW_DAMAGE, 1).addItemFlag(ItemFlag.HIDE_ENCHANTS).build());

        return items;
    }
}
