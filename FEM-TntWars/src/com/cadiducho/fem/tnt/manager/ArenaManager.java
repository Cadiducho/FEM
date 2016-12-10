package com.cadiducho.fem.tnt.manager;

import com.cadiducho.fem.core.util.ItemBuilder;
import com.cadiducho.fem.core.util.MerchantBuilder;
import com.cadiducho.fem.tnt.TntWars;
import com.cadiducho.fem.core.util.Metodos;
import com.cadiducho.fem.tnt.Generador;
import com.cadiducho.fem.tnt.Generador.GenType;
import com.cadiducho.fem.tnt.TntIsland;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
import org.bukkit.potion.PotionType;

public class ArenaManager {

    private final TntWars plugin;
    private final Random r = new Random();

    @Getter private int minPlayers = 2;
    @Getter private int maxPlayers = 16;
    @Getter private final ArrayList<TntIsland> islas = new ArrayList<>();
    @Getter private final ArrayList<Generador> generadores = new ArrayList<>();
    @Getter @Setter private ArrayList<TntIsland> unAssignedIslas = new ArrayList<>();
    Location areaBorder1;
    Location areaBorder2;
    @Getter private final Location lobby;
    
    //Aldeanos
    @Getter private Merchant buildingShop;
    @Getter private Merchant weaponsShop;
    @Getter private Merchant armourShop;
    @Getter private Merchant foodShop;
    @Getter private Merchant toolsShop;
    @Getter private Merchant archeryShop;
    @Getter private Merchant miscShop;
    
    public ArenaManager(TntWars instance) {
        plugin = instance;
        minPlayers = plugin.getConfig().getInt("Tnt.Arena.usersMin");
        maxPlayers = plugin.getConfig().getInt("Tnt.Arena.usersMax");
        
        areaBorder1 = Metodos.stringToLocation(plugin.getConfig().getString("Tnt.Arena.areaBorder1"));
        areaBorder2 = Metodos.stringToLocation(plugin.getConfig().getString("Tnt.Arena.areaBorder2"));
        lobby = Metodos.stringToLocation(plugin.getConfig().getString("Tnt.Arena.lobby"));
    }

    public void prepareWorld(World w) {
        w.setPVP(true);
        w.setGameRuleValue("doDaylightCycle", "false");
        w.setStorm(false);
        w.setDifficulty(Difficulty.EASY);
        w.setTime(6000);    
        w.getLivingEntities().stream()
                    .filter(e -> !e.getType().equals(EntityType.PLAYER))
                    .forEach(e -> e.damage(e.getMaxHealth()));
        
        initArena();
        setupChest(w);
        setupVillagersTrades();
        w.setAutoSave(false);
        plugin.getLogger().log(Level.INFO, "Mundo para {0}/{1} preparado", new Object[]{minPlayers, maxPlayers});
    }
    
    public void initArena() {
        for (String str : plugin.getConfig().getConfigurationSection("Tnt.Arena.islas").getKeys(false)) {
            plugin.getLogger().log(Level.INFO, "Intentando cargar isla {0}", str);
            ConfigurationSection cfg = plugin.getConfig().getConfigurationSection("Tnt.Arena.islas." + str);
            Location loc1 = Metodos.stringToLocation(cfg.getString("borderPos1"));
            Location loc2 = Metodos.stringToLocation(cfg.getString("borderPos2"));
            
            int topBlockX = (loc1.getBlockX() < loc2.getBlockX() ? loc2.getBlockX() : loc1.getBlockX());
            int bottomBlockX = (loc1.getBlockX() > loc2.getBlockX() ? loc2.getBlockX() : loc1.getBlockX());

            int topBlockY = (loc1.getBlockY() < loc2.getBlockY() ? loc2.getBlockY() : loc1.getBlockY());
            int bottomBlockY = (loc1.getBlockY() > loc2.getBlockY() ? loc2.getBlockY() : loc1.getBlockY());

            int topBlockZ = (loc1.getBlockZ() < loc2.getBlockZ() ? loc2.getBlockZ() : loc1.getBlockZ());
            int bottomBlockZ = (loc1.getBlockZ() > loc2.getBlockZ() ? loc2.getBlockZ() : loc1.getBlockZ());

            TntIsland isla = new TntIsland();
            for (int x = bottomBlockX; x <= topBlockX; x++) {
                for (int z = bottomBlockZ; z <= topBlockZ; z++) {
                    for (int y = bottomBlockY; y <= topBlockY; y++) {
                        Block block = loc1.getWorld().getBlockAt(x, y, z);
                        if (block.getType() == Material.IRON_BLOCK) {
                            Generador gen = new Generador();
                            gen.setLoc(block.getLocation());
                            gen.setType(GenType.IRON);
                            generadores.add(gen);
                        } else if (block.getType() == Material.GOLD_BLOCK) {
                            Generador gen = new Generador();
                            gen.setLoc(block.getLocation());
                            gen.setType(GenType.GOLD);
                            generadores.add(gen);
                        } else if (block.getType() == Material.DIAMOND_BLOCK) {
                            Generador gen = new Generador();
                            gen.setLoc(block.getLocation());
                            gen.setType(GenType.DIAMOND);
                            generadores.add(gen);
                            if (!"centro".equals(str)) { //Los de las islas personales no estar iniciados
                                gen.setLevel(0);
                            }
                        } else if (block.getType() == Material.TNT) {
                            Generador gen = new Generador();
                            gen.setLoc(block.getLocation());
                            gen.setType(GenType.TNT);
                            generadores.add(gen);
                        } else if (block.getType() == Material.BEDROCK) {
                            isla.setBedrockCore(block);
                        } else if (block.getType() != Material.AIR) {
                            isla.addBlock(block);
                        }
                    }
                }
            }
            isla.setId(str);
            if (!"centro".equals(str)) {
                System.out.println("Poniendo isla en " + Metodos.stringToLocation(cfg.getString("spawn")));
                isla.setSpawn(Metodos.centre(Metodos.stringToLocation(cfg.getString("spawn"))));
                isla.setColor(ChatColor.valueOf(cfg.getString("color")));
                Villager v = (Villager) loc1.getWorld().spawnEntity(Metodos.centre(Metodos.stringToLocation(cfg.getString("aldeano"))), EntityType.VILLAGER);
                v.setCollidable(false);
                v.setCustomName(Metodos.colorizar("&6Tienda TNTWars"));
                v.setAI(false);     
            }
            islas.add(isla);
        }
        plugin.getLogger().log(Level.INFO, "Encontradas {0} islas y {1} generadores", new Object[]{islas.size(), generadores.size()});
        setUnAssignedIslas((ArrayList<TntIsland>) islas.clone());
        unAssignedIslas.remove(TntIsland.getIsland("centro"));
    }
    
    public void setupChest(World w) {
        for (Chunk chunk : w.getLoadedChunks()) {
            for (BlockState e : chunk.getTileEntities()) {
                if (e instanceof Chest) {
                    Chest chest = (Chest) e;
                    chest.getInventory().clear();
                    Inventory inv = chest.getBlockInventory();
                    inv.clear();

                    //Número de objetos de cada lista que habrá en el cofre
                    int i1 = r.nextInt(plugin.getChestItems().items_basic.size());
                    int i2 = r.nextInt(plugin.getChestItems().armaduras_basic.size());
                    int i3 = r.nextInt(plugin.getChestItems().armas_basic.size());
                    int i4 = r.nextInt(plugin.getChestItems().comida_basic.size());

                    //Qué objetos serán esos aleatorios
                    ArrayList<ItemStack> aleatorios = new ArrayList<>();
                    for (int i = 0; i < i1; i++) {
                        //Uno aleatorio de toda la lista
                        aleatorios.add(plugin.getChestItems().items_basic.get(r.nextInt(plugin.getChestItems().items_basic.size())));
                    }
                    for (int i = 0; i < i2; i++) {
                        aleatorios.add(plugin.getChestItems().armaduras_basic.get(r.nextInt(plugin.getChestItems().armaduras_basic.size())));
                    }
                    for (int i = 0; i < i3; i++) {
                        aleatorios.add(plugin.getChestItems().armas_basic.get(r.nextInt(plugin.getChestItems().armas_basic.size())));
                    }
                    for (int i = 0; i < i4; i++) {
                        aleatorios.add(plugin.getChestItems().comida_basic.get(r.nextInt(plugin.getChestItems().comida_basic.size())));
                    }

                    aleatorios.stream().forEach((is) -> {
                        inv.setItem(r.nextInt(inv.getSize()), is);
                    });
                    chest.update();
                }
            }
        }
    }
    
    public void setupVillagersTrades() {
        buildingShop = plugin.getServer().createMerchant(Metodos.colorizar("&aConstrucción"));
        weaponsShop = plugin.getServer().createMerchant(Metodos.colorizar("&aArmas"));
        armourShop = plugin.getServer().createMerchant(Metodos.colorizar("&aArmadura"));
        foodShop = plugin.getServer().createMerchant(Metodos.colorizar("&aComida"));
        toolsShop = plugin.getServer().createMerchant(Metodos.colorizar("&aHerramientas"));
        archeryShop = plugin.getServer().createMerchant(Metodos.colorizar("&aTiro"));
        miscShop = plugin.getServer().createMerchant(Metodos.colorizar("&aMisceláneo"));

        buildingShop.setRecipe(0, new MerchantBuilder(new ItemStack(Material.SANDSTONE, 3))
                .addIngredient(new ItemStack(Material.IRON_INGOT, 1)).build());
        buildingShop.setRecipe(1, new MerchantBuilder(new ItemStack(Material.WOOD, 3))
                .addIngredient(new ItemStack(Material.IRON_INGOT, 2)).build());
        buildingShop.setRecipe(2, new MerchantBuilder(new ItemStack(Material.GLASS, 2))
                .addIngredient(new ItemStack(Material.IRON_INGOT, 1)).build());
        buildingShop.setRecipe(3, new MerchantBuilder(new ItemStack(Material.GLOWSTONE, 2))
                .addIngredient(new ItemStack(Material.IRON_INGOT, 3)).build());
        buildingShop.setRecipe(4, new MerchantBuilder(new ItemStack(Material.HAY_BLOCK, 1))
                .addIngredient(new ItemStack(Material.IRON_INGOT, 5)).build());
        buildingShop.setRecipe(5, new MerchantBuilder(new ItemStack(Material.OBSIDIAN, 1))
                .addIngredient(new ItemStack(Material.DIAMOND, 5)).build());
        
        weaponsShop.setRecipe(0, new MerchantBuilder(new ItemStack(Material.WOOD_SWORD, 1))
                .addIngredient(new ItemStack(Material.IRON_INGOT, 5)).build());
        weaponsShop.setRecipe(1, new MerchantBuilder(new ItemStack(Material.WOOD_AXE, 1))
                .addIngredient(new ItemStack(Material.IRON_INGOT, 8)).build());
        weaponsShop.setRecipe(2, new MerchantBuilder(new ItemStack(Material.STONE_SWORD, 1))
                .addIngredient(new ItemStack(Material.IRON_INGOT, 32)).build());
        weaponsShop.setRecipe(3, new MerchantBuilder(new ItemStack(Material.STONE_AXE, 1))
                .addIngredient(new ItemStack(Material.IRON_INGOT, 48)).build());
        weaponsShop.setRecipe(4, new MerchantBuilder(new ItemStack(Material.IRON_SWORD, 1))
                .addIngredient(new ItemStack(Material.GOLD_INGOT, 16)).build());
        weaponsShop.setRecipe(5, new MerchantBuilder(new ItemStack(Material.IRON_AXE, 1))
                .addIngredient(new ItemStack(Material.GOLD_INGOT, 24)).build());
        weaponsShop.setRecipe(6, new MerchantBuilder(new ItemStack(Material.DIAMOND_SWORD, 1))
                .addIngredient(new ItemStack(Material.DIAMOND, 12)).build());
        weaponsShop.setRecipe(7, new MerchantBuilder(new ItemStack(Material.DIAMOND_AXE, 1))
                .addIngredient(new ItemStack(Material.DIAMOND, 20)).build());
        
        armourShop.setRecipe(0, new MerchantBuilder(new ItemStack(Material.LEATHER_HELMET, 1))
                .addIngredient(new ItemStack(Material.IRON_INGOT, 1)).build());
        armourShop.setRecipe(1, new MerchantBuilder(new ItemStack(Material.LEATHER_CHESTPLATE, 1))
                .addIngredient(new ItemStack(Material.IRON_INGOT, 1)).build());
        armourShop.setRecipe(2, new MerchantBuilder(new ItemStack(Material.LEATHER_LEGGINGS, 1))
                .addIngredient(new ItemStack(Material.IRON_INGOT, 1)).build());
        armourShop.setRecipe(3, new MerchantBuilder(new ItemStack(Material.LEATHER_BOOTS, 1))
                .addIngredient(new ItemStack(Material.IRON_INGOT, 1)).build());
        armourShop.setRecipe(4, new MerchantBuilder(new ItemStack(Material.CHAINMAIL_HELMET, 1))
                .addIngredient(new ItemStack(Material.GOLD_INGOT, 10)).build());
        armourShop.setRecipe(5, new MerchantBuilder(new ItemStack(Material.CHAINMAIL_CHESTPLATE, 1))
                .addIngredient(new ItemStack(Material.GOLD_INGOT, 10)).build());
        armourShop.setRecipe(6, new MerchantBuilder(new ItemStack(Material.CHAINMAIL_LEGGINGS, 1))
                .addIngredient(new ItemStack(Material.GOLD_INGOT, 10)).build());
        armourShop.setRecipe(7, new MerchantBuilder(new ItemStack(Material.CHAINMAIL_BOOTS, 1))
                .addIngredient(new ItemStack(Material.GOLD_INGOT, 10)).build());
        armourShop.setRecipe(8, new MerchantBuilder(new ItemStack(Material.IRON_HELMET, 1))
                .addIngredient(new ItemStack(Material.DIAMOND, 5)).build());
        armourShop.setRecipe(9, new MerchantBuilder(new ItemStack(Material.IRON_CHESTPLATE, 1))
                .addIngredient(new ItemStack(Material.DIAMOND, 5)).build());
        armourShop.setRecipe(10, new MerchantBuilder(new ItemStack(Material.IRON_LEGGINGS, 1))
                .addIngredient(new ItemStack(Material.DIAMOND, 5)).build());
        armourShop.setRecipe(11, new MerchantBuilder(new ItemStack(Material.IRON_BOOTS, 1))
                .addIngredient(new ItemStack(Material.DIAMOND, 5)).build());
        armourShop.setRecipe(12, new MerchantBuilder(new ItemStack(Material.SHIELD, 1))
                .addIngredient(new ItemStack(Material.DIAMOND, 15)).build());
        
        foodShop.setRecipe(0, new MerchantBuilder(new ItemStack(Material.CARROT_ITEM, 1))
                .addIngredient(new ItemStack(Material.IRON_INGOT, 1)).build());
        foodShop.setRecipe(1, new MerchantBuilder(new ItemStack(Material.COOKED_BEEF, 1))
                .addIngredient(new ItemStack(Material.IRON_INGOT, 5)).build());
        foodShop.setRecipe(2, new MerchantBuilder(new ItemStack(Material.CAKE, 1))
                .addIngredient(new ItemStack(Material.IRON_INGOT, 12)).build());
        foodShop.setRecipe(3, new MerchantBuilder(new ItemStack(Material.GOLDEN_APPLE, 1))
                .addIngredient(new ItemStack(Material.GOLD_INGOT, 25)).build());
       
        toolsShop.setRecipe(0, new MerchantBuilder(new ItemStack(Material.STONE_PICKAXE, 1))
                .addIngredient(new ItemStack(Material.IRON_INGOT, 10)).build());
        toolsShop.setRecipe(1, new MerchantBuilder(new ItemStack(Material.IRON_PICKAXE, 1))
                .addIngredient(new ItemStack(Material.GOLD_INGOT, 3)).build());
        toolsShop.setRecipe(2, new MerchantBuilder(new ItemStack(Material.DIAMOND_PICKAXE, 1))
                .addIngredient(new ItemStack(Material.GOLD_INGOT, 10)).build());
        
        archeryShop.setRecipe(0, new MerchantBuilder(new ItemStack(Material.BOW, 1))
                .addIngredient(new ItemStack(Material.DIAMOND, 5)).build());
        archeryShop.setRecipe(1, new MerchantBuilder(new ItemStack(Material.ARROW, 5))
                .addIngredient(new ItemStack(Material.GOLD_INGOT, 2)).build());
        archeryShop.setRecipe(2, new MerchantBuilder(new ItemStack(Material.SPECTRAL_ARROW, 1))
                .addIngredient(new ItemStack(Material.GOLD_INGOT, 2)).build());
        archeryShop.setRecipe(3, new MerchantBuilder(new ItemBuilder().setType(Material.TIPPED_ARROW).addPotionType(PotionType.INSTANT_DAMAGE, false, false).build())
                .addIngredient(new ItemStack(Material.GOLD_INGOT, 2)).build());
        archeryShop.setRecipe(4, new MerchantBuilder(new ItemBuilder().setType(Material.TIPPED_ARROW).addPotionType(PotionType.SLOWNESS, false, false).build())
                .addIngredient(new ItemStack(Material.GOLD_INGOT, 2)).build());
        archeryShop.setRecipe(5, new MerchantBuilder(new ItemBuilder().setType(Material.TIPPED_ARROW).addPotionType(PotionType.POISON, false, false).build())
                .addIngredient(new ItemStack(Material.GOLD_INGOT, 6)).build());
        
        miscShop.setRecipe(0, new MerchantBuilder(new ItemStack(Material.CHEST, 1))
                .addIngredient(new ItemStack(Material.GOLD_INGOT, 2)).build());
    }
    
    public void teleport(Player p) {
        for (TntIsland i : unAssignedIslas) {
            System.out.println("Asignando isla " + i.getId() + " (" + i.getColor() + ") a " + p.getName());
            p.teleport(i.getSpawn());
            i.setOwner(p.getUniqueId());
            unAssignedIslas.remove(i);
            break;
        }
    }
}
