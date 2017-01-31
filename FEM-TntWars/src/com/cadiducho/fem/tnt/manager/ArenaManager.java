package com.cadiducho.fem.tnt.manager;

import com.cadiducho.fem.core.util.Metodos;
import com.cadiducho.fem.core.util.NoAI;
import com.cadiducho.fem.tnt.Generador;
import com.cadiducho.fem.tnt.Generador.GenType;
import com.cadiducho.fem.tnt.TntIsland;
import com.cadiducho.fem.tnt.TntWars;
import lombok.Getter;
import lombok.Setter;
import me.cybermaxke.merchants.api.Merchant;
import me.cybermaxke.merchants.api.MerchantAPI;
import me.cybermaxke.merchants.api.Merchants;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;

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
                v.setCustomName(Metodos.colorizar("&6Tienda TNTWars"));
                NoAI.setAiEnabled(v, false);

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
        MerchantAPI api = Merchants.get();

        buildingShop = api.newMerchant(Metodos.colorizar("&aConstrucción"));
        weaponsShop = api.newMerchant(Metodos.colorizar("&aArmas"));
        armourShop = api.newMerchant(Metodos.colorizar("&aArmadura"));
        foodShop = api.newMerchant(Metodos.colorizar("&aComida"));
        toolsShop = api.newMerchant(Metodos.colorizar("&aHerramientas"));
        archeryShop = api.newMerchant(Metodos.colorizar("&aTiro"));
        miscShop = api.newMerchant(Metodos.colorizar("&aMisceláneo"));

        buildingShop.addOffer(api.newOffer(new ItemStack(Material.SANDSTONE, 3), new ItemStack(Material.IRON_INGOT, 1)));
        buildingShop.addOffer(api.newOffer(new ItemStack(Material.WOOD, 3), new ItemStack(Material.IRON_INGOT, 2)));
        buildingShop.addOffer(api.newOffer(new ItemStack(Material.GLASS, 2), new ItemStack(Material.IRON_INGOT, 1)));
        buildingShop.addOffer(api.newOffer(new ItemStack(Material.GLOWSTONE, 2), new ItemStack(Material.IRON_INGOT, 3)));
        buildingShop.addOffer(api.newOffer(new ItemStack(Material.HAY_BLOCK, 1), new ItemStack(Material.IRON_INGOT, 5)));
        buildingShop.addOffer(api.newOffer(new ItemStack(Material.OBSIDIAN, 1), new ItemStack(Material.DIAMOND, 5)));

        weaponsShop.addOffer(api.newOffer(new ItemStack(Material.WOOD_SWORD, 1), new ItemStack(Material.IRON_INGOT, 5)));
        weaponsShop.addOffer(api.newOffer(new ItemStack(Material.WOOD_AXE, 1), new ItemStack(Material.IRON_INGOT, 8)));
        weaponsShop.addOffer(api.newOffer(new ItemStack(Material.STONE_SWORD, 1), new ItemStack(Material.IRON_INGOT, 32)));
        weaponsShop.addOffer(api.newOffer(new ItemStack(Material.STONE_AXE, 1), new ItemStack(Material.IRON_INGOT, 48)));
        weaponsShop.addOffer(api.newOffer(new ItemStack(Material.IRON_SWORD, 1), new ItemStack(Material.GOLD_INGOT, 16)));
        weaponsShop.addOffer(api.newOffer(new ItemStack(Material.IRON_AXE, 1), new ItemStack(Material.GOLD_INGOT, 24)));
        weaponsShop.addOffer(api.newOffer(new ItemStack(Material.DIAMOND_SWORD, 1), new ItemStack(Material.DIAMOND, 12)));
        weaponsShop.addOffer(api.newOffer(new ItemStack(Material.DIAMOND_AXE, 1), new ItemStack(Material.DIAMOND, 20)));

        armourShop.addOffer(api.newOffer(new ItemStack(Material.LEATHER_HELMET, 1), new ItemStack(Material.IRON_INGOT, 1)));
        armourShop.addOffer(api.newOffer(new ItemStack(Material.LEATHER_CHESTPLATE, 1), new ItemStack(Material.IRON_INGOT, 1)));
        armourShop.addOffer(api.newOffer(new ItemStack(Material.LEATHER_LEGGINGS, 1), new ItemStack(Material.IRON_INGOT, 1)));
        armourShop.addOffer(api.newOffer(new ItemStack(Material.LEATHER_BOOTS, 1), new ItemStack(Material.IRON_INGOT, 1)));
        armourShop.addOffer(api.newOffer(new ItemStack(Material.CHAINMAIL_HELMET, 1), new ItemStack(Material.GOLD_INGOT, 10)));
        armourShop.addOffer(api.newOffer(new ItemStack(Material.CHAINMAIL_CHESTPLATE, 1), new ItemStack(Material.GOLD_INGOT, 10)));
        armourShop.addOffer(api.newOffer(new ItemStack(Material.CHAINMAIL_LEGGINGS, 1), new ItemStack(Material.GOLD_INGOT, 10)));
        armourShop.addOffer(api.newOffer(new ItemStack(Material.CHAINMAIL_BOOTS, 1), new ItemStack(Material.GOLD_INGOT, 10)));
        armourShop.addOffer(api.newOffer(new ItemStack(Material.IRON_HELMET, 1), new ItemStack(Material.DIAMOND, 5)));
        armourShop.addOffer(api.newOffer(new ItemStack(Material.IRON_CHESTPLATE, 1), new ItemStack(Material.DIAMOND, 5)));
        armourShop.addOffer(api.newOffer(new ItemStack(Material.IRON_LEGGINGS, 1), new ItemStack(Material.DIAMOND, 5)));
        armourShop.addOffer(api.newOffer(new ItemStack(Material.IRON_BOOTS, 1), new ItemStack(Material.DIAMOND, 5)));

        foodShop.addOffer(api.newOffer(new ItemStack(Material.CARROT_ITEM, 1), new ItemStack(Material.IRON_INGOT, 1)));
        foodShop.addOffer(api.newOffer(new ItemStack(Material.COOKED_BEEF, 1), new ItemStack(Material.IRON_INGOT, 5)));
        foodShop.addOffer(api.newOffer(new ItemStack(Material.CAKE, 1), new ItemStack(Material.IRON_INGOT, 12)));
        foodShop.addOffer(api.newOffer(new ItemStack(Material.GOLDEN_APPLE, 1), new ItemStack(Material.GOLD_INGOT, 30)));

        toolsShop.addOffer(api.newOffer(new ItemStack(Material.STONE_PICKAXE, 1), new ItemStack(Material.IRON_INGOT, 10)));
        toolsShop.addOffer(api.newOffer(new ItemStack(Material.IRON_PICKAXE, 1), new ItemStack(Material.GOLD_INGOT, 3)));
        toolsShop.addOffer(api.newOffer(new ItemStack(Material.DIAMOND_PICKAXE, 1), new ItemStack(Material.GOLD_INGOT, 10)));

        archeryShop.addOffer(api.newOffer(new ItemStack(Material.BOW, 1), new ItemStack(Material.DIAMOND, 5)));
        archeryShop.addOffer(api.newOffer(new ItemStack(Material.ARROW, 5), new ItemStack(Material.GOLD_INGOT, 2)));

        miscShop.addOffer(api.newOffer(new ItemStack(Material.CHEST, 1), new ItemStack(Material.GOLD_INGOT, 5)));
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
