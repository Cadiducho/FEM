package com.cadiducho.fem.core.listeners;

import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.core.util.Title;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.plugin.Plugin;

/**
 * Evento a registrar en los minijuegos que usen ResourcePacks
 * Garantiza la descarga e instalación de las texturas para cada juego
 */
public class ResourcePackManager implements Listener {
    
    private final Plugin plugin;
    private final String pack;
    
    public ResourcePackManager(Plugin instance, String url) {
        plugin = instance;
        pack = url;
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onResourcePlayer(PlayerResourcePackStatusEvent e){
        Player p = e.getPlayer();
        
        switch (e.getStatus()) {
            case DECLINED:
            case FAILED_DOWNLOAD:
                new Title(ChatColor.RED + "Debes aceptar el ResourcePack").send(p);
                plugin.getServer().getScheduler().runTaskLater(plugin, ()-> FEMServer.getUser(p).sendToLobby(), 40L);
                break;
        }
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent e) {    
        e.getPlayer().setResourcePack(pack);
    }
}
