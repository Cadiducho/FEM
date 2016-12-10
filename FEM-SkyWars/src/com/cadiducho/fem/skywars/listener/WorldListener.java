package com.cadiducho.fem.skywars.listener;

import com.cadiducho.fem.skywars.SkyWars;
import com.cadiducho.fem.skywars.manager.GameState;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.ItemStack;

public class WorldListener implements Listener {

    private final SkyWars plugin;

    public WorldListener(SkyWars instnace) {
        plugin = instnace;
    }
    
    @EventHandler
    public void onMotdChange(ServerListPingEvent e){
        e.setMotd(GameState.getParsedStatus());
    }
}