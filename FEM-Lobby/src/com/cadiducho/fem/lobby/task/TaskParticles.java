package com.cadiducho.fem.lobby.task;

import com.cadiducho.fem.lobby.Lobby;
import com.cadiducho.fem.lobby.utils.ParticleType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TaskParticles extends BukkitRunnable{

    //private HashMap<FEMUser, ParticleType> effects = new HashMap<>();

    private final Player p;
    private final ParticleType pt;

    private final Lobby plugin;

    public TaskParticles(Lobby plugin, Player p, ParticleType pt) {
        this.plugin = plugin;
        this.p = p;
        this.pt = pt;
    }

    @Override
    public void run() {
        if (!p.isOnline()) {
            cancel();
            return;
        }
        plugin.getMathsUtils().drawParticles(p, pt);
    }
}
