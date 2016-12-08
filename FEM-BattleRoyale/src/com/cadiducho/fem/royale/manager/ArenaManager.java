package com.cadiducho.fem.royale.manager;

import com.cadiducho.fem.core.util.ItemBuilder;
import com.cadiducho.fem.core.util.MerchantBuilder;
import com.cadiducho.fem.core.util.Metodos;
import com.cadiducho.fem.royale.BattlePlayer;
import java.util.ArrayList;
import java.util.Random;
import com.cadiducho.fem.royale.BattleRoyale;
import com.cadiducho.fem.royale.utils.ChestItems;
import lombok.Getter;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;

public class ArenaManager {

    private final BattleRoyale plugin;

    public ArenaManager(BattleRoyale instance) {
        plugin = instance;
        
        gameTime = plugin.getConfig().getInt("gameTime");
        deathMatchTime = plugin.getConfig().getInt("deathMatchTime");
        maxPlayers = plugin.getConfig().getInt("Arena.Max");
        minPlayers = plugin.getConfig().getInt("Arena.Min");
        areaBorder1 = Metodos.stringToLocation(plugin.getConfig().getString("Arena.areaBorder1"));
        areaBorder2 = Metodos.stringToLocation(plugin.getConfig().getString("Arena.areaBorder2"));
        lobby = Metodos.stringToLocation(plugin.getConfig().getString("Arena.Lobby"));
        loadWorld(plugin.getWorld());
        setupVillagersTrades();
        loadSpawn();
    }

    @Getter private final int maxPlayers;
    @Getter private final int minPlayers;
    @Getter private final ArrayList<Location> spawnList = new ArrayList<>();
    @Getter private ArrayList<Location> deathmatchSpawnList = new ArrayList<>();
    @Getter private final ArrayList<Location> chestRandomList = new ArrayList<>();
    @Getter private final ArrayList<Location> chestRefillList = new ArrayList<>();
    @Getter private int spawn;
    @Getter private final Location lobby;
    @Getter private WorldBorder wb;
    Location areaBorder1;
    Location areaBorder2;
    public int gameTime;
    public int deathMatchTime;
    
    @Getter private Merchant vendo;
    @Getter private Merchant compro;
    
    private final Random rand = new Random();
    
    private void loadWorld(World w) {
        w = plugin.getWorld();
        w.setPVP(true);
        w.setGameRuleValue("doDaylightCycle", "false");
        w.setStorm(false);
        w.setDifficulty(Difficulty.EASY);
        w.setTime(6000);
        w.setAutoSave(false);
        
        wb = w.getWorldBorder();
        wb.setCenter(plugin.getConfig().getInt("worldBorderCenterX"), plugin.getConfig().getInt("worldBorderCenterY"));
        wb.setSize(plugin.getConfig().getInt("worldBorderSize"));
        wb.setDamageAmount(1.0);
        wb.setWarningDistance(5);
        wb.setDamageBuffer(1.0);
        wb.setDamageAmount(4.0);
        indexBlocks();
    }

    public void indexBlocks() {
        int topBlockX = (areaBorder1.getBlockX() < areaBorder2.getBlockX() ? areaBorder2.getBlockX() : areaBorder1.getBlockX());
        int bottomBlockX = (areaBorder1.getBlockX() > areaBorder2.getBlockX() ? areaBorder2.getBlockX() : areaBorder1.getBlockX());

        int topBlockY = (areaBorder1.getBlockY() < areaBorder2.getBlockY() ? areaBorder2.getBlockY() : areaBorder1.getBlockY());
        int bottomBlockY = (areaBorder1.getBlockY() > areaBorder2.getBlockY() ? areaBorder2.getBlockY() : areaBorder1.getBlockY());

        int topBlockZ = (areaBorder1.getBlockZ() < areaBorder2.getBlockZ() ? areaBorder2.getBlockZ() : areaBorder1.getBlockZ());
        int bottomBlockZ = (areaBorder1.getBlockZ() > areaBorder2.getBlockZ() ? areaBorder2.getBlockZ() : areaBorder1.getBlockZ());

        for (int x = bottomBlockX; x <= topBlockX; x++) {
            for (int z = bottomBlockZ; z <= topBlockZ; z++) {
                for (int y = bottomBlockY; y <= topBlockY; y++) {
                    Block block = areaBorder1.getWorld().getBlockAt(x, y, z);
                    switch (block.getType()) {
                        case CHEST:
                            Chest chest = (Chest) block.getState();
                            chest.getInventory().clear();
                            Inventory inv = chest.getBlockInventory();
                            inv.clear();
                            fillChest(inv, false);
                            chest.update();
                            chestRefillList.add(block.getLocation());
                            break;
                        case ENDER_PORTAL_FRAME:
                            chestRandomList.add(block.getLocation().add(0D, 2D, 0D));
                            break;
                    }
                }
            }
        }
    }

    public void fillChest(Inventory inv, boolean especial) {
        inv.clear();
        int low = 5;
        int high = 10;
        ArrayList<ItemStack> items = (especial ? ChestItems.especiales : ChestItems.muyProbables);

        for (int i = 0; i < rand.nextInt(high - low) + low; i++) {
            int proporcion = rand.nextInt(100);
            if (!especial) {
                if (proporcion > 50) {
                    items = ChestItems.muyProbables;
                } else if (proporcion < 50 && proporcion > 20) {
                    items = ChestItems.probables;
                } else {
                    items = ChestItems.pocoProbables;
                }
            }
            int rand2 = rand.nextInt(items.size());
            if (especial) {
                if (inv.contains(items.get(rand2).getType())) {
                    rand2 = rand.nextInt(items.size()); //No repetir materiales en los cofres especiales
                }    
            }
            inv.setItem(rand.nextInt(27), items.get(rand2)); 
        }
    }
    
    public void refillChests() {
        chestRefillList.forEach(loc -> {
            Chest chest = (Chest) loc.getBlock().getState();
            chest.getInventory().clear();
            Inventory inv = chest.getBlockInventory();
            inv.clear();
            fillChest(inv, false);
            chest.update();
        });
    }

    public void fixPlayer(Location loc) {
        loc.add(0, 0.5, 0);
    }

    public void teleport(Player player) {
        int rd = rand.nextInt(spawnList.size());
        player.teleport(spawnList.get(rd));
        spawnList.remove(rd);
    }
    
    public void teleportDeathmatch(Player player) {
        int rd = rand.nextInt(deathmatchSpawnList.size());
        player.teleport(deathmatchSpawnList.get(rd));
        deathmatchSpawnList.remove(rd);
    }
    
    public Location spawnRandomChest() {
        int rd = rand.nextInt(chestRandomList.size() - 1);
        Location loc = chestRandomList.get(rd);
        
        /*//Comprobar si la localización está dentro del worldborder
        if (Math.abs(getWb().getCenter().getX()) + getWb().getSize() > loc.getX() ||
                Math.abs(getWb().getCenter().getZ()) + getWb().getSize() > loc.getZ()) {
            //Eliminar localizacion no valida y generar otra
            chestRandomList.remove(rd);
            return spawnRandomChest();
        }*/
        chestRandomList.remove(rd);
        return loc;
    }

    public final void loadSpawn() {
        spawnList.clear();
        try {
            for (int i = 1; i <= maxPlayers; i++) {
                spawnList.add(Metodos.stringToLocation(plugin.getConfig().getString("Arena.Spawn." + i)));
            }
        } catch (Exception e) {
        }
        deathmatchSpawnList = (ArrayList<Location>) spawnList.clone();
    }
    
    private void setupVillagersTrades() {
        compro = plugin.getServer().createMerchant(Metodos.colorizar("&a¡Compro!"));
        vendo = plugin.getServer().createMerchant(Metodos.colorizar("&a¡Vendo!"));
        
        ItemStack moneda2 = plugin.getMoneda().clone();
        moneda2.setAmount(2);
        ItemStack moneda3 = plugin.getMoneda().clone();
        moneda3.setAmount(3);
        ItemStack moneda4 = plugin.getMoneda().clone();
        moneda4.setAmount(4);
        ItemStack moneda5 = plugin.getMoneda().clone();
        moneda5.setAmount(5);
        ItemStack moneda8 = plugin.getMoneda().clone();
        moneda8.setAmount(8);
        ItemStack moneda10 = plugin.getMoneda().clone();
        moneda10.setAmount(10);
        ItemStack moneda15 = plugin.getMoneda().clone();
        moneda15.setAmount(15);
        ItemStack moneda20 = plugin.getMoneda().clone();
        moneda20.setAmount(20);
        ItemStack moneda25 = plugin.getMoneda().clone();
        moneda25.setAmount(25);
        ItemStack moneda30 = plugin.getMoneda().clone();
        moneda30.setAmount(30);

        compro.setRecipe(0, new MerchantBuilder(plugin.getMoneda()).addIngredient(new ItemStack(Material.WOOD_SWORD, 1)).build());
        compro.setRecipe(1, new MerchantBuilder(plugin.getMoneda()).addIngredient(new ItemStack(Material.WOOD_AXE, 1)).build());
        compro.setRecipe(2, new MerchantBuilder(moneda2).addIngredient(new ItemStack(Material.STONE_SWORD, 1)).build());
        compro.setRecipe(3, new MerchantBuilder(moneda2).addIngredient(new ItemStack(Material.STONE_AXE, 1)).build());
        compro.setRecipe(4, new MerchantBuilder(moneda2).addIngredient(new ItemStack(Material.ARROW, 5)).build());
        compro.setRecipe(5, new MerchantBuilder(moneda2).addIngredient(new ItemStack(Material.BOW, 1)).build());
        compro.setRecipe(6, new MerchantBuilder(moneda3).addIngredient(new ItemStack(Material.CHAINMAIL_BOOTS, 1)).build());
        compro.setRecipe(7, new MerchantBuilder(moneda3).addIngredient(new ItemStack(Material.CHAINMAIL_HELMET, 1)).build());
        compro.setRecipe(8, new MerchantBuilder(moneda4).addIngredient(new ItemStack(Material.IRON_SWORD, 1)).build());
        compro.setRecipe(9, new MerchantBuilder(moneda4).addIngredient(new ItemStack(Material.IRON_AXE, 1)).build());
        compro.setRecipe(10, new MerchantBuilder(moneda5).addIngredient(new ItemStack(Material.ELYTRA, 1)).build());
        compro.setRecipe(11, new MerchantBuilder(moneda8).addIngredient(new ItemStack(Material.DIAMOND_BLOCK, 1)).build());
        compro.setRecipe(12, new MerchantBuilder(moneda10).addIngredient(new ItemStack(Material.GOLDEN_APPLE, 1)).build());
        
        vendo.setRecipe(0, new MerchantBuilder(new ItemBuilder().setType(Material.BOW).addEnchant(Enchantment.ARROW_DAMAGE, 1, true).build()).addIngredient(moneda15).build());
        vendo.setRecipe(0, new MerchantBuilder(new ItemBuilder().setType(Material.IRON_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 1, true).build()).addIngredient(moneda15).build());
        vendo.setRecipe(0, new MerchantBuilder(new ItemBuilder().setType(Material.DIAMOND_SWORD).build()).addIngredient(moneda20).build());
        vendo.setRecipe(0, new MerchantBuilder(new ItemBuilder().setType(Material.BOW).addEnchant(Enchantment.ARROW_DAMAGE, 1, true).addEnchant(Enchantment.ARROW_INFINITE, 1, true).addEnchant(Enchantment.ARROW_FIRE, 1, true).build()).addIngredient(moneda25).build());
        vendo.setRecipe(0, new MerchantBuilder(new ItemBuilder().setType(Material.GOLDEN_APPLE).build()).addIngredient(moneda25).build());
        vendo.setRecipe(0, new MerchantBuilder(new ItemBuilder().setType(Material.IRON_SWORD).addEnchant(Enchantment.FIRE_ASPECT, 2, true).build()).addIngredient(moneda25).build());
        
        vendo.setRecipe(0, new MerchantBuilder(new ItemBuilder().setType(Material.IRON_HELMET).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2, true).build()).addIngredient(moneda25).build());
        vendo.setRecipe(0, new MerchantBuilder(new ItemBuilder().setType(Material.IRON_CHESTPLATE).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2, true).build()).addIngredient(moneda25).build());
        vendo.setRecipe(0, new MerchantBuilder(new ItemBuilder().setType(Material.IRON_LEGGINGS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2, true).build()).addIngredient(moneda25).build());
        vendo.setRecipe(0, new MerchantBuilder(new ItemBuilder().setType(Material.IRON_BOOTS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2, true).build()).addIngredient(moneda25).build());
        vendo.setRecipe(0, new MerchantBuilder(new ItemBuilder().setType(Material.DIAMOND_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 2, true).addEnchant(Enchantment.KNOCKBACK, 1, true).build()).addIngredient(moneda25).build());
        vendo.setRecipe(0, new MerchantBuilder(new ItemBuilder().setType(Material.DIAMOND_CHESTPLATE).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3, true).addEnchant(Enchantment.DURABILITY, 1, true).build()).addIngredient(moneda25).build());
        vendo.setRecipe(0, new MerchantBuilder(new ItemBuilder().setType(Material.DIAMOND_LEGGINGS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3, true).addEnchant(Enchantment.DURABILITY, 1, true).build()).addIngredient(moneda30).build());
        
        //Desactivado hasta encontrar MerchantListener
        /*
        vendo.addListener((Merchant merchant, MerchantOffer offer, Player customer) -> {
            final BattlePlayer bp = BattleRoyale.getPlayer(customer);
            bp.getUserData().setBrIntercambios(bp.getUserData().getBrIntercambios() + 1);
        }); 
        compro.addListener((Merchant merchant, MerchantOffer offer, Player customer) -> {
            final BattlePlayer bp = BattleRoyale.getPlayer(customer);
            bp.getUserData().setBrIntercambios(bp.getUserData().getBrIntercambios() + 1);
        });*/
    }
}