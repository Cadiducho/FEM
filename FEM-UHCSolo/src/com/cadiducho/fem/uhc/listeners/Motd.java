package com.cadiducho.fem.uhc.listeners;

import com.cadiducho.fem.uhc.Main;
import com.cadiducho.fem.uhc.manager.GameState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class Motd implements Listener{

    public Main plugin;

    public Motd(Main plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onMotdChange(ServerListPingEvent e){
        e.setMotd("§aEsperando");
        if(GameState.state == GameState.LOBBY){
            e.setMotd("§aEsperando");
        } else if(GameState.state == GameState.TELETRANSPORTE || GameState.state == GameState.JUEGO || GameState.state == GameState.PVE || GameState.state == GameState.ELIMINACION){
            e.setMotd("§cEn juego");
        } else {
            e.setMotd("§aEsperando");
        }
    }

}
