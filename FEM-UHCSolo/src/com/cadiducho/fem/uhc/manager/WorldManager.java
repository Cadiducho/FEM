package com.cadiducho.fem.uhc.manager;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.WorldBorder;

import com.cadiducho.fem.uhc.Main;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class WorldManager {

    public Main plugin;

    public WorldManager(Main plugin) {
        this.plugin = plugin;
    }

    public String server_dir = new File(".").getAbsolutePath().substring(0, new File(".").getAbsolutePath().length() - 1);

    private World w;
    private World wn;
    private WorldBorder wb;

    public void setFinalStatic(Field field, Object obj) throws Exception {
        field.setAccessible(true);
        Field mf = Field.class.getDeclaredField("modifiers");
        mf.setAccessible(true);
        mf.setInt(field, field.getModifiers() & 0xFFFFFFEF);
        field.set(null, obj);
    }

    public boolean isWorldLoaded(String name) {
        World w = Bukkit.getWorld(name);
        return w != null;
    }

    public boolean doesWorldExist(String name) {
        if (isWorldLoaded(name)) {
            return true;
        }
        File world_dir = new File(server_dir + File.separator + name);
        File level_dat = new File(server_dir + File.separator + name + File.separator + "level.dat");

        return (world_dir.exists()) && (level_dat.exists());
    }

    public void deleteWorld(String name, boolean deleteAll) {
        try {
            if (!doesWorldExist(name)) {
                return;
            }
            if (isWorldLoaded(name)) {
                unloadWorld(name);
            }
            File world_dir = new File(server_dir + File.separator + name);
            deleteFolder(world_dir, deleteAll);
        } catch (Exception e) {
        }
    }

    public boolean unloadWorld(String name) {
        if (!doesWorldExist(name)) {
            return false;
        }
        if (!isWorldLoaded(name)) {
            return true;
        }

        Bukkit.unloadWorld(name, true);
        return !isWorldLoaded(name);
    }

    public boolean loadWorld(String name) {
        if (!doesWorldExist(name)) {
            return false;
        }
        if (isWorldLoaded(name)) {
            return true;
        }
        Bukkit.createWorld(new WorldCreator(name));
        return isWorldLoaded(name);
    }

    public void deleteFolder(File folder, boolean deleteAll) {
        File[] files = folder.listFiles();
        boolean isEmpty = true;
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    deleteFolder(f, deleteAll);
                } else if (!f.getName().contains("session.lock") || deleteAll) {
                    f.delete();
                } else {
                    isEmpty = false;
                }
            }
        }
        if (isEmpty) {
            folder.delete();
        }
    }

    public void init() {

    }

    public void removePlayersFromNether() {
        getNetherWorld().getPlayers().stream().map((netherPlayers) -> {
            nether(netherPlayers);
            return netherPlayers;
        }).map((netherPlayers) -> {
            plugin.msg.sendMessage(netherPlayers, "&7Has sido teletransportado.");
            return netherPlayers;
        }).forEach((netherPlayers) -> {
            netherPlayers.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 3));
        });
    }

    public void nether(Player player) {
        int max = 700;
        int min = -700;
        double newX = min + (int) (Math.random() * (max - min + 1));
        double newZ = min + (int) (Math.random() * (max - min + 1));
        Location location = new Location(getArenaWorld(), newX, 120, newZ);
        if (!location.getChunk().isLoaded()) {
            location.getChunk().load(true);
        }
        player.teleport(location.add(0, 5, 0));
    }

    public void enviarJugadores(Player player) {
        int max = 740;
        int min = -740;
        double newX = min + (int) (Math.random() * (max - min + 1));
        double newZ = min + (int) (Math.random() * (max - min + 1));
        Location location = new Location(getArenaWorld(), newX, 120, newZ);
        if (!location.getChunk().isLoaded()) {
            location.getChunk().load(true);
        }
        player.teleport(location.add(0, 5, 0));
        plugin.grm.setBuildGround(location);
    }

    public void prepareWorld(World arena) {
        w = arena;
        w.setPVP(true);
        w.setGameRuleValue("naturalRegeneration", "false");
        w.setGameRuleValue("doDaylightCycle", "false");
        w.setStorm(false);
        w.setDifficulty(Difficulty.NORMAL);
        w.setSpawnLocation(0, 60, 0);
        w.setTime(6000);

        wb = w.getWorldBorder();
        wb.setCenter(0.0, 0.0);
        wb.setSize(1500);
        wb.setDamageAmount(1.0);
        wb.setWarningDistance(10);
        wb.setDamageBuffer(1.0);
        wb.setDamageAmount(4.0);
    }

    public void prepareNether(World nether) {
        wn = nether;
        wn.setPVP(true);
        wn.setDifficulty(Difficulty.NORMAL);
        wn.setGameRuleValue("naturalRegeneration", "false");
        wn.setSpawnLocation(0, 60, 0);
    }

    public World getArenaWorld() {
        return w;
    }

    public World getNetherWorld() {
        return wn;
    }

}
