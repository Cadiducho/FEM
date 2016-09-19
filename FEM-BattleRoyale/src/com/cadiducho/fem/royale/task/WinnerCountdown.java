package com.cadiducho.fem.royale.task;

import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.royale.BattleRoyale;
import org.bukkit.scheduler.BukkitRunnable;

public class WinnerCountdown extends BukkitRunnable{
    
    private final BattleRoyale plugin;

    public WinnerCountdown(BattleRoyale plugin) {
        this.plugin = plugin;
    }
    
    private int game = 9;

    @Override
    public void run() {
        if(game == 5){
            plugin.getServer().getOnlinePlayers().stream().forEach(p -> FEMServer.getUser(p).sendToLobby());
        } else if(game == 0){
            plugin.getServer().shutdown();
            cancel();            
        }
        -- game;
    } 
}
