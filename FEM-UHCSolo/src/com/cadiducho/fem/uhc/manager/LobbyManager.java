package com.cadiducho.fem.uhc.manager;

import com.cadiducho.fem.uhc.Main;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class LobbyManager {

    public Main plugin;

    public LobbyManager(Main plugin) {
        this.plugin = plugin;
    }

    public ArrayList<Location> spawnList = new ArrayList<>();

    public int spawn;

    public void loadSpawn(Player player) {
        Random rand = new Random();
        int rd = rand.nextInt(spawnList.size());
        if (!spawnList.get(rd).getChunk().isLoaded()) {
            spawnList.get(rd).getChunk().load(true);
        }
        player.teleport(spawnList.get(rd));
        plugin.grm.setBuildGround(spawnList.get(rd));
        spawnList.remove(rd);
    }

    public void addSpawnOnQuit(Player player) {
        spawnList.add(player.getLocation());
    }

    public void setSpawn(Player player) {
        String l = plugin.lu.locationToString(player.getLocation());
        spawn = spawn + 1;
        plugin.getConfig().set("DonkeyUHC.Arena.Spawn." + spawn, l);
        plugin.saveConfig();
        plugin.reloadConfig();
        spawnList.clear();
        for (int i = 1; i <= spawn; i++) {
            spawnList.add(plugin.lu.stringToLoc(plugin.getConfig().getString("DonkeyUHC.Arena.Spawn." + i)));
        }
        plugin.msg.sendMessage(player, "&7Has seteado el spawn &e&l" + spawn);
        plugin.grm.setBuildGround(player.getLocation());
    }

    public void loadSpawns() {
        spawnList.clear();
        plugin.getLogger().log(Level.CONFIG, "HA COMENZADO A CARGAR LOS SPAWNS");
        try {
            for (int i = 1; i <= 41; i++) {
                spawnList.add(plugin.lu.stringToLoc(plugin.getConfig().getString("DonkeyUHC.Arena.Spawn." + i)));
                plugin.getLogger().log(Level.CONFIG, "CARGANDO SPAWN {0}", i);

            }
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "HA OCURRIDO UN ERROR");
        }
    }

    public void generateSpawns() {
        spawnList.stream().forEach((loc) -> {
            plugin.grm.setBuildGround(loc);
        });
    }

    public void clearSpawn() {
        spawnList.stream().forEach(plugin.grm::clear);
    }

}
