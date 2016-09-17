package com.cadiducho.fem.core.util;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.Wool;

/**
 *
 * @author Jake, Cadiducho
 */
public class ItemUtil {
    public static ItemStack createItem(Material material, String displayname, String lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Metodos.colorizar(displayname));
        ArrayList<String> Lore = new ArrayList<>();
        Lore.add(Metodos.colorizar(lore));
        meta.setLore(Lore);

        item.setItemMeta(meta);
        return item;
    }
    
    public static ItemStack createItem(Material material, String displayname, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Metodos.colorizar(displayname));
        meta.setLore(lore);

        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createItem(Material material, int amount, String displayname, String lore) {
        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Metodos.colorizar(displayname));
        ArrayList<String> Lore = new ArrayList<>();
        Lore.add(lore);
        meta.setLore(Lore);

        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createItem(Material material, String displayname) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Metodos.colorizar(displayname));
        item.setItemMeta(meta);
        return item;
    }
    
    public static ItemStack createWool(String displayname, DyeColor dye) {
        ItemStack item = new Wool(dye).toItemStack();
        ItemMeta meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_DESTROYS);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.setDisplayName(displayname);
        item.setItemMeta(meta);
        return item;
    }
    
    public static ItemStack createGlass(String displayname, String lore, DyeColor dye) {
        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, dye.getData());
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(displayname);
        ArrayList<String> Lore = new ArrayList<>();
        Lore.add(lore);
        meta.setLore(Lore);

        item.setItemMeta(meta);
        return item;
    }
    
    public static ItemStack createHeadPlayer(String name, List<String> lore) {
        ItemStack playerHead = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
        SkullMeta sm = (SkullMeta)playerHead.getItemMeta();
        sm.setOwner(name);
        sm.setLore(lore);
        sm.setDisplayName(Metodos.colorizar("&e"+name));
        playerHead.setItemMeta(sm);
        return playerHead;
    }
    
    public static ItemStack createBanner(String name, String lore, DyeColor color) {
        ItemStack banner = new ItemStack(Material.BANNER);
        BannerMeta itemMeta = (BannerMeta) banner.getItemMeta();
        itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemMeta.addItemFlags(ItemFlag.HIDE_DESTROYS);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
        itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        ArrayList<String> Lore = new ArrayList<>();
        Lore.add(lore);
        itemMeta.setLore(Lore);
        itemMeta.setBaseColor(color);
        itemMeta.setDisplayName(name);
        banner.setItemMeta(itemMeta);
        return banner;
    }
}
