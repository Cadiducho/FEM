package com.cadiducho.fem.tnt.task;

import com.cadiducho.fem.tnt.TntIsland;
import com.cadiducho.fem.tnt.TntPlayer;
import com.cadiducho.fem.tnt.TntWars;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class TntExplodeTask extends BukkitRunnable {
    
    private final TntIsland isla;
    private final UUID exploder;
    
    public TntExplodeTask(TntIsland instance, UUID p) {
        isla = instance;
        exploder = p;
    }
    
    private int count = 10;
    
    @Override
    public void run() {
        final TntPlayer tpExploder = TntWars.getPlayer(TntWars.getInstance().getServer().getOfflinePlayer(exploder));
        final TntPlayer tpOwner = TntWars.getPlayer(TntWars.getInstance().getServer().getOfflinePlayer(isla.getOwner()));
        
        if (count == 10) {
            tpExploder.getUserData().setTntPuestas(tpExploder.getUserData().getTntPuestas() + 1);
            tpExploder.save();
            tpExploder.sendMessage("&aHas puesto la TNT y explotará en 10 segundos");
            tpOwner.sendMessage("&cTu isla explotará en &b10 segundos &csi no lo evitas");
        } else if (count > 0 && count < 4) {
            TntWars.getInstance().getGm().getPlayersInGame().forEach(p -> p.playSound(isla.getBedrockCore().getLocation(), Sound.ANVIL_LAND, (9F + count), 1F));
            tpOwner.sendMessage("&c¡Tu isla explotará en &b" + count + " segundo" + (count == 1 ? "" : "s") + " &csi no lo evitas!");
        } else if (count == 0) {
            isla.explode();
            tpExploder.getUserData().setTntExplotadas(tpExploder.getUserData().getTntExplotadas() + 1);
            tpExploder.save();
            TntWars.getInstance().getMsg().sendBroadcast("&eLa isla de &c" + tpOwner.getName() + " &eha sido destruida por &b" + tpExploder.getName());
            tpExploder.getUserData().setCoins(tpExploder.getUserData().getCoins() + 1);
            tpExploder.save();
            cancel();
        }
        
        count--;
    }
    
}
