package com.cadiducho.fem.lucky.utils;

import com.cadiducho.fem.core.util.ItemUtil;
import com.cadiducho.fem.lucky.LuckyWarriors;
import java.util.ArrayList;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

public class ChestItems {

    public static final ArrayList<ItemStack> ITEMS = new ArrayList();
    private static final Random random = new Random();

    public static void initItems() {
        ITEMS.clear();
        addArmas();
        addArmor();
        addComida();
        addOtros();
    }

    private static void addArmor() {
        ItemStack ARMOR1 = new ItemStack(Material.DIAMOND_HELMET);
        ItemStack ARMOR2 = new ItemStack(Material.DIAMOND_CHESTPLATE);
        ItemStack ARMOR3 = new ItemStack(Material.DIAMOND_LEGGINGS);
        ItemStack ARMOR4 = new ItemStack(Material.DIAMOND_BOOTS);
        ITEMS.add(ARMOR1);
        ITEMS.add(ARMOR2);
        ITEMS.add(ARMOR3);
        ITEMS.add(ARMOR4);
    }

    private static void addArmas() {
        ItemStack ARMA1 = new ItemStack(Material.IRON_SWORD);
        ItemStack ARMA2 = new ItemStack(Material.DIAMOND_SWORD);
        ItemStack ARMA3 = new ItemStack(Material.BOW);
        ItemStack ARMA4 = new ItemStack(Material.FISHING_ROD);
        ItemStack ARMA5 = new ItemStack(Material.ARROW, 3);
        ITEMS.add(ARMA1);
        ITEMS.add(ARMA2);
        ITEMS.add(ARMA3);
        ITEMS.add(ARMA4);
        ITEMS.add(ARMA5);
    }

    private static void addComida() {
        ItemStack COMIDA1 = new ItemStack(Material.COOKED_BEEF, 3);
        ItemStack COMIDA2 = new ItemStack(Material.GOLDEN_APPLE, 1);
        ITEMS.add(COMIDA1);
        ITEMS.add(COMIDA2);
    }

    private static void addOtros() {
        ItemStack BLOQUES = new ItemStack(Material.WOOD, 4);
        ItemStack BLOQUES2 = new ItemStack(Material.COBBLESTONE, 4);
        ItemStack OTROS2 = new ItemStack(Material.EXP_BOTTLE, 6);
        ItemStack LAPIZ = new ItemStack(Material.INK_SACK, 3, (short) 4);
        ItemStack LAVA = new ItemStack(Material.LAVA_BUCKET, 1);
        ItemStack AGUA = new ItemStack(Material.WATER_BUCKET, 1);
        ItemStack ORO = new ItemStack(Material.GOLD_INGOT, 4);
        ItemStack DIAMOND = new ItemStack(Material.DIAMOND, 2);
        ItemStack MANZANA = new ItemStack(Material.APPLE, 1);
        ITEMS.add(BLOQUES);
        ITEMS.add(BLOQUES2);
        ITEMS.add(OTROS2);
        ITEMS.add(LAPIZ);
        ITEMS.add(LAVA);
        ITEMS.add(AGUA);
        ITEMS.add(ORO);
        ITEMS.add(DIAMOND);
        ITEMS.add(MANZANA);
        ITEMS.add(getCustomEnchantedBook(Enchantment.DAMAGE_ALL, 1));
        ITEMS.add(getCustomEnchantedBook(Enchantment.PROTECTION_ENVIRONMENTAL, 2));
        ITEMS.add(getCustomEnchantedBook(Enchantment.PROTECTION_ENVIRONMENTAL, 1));
        ITEMS.add(getCustomEnchantedBook(Enchantment.ARROW_DAMAGE, 1));
        ITEMS.add(getCustomEnchantedBook(Enchantment.ARROW_FIRE, 1));
        ITEMS.add(getCustomEnchantedBook(Enchantment.ARROW_KNOCKBACK, 1));
        ITEMS.add(getCustomEnchantedBook(Enchantment.PROTECTION_PROJECTILE, 1));
    }

    public static ItemStack[] getRandomItems() {
        int ran = 5;
        ItemStack[] items = new ItemStack[ran];
        for (int i = 0; i < ran; i++) {
            int id = random.nextInt(ITEMS.size() - 1);
            items[i] = ((ItemStack) ITEMS.get(id));
        }
        return items;
    }

    private static ItemStack getCustomEnchantedBook(Enchantment enchant, int level) {
        ItemStack item = new ItemStack(Material.ENCHANTED_BOOK, 1);
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
        meta.addStoredEnchant(enchant, level, true);
        item.setItemMeta(meta);
        return item;
    }
}
