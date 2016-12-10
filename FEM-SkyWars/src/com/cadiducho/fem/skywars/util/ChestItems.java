package com.cadiducho.fem.skywars.util;

import com.cadiducho.fem.core.util.ItemBuilder;
import com.cadiducho.fem.skywars.SkyWars;
import java.util.ArrayList;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import org.bukkit.inventory.ItemStack;

//ToDo: Votación de Items chetos, modo de juego...
public final class ChestItems {

    public SkyWars plugin;

    public ChestItems(SkyWars plugin) {
        this.plugin = plugin;
        addArmadura();
        addArmadurasBasic();
        addArmadurasOP();
        addArmas();
        addArmasBasic();
        addArmasOP();
        addComida();
        addComidaBasic();
        addComida();
        addComidaBasic();
        addComidaOP();
        addItemBasic();
        addItems();
        addItemsOP();     
    }

    public void reload() {
        addArmadura();
        addArmadurasBasic();
        addArmadurasOP();
        addArmas();
        addArmasBasic();
        addArmasOP();
        addComida();
        addComidaBasic();
        addComida();
        addComidaBasic();
        addComidaOP();
        addItemBasic();
        addItems();
        addItemsOP();   
    }

    public ArrayList<ItemStack> comida = new ArrayList<>();
    public ArrayList<ItemStack> armas = new ArrayList<>();
    public ArrayList<ItemStack> armaduras = new ArrayList<>();
    public ArrayList<ItemStack> items = new ArrayList<>();

    public ArrayList<ItemStack> comida_op = new ArrayList<>();
    public ArrayList<ItemStack> armas_op = new ArrayList<>();
    public ArrayList<ItemStack> armaduras_op = new ArrayList<>();
    public ArrayList<ItemStack> items_op = new ArrayList<>();

    public ArrayList<ItemStack> comida_basic = new ArrayList<>();
    public ArrayList<ItemStack> armas_basic = new ArrayList<>();
    public ArrayList<ItemStack> armaduras_basic = new ArrayList<>();
    public ArrayList<ItemStack> items_basic = new ArrayList<>();

    public void addComidaBasic() {
        comida_basic.clear();
        ItemStack COMIDA1 = new ItemBuilder(Material.COOKED_CHICKEN).setAmount(1).build();
        comida_basic.add(COMIDA1);
        ItemStack COMIDA2 = new ItemBuilder(Material.COOKED_FISH).setAmount(2).build();
        comida_basic.add(COMIDA2);
        ItemStack COMIDA3 = new ItemBuilder(Material.COOKED_RABBIT).setAmount(2).build();
        comida_basic.add(COMIDA3);
    }

    public void addArmadurasBasic() {
        armaduras_basic.clear();
        ItemStack ARMADURA1 = new ItemBuilder(Material.LEATHER_HELMET).setAmount(1).build();
        armaduras_basic.add(ARMADURA1);
        ItemStack ARMADURA2 = new ItemBuilder(Material.IRON_LEGGINGS).setAmount(1).build();
        armaduras_basic.add(ARMADURA2);
        ItemStack ARMADURA3 = new ItemBuilder(Material.CHAINMAIL_CHESTPLATE).setAmount(1).build();
        armaduras_basic.add(ARMADURA3);
        ItemStack ARMADURA4 = new ItemBuilder(Material.GOLD_BOOTS).setAmount(1).build();
        armaduras_basic.add(ARMADURA4);
    }

    public void addItemBasic() {
        items_basic.clear();
        ItemStack MADERA = new ItemBuilder(Material.WOOD).setAmount(6).build();
        items_basic.add(MADERA);
        ItemStack MADERA2 = new ItemBuilder(Material.WORKBENCH).setAmount(3).build();
        items_basic.add(MADERA2);
        ItemStack SNOWBALL = new ItemBuilder(Material.SNOW_BALL).setAmount(6).build();
        items_basic.add(SNOWBALL);
    }

    public void addArmasBasic() {
        armas_basic.clear();
        ItemStack ESPADA1 = new ItemBuilder(Material.STONE_SWORD).setAmount(1).build();
        armas_basic.add(ESPADA1);
        ItemStack ESPADA2 = new ItemBuilder(Material.WOOD_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 1, false).setAmount(1).build();
        armas_basic.add(ESPADA2);
        ItemStack ARCO = new ItemBuilder(Material.BOW).setAmount(1).build();
        armas_basic.add(ARCO);
        ItemStack CAÑA = new ItemBuilder(Material.FISHING_ROD).setAmount(1).build();
        armas_basic.add(CAÑA);

    }

    //OP
    public void addArmadurasOP() {
        armaduras_op.clear();
        ItemStack ARMADURA = new ItemBuilder(Material.DIAMOND_CHESTPLATE).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3, false).build();
        armaduras_op.add(ARMADURA);
        ItemStack ARMADURA2 = new ItemBuilder(Material.IRON_CHESTPLATE).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4, false).build();
        armaduras_op.add(ARMADURA2);
        ItemStack ARMADURA3 = new ItemBuilder(Material.DIAMOND_HELMET).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2, false).build();
        armaduras_op.add(ARMADURA3);
        ItemStack ARMADURA4 = new ItemBuilder(Material.IRON_LEGGINGS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4, false).build();
        armaduras_op.add(ARMADURA4);
        ItemStack ARMADURA5 = new ItemBuilder(Material.DIAMOND_LEGGINGS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2, false).build();
        armaduras_op.add(ARMADURA5);
        ItemStack ARMADURA6 = new ItemBuilder(Material.GOLD_BOOTS).addEnchant(Enchantment.PROTECTION_FALL, 6, true).build();
        armaduras_op.add(ARMADURA6);

    }

    public void addComidaOP() {
        comida_op.clear();
        ItemStack MANZANA_OP = new ItemStack(Material.GOLDEN_APPLE, 1, (byte) 1);
        comida_op.add(MANZANA_OP);

        ItemStack BEEF_OP = new ItemBuilder(Material.COOKED_BEEF).setAmount(16).build();
        comida_op.add(BEEF_OP);

        ItemStack MANZANA_OP2 = new ItemBuilder(Material.GOLDEN_APPLE).setAmount(16).build();
        comida_op.add(MANZANA_OP2);

        ItemStack GALLETA = new ItemBuilder(Material.COOKIE).setAmount(1).addEnchant(Enchantment.DAMAGE_ALL, 6, true).build();
        comida_op.add(GALLETA);

    }

    public void addArmasOP() {
        armas_op.clear();
        ItemStack ESPÁDA = new ItemBuilder(Material.WOOD_SWORD).setAmount(1).addEnchant(Enchantment.KNOCKBACK, 2, false).build();
        armas_op.add(ESPÁDA);
        ItemStack ESPADA2 = new ItemBuilder(Material.DIAMOND_SWORD).setAmount(1).addEnchant(Enchantment.DAMAGE_ALL, 5, false).build();
        armas_op.add(ESPADA2);
        ItemStack ESPADA3 = new ItemBuilder(Material.IRON_SWORD).setAmount(1).addEnchant(Enchantment.FIRE_ASPECT, 2, false).build();
        armas_op.add(ESPADA3);
        ItemStack ARCO = new ItemBuilder(Material.BOW).setAmount(1).addEnchant(Enchantment.ARROW_DAMAGE, 3, false).build();
        armas_op.add(ARCO);
        ItemStack ARCO2 = new ItemBuilder(Material.BOW).setAmount(1).addEnchant(Enchantment.ARROW_FIRE, 1, false).build();
        armas_op.add(ARCO2);
        ItemStack ARCO3 = new ItemBuilder(Material.BOW).setAmount(1).addEnchant(Enchantment.ARROW_KNOCKBACK, 4, true).setDisplayName("§3Jodedor de Vídeos").build();
        armas_op.add(ARCO3);

    }

    public void addItemsOP() {
        items_op.clear();
        ItemStack HUEVOS = new ItemBuilder(Material.EGG).setAmount(64).build();
        items_op.add(HUEVOS);
        ItemStack BOLAS = new ItemBuilder(Material.SNOW_BALL).setAmount(64).build();
        items_op.add(BOLAS);
        ItemStack ARROWS = new ItemBuilder(Material.ARROW).setAmount(32).build();
        items_op.add(ARROWS);
        ItemStack BLOQUES = new ItemBuilder(Material.STONE).setAmount(64).build();
        items_op.add(BLOQUES);
        ItemStack BLOQUES2 = new ItemBuilder(Material.WOOD).setAmount(64).build();
        items_op.add(BLOQUES2);
        ItemStack XP = new ItemBuilder(Material.EXP_BOTTLE).setAmount(64).build();
        items_op.add(XP);
        ItemStack LAPIZ = new ItemStack(Material.INK_SACK, 64, (byte) 4);
        items_op.add(LAPIZ);
        ItemStack LAVA = new ItemBuilder(Material.LAVA_BUCKET).setAmount(1).build();
        items_op.add(LAVA);
        ItemStack AGUA = new ItemBuilder(Material.WATER_BUCKET).setAmount(1).build();
        items_op.add(AGUA);

    }

    //NORMAL
    public void addComida() {
        comida.clear();
        ItemStack COMIDA1 = new ItemBuilder(Material.COOKED_BEEF).setAmount(6).build();
        comida.add(COMIDA1);
        ItemStack COMIDA2 = new ItemBuilder(Material.APPLE).setAmount(1).build();
        comida.add(COMIDA2);
        ItemStack COMIDA3 = new ItemBuilder(Material.GOLDEN_CARROT).setAmount(3).build();
        comida.add(COMIDA3);
        ItemStack COMIDA4 = new ItemBuilder(Material.COOKIE).setAmount(6).setDisplayName("§dGalleta de Felipe Fonseca").build();
        comida.add(COMIDA4);
        ItemStack ami_français = new ItemBuilder(Material.COOKED_CHICKEN).setAmount(2).setDisplayName("§7poulet GrayyChocolate").build();
        comida.add(ami_français);
        ItemStack GOLDENAPPLE = new ItemBuilder(Material.GOLDEN_APPLE).build();
        comida.add(GOLDENAPPLE);

    }

    public void addArmas() {
        armas.clear();
        ItemStack ESPADA1 = new ItemBuilder(Material.WOOD_SWORD).setAmount(1).addEnchant(Enchantment.DAMAGE_ALL, 1, false).build();
        armas.add(ESPADA1);
        ItemStack ESPADA2 = new ItemBuilder(Material.IRON_SWORD).setAmount(1).addEnchant(Enchantment.DURABILITY, 3, false).build();
        armas.add(ESPADA2);
        ItemStack ESPADA3 = new ItemBuilder(Material.DIAMOND_SWORD).setAmount(1).build();
        armas.add(ESPADA3);
        ItemStack ESPADA4 = new ItemBuilder(Material.STONE_AXE).setAmount(1).addEnchant(Enchantment.DAMAGE_ALL, 1, false).build();
        armas.add(ESPADA4);
        ItemStack ARCO1 = new ItemBuilder(Material.BOW).setAmount(1).build();
        armas.add(ARCO1);
        ItemStack ARCO2 = new ItemBuilder(Material.BOW).setAmount(1).addEnchant(Enchantment.ARROW_DAMAGE, 1, true).build();
        armas.add(ARCO2);
        ItemStack CAÑA = new ItemBuilder(Material.FISHING_ROD).setAmount(1).build();
        armas.add(CAÑA);
        ItemStack PICO = new ItemBuilder(Material.DIAMOND_PICKAXE).setAmount(1).addEnchant(Enchantment.LUCK, 2, true).build();
        armas.add(PICO);
        ItemStack PICO2 = new ItemBuilder(Material.IRON_PICKAXE).setAmount(1).addEnchant(Enchantment.DAMAGE_ALL, 1, true).build();
        armas.add(PICO2);
    }

    public void addArmadura() {
        armaduras.clear();
        ItemStack ARMADURA1 = new ItemBuilder(Material.IRON_HELMET).setAmount(1).build();
        ItemStack ARMADURA2 = new ItemBuilder(Material.IRON_CHESTPLATE).setAmount(1).build();
        ItemStack ARMADURA3 = new ItemBuilder(Material.IRON_LEGGINGS).setAmount(1).build();
        ItemStack ARMADURA4 = new ItemBuilder(Material.IRON_BOOTS).setAmount(1).build();
        ItemStack ARMADURA5 = new ItemBuilder(Material.GOLD_BOOTS).setAmount(1).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, false).build();
        ItemStack ARMADURA6 = new ItemBuilder(Material.DIAMOND_CHESTPLATE).setAmount(1).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true).build();
        ItemStack ARMADURA7 = new ItemBuilder(Material.DIAMOND_BOOTS).setAmount(1).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, false).build();
        ItemStack ARMADURA8 = new ItemBuilder(Material.LEATHER_CHESTPLATE).setAmount(1).addEnchant(Enchantment.PROTECTION_PROJECTILE, 2, true).build();
        ItemStack ARMADURA9 = new ItemBuilder(Material.CHAINMAIL_CHESTPLATE).setAmount(1).build();
        armaduras.add(ARMADURA1);
        armaduras.add(ARMADURA2);
        armaduras.add(ARMADURA3);
        armaduras.add(ARMADURA4);
        armaduras.add(ARMADURA5);
        armaduras.add(ARMADURA6);
        armaduras.add(ARMADURA7);
        armaduras.add(ARMADURA8);
        armaduras.add(ARMADURA9);
    }

    public void addItems() {
        items.clear();
        ItemStack XP = new ItemBuilder(Material.EXP_BOTTLE).setAmount(8).build();
        items.add(XP);
        ItemStack LAPIZ = new ItemStack(Material.INK_SACK, 3, (byte) 4);
        items.add(LAPIZ);
        ItemStack BLOQUES1 = new ItemBuilder(Material.STONE).setAmount(32).build();
        items.add(BLOQUES1);
        ItemStack BLOQUES2 = new ItemBuilder(Material.WOOD).setAmount(32).build();
        items.add(BLOQUES2);
        ItemStack BLOQUES3 = new ItemBuilder(Material.LOG).setAmount(16).build();
        items.add(BLOQUES3);
        ItemStack FLECHAS = new ItemBuilder(Material.ARROW).setAmount(16).build();
        items.add(FLECHAS);
        ItemStack SNOWBALL = new ItemBuilder(Material.SNOW_BALL).setAmount(16).build();
        items.add(SNOWBALL);
        ItemStack LAVA = new ItemBuilder(Material.LAVA_BUCKET).setAmount(1).build();
        items.add(LAVA);
        ItemStack AGUA = new ItemBuilder(Material.WATER_BUCKET).setAmount(1).build();
        items.add(AGUA);
    }
}