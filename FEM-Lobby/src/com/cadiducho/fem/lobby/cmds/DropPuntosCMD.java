package com.cadiducho.fem.lobby.cmds;

import com.cadiducho.fem.core.api.FEMUser;
import com.cadiducho.fem.core.cmds.FEMCmd;
import com.cadiducho.fem.lobby.task.TaskDropCoins;
import java.util.Arrays;
import org.bukkit.scheduler.BukkitTask;

public class DropPuntosCMD extends FEMCmd {

    public DropPuntosCMD() {
        super("droppuntos", Grupo.YT, Arrays.asList());
    }

    @Override
    public void run(FEMUser user, String lbl, String[] args) {
        int tiempo = 10;
        
        if (user.getUserData().getCoins() < 10) {
            user.sendMessage("No tienes coins suficientes");
            return;
        }
        BukkitTask i = new TaskDropCoins(user).runTaskTimer(plugin, 1L, 20L);
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            i.cancel();
            user.save();
        }, tiempo * 20L);
    }
}
