package com.cadiducho.fem.royale.utils;

import com.cadiducho.fem.core.util.ItemBuilder;
import com.cadiducho.fem.royale.BattleRoyale;
import java.util.ArrayList;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

//Qué horror de clase...
public class ChestItems {

    public static final ArrayList<ItemStack> muyProbables = new ArrayList<>();
    public static final ArrayList<ItemStack> probables = new ArrayList<>();
    public static final ArrayList<ItemStack> pocoProbables = new ArrayList<>();
    public static final ArrayList<ItemStack> especiales = new ArrayList<>();
    private static final Random random = new Random();

    public static void initItems() {
        muyProbables.clear();
        probables.clear();
        pocoProbables.clear();
        especiales.clear();
        addMuyProbables();
        addProbables();
        addPocoProbables();
        addEspeciales();
    }
    
    private static void addMuyProbables() {
        muyProbables.add(new ItemBuilder().setType(Material.STICK).build());
        muyProbables.add(new ItemBuilder().setType(Material.LOG).build());
        muyProbables.add(new ItemBuilder().setType(Material.IRON_INGOT).build());
        muyProbables.add(new ItemBuilder().setType(Material.WOOD).setAmount(2).build());
        muyProbables.add(new ItemBuilder().setType(Material.ARROW).setAmount(4).build());
        muyProbables.add(new ItemBuilder().setType(Material.WOOD_SWORD).build());
        muyProbables.add(new ItemBuilder().setType(Material.WOOD_SPADE).build());
        muyProbables.add(new ItemBuilder().setType(Material.STONE_AXE).build());
        muyProbables.add(new ItemBuilder().setType(Material.CARROT).setAmount(2).build());
        muyProbables.add(new ItemBuilder().setType(Material.BAKED_POTATO).setAmount(2).build());
        muyProbables.add(new ItemBuilder().setType(Material.LEATHER_BOOTS).build());
        muyProbables.add(new ItemBuilder().setType(Material.LEATHER_CHESTPLATE).build());
        muyProbables.add(new ItemBuilder().setType(Material.LEATHER_HELMET).build());
        muyProbables.add(new ItemBuilder().setType(Material.LEATHER_LEGGINGS).build());
        muyProbables.add(new ItemBuilder().setType(Material.COOKIE).setAmount(2).build());
        muyProbables.add(new ItemBuilder().setType(Material.TORCH).setAmount(4).build());
        muyProbables.add(new ItemBuilder().setType(Material.MELON).setAmount(2).build());
        muyProbables.add(new ItemBuilder().setType(Material.GOLD_NUGGET).setAmount(2).build());
        ItemStack moneda4 = BattleRoyale.getInstance().getMoneda().clone();
        moneda4.setAmount(4);      
        muyProbables.add(moneda4);
    }
    
    private static void addProbables() {
        probables.add(new ItemBuilder().setType(Material.GOLD_INGOT).build());
        probables.add(new ItemBuilder().setType(Material.IRON_INGOT).build());
        probables.add(new ItemBuilder().setType(Material.STONE_SWORD).build());
        probables.add(new ItemBuilder().setType(Material.COOKED_BEEF).build());
        probables.add(new ItemBuilder().setType(Material.CHAINMAIL_HELMET).build());
        probables.add(new ItemBuilder().setType(Material.CHAINMAIL_BOOTS).build());
        probables.add(new ItemBuilder().setType(Material.BREAD).build());
        probables.add(new ItemBuilder().setType(Material.EXP_BOTTLE).setAmount(2).build());
        probables.add(new ItemBuilder().setType(Material.COOKED_FISH).build());
        probables.add(new ItemBuilder().setType(Material.COOKED_RABBIT).build());
        probables.add(new ItemBuilder().setType(Material.WHEAT).setAmount(2).build());
        probables.add(new ItemBuilder().setType(Material.ARROW).setAmount(2).build());
        ItemStack moneda2 = BattleRoyale.getInstance().getMoneda().clone();
        moneda2.setAmount(2);      
        muyProbables.add(moneda2);
    }
    
    private static void addPocoProbables() {
        pocoProbables.add(new ItemBuilder().setType(Material.WORKBENCH).build());
        pocoProbables.add(new ItemBuilder().setType(Material.IRON_SWORD).build());
        pocoProbables.add(new ItemBuilder().setType(Material.STONE_SWORD).addEnchant(Enchantment.KNOCKBACK, 1, true).build());
        pocoProbables.add(new ItemBuilder().setType(Material.IRON_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 1, true).build());
        pocoProbables.add(new ItemBuilder().setType(Material.IRON_AXE).addEnchant(Enchantment.DAMAGE_ALL, 1, true).build());
        pocoProbables.add(new ItemBuilder().setType(Material.GOLD_SWORD).addEnchant(Enchantment.KNOCKBACK, 1, true).build());
        pocoProbables.add(new ItemBuilder().setType(Material.GOLD_PICKAXE).addEnchant(Enchantment.DAMAGE_ALL, 1, true).build());
        pocoProbables.add(new ItemBuilder().setType(Material.DIAMOND).build());
        pocoProbables.add(new ItemBuilder().setType(Material.IRON_INGOT).build());
        pocoProbables.add(new ItemBuilder().setType(Material.TNT).build());
        pocoProbables.add(new ItemBuilder().setType(Material.FLINT_AND_STEEL).build());
        pocoProbables.add(new ItemBuilder().setType(Material.CHAINMAIL_HELMET).addEnchant(Enchantment.THORNS, 1, true).build());
        pocoProbables.add(new ItemBuilder().setType(Material.CHAINMAIL_BOOTS).addEnchant(Enchantment.PROTECTION_FALL, 1, true).build());
        pocoProbables.add(new ItemBuilder().setType(Material.CHAINMAIL_CHESTPLATE).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true).build());
        pocoProbables.add(new ItemBuilder().setType(Material.CHAINMAIL_LEGGINGS).build());
        pocoProbables.add(new ItemBuilder().setType(Material.CHAINMAIL_LEGGINGS).addEnchant(Enchantment.PROTECTION_FIRE, 1, true).build());
        pocoProbables.add(new ItemBuilder().setType(Material.IRON_BOOTS).build());
        pocoProbables.add(new ItemBuilder().setType(Material.IRON_CHESTPLATE).build());
        pocoProbables.add(new ItemBuilder().setType(Material.IRON_LEGGINGS).build());
        pocoProbables.add(new ItemBuilder().setType(Material.IRON_HELMET).build());
        pocoProbables.add(new ItemBuilder().setType(Material.IRON_HELMET).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true).build());
        pocoProbables.add(new ItemBuilder().setType(Material.BOW).build());
        pocoProbables.add(new ItemBuilder().setType(Material.COOKED_CHICKEN).setAmount(2).build());
        pocoProbables.add(new ItemBuilder().setType(Material.SPECTRAL_ARROW).build());
        pocoProbables.add(new ItemBuilder().setType(Material.TORCH).setAmount(2).build());
        pocoProbables.add(BattleRoyale.getInstance().getMoneda());
    }
  
    private static void addEspeciales() {
        especiales.add(new ItemBuilder().setType(Material.DIAMOND_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 3, true).addEnchant(Enchantment.FIRE_ASPECT, 2, true).build());
        especiales.add(new ItemBuilder().setType(Material.DIAMOND_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 5, true).build());
        especiales.add(new ItemBuilder().setType(Material.DIAMOND_SWORD).addEnchant(Enchantment.FIRE_ASPECT, 2, true).addEnchant(Enchantment.KNOCKBACK, 2, true).build());
        especiales.add(new ItemBuilder().setType(Material.DIAMOND_HELMET).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4, true).build());
        especiales.add(new ItemBuilder().setType(Material.DIAMOND_CHESTPLATE).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3, true).addEnchant(Enchantment.PROTECTION_FIRE, 2, true).build());
        especiales.add(new ItemBuilder().setType(Material.DIAMOND_LEGGINGS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3, true).addEnchant(Enchantment.PROTECTION_PROJECTILE, 1, true).build());
        especiales.add(new ItemBuilder().setType(Material.DIAMOND_BOOTS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4, true).build());
        especiales.add(new ItemBuilder().setType(Material.DIAMOND_HELMET).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2, true).addEnchant(Enchantment.PROTECTION_FIRE, 2, true).build());
        especiales.add(new ItemBuilder().setType(Material.DIAMOND_CHESTPLATE).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2, true).addEnchant(Enchantment.THORNS, 2, true).build());
        especiales.add(new ItemBuilder().setType(Material.DIAMOND_LEGGINGS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2, true).addEnchant(Enchantment.PROTECTION_PROJECTILE, 2, true).build());
        especiales.add(new ItemBuilder().setType(Material.DIAMOND_BOOTS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2, true).addEnchant(Enchantment.PROTECTION_FALL, 2, true).build());
        especiales.add(new ItemBuilder().setType(Material.COOKED_BEEF).setAmount(5).build());
        especiales.add(new ItemBuilder().setType(Material.PORK).setAmount(5).build());
        especiales.add(new ItemBuilder().setType(Material.BREAD).setAmount(5).build());
        especiales.add(new ItemBuilder().setType(Material.GOLDEN_APPLE).setAmount(1).build());
        especiales.add(new ItemBuilder().setType(Material.DIAMOND).setAmount(3).build());
        especiales.add(new ItemBuilder().setType(Material.DIAMOND_BLOCK).setAmount(1).build());
        especiales.add(new ItemBuilder().setType(Material.ARROW).setAmount(15).build());
        especiales.add(new ItemBuilder().setType(Material.PORK).setAmount(5).build());
    }

    public static ItemStack[] getRandomItems() {
        int ran = 5;
        ItemStack[] items = new ItemStack[ran];
        for (int i = 0; i < ran; i++) {
            int id = random.nextInt(muyProbables.size() - 1);
            items[i] = ((ItemStack) muyProbables.get(id));
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
