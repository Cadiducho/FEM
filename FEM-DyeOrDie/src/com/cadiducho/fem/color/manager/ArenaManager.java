package com.cadiducho.fem.color.manager;

import com.cadiducho.fem.color.DyeMiniArea;
import com.cadiducho.fem.color.DyeOrDie;
import com.cadiducho.fem.color.DyePlayer;
import com.cadiducho.fem.core.util.Metodos;
import com.cadiducho.fem.core.util.Title;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import lombok.Getter;
import org.bukkit.ChatColor;


import org.bukkit.Difficulty;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class ArenaManager {

    private final DyeOrDie plugin;
    private final Random rand = new Random();

    @Getter private final DyeColor arenacolor = DyeColor.WHITE; //¿config?
    @Getter private final ArrayList<Location> baseBlocks = new ArrayList<>(); //Bloques del suelo, en general blancos.
    private final ArrayList<Location> matsalreadyin = new ArrayList<>(); //Bloques ya registrados en arenas
    private final ArrayList<DyeColor> allowedcolors = new ArrayList<>();
    @Getter private final ArrayList<DyeMiniArea> areasColor = new ArrayList<>();
    
    @Getter private DyeColor currentcolor = DyeColor.BLUE;
    @Getter private int round = 0;
    @Getter private final double decayvalue = 0.1D;
    @Getter private Double timeleft = 0.0;
    
    @Getter private int minPlayers = 2;
    @Getter private int maxPlayers = 16;
    @Getter private final Location areaBorder1;
    @Getter private final Location areaBorder2;
    @Getter private final Location lobby;
    
    public ArenaManager(DyeOrDie instance) {
        plugin = instance;
        minPlayers = plugin.getConfig().getInt("Color.Arena.usersMin");
        maxPlayers = plugin.getConfig().getInt("Color.Arena.usersMax");
        
        areaBorder1 = Metodos.stringToLocation(plugin.getConfig().getString("Color.Arena.borderPos1"));
        areaBorder2 = Metodos.stringToLocation(plugin.getConfig().getString("Color.Arena.borderPos2"));
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
    
    //Calculos para iniciar la arena. Organizar bloques y zonas
    public void initArena() {
        int topBlockX = (areaBorder1.getBlockX() < areaBorder2.getBlockX() ? areaBorder2.getBlockX() : areaBorder1.getBlockX());
        int bottomBlockX = (areaBorder1.getBlockX() > areaBorder2.getBlockX() ? areaBorder2.getBlockX() : areaBorder1.getBlockX());

        int topBlockY = (areaBorder1.getBlockY() < areaBorder2.getBlockY() ? areaBorder2.getBlockY() : areaBorder1.getBlockY());
        int bottomBlockY = (areaBorder1.getBlockY() > areaBorder2.getBlockY() ? areaBorder2.getBlockY() : areaBorder1.getBlockY());

        int topBlockZ = (areaBorder1.getBlockZ() < areaBorder2.getBlockZ() ? areaBorder2.getBlockZ() : areaBorder1.getBlockZ());
        int bottomBlockZ = (areaBorder1.getBlockZ() > areaBorder2.getBlockZ() ? areaBorder2.getBlockZ() : areaBorder1.getBlockZ());

        for (int y = bottomBlockY; y < topBlockY; y++) {
            for (int x = bottomBlockX; x < topBlockX; x++) {
                for (int z = bottomBlockZ; z < topBlockZ; z++) {
                    Block ice = areaBorder1.getWorld().getBlockAt(x, y, z);
                    Block carpet = ice.getRelative(BlockFace.UP);
                    if (carpet.getType() == Material.CARPET) {
                        if (carpet.getData() == arenacolor.getWoolData()) {
                            baseBlocks.add(carpet.getLocation());
                        } else if (!matsalreadyin.contains(carpet.getLocation())) {
                            DyeMiniArea mat = new DyeMiniArea();
                            boolean morex = true;
                            for (int x1 = x; morex; x1++) {
                                boolean morez = true;
                                for (int z1 = z; morez; z1++) {
                                    Block carpettemp = new Location(areaBorder1.getWorld(), x1, carpet.getLocation().getBlockY(), z1).getBlock();
                                    if ((carpettemp.getType() == Material.CARPET) && (carpettemp.getData() == carpet.getData())) {
                                        mat.addBlock(carpettemp);
                                        matsalreadyin.add(carpettemp.getLocation());
                                    } else {
                                        if (z == z1) {
                                            morex = false;
                                        }
                                        morez = false;
                                    }
                                }
                            }
                            areasColor.add(mat);
                        }
                    }
                }
            }
        }
        allowedcolors.add(DyeColor.LIME);
        allowedcolors.add(DyeColor.BROWN);
        allowedcolors.add(DyeColor.CYAN);
        allowedcolors.add(DyeColor.YELLOW);
        allowedcolors.add(DyeColor.GRAY);
        allowedcolors.add(DyeColor.BLUE);
        allowedcolors.add(DyeColor.BLACK);
        allowedcolors.add(DyeColor.PINK);
        allowedcolors.add(DyeColor.ORANGE);
        allowedcolors.add(DyeColor.PURPLE);
        allowedcolors.add(DyeColor.RED);
        allowedcolors.add(DyeColor.GREEN);
        
        plugin.getLogger().log(Level.INFO, "Encontrados {0} áreas y {1} bloques suelo", new Object[]{areasColor.size(), baseBlocks.size()});
    }
    
    //Mezclarlos aleatoriamente
    public ArrayList<DyeMiniArea> shuffleMats() {
        ArrayList<DyeColor> colors = new ArrayList<>();
        for (DyeColor color : allowedcolors) {
            colors.add(color);
        }
        for (int i = colors.size(); i < areasColor.size(); i++) {
            colors.add((DyeColor) allowedcolors.get(rand.nextInt(allowedcolors.size())));
        }
        
        for (DyeMiniArea mat : areasColor) {
            mat.setColor((DyeColor) colors.remove(rand.nextInt(colors.size())));
        }
        return areasColor;
    }
    
    //Remplazar el suelo con los colores aleatorios
    public void replaceFloor() {
        Block iceblock;
        for (Location loc : baseBlocks) {
            Block carpetblock = loc.getBlock();
            carpetblock.setType(Material.CARPET);
            carpetblock.setData(arenacolor.getWoolData());
            iceblock = carpetblock.getRelative(BlockFace.DOWN);
            iceblock.setType(Material.ICE);
        }
        for (DyeMiniArea mat : areasColor) {
            for (Location loc : mat.getBlocks()) {
                Block b = loc.getBlock();
                b.setType(Material.CARPET);
                b.setData(mat.color.getWoolData());
                iceblock = b.getRelative(BlockFace.DOWN);
                iceblock.setType(Material.ICE);
            }
        }
    }
    
    //Aleatoriedad en el suelo
    public DyeColor spinColors(boolean samecolor) {
        DyeColor tempcolor = (DyeColor) allowedcolors.get(rand.nextInt(allowedcolors.size() - 1));

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
    
    //Código a repetir cada ronda. Aumentar número de ronda, guardar estadísticas de rondas jugadas. Mezclar colores y mandar el color al jugador
    public void startRound() {
        round += 1;
        currentcolor = spinColors(true);
        plugin.getMsg().sendBroadcast("El color actual es " + getColorFromWool(currentcolor) + nombreCastellano(currentcolor) + "! " + ChatColor.DARK_GREEN + "¡Ya!");
        for (Player p : plugin.getGm().getPlayersInGame()) {
            new Title(getColorFromWool(currentcolor) + nombreCastellano(currentcolor), "", 1, 2, 1).send(p);
            p.setLevel(round);
            p.setExp(0.9999F);
            p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 1F);
            
            final DyePlayer dp = DyeOrDie.getPlayer(p);
            dp.getUserData().setRondas_dod(dp.getUserData().getRondas_dod() + 1);
            if (round > dp.getUserData().getRecord_dod()) {
                dp.getUserData().setRecord_dod(round);
                dp.sendMessage("Has batido tu record de rondas seguidas, ahora es " + round + "!");
            }
            dp.save();
        }
    }

    public void roundEnded() {
        setTimeLeft(0.0F);
        Block iceblock;
        for (Location loc : baseBlocks) {
            Block carpetblock = loc.getBlock();
            carpetblock.setType(Material.AIR);
            iceblock = carpetblock.getRelative(BlockFace.DOWN);
            iceblock.setType(Material.AIR);
            if (rand.nextInt(3) == 1) { //No hacer caer todos
                iceblock.getWorld().spawnFallingBlock(iceblock.getLocation(), Material.WOOL, arenacolor.getWoolData()).setDropItem(false);
            }
        }
        for (DyeMiniArea mat : areasColor) {
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
    
    //Mostrar a los usuarios el tiempo que queda
    public void setTimeLeft(float tl) {
        timeleft = Double.parseDouble(""+tl) / 20;
        plugin.getGm().getPlayersInGame().stream().forEach(p -> plugin.getMsg().sendActionBar(p, "&aTiempo restante: &e" + timeleft.intValue()));
    }
    
    //Dar formato visual y idioma castellano a los colores
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
    
    private String nombreCastellano(DyeColor dye) {
        switch (dye) {
            case BLACK: return "Negro";
            case BLUE: return "Azul";
            case BROWN: return "Marrón";
            case CYAN: return "Cian";
            case GRAY: return "Gris";
            case GREEN: return "Verde";
            case LIME: return "Lima";
            case MAGENTA: return "Magenta";
            case ORANGE: return "Naranja";
            case PINK: return "Rosa";
            case PURPLE: return "Morado";
            case RED: return "Rojo";
            case YELLOW: return "Amarillo";
        }
        return "?";
    }
}
