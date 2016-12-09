package com.cadiducho.fem.lobby.task;

import com.cadiducho.fem.lobby.utils.MathsUtils;
import com.cadiducho.fem.lobby.utils.ParticleType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TaskParticles extends BukkitRunnable{

    //private HashMap<FEMUser, ParticleType> effects = new HashMap<>();

    private final Player p;
    private final ParticleType pt;

    public TaskParticles(Player p, ParticleType pt) {
        this.p = p;
        this.pt = pt;
    }

    @Override
    public void run() {
        if (!p.isOnline()) return;

        MathsUtils mu = new MathsUtils();
        mu.drawParticles(p, pt);
    }
}
