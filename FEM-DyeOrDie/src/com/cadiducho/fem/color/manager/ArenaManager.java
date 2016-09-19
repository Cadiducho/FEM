package com.cadiducho.fem.color.manager;

import com.cadiducho.fem.color.DyeMiniArea;
import com.cadiducho.fem.color.DyeOrDie;
import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.core.util.Metodos;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import lombok.Getter;
import org.bukkit.ChatColor;


import org.bukkit.Difficulty;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class ArenaManager {

    private final DyeOrDie plugin;

    ConcurrentHashMap<Location, Boolean> matsalreadyin = new ConcurrentHashMap();
    @Getter private final DyeColor arenacolor = DyeColor.WHITE; //¿config?
    @Getter private DyeColor currentcolor = DyeColor.BLUE;
    @Getter private int round = 0;
    @Getter private final ArrayList<Location> whiteblocks = new ArrayList<>();
    @Getter private final double decayvalue = 0.1D;
    private final Random rand = new Random();
    private final ArrayList<DyeColor> allowedcolors = new ArrayList<>();    
    
    @Getter private int minPlayers = 2;
    @Getter private int maxPlayers = 16;
    @Getter private final Location pos1;
    @Getter private final Location pos2;
    @Getter private final Location lobby;
    @Getter private final ArrayList<DyeMiniArea> colormats = new ArrayList<>();
    
    public ArenaManager(DyeOrDie instance) {
        plugin = instance;
        minPlayers = plugin.getConfig().getInt("Color.Arena.usersMin");
        maxPlayers = plugin.getConfig().getInt("Color.Arena.usersMax");
        
        pos1 = Metodos.stringToLocation(plugin.getConfig().getString("Color.Arena.borderPos1"));
        pos2 = Metodos.stringToLocation(plugin.getConfig().getString("Color.Arena.borderPos2"));
        lobby = Metodos.stringToLocation(plugin.getConfig().getString("Color.Arena.Lobby"));
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
        w.setAutoSave(false);
        initArena();
        
        plugin.getLogger().log(Level.INFO, "Mundo para {0}/{1} preparado", new Object[]{minPlayers, maxPlayers});
    }
    
    public void initArena() {
        int lowx = pos1.getBlockX();
        int lowy = pos1.getBlockY();
        int lowz = pos1.getBlockZ();
        int highx = pos2.getBlockX();
        int highy = pos2.getBlockY();
        int highz = pos2.getBlockZ();
        if (highx < lowx) {
            int lowestx = highx;
            highx = lowx;
            lowx = lowestx;
        }
        if (highy < lowy) {
            int lowesty = highy;
            highy = lowy;
            lowy = lowesty;
        }
        if (highz < lowz) {
            int lowestz = highz;
            highz = lowz;
            lowz = lowestz;
        }
        for (int y = lowy; y < highy; y++) {
            for (int x = lowx; x < highx; x++) {
                for (int z = lowz; z < highz; z++) {
                    Location loc3 = new Location(pos1.getWorld(), x, y, z);
                    Block block = loc3.getBlock();
                    Block carpet = block.getRelative(BlockFace.UP);
                    if (carpet.getType() == Material.CARPET) {
                        if (carpet.getData() == arenacolor.getWoolData()) {
                            whiteblocks.add(carpet.getLocation());
                        } else if (!this.matsalreadyin.containsKey(carpet.getLocation())) {
                            DyeMiniArea mat = new DyeMiniArea();
                            boolean morex = true;
                            for (int x1 = x; morex; x1++) {
                                boolean morez = true;
                                for (int z1 = z; morez; z1++) {
                                    Block carpettemp = new Location(pos1.getWorld(), x1, carpet.getLocation().getBlockY(), z1).getBlock();
                                    if ((carpettemp.getType() == Material.CARPET) && (carpettemp.getData() == carpet.getData())) {
                                        mat.addBlock(carpettemp);
                                        this.matsalreadyin.put(carpettemp.getLocation(), true);
                                    } else {
                                        if (z == z1) {
                                            morex = false;
                                        }
                                        morez = false;
                                    }
                                }
                            }
                            colormats.add(mat);
                        }
                    }
                }
            }
        }
        allowedcolors.add(DyeColor.BLACK);
        allowedcolors.add(DyeColor.CYAN);
        allowedcolors.add(DyeColor.GREEN);
        allowedcolors.add(DyeColor.PINK);
        allowedcolors.add(DyeColor.YELLOW);
        allowedcolors.add(DyeColor.MAGENTA);
        
        plugin.getLogger().log(Level.INFO, "Encontrados {0} \u00e1reas y {1} bloques suelo", new Object[]{colormats.size(), whiteblocks.size()});
    }
    
    public ArrayList<DyeMiniArea> shuffleMats() {
        ArrayList<DyeColor> colors = new ArrayList();
        for (DyeColor color : allowedcolors) {
            colors.add(color);
        }
        for (int i = colors.size(); i < colormats.size(); i++) {
            colors.add((DyeColor) allowedcolors.get(rand.nextInt(allowedcolors.size())));
        }
        for (DyeMiniArea mat : colormats) {
            mat.setColor((DyeColor) colors.remove(rand.nextInt(colors.size())));
        }
        return colormats;
    }
    
    public void replaceFloor() {
        Block iceblock;
        for (Location loc : whiteblocks) {
            Block carpetblock = loc.getBlock();
            carpetblock.setType(Material.CARPET);
            carpetblock.setData(arenacolor.getWoolData());
            iceblock = carpetblock.getRelative(BlockFace.DOWN);
            iceblock.setType(Material.ICE);
        }
        for (DyeMiniArea mat : colormats) {
            for (Location loc : mat.getBlocks()) {
                Block b = loc.getBlock();
                b.setType(Material.CARPET);
                b.setData(mat.color.getWoolData());
                iceblock = b.getRelative(BlockFace.DOWN);
                iceblock.setType(Material.ICE);
            }
        }
    }
    
    public DyeColor spinColors(boolean samecolor) {
        DyeColor tempcolor = (DyeColor) allowedcolors.get(rand.nextInt(allowedcolors.size()));

        if (samecolor) {
            ItemStack wool = new ItemStack(Material.WOOL, 1, tempcolor.getWoolData());
            for (Player player : plugin.getGm().getPlayersInGame()) {
                PlayerInventory inv = player.getInventory();
                inv.clear();
                for (int i = 0; i < 9; i++) {
                    inv.setItem(i, wool);
                }
            }
            return tempcolor;
        }
        ItemStack wool1 = new ItemStack(Material.WOOL, 1, tempcolor.getWoolData());
        ItemStack wool2 = new ItemStack(Material.WOOL, 1, ((DyeColor) allowedcolors.get(rand.nextInt(allowedcolors.size()))).getWoolData());
        ItemStack wool3 = new ItemStack(Material.WOOL, 1, ((DyeColor) allowedcolors.get(rand.nextInt(allowedcolors.size()))).getWoolData());
        ItemStack wool4 = new ItemStack(Material.WOOL, 1, ((DyeColor) allowedcolors.get(rand.nextInt(allowedcolors.size()))).getWoolData());
        ItemStack wool5 = new ItemStack(Material.WOOL, 1, ((DyeColor) allowedcolors.get(rand.nextInt(allowedcolors.size()))).getWoolData());
        ItemStack wool6 = new ItemStack(Material.WOOL, 1, ((DyeColor) allowedcolors.get(rand.nextInt(allowedcolors.size()))).getWoolData());
        ItemStack wool7 = new ItemStack(Material.WOOL, 1, ((DyeColor) allowedcolors.get(rand.nextInt(allowedcolors.size()))).getWoolData());
        ItemStack wool8 = new ItemStack(Material.WOOL, 1, ((DyeColor) allowedcolors.get(rand.nextInt(allowedcolors.size()))).getWoolData());
        ItemStack wool9 = new ItemStack(Material.WOOL, 1, ((DyeColor) allowedcolors.get(rand.nextInt(allowedcolors.size()))).getWoolData());
        for (Player player : plugin.getGm().getPlayersInGame()) {
            PlayerInventory inv = player.getInventory();
            inv.clear();
            inv.setItem(0, wool1);
            inv.setItem(1, wool2);
            inv.setItem(2, wool3);
            inv.setItem(3, wool4);
            inv.setItem(4, wool5);
            inv.setItem(5, wool6);
            inv.setItem(6, wool7);
            inv.setItem(7, wool8);
            inv.setItem(8, wool9);
        }
        return tempcolor;
    }
    
    public void startRound() {
        round += 1;
        currentcolor = spinColors(true);
        plugin.getMsg().sendBroadcast("El color actual es " + getColorFromWool(currentcolor) + currentcolor.toString().replace("_", " ").toLowerCase() + "! " + ChatColor.DARK_GREEN + "¡Ya!");
        for (Player player : plugin.getGm().getPlayersInGame()) {
            player.setLevel(round);
            player.setExp(0.9999F);
            Integer rondas = FEMServer.getUser(player).getUserData().getRondas_dod();        
            FEMServer.getUser(player).getUserData().setRondas_dod(rondas + 1);
            if (round > FEMServer.getUser(player).getUserData().getRecord_dod()) {
                FEMServer.getUser(player).getUserData().setRecord_dod(round);
                player.sendMessage("Has batido tu record de rondas seguidas, ahora es " + round + "!");
            }
            FEMServer.getUser(player).save();
        }
    }

    public void roundEnded() {
        setTimeLeft(0.0F);
        Block iceblock;
        for (Location loc : whiteblocks) {
            Block carpetblock = loc.getBlock();
            carpetblock.setType(Material.AIR);
            iceblock = carpetblock.getRelative(BlockFace.DOWN);
            iceblock.setType(Material.AIR);
            if (rand.nextInt(3) == 1) { //No hacer caer todos
                iceblock.getWorld().spawnFallingBlock(iceblock.getLocation(), Material.WOOL, arenacolor.getWoolData()).setDropItem(false);
            }
        }
        for (DyeMiniArea mat : this.colormats) {
            if (mat.color != currentcolor) {
                for (Location loc : mat.getBlocks()) {
                    Block carpetblock = loc.getBlock();
                    carpetblock.setType(Material.AIR);
                    iceblock = carpetblock.getRelative(BlockFace.DOWN);
                    iceblock.setType(Material.AIR);
                    if (rand.nextInt(3) == 1) { //No hacer caer todos
                        iceblock.getWorld().spawnFallingBlock(iceblock.getLocation(), Material.WOOL, mat.color.getWoolData()).setDropItem(false);
                    }
                }
            }
        }
    }
    
    public void setTimeLeft(float timeleft) {
        plugin.getGm().getPlayersInGame().stream().forEach(player -> player.setExp(timeleft));
    }
    
    public ChatColor getColorFromWool(DyeColor dye) {
        switch (dye) {
            case BLACK: return ChatColor.DARK_GRAY;
            case BLUE: return ChatColor.DARK_BLUE;
            case BROWN: return ChatColor.GOLD;
            case CYAN: return ChatColor.AQUA;
            case GRAY: return ChatColor.GRAY;
            case GREEN: return ChatColor.DARK_GREEN;
            case LIGHT_BLUE: return ChatColor.BLUE;
            case LIME: return ChatColor.GREEN;
            case MAGENTA: return ChatColor.LIGHT_PURPLE;
            case ORANGE: return ChatColor.GOLD;
            case PINK: return ChatColor.LIGHT_PURPLE;
            case PURPLE: return ChatColor.DARK_PURPLE;
            case RED: return ChatColor.DARK_RED;
            case SILVER: return ChatColor.GRAY;
            case WHITE: return ChatColor.WHITE;
            case YELLOW: return ChatColor.YELLOW;
        }
        return ChatColor.RESET;
    }
}
