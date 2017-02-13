package com.cadiducho.fem.dropper.manager;

import com.cadiducho.fem.core.util.Metodos;
import com.cadiducho.fem.dropper.DropPlayer;
import com.cadiducho.fem.dropper.Dropper;
import lombok.Getter;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;

public class ArenaManager {

    private final Dropper plugin;
    @Getter private final Location lobby;

    @Getter private ArrayList<DropPlayer> completed;

    public ArenaManager(Dropper instance) {
        plugin = instance;
        completed = new ArrayList<>();

        lobby = Metodos.stringToLocation(plugin.getConfig().getString("Dropper.lobby"));
        prepareWorld(plugin.getConfig().getString("Dropper.lobby").split("%")[0]);
        plugin.getConfig().getStringList("Dropper.mapas").forEach(str -> {
            prepareWorld(str);
        });
    }

    public final void prepareWorld(String str) {
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
    }
}
