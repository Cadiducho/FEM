package com.cadiducho.fem.tnt.task;

import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.tnt.TntIsland;
import com.cadiducho.fem.tnt.TntWars;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TntExplodeTask extends BukkitRunnable {
    
    private final TntIsland isla;
    private final Player exploder;
    
    public TntExplodeTask(TntIsland instance, Player p) {
        isla = instance;
        exploder = p;
    }
    
    private int count = 10;
    
    @Override
    public void run() {    
        if (count == 10) {
            FEMServer.getUser(exploder).getUserData().setTntPuestas(FEMServer.getUser(exploder).getUserData().getTntPuestas() + 1);
            FEMServer.getUser(exploder).save();
            FEMServer.getUser(exploder).sendMessage("Has puesto la TNT y explotará en 10 segundos");
            isla.getOwner().sendMessage("Tu isla explotará en 10 segundos si no lo evitas");
        } else if (count == 0) {
            isla.explode();
            FEMServer.getUser(exploder).getUserData().setTntExplotadas(FEMServer.getUser(exploder).getUserData().getTntExplotadas() + 1);
            FEMServer.getUser(exploder).save();
            TntWars.getInstance().getMsg().sendBroadcast("La isla de " + isla.getOwner().getDisplayName() + " ha sido destruida por " + exploder.getDisplayName());
            cancel();
        }
        
        count--;
    }
    
}
