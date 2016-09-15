package com.cadiducho.fem.royale.manager;

import com.cadiducho.fem.core.util.Metodos;
import java.util.ArrayList;
import java.util.Random;

import com.cadiducho.fem.royale.BattleRoyale;
import com.cadiducho.fem.royale.utils.ChestItems;
import lombok.Getter;
import me.cybermaxke.merchants.api.Merchant;
import me.cybermaxke.merchants.api.MerchantAPI;
import me.cybermaxke.merchants.api.Merchants;
import org.bukkit.Chunk;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ArenaManager {

    private final BattleRoyale plugin;

    public ArenaManager(BattleRoyale plugin) {
        this.plugin = plugin;
    }

    @Getter private int maxPlayers;
    @Getter private int minPlayers;
    @Getter private final ArrayList<Location> spawnList = new ArrayList<>();
    @Getter private final ArrayList<Location> chestRandomList = new ArrayList<>();
    @Getter private int spawn;
    @Getter private Location lobby;
    @Getter private WorldBorder wb;
    public int gameTime;
    public int deathMatchTime;
    
    @Getter private Merchant vendo;
    @Getter private Merchant compro;
    
    private final Random rand = new Random();

    public void init() {
        gameTime = plugin.getConfig().getInt("gameTime");
        deathMatchTime = plugin.getConfig().getInt("deathMatchTime");
        maxPlayers = plugin.getConfig().getInt("Arena.Max");
        minPlayers = plugin.getConfig().getInt("Arena.Min");
        lobby = Metodos.stringToLocation(plugin.getConfig().getString("Arena.Lobby"));
        loadWorld(plugin.getWorld());
        setupVillagersTrades();
        loadSpawn();
    }

    public void fillChests() {
        for (Chunk chunk : plugin.getWorld().getLoadedChunks()) {
            for (BlockState e : chunk.getTileEntities()) {
                if (e instanceof Chest) {
                    Chest chest = (Chest) e;
                    chest.getInventory().clear();
                    Inventory inv = chest.getBlockInventory();
                    inv.clear();
                    fillChest(inv);
                    chest.update();
                }
                if (e.getType().equals(Material.ENDER_PORTAL_FRAME)) {
                    chestRandomList.add(e.getLocation().add(0D, 2D, 0D));
                }
            }
        }
    }

    public void fillChest(Inventory inv) {
        inv.clear();
        int low = 5;
        int high = 10;

        for (int i = 0; i < rand.nextInt(high - low) + low; i++) {
            int rand2 = rand.nextInt(ChestItems.ITEMS.size());
            if (inv.contains(ChestItems.ITEMS.get(rand2))) {
                continue;
            }
            if (ChestItems.ITEMS.get(rand2).getType() == Material.DIAMOND) {
                if (rand.nextInt(5) == 5) {
                    inv.setItem(rand.nextInt(27), ChestItems.ITEMS.get(rand2));
                }
            } else {
                inv.setItem(rand.nextInt(27), ChestItems.ITEMS.get(rand2));
            }
        }
    }

    public void fixPlayer(Location loc) {
        loc.add(0, 0.5, 0);
    }

    private void loadWorld(World w) {
        w = plugin.getWorld();
        w.setPVP(true);
        w.setGameRuleValue("naturalRegeneration", "false");
        w.setGameRuleValue("doDaylightCycle", "false");
        w.setStorm(false);
        w.setDifficulty(Difficulty.NORMAL);
        w.setTime(6000);
        
        wb = w.getWorldBorder();
        wb.setCenter(plugin.getConfig().getInt("worldBorderCenterX"), plugin.getConfig().getInt("worldBorderCenterY"));
        wb.setSize(plugin.getConfig().getInt("worldBorderSize"));
        wb.setDamageAmount(1.0);
        wb.setWarningDistance(5);
        wb.setDamageBuffer(1.0);
        wb.setDamageAmount(4.0);
    }

    public void teleport(Player player) {
        int rd = rand.nextInt(spawnList.size());
        player.teleport(spawnList.get(rd));
        spawnList.remove(rd);
    }
    
    public Location spawnRandomChest() {
        int rd = rand.nextInt(chestRandomList.size());
        chestRandomList.remove(rd);
        return chestRandomList.get(rd);
    }

    public void loadSpawn() {
        spawnList.clear();
        try {
            for (int i = 1; i <= maxPlayers; i++) {
                spawnList.add(Metodos.stringToLocation(plugin.getConfig().getString("Arena.Spawn." + i)));
            }
        } catch (Exception e) {
        }
    }
    
    public void setupVillagersTrades() {
        MerchantAPI api = Merchants.get();

        //tntWarsShop = api.newMerchant(Metodos.colorizar("&6Tienda TNTWars"));
        compro = api.newMerchant(Metodos.colorizar("&a¡Compro!"));
        vendo = api.newMerchant(Metodos.colorizar("&a¡Vendo!"));
        
        ItemStack moneda2 = plugin.getMoneda().clone();
        moneda2.setAmount(2);
        ItemStack moneda3 = plugin.getMoneda().clone();
        moneda3.setAmount(3);
        ItemStack moneda4 = plugin.getMoneda().clone();
        moneda4.setAmount(4);
        ItemStack moneda5 = plugin.getMoneda().clone();
        moneda5.setAmount(5);
        ItemStack moneda10 = plugin.getMoneda().clone();
        moneda10.setAmount(10);

        compro.addOffer(api.newOffer(plugin.getMoneda(), new ItemStack(Material.WOOD, 1)));
        compro.addOffer(api.newOffer(plugin.getMoneda(), new ItemStack(Material.COBBLESTONE, 1)));
        compro.addOffer(api.newOffer(plugin.getMoneda(), new ItemStack(Material.STONE, 3)));
        compro.addOffer(api.newOffer(plugin.getMoneda(), new ItemStack(Material.SAND, 1)));
        compro.addOffer(api.newOffer(plugin.getMoneda(), new ItemStack(Material.SANDSTONE, 1)));
        
        vendo.addOffer(api.newOffer(new ItemStack(Material.STONE_SWORD, 1), plugin.getMoneda()));
        vendo.addOffer(api.newOffer(new ItemStack(Material.DIAMOND_SWORD, 1), moneda3));
        vendo.addOffer(api.newOffer(new ItemStack(Material.BOW, 1), plugin.getMoneda()));
        vendo.addOffer(api.newOffer(new ItemStack(Material.ARROW, 32), moneda4));
    }
}