package com.cadiducho.fem.uhc.listeners;

import java.util.Collection;
import com.cadiducho.fem.uhc.Main;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChat implements Listener {

    public Main plugin;

    public PlayerChat(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        Collection<UUID> inGame = plugin.gm.PLAYERS_IN_GAME;
        Collection<UUID> espect = plugin.gm.SPECTATORS;
        
        if (espect.contains(p.getUniqueId())) {
            espect.stream()
                    .map(id -> plugin.getServer().getPlayer(id))
                    .forEach(pl -> pl.sendMessage("§d" + p.getDisplayName() + "§f - " + e.getMessage()));

            e.setCancelled(true);
            return;
        }
        
        if (inGame.contains(p.getUniqueId())) {
            inGame.stream()
                    .map(id -> plugin.getServer().getPlayer(id))
                    .forEach((jug) -> jug.sendMessage("§a" + p.getDisplayName() + "§f - " + e.getMessage()));
            e.setCancelled(true);
        }    
    }
}
