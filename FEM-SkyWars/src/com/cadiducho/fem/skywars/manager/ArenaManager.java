package com.cadiducho.fem.skywars.manager;

import com.cadiducho.fem.skywars.SkyWars;
import com.cadiducho.fem.core.util.Metodos;
import com.cadiducho.fem.skywars.SkyIsland;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import lombok.Getter;
import lombok.Setter;
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
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ArenaManager {

    private final SkyWars plugin;
    private final Random r = new Random();

    @Getter private int minPlayers = 2;
    @Getter private int maxPlayers = 16;
    @Getter private final ArrayList<SkyIsland> islas = new ArrayList<>();
    @Getter @Setter private ArrayList<SkyIsland> unAssignedIslas = new ArrayList<>();
    Location areaBorder1;
    Location areaBorder2;
    World world;
    @Getter private final Location lobby;
    
    public ArenaManager(SkyWars instance) {
        plugin = instance;
        minPlayers = plugin.getConfig().getInt("Sky.Arena.usersMin");
        maxPlayers = plugin.getConfig().getInt("Sky.Arena.usersMax");
        
        areaBorder1 = Metodos.stringToLocation(plugin.getConfig().getString("Sky.Arena.areaBorder1"));
        areaBorder2 = Metodos.stringToLocation(plugin.getConfig().getString("Sky.Arena.areaBorder2"));
        lobby = Metodos.stringToLocation(plugin.getConfig().getString("Sky.Arena.lobby"));
    }

    public void prepareWorld(World w) {
        world = w;
        w.setPVP(true);
        w.setGameRuleValue("doDaylightCycle", "false");
        w.setStorm(false);
        w.setDifficulty(Difficulty.EASY);
        w.setTime(6000);    
        w.getLivingEntities().stream()
                    .filter(e -> !e.getType().equals(EntityType.PLAYER))
                    .forEach(e -> e.damage(e.getMaxHealth()));
        
        initArena();
        w.setAutoSave(false);
        plugin.getLogger().log(Level.INFO, "Mundo para {0}/{1} preparado", new Object[]{minPlayers, maxPlayers});
    }
    
    public void initArena() {
        plugin.getConfig().getConfigurationSection("Sky.Arena.islas").getKeys(false).forEach(str -> {
            plugin.getLogger().log(Level.INFO, "Intentando cargar isla {0}", str);
            ConfigurationSection cfg = plugin.getConfig().getConfigurationSection("Sky.Arena.islas." + str);

            Location loc1 = Metodos.stringToLocation(cfg.getString("borderPos1"));
            Location loc2 = Metodos.stringToLocation(cfg.getString("borderPos2"));
            
            int topBlockX = (loc1.getBlockX() < loc2.getBlockX() ? loc2.getBlockX() : loc1.getBlockX());
            int bottomBlockX = (loc1.getBlockX() > loc2.getBlockX() ? loc2.getBlockX() : loc1.getBlockX());

            int topBlockY = (loc1.getBlockY() < loc2.getBlockY() ? loc2.getBlockY() : loc1.getBlockY());
            int bottomBlockY = (loc1.getBlockY() > loc2.getBlockY() ? loc2.getBlockY() : loc1.getBlockY());

            int topBlockZ = (loc1.getBlockZ() < loc2.getBlockZ() ? loc2.getBlockZ() : loc1.getBlockZ());
            int bottomBlockZ = (loc1.getBlockZ() > loc2.getBlockZ() ? loc2.getBlockZ() : loc1.getBlockZ());

            SkyIsland isla = new SkyIsland();
            for (int x = bottomBlockX; x <= topBlockX; x++) {
                for (int z = bottomBlockZ; z <= topBlockZ; z++) {
                    for (int y = bottomBlockY; y <= topBlockY; y++) {
                        Block block = loc1.getWorld().getBlockAt(x, y, z);
                        if (block.getType() != Material.AIR) {
                            isla.addBlock(block);
                        }
                    }
                }
            }
            isla.setId(str);

            System.out.println("Poniendo isla en " + Metodos.stringToLocation(cfg.getString("spawn")));
            isla.setSpawn(Metodos.centre(Metodos.stringToLocation(cfg.getString("spawn"))));

            islas.add(isla);
        });
        setUnAssignedIslas((ArrayList<SkyIsland>) islas.clone());
    }
    
    public void fillChests() {
        for (Chunk chunk : world.getLoadedChunks()) {
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
    
    public void teleport(Player p) {
        for (SkyIsland i : unAssignedIslas) {
            System.out.println("Asignando isla " + i.getId() + " a " + p.getName());
            p.teleport(i.getSpawn());
            i.setOwner(p.getUniqueId());
            unAssignedIslas.remove(i);
            break;
        }
    }
}
