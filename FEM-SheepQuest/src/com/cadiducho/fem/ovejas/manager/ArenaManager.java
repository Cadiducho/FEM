package com.cadiducho.fem.ovejas.manager;

import com.cadiducho.fem.core.util.Metodos;
import com.cadiducho.fem.ovejas.SheepQuest;
import java.util.HashMap;


import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

public class ArenaManager {

    private final SheepQuest plugin;

    public ArenaManager(SheepQuest instance) {
        plugin = instance;
    }

    private final double bordeSize = 1000;
    private final double deathMatchSize = 50;

    private final int minPlayers = 2;
    private final int maxPlayers = 16;

    public void prepareWorld(World w) {
        w.setPVP(true);
        w.setGameRuleValue("naturalRegeneration", "false");
        w.setGameRuleValue("doDaylightCycle", "false");
        w.setStorm(false);
        w.setDifficulty(Difficulty.EASY);
        w.setTime(6000);
    }

    public double getBordeSize() {
        return bordeSize;
    }

    public double getDeathMatchSize() {
        return deathMatchSize;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

}
