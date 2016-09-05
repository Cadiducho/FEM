package com.cadiducho.fem.tnt.task;

import com.cadiducho.fem.tnt.TntIsland;
import com.cadiducho.fem.tnt.TntPlayer;
import com.cadiducho.fem.tnt.TntWars;
import org.bukkit.scheduler.BukkitRunnable;

public class TntExplodeTask extends BukkitRunnable {
    
    private final TntIsland isla;
    private final TntPlayer exploder;
    
    public TntExplodeTask(TntIsland instance, TntPlayer p) {
        isla = instance;
        exploder = p;
    }
    
    private int count = 5;
    
    @Override
    public void run() {    
        if (count == 5) {
            exploder.getBase().getUserData().setTntPuestas(exploder.getBase().getUserData().getTntPuestas() + 1);
            exploder.getBase().save();
            exploder.getBase().sendMessage("Has puesto la TNT y explotará en 5 segundos");
            isla.getOwner().sendMessage("Tu isla explotará en 5 segundos si no lo evitas");
        } else if (count == 0) {
            isla.explode();
            exploder.getBase().getUserData().setTntExplotadas(exploder.getBase().getUserData().getTntExplotadas() + 1);
            exploder.getBase().save();
            TntWars.getInstance().getMsg().sendBroadcast("La isla de " + isla.getOwner().getDisplayName() + " ha sido destruida por " + exploder.getBase().getDisplayName());
            cancel();
        }
        
        count--;
    }
    
}
