package com.cadiducho.fem.lucky.manager;

import com.cadiducho.fem.core.util.Metodos;
import com.cadiducho.fem.lucky.LuckyWarriors;
import java.util.ArrayList;
import java.util.Random;
import java.util.HashMap;
import lombok.Getter;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class ArenaManager {

    private final LuckyWarriors plugin;

    public ArenaManager(LuckyWarriors instance) {
        plugin = instance;
    }

    @Getter private int maxPlayers;
    @Getter private int minPlayers;
    @Getter private final ArrayList<Location> luckySpawnList = new ArrayList<>();
    @Getter private final ArrayList<Location> dungeonSpawnList = new ArrayList<>();
    @Getter private final ArrayList<Location> arenaSpawnList = new ArrayList<>();
    private final HashMap<Player, Location> assignedLuckySpawn = new HashMap<>();
    @Getter private Location lobby;
    public int luckyTime;
    public int craftTime;
    public int gameTime;
    public int deathMatchTime;
    
    private final Random rand = new Random();

    public void init() {
        luckyTime = plugin.getConfig().getInt("luckyTime");
        craftTime = plugin.getConfig().getInt("craftTime");
        gameTime = plugin.getConfig().getInt("gameTime");
        deathMatchTime = plugin.getConfig().getInt("deathMatchTime");
        maxPlayers = plugin.getConfig().getInt("Arena.Max");
        minPlayers = plugin.getConfig().getInt("Arena.Min");
        lobby = Metodos.stringToLocation(plugin.getConfig().getString("Arena.Lobby"));
        loadWorld(plugin.getWorld());
        loadSpawn();
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
        w.setAutoSave(false);
    }

    public void teleportLucky(Player p) {
        if (!assignedLuckySpawn.containsKey(p)) {
            int rd = rand.nextInt(luckySpawnList.size());
            assignedLuckySpawn.put(p, luckySpawnList.get(rd));
            luckySpawnList.remove(rd);
        }
        p.teleport(assignedLuckySpawn.get(p));
    }
    
    public void teleportDungeon(Player p) {
        int rd = rand.nextInt(dungeonSpawnList.size());
        p.teleport(dungeonSpawnList.get(rd));
        dungeonSpawnList.remove(rd);
    }
    
    public void teleportArena(Player p) {
        int rd = rand.nextInt(arenaSpawnList.size());
        p.teleport(arenaSpawnList.get(rd));
        arenaSpawnList.remove(rd);
    }

    public void loadSpawn() {
        luckySpawnList.clear();
        dungeonSpawnList.clear();
        arenaSpawnList.clear();
        try {
            for (int i = 1; i <= maxPlayers; i++) {
                luckySpawnList.add(Metodos.stringToLocation(plugin.getConfig().getString("Arena.LuckySpawns." + i)).add(0, 0.5, 0));
                dungeonSpawnList.add(Metodos.stringToLocation(plugin.getConfig().getString("Arena.DungeonSpawns." + i)));
                arenaSpawnList.add(Metodos.stringToLocation(plugin.getConfig().getString("Arena.ArenaSpawns." + i)));
            }
        } catch (Exception e) {
        }
    }
}