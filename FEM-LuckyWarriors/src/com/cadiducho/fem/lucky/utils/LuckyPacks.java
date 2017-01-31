package com.cadiducho.fem.lucky.utils;

import com.cadiducho.fem.core.util.ItemBuilder;
import com.cadiducho.fem.core.util.ItemUtil;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class LuckyPacks {

    public static final ArrayList<List<ItemStack>> NORMAL = new ArrayList<>(); //Objetos útiles y básicos, con los que craftear o defenderte de forma sencilla.
    public static final ArrayList<List<ItemStack>> UTIL = new ArrayList<>(); //Armas con encantamientos simples (Nivel 15 o inferior), armaduras y otros objetos más necesarios.
    public static final ArrayList<List<ItemStack>> MIERDA = new ArrayList<>(); //Objetos inútiles. Objetos “troll” con efectos adversos. Objetos de broma o absurdos.
    public static final ArrayList<List<ItemStack>> CHETO = new ArrayList<>(); //Armas con encantamientos (Nivel 16 a 30), diamante, objetos especiales.
    
    private static final Random random = new Random();

    /**
     * Cargar todos los objetos a las listas cuando el plugin se inicie
     */
    public static void initItems() {
        NORMAL.clear();
        UTIL.clear();
        MIERDA.clear();
        CHETO.clear();
        
        addNormal();
        addUtil();
        addMierda();
        addChetos();
    }

    /*
     * Agregarlos por categoría 
     */
    private static void addMierda() {
        MIERDA.add(Arrays.asList(new ItemBuilder().setType(Material.SAPLING).build(),
                new ItemBuilder().setType(Material.SAPLING).build(),
                new ItemBuilder().setType(Material.SAPLING).build(),
                new ItemBuilder().setType(Material.SAPLING).build(),
                new ItemBuilder().setType(Material.SAPLING).build()));
        MIERDA.add(Arrays.asList(new ItemBuilder().setType(Material.DOUBLE_PLANT).build(),
                new ItemBuilder().setType(Material.RED_ROSE).build(),
                new ItemBuilder().setType(Material.YELLOW_FLOWER).build()));
        MIERDA.add(Arrays.asList(ItemUtil.createWool("", DyeColor.WHITE),
                ItemUtil.createWool("", DyeColor.LIME),
                ItemUtil.createWool("", DyeColor.PINK),
                ItemUtil.createWool("", DyeColor.GRAY),
                ItemUtil.createWool("", DyeColor.GREEN),
                ItemUtil.createWool("", DyeColor.PURPLE),
                ItemUtil.createWool("", DyeColor.LIGHT_BLUE),
                ItemUtil.createWool("", DyeColor.YELLOW)));
        MIERDA.add(Arrays.asList(ItemUtil.createGlass("", "", DyeColor.WHITE),
                ItemUtil.createGlass("", "", DyeColor.LIME),
                ItemUtil.createGlass("", "", DyeColor.PINK),
                ItemUtil.createGlass("", "", DyeColor.GRAY),
                ItemUtil.createGlass("", "", DyeColor.GREEN),
                ItemUtil.createGlass("", "", DyeColor.PURPLE),
                ItemUtil.createGlass("", "", DyeColor.LIGHT_BLUE),
                ItemUtil.createGlass("", "", DyeColor.YELLOW)));
        MIERDA.add(Arrays.asList(new ItemBuilder().setType(Material.SOUL_SAND).addEnchant(Enchantment.DURABILITY, 1, true).setDisplayName("Zurullo Bendito").build()));
        MIERDA.add(Arrays.asList(new ItemBuilder().setType(Material.JUKEBOX).build(),
                new ItemBuilder().setType(Material.RECORD_10).build(),
                new ItemBuilder().setType(Material.RECORD_3).build(),
                new ItemBuilder().setType(Material.RECORD_5).build(),
                new ItemBuilder().setType(Material.RECORD_7).build(),
                new ItemBuilder().setType(Material.RECORD_6).build(),
                new ItemBuilder().setType(Material.RECORD_8).build(),
                new ItemBuilder().setType(Material.RECORD_10).build()));
        MIERDA.add(Arrays.asList(new ItemBuilder().setType(Material.BOAT).build(),
                new ItemBuilder().setType(Material.MINECART).build()));
        MIERDA.add(Arrays.asList(ItemUtil.createBanner("Kit de rendición", "¡Cobarde!", DyeColor.WHITE)));
        MIERDA.add(Arrays.asList(new ItemBuilder().setType(Material.FIREWORK).setAmount(5).build()));
        MIERDA.add(Arrays.asList(new ItemBuilder().setType(Material.CARROT_STICK).build(),
                new ItemBuilder().setType(Material.SADDLE).build(),
                new ItemBuilder().setType(Material.PORK).build()));
        MIERDA.add(Arrays.asList(new ItemBuilder().setType(Material.RED_ROSE).setAmount(5).setDisplayName("Sangre 0+").build()));
        MIERDA.add(Arrays.asList(new ItemStack(Material.RAW_FISH, 1, (short) 2)));
        MIERDA.add(Arrays.asList(new ItemBuilder().setType(Material.EGG).setAmount(20).build()));
        MIERDA.add(Arrays.asList(new ItemBuilder().setType(Material.FISHING_ROD).build(),
                new ItemStack(Material.RAW_FISH, 1),
                new ItemStack(Material.RAW_FISH, 1, (short) 2),
                new ItemStack(Material.RAW_FISH, 1, (short) 3)));
        MIERDA.add(Arrays.asList(new ItemBuilder().setType(Material.SNOW_BALL).setAmount(10).build(),
                new ItemBuilder().setType(Material.PACKED_ICE).setAmount(10).build(),
                new ItemBuilder().setType(Material.ICE).build(),
                new ItemBuilder().setType(Material.SNOW_BLOCK).setAmount(10).build()));
        MIERDA.add(Arrays.asList(new ItemBuilder().setType(Material.REDSTONE).setAmount(16).build(),
                new ItemBuilder().setType(Material.DAYLIGHT_DETECTOR).build(),
                new ItemBuilder().setType(Material.REDSTONE_LAMP_OFF).build(),
                new ItemBuilder().setType(Material.PISTON_BASE).build(),
                new ItemBuilder().setType(Material.LEVER).build()));
        MIERDA.add(Arrays.asList(new ItemBuilder().setType(Material.BOWL).build(),
                new ItemBuilder().setType(Material.MUSHROOM_SOUP).build(),
                new ItemBuilder().setType(Material.RABBIT_STEW).build()));
        MIERDA.add(Arrays.asList(new ItemBuilder().setType(Material.SLIME_BLOCK).setAmount(4).build(),
                new ItemBuilder().setType(Material.SLIME_BALL).setAmount(8).build()));
        MIERDA.add(Arrays.asList(new ItemBuilder().setType(Material.PUMPKIN).setAmount(4).build(),
                new ItemBuilder().setType(Material.MELON_BLOCK).setAmount(4).build()));
        MIERDA.add(Arrays.asList(new ItemBuilder().setType(Material.TORCH).setAmount(32).build()));
        MIERDA.add(Arrays.asList(new ItemBuilder().setType(Material.NAME_TAG).setDisplayName("#UnderGamesMola").build(),
                new ItemBuilder().setType(Material.NAME_TAG).setDisplayName("#SubForSub").build(),
                new ItemBuilder().setType(Material.NAME_TAG).setDisplayName("Insertar Texto Aquí").build()));
        MIERDA.add(Arrays.asList(new ItemBuilder().setType(Material.DRAGON_EGG).setDisplayName("¡Te quiero un huevo!").build()));
        MIERDA.add(Arrays.asList(new ItemBuilder().setType(Material.BRICK).setDisplayName("Noquia 3310").build()));
        MIERDA.add(Arrays.asList(new ItemBuilder().setType(Material.POTION).addPotionType(PotionType.POISON, 8, 1).setDisplayName("Virus-T").build()));
    }
    
    private static void addNormal() {
        NORMAL.add(Arrays.asList(new ItemBuilder().setType(Material.WEB).setAmount(8).build()));
        NORMAL.add(Arrays.asList(new ItemBuilder().setType(Material.EXP_BOTTLE).setAmount(4).build(),
                new ItemBuilder().setType(Material.LAPIS_ORE).setAmount(6).build()));
        NORMAL.add(Arrays.asList(new ItemBuilder().setType(Material.WOOD).build(),
                new ItemBuilder().setType(Material.WOOD).build(),
                new ItemBuilder().setType(Material.WOOD).build(),
                new ItemBuilder().setType(Material.WOOD).build(),
                new ItemBuilder().setType(Material.WOOD).build()));
        NORMAL.add(Arrays.asList(new ItemBuilder().setType(Material.STICK).setAmount(4).build(),
                new ItemBuilder().setType(Material.COAL).setAmount(4).build(),
                new ItemBuilder().setType(Material.IRON_INGOT).setAmount(4).build(),
                new ItemBuilder().setType(Material.GOLD_INGOT).setAmount(4).build(),
                new ItemBuilder().setType(Material.LAPIS_ORE).setAmount(2).build(),
                new ItemBuilder().setType(Material.DIAMOND).setAmount(2).build()));
        NORMAL.add(Arrays.asList(new ItemBuilder().setType(Material.WOOD_SWORD).build(), 
                new ItemBuilder().setType(Material.WOOD_AXE).build(), 
                new ItemBuilder().setType(Material.WOOD_SPADE).build(), 
                new ItemBuilder().setType(Material.WOOD_PICKAXE).build()));
        NORMAL.add(Arrays.asList(new ItemBuilder().setType(Material.WATER_BUCKET).build()));
        NORMAL.add(Arrays.asList(new ItemBuilder().setType(Material.ARROW).setAmount(8).build(),
                new ItemBuilder().setType(Material.BOW).build()));
        NORMAL.add(Arrays.asList(new ItemBuilder().setType(Material.HAY_BLOCK).setAmount(3).build(),
                new ItemBuilder().setType(Material.WHEAT).setAmount(3).build(),
                new ItemBuilder().setType(Material.BREAD).setAmount(3).build()));
        NORMAL.add(Arrays.asList(new ItemBuilder().setType(Material.FLINT_AND_STEEL).build()));
        NORMAL.add(Arrays.asList(new ItemBuilder().setType(Material.LAVA_BUCKET).build()));
        NORMAL.add(Arrays.asList(new ItemBuilder().setType(Material.IRON_PICKAXE).build(),
                new ItemBuilder().setType(Material.IRON_AXE).build(),
                new ItemBuilder().setType(Material.IRON_SPADE).build()));
        NORMAL.add(Arrays.asList(new ItemBuilder().setType(Material.STONE_SWORD).addEnchant(Enchantment.KNOCKBACK, 2, true).build()));
        NORMAL.add(Arrays.asList(new ItemBuilder().setType(Material.STONE_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 1, true).build()));
        NORMAL.add(Arrays.asList(new ItemBuilder().setType(Material.STONE_SWORD).addEnchant(Enchantment.DAMAGE_UNDEAD, 2, true).build()));
        NORMAL.add(Arrays.asList(new ItemBuilder().setType(Material.GOLD_SWORD).addEnchant(Enchantment.KNOCKBACK, 1, true).addEnchant(Enchantment.DAMAGE_ALL, 2, true).build()));
        NORMAL.add(Arrays.asList(new ItemBuilder().setType(Material.GOLD_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 2, true).addEnchant(Enchantment.DURABILITY, 2, true).build()));
        NORMAL.add(Arrays.asList(new ItemBuilder().setType(Material.GOLD_SWORD).addEnchant(Enchantment.DURABILITY, 3, true).addEnchant(Enchantment.DAMAGE_UNDEAD, 1, true).build()));
        NORMAL.add(Arrays.asList(new ItemBuilder().setType(Material.IRON_SWORD).build()));
        NORMAL.add(Arrays.asList(new ItemBuilder().setType(Material.LEATHER_HELMET).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true).build(),
                new ItemBuilder().setType(Material.LEATHER_CHESTPLATE).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true).build(),
                new ItemBuilder().setType(Material.LEATHER_LEGGINGS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true).build(),
                new ItemBuilder().setType(Material.LEATHER_BOOTS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true).build()));
        NORMAL.add(Arrays.asList(new ItemBuilder().setType(Material.LEATHER_CHESTPLATE).addEnchant(Enchantment.PROTECTION_PROJECTILE, 2, true).addEnchant(Enchantment.THORNS, 1, true).build(),
                new ItemBuilder().setType(Material.LEATHER_BOOTS).build()));
        NORMAL.add(Arrays.asList(new ItemBuilder().setType(Material.GOLD_HELMET).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2, true).build(),
                new ItemBuilder().setType(Material.GOLD_LEGGINGS).addEnchant(Enchantment.PROTECTION_FIRE, 1, true).build()));
        NORMAL.add(Arrays.asList(new ItemBuilder().setType(Material.CAKE).build(),
                new ItemBuilder().setType(Material.COOKIE).setAmount(16).build(),
                new ItemBuilder().setType(Material.PUMPKIN_PIE).build()));
        NORMAL.add(Arrays.asList(new ItemBuilder().setType(Material.COOKED_BEEF).setAmount(4).build(),
                new ItemBuilder().setType(Material.COOKED_MUTTON).setAmount(4).build(),
                new ItemBuilder().setType(Material.PORK).setAmount(4).build()));
        NORMAL.add(Arrays.asList(new ItemBuilder().setType(Material.COOKED_FISH).setAmount(4).build(),
                new ItemBuilder().setType(Material.COOKED_CHICKEN).setAmount(4).build(),
                new ItemBuilder().setType(Material.COOKED_RABBIT).setAmount(4).build()));
    }
    
    private static void addUtil() {
        UTIL.add(Arrays.asList(new ItemBuilder().setType(Material.ARROW).setAmount(16).build()));
        UTIL.add(Arrays.asList(new ItemBuilder().setType(Material.IRON_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 3, true).addEnchant(Enchantment.DAMAGE_ARTHROPODS, 3, true).build()));
        UTIL.add(Arrays.asList(new ItemBuilder().setType(Material.IRON_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 2, true).addEnchant(Enchantment.KNOCKBACK, 1, true).addEnchant(Enchantment.FIRE_ASPECT, 1, true).build()));
        UTIL.add(Arrays.asList(new ItemBuilder().setType(Material.IRON_SWORD).addEnchant(Enchantment.KNOCKBACK, 2, true).addEnchant(Enchantment.FIRE_ASPECT, 2, true).build()));
        UTIL.add(Arrays.asList(new ItemBuilder().setType(Material.IRON_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 3, true).addEnchant(Enchantment.DAMAGE_UNDEAD, 2, true).addEnchant(Enchantment.DAMAGE_ARTHROPODS, 2, true).build()));
        UTIL.add(Arrays.asList(new ItemBuilder().setType(Material.DIAMOND_SWORD).build()));
        UTIL.add(Arrays.asList(new ItemBuilder().setType(Material.ARROW).setAmount(16).build(), 
                new ItemBuilder().setType(Material.BOW).addEnchant(Enchantment.ARROW_DAMAGE, 2, true).addEnchant(Enchantment.ARROW_KNOCKBACK, 1, true).build()));
        UTIL.add(Arrays.asList(new ItemBuilder().setType(Material.ARROW).setAmount(8).build(), 
                new ItemBuilder().setType(Material.BOW).addEnchant(Enchantment.ARROW_DAMAGE, 2, true).addEnchant(Enchantment.ARROW_KNOCKBACK, 1, true).addEnchant(Enchantment.ARROW_FIRE, 1, true).build()));
        UTIL.add(Arrays.asList(new ItemBuilder().setType(Material.POTION).addPotionType(PotionType.REGEN, 5).build(),
                new ItemBuilder().setType(Material.POTION).addPotionType(PotionType.STRENGTH, 15).build()));
        //TODO: Splash
        UTIL.add(Arrays.asList(new ItemBuilder().setType(Material.POTION).addPotionType(PotionType.INSTANT_DAMAGE, 1).build(),
                new ItemBuilder().setType(Material.POTION).addPotionType(PotionType.WEAKNESS, 20).build()));
        UTIL.add(Arrays.asList(new ItemBuilder().setType(Material.CHAINMAIL_LEGGINGS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2, true).build(), 
                new ItemBuilder().setType(Material.CHAINMAIL_BOOTS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2, true).build()));
        UTIL.add(Arrays.asList(new ItemBuilder().setType(Material.CHAINMAIL_CHESTPLATE).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2, true).build(), 
                new ItemBuilder().setType(Material.CHAINMAIL_HELMET).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2, true).build()));
        UTIL.add(Arrays.asList(new ItemBuilder().setType(Material.IRON_HELMET).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2, true).addEnchant(Enchantment.PROTECTION_EXPLOSIONS, 1, true).build(), 
                new ItemBuilder().setType(Material.IRON_LEGGINGS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2, true).addEnchant(Enchantment.PROTECTION_FIRE, 1, true).build()));
        UTIL.add(Arrays.asList(new ItemBuilder().setType(Material.IRON_CHESTPLATE).addEnchant(Enchantment.THORNS, 2, true).build(), 
                new ItemBuilder().setType(Material.IRON_BOOTS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2, true).addEnchant(Enchantment.THORNS, 2, true).build()));
        UTIL.add(Arrays.asList(new ItemBuilder().setType(Material.CHAINMAIL_HELMET).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2, true).addEnchant(Enchantment.PROTECTION_FIRE, 2, true).build()));
        UTIL.add(Arrays.asList(new ItemBuilder().setType(Material.CHAINMAIL_CHESTPLATE).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2, true).addEnchant(Enchantment.PROTECTION_EXPLOSIONS, 2, true).build()));
        UTIL.add(Arrays.asList(new ItemBuilder().setType(Material.CHAINMAIL_LEGGINGS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2, true).addEnchant(Enchantment.PROTECTION_PROJECTILE, 2, true).build()));
        UTIL.add(Arrays.asList(new ItemBuilder().setType(Material.CHAINMAIL_BOOTS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2, true).addEnchant(Enchantment.PROTECTION_PROJECTILE, 2, true).build()));
        UTIL.add(Arrays.asList(new ItemBuilder().setType(Material.IRON_HELMET).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2, true).addEnchant(Enchantment.PROTECTION_PROJECTILE, 2, true).addEnchant(Enchantment.PROTECTION_FIRE, 2, true).build()));
        UTIL.add(Arrays.asList(new ItemBuilder().setType(Material.IRON_CHESTPLATE).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2, true).addEnchant(Enchantment.THORNS, 2, true).addEnchant(Enchantment.PROTECTION_EXPLOSIONS, 1, true).build()));
        UTIL.add(Arrays.asList(new ItemBuilder().setType(Material.IRON_LEGGINGS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2, true).addEnchant(Enchantment.PROTECTION_PROJECTILE, 1, true).addEnchant(Enchantment.PROTECTION_FIRE, 1, true).addEnchant(Enchantment.THORNS, 1, true).build()));
        UTIL.add(Arrays.asList(new ItemBuilder().setType(Material.IRON_BOOTS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2, true).addEnchant(Enchantment.PROTECTION_FALL, 2, true).addEnchant(Enchantment.PROTECTION_EXPLOSIONS, 1, true).addEnchant(Enchantment.THORNS, 1, true).build()));
    }

    private static void addChetos() {
        CHETO.add(Arrays.asList(new ItemBuilder().setType(Material.DIAMOND_HELMET).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4, true).build()));
        CHETO.add(Arrays.asList(new ItemBuilder().setType(Material.DIAMOND_HELMET).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3, true).addEnchant(Enchantment.PROTECTION_FIRE, 3, true).build()));
        CHETO.add(Arrays.asList(new ItemBuilder().setType(Material.DIAMOND_CHESTPLATE).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4, true).build()));
        CHETO.add(Arrays.asList(new ItemBuilder().setType(Material.DIAMOND_CHESTPLATE).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3, true).addEnchant(Enchantment.THORNS, 3, true).build()));
        CHETO.add(Arrays.asList(new ItemBuilder().setType(Material.DIAMOND_LEGGINGS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4, true).build()));
        CHETO.add(Arrays.asList(new ItemBuilder().setType(Material.DIAMOND_LEGGINGS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3, true).addEnchant(Enchantment.PROTECTION_PROJECTILE, 3, true).build()));
        CHETO.add(Arrays.asList(new ItemBuilder().setType(Material.DIAMOND_BOOTS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4, true).build()));
        CHETO.add(Arrays.asList(new ItemBuilder().setType(Material.DIAMOND_BOOTS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3, true).addEnchant(Enchantment.PROTECTION_EXPLOSIONS, 3, true).build()));
        CHETO.add(Arrays.asList(new ItemBuilder().setType(Material.DIAMOND_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 5, true).build()));
        CHETO.add(Arrays.asList(new ItemBuilder().setType(Material.DIAMOND_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 3, true).addEnchant(Enchantment.FIRE_ASPECT, 1, true).build()));
        CHETO.add(Arrays.asList(new ItemBuilder().setType(Material.DIAMOND_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 2, true).addEnchant(Enchantment.KNOCKBACK, 2, true).addEnchant(Enchantment.FIRE_ASPECT, 2, true).setDisplayName("Espada Lanzadora").build()));
        CHETO.add(Arrays.asList(new ItemBuilder().setType(Material.DIAMOND_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 5, true).addEnchant(Enchantment.FIRE_ASPECT, 2, true).setDisplayName("Espada Infernal").build()));
        CHETO.add(Arrays.asList(new ItemBuilder().setType(Material.DIAMOND_HELMET).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4, true).addEnchant(Enchantment.PROTECTION_FIRE, 4, true).addEnchant(Enchantment.THORNS, 3, true).setDisplayName("Casco Espinoso").build()));
        CHETO.add(Arrays.asList(new ItemBuilder().setType(Material.DIAMOND_BOOTS).addEnchant(Enchantment.PROTECTION_FALL, 4, true).addEnchant(Enchantment.PROTECTION_EXPLOSIONS, 4, true).addEnchant(Enchantment.PROTECTION_PROJECTILE, 4, true).setDisplayName("Botas Celestiales").build()));
        CHETO.add(Arrays.asList(new ItemBuilder().setType(Material.POTION).addPotionType(PotionType.POISON, 10).setAmount(2).setDisplayName("Granada").build(),
                new ItemBuilder().setType(Material.POTION).addPotionType(PotionType.SLOWNESS, 10).setAmount(2).setDisplayName("Congelador").build()));
        CHETO.add(Arrays.asList(new ItemBuilder().setType(Material.POTION).addPotionType(PotionType.REGEN, 10).setAmount(2).setDisplayName("Botiquín").build(),
                new ItemBuilder().setType(Material.POTION).addPotionType(PotionType.INVISIBILITY, 120).setAmount(2).setDisplayName("Desintegrador").build()));
        CHETO.add(Arrays.asList(new ItemBuilder().setType(Material.POTION).addPotionType(PotionType.STRENGTH, 30).setAmount(2).setDisplayName("Esteroides").build(),
                new ItemBuilder().setType(Material.POTION).addPotionType(PotionType.SPEED, 150).setAmount(2).setDisplayName("RedPull").build()));
        CHETO.add(Arrays.asList(new ItemBuilder().setType(Material.ARROW).setAmount(16).build(), 
                new ItemBuilder().setType(Material.BOW).addEnchant(Enchantment.ARROW_DAMAGE, 5, true).addEnchant(Enchantment.ARROW_FIRE, 1, true).addEnchant(Enchantment.ARROW_KNOCKBACK, 2, true).setDisplayName("Chispeante X").build()));
        CHETO.add(Arrays.asList(new ItemBuilder().setType(Material.ARROW).build(), 
                new ItemBuilder().setType(Material.BOW).addEnchant(Enchantment.ARROW_DAMAGE, 3, true).addEnchant(Enchantment.ARROW_INFINITE, 1, true).addEnchant(Enchantment.ARROW_KNOCKBACK, 2, true).setDisplayName("Infinator 2000").build()));
        CHETO.add(Arrays.asList(new ItemBuilder().setType(Material.DIAMOND_AXE).addEnchant(Enchantment.DAMAGE_ARTHROPODS, 5, true).addEnchant(Enchantment.DAMAGE_ALL, 4, true).addEnchant(Enchantment.DAMAGE_UNDEAD, 1, true).setDisplayName("Machacador").build()));
        CHETO.add(Arrays.asList(new ItemBuilder().setType(Material.GOLDEN_APPLE).setAmount(2).build()));
        CHETO.add(Arrays.asList(new ItemStack(Material.GOLDEN_APPLE, 1, (short)1)));
    }
    
    /*
     * Droppear los packs aleatoriamente en la categoría
     */
    public static void getRandomPackUtil(Location loc) {
        int id = random.nextInt(UTIL.size() - 1);
        UTIL.get(id).forEach(is -> loc.getWorld().dropItemNaturally(loc, is));
    }
    
    public static void getRandomPackGenericos(Location loc) {
        int id = random.nextInt(NORMAL.size() - 1);
        NORMAL.get(id).forEach(is -> loc.getWorld().dropItemNaturally(loc, is));
    }
    
    public static void getRandomPackMierda(Location loc) {
        int id = random.nextInt(MIERDA.size() - 1);
        MIERDA.get(id).forEach(is -> loc.getWorld().dropItemNaturally(loc, is));
    }
    
    public static void getRandomPackChetos(Location loc) {
        int id = random.nextInt(CHETO.size() - 1);
        CHETO.get(id).forEach(is -> loc.getWorld().dropItemNaturally(loc, is));
    }
}
