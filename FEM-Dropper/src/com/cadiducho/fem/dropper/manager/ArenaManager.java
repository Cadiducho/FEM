package com.cadiducho.fem.dropper.manager;

import com.cadiducho.fem.dropper.Dropper;
import com.cadiducho.fem.core.util.Metodos;
import lombok.Getter;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.EntityType;

public class ArenaManager {

    private final Dropper plugin;
    @Getter private final Location lobby;

    public ArenaManager(Dropper instance) {
        plugin = instance;

        lobby = Metodos.stringToLocation(plugin.getConfig().getString("Dropper.lobby"));
        prepareWorld(plugin.getConfig().getString("Dropper.lobby").split("%")[0]);
        for (String str : plugin.getConfig().getStringList("Dropper.mapas")) {
            prepareWorld(str);
        }
    }

    public void prepareWorld(String str) {
        new WorldCreator(str).createWorld();
        World w = plugin.getServer().getWorld(str);
        w.setPVP(false);
        w.setGameRuleValue("doDaylightCycle", "false");
        w.setStorm(false);
        w.setDifficulty(Difficulty.PEACEFUL);
        w.setTime(6000);
        w.getLivingEntities().stream()
                .filter(e -> !e.getType().equals(EntityType.PLAYER))
                .forEach(e -> e.damage(e.getMaxHealth()));
        w.setAutoSave(false);
    }
}
