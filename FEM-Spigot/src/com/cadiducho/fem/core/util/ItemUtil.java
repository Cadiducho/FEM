package com.cadiducho.fem.core.util;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.Wool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Jake, Cadiducho
 */
public class ItemUtil {
    
    public static ItemStack createItem(Material material, String displayname) {
        return createItem(material, 1, displayname, (List<String>) null);
    }
    
    public static ItemStack createItem(Material material, String displayname, String lore) {
        return createItem(material, 1, displayname, Arrays.asList(lore));
    }
    
    public static ItemStack createItem(Material material, int amount, String displayname, String lore) {
        return createItem(material, amount, displayname, Arrays.asList(lore));
    }
    
    public static ItemStack createItem(Material material, String displayname, List<String> lore) {
        return createItem(material, 1, displayname, lore);
    }

    public static ItemStack createItem(Material material, int amount, String displayname, List<String> lore) {
        return createItem(material, amount, (short)0, displayname, lore);
    }
    
    public static ItemStack createItem(Material material, int amount, short data, String displayname, List<String> lore) {
        ItemStack item = new ItemStack(material, amount, data);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Metodos.colorizar(displayname));
        ArrayList<String> colorLore = new ArrayList<>();
        if (lore != null) {
            lore.forEach(str -> colorLore.add(Metodos.colorizar(str)));
            meta.setLore(colorLore);
        }
        
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_ATTRIBUTES, 
                ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_UNBREAKABLE);

        item.setItemMeta(meta);
        return item;
    }
    
    public static ItemStack createWool(String displayname, DyeColor dye) {
        ItemStack item = new Wool(dye).toItemStack(1);
        ItemMeta meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_ATTRIBUTES, 
                ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_UNBREAKABLE);
        meta.setDisplayName(displayname);
        item.setItemMeta(meta);
        return item;
    }
    
    public static ItemStack createClay(String displayname, DyeColor dye) {
        return createClay(displayname, null, dye);
    }
    
    public static ItemStack createClay(String displayname, List<String> lore, DyeColor dye) {
        ItemStack item = new ItemStack(Material.STAINED_CLAY, 1, dye.getWoolData());
        ItemMeta meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_ATTRIBUTES, 
                ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_UNBREAKABLE);
        meta.setDisplayName(displayname);
        ArrayList<String> colorLore = new ArrayList<>();
        if (lore != null) {
            lore.forEach(str -> colorLore.add(Metodos.colorizar(str)));
            meta.setLore(colorLore);
        }
        
        item.setItemMeta(meta);
        return item;
    }
    
    public static ItemStack createGlass(String displayname, String lore, DyeColor dye) {
        return createGlass(displayname, Arrays.asList(lore), dye);
    }
    
    public static ItemStack createGlass(String displayname, List<String> lore, DyeColor dye) {
        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, dye.getWoolData());
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Metodos.colorizar(displayname));
        
        ArrayList<String> colorLore = new ArrayList<>();
        if (lore != null) {
            lore.forEach(str -> colorLore.add(Metodos.colorizar(str)));
            meta.setLore(colorLore);
        }

        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_ATTRIBUTES, 
                ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_UNBREAKABLE);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createHeadPlayer(String displayname, String username, List<String> lore) {
        ItemStack playerHead = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta sm = (SkullMeta)playerHead.getItemMeta();
        sm.setOwner(username);
        ArrayList<String> colorLore = new ArrayList<>();
        if (lore != null) {
            lore.forEach(str -> colorLore.add(Metodos.colorizar(str)));
            sm.setLore(colorLore);
        }
        
        sm.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_ATTRIBUTES, 
                ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_UNBREAKABLE);
        sm.setDisplayName(Metodos.colorizar(displayname));
        playerHead.setItemMeta(sm);
        return playerHead;
    }
    
    public static ItemStack createBanner(String name, String lore, DyeColor color) {
        return createBanner(name, Arrays.asList(lore), color);
    }

    public static ItemStack createBanner(String name, List<String> lore, DyeColor color){
        ItemStack banner = new ItemStack(Material.BANNER);
        BannerMeta itemMeta = (BannerMeta) banner.getItemMeta();
        itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_ATTRIBUTES,
                ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_UNBREAKABLE);
        ArrayList<String> colorLore = new ArrayList<>();
        if (lore != null) {
            lore.forEach(str -> colorLore.add(Metodos.colorizar(str)));
            itemMeta.setLore(colorLore);
        }
        itemMeta.setBaseColor(color);
        itemMeta.setDisplayName(name);
        banner.setItemMeta(itemMeta);
        return banner;
    }
}
