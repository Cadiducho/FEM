package com.cadiducho.fem.core.listeners;

import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.core.util.Title;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.plugin.Plugin;
import us.myles.ViaVersion.api.Via;

/**
 * Evento a registrar en los minijuegos que usen ResourcePacks
 * Garantiza la descarga e instalación de las texturas para cada juego
 */
public class ResourcePackManager implements Listener {

    //ToDo: Mantener actualizado

    @AllArgsConstructor
    public enum Games{
        LOBBY("nada"),
        BATTLE_ROYALE("Royale"),
        LUCKY_WARRIORS("Lucky-Pack"),
        PICTOGRAMA("PictogramaResourcePack");

        @Getter private String pack;
    }

    private final Plugin plugin;
    private String name; // Nombre del pack sin .zip

    private String host = "http://cadox8.cf/ug/";

    //
    
    public ResourcePackManager(Plugin instance, Games game) {
        plugin = instance;
        this.name = game.getPack();
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onResourcePlayer(PlayerResourcePackStatusEvent e){
        Player p = e.getPlayer();

        switch (e.getStatus()) {
            case DECLINED:
            case FAILED_DOWNLOAD:
                Title.sendTitle(p, 1, 7, 1, ChatColor.RED + "Debes aceptar el ResourcePack", "");
                plugin.getServer().getScheduler().runTaskLater(plugin, ()-> FEMServer.getUser(p).sendToLobby(), 60L);
                break;
        }
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent e) {
        String version = "1_8"; //Por defecto está la 1.8, ya que la 1.9/10/11 van con esa versión
        switch (Via.getAPI().getPlayerVersion(e.getPlayer().getUniqueId())){
            case 315: //1.11
                version = "1_11";
                break;
            case 210: //1.10.X
            case 110: //1.9.3/4
            case 109: //1.9.2
            case 108: //1.9.1
            case 107: //1.9
                version = "1_9_10";
                break;
            case 47: //1.8.X
                version = "1_8";
                break;
        }
        e.getPlayer().setResourcePack(host + version + "/" + name + ".zip");
    }
}
