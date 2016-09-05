package com.cadiducho.fem.ovejas.manager.tasks;

import com.cadiducho.fem.core.util.Cooldown;
import com.cadiducho.fem.core.util.Metodos;
import com.cadiducho.fem.ovejas.SheepQuest;
import java.util.ArrayList;
import java.util.Random;
import lombok.Getter;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Sheep;
import org.bukkit.scheduler.BukkitRunnable;

public class OvejasTask extends BukkitRunnable {

    private final SheepQuest plugin;
    private final Location spawn;

    public OvejasTask(SheepQuest instance) {
        plugin = instance;
        spawn = Metodos.stringToLocation(plugin.getConfig().getString("SheepQuest.Arena.Spawn.blanco"));
    }
    
    @Getter final static Cooldown cool = new Cooldown(20);

    @Override
    public void run() {
        Random rand = new Random();
        Sheep sheep = (Sheep) spawn.getWorld().spawnEntity(spawn, EntityType.SHEEP);
        if (rand.nextInt(5) == 1) { //Baja probabilidad de spawnear una oveja especial
            sheep.setCustomName("jeb_");
            sheep.setCustomNameVisible(false);
        } else {
            sheep.setColor(DyeColor.WHITE);
        }
        sheep.setAI(false);

        cool.setOnCooldown("sheep");
    }
}
