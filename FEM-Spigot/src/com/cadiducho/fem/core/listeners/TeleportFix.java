package com.cadiducho.fem.core.listeners;

import java.util.ArrayList;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;

/**
 * Evento a registrar en cada minijuego para subsanar error de no ver jugadores al teletransportarse
 * Recurrente en: Fase lobby de minijuegos
 */
public class TeleportFix implements Listener {
    
    private final Plugin plugin;
    
    public TeleportFix(Plugin instance) {
        plugin = instance;
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        final Player player = event.getPlayer();
        
        // if (event.getFrom().getWorld() != event.getTo().getWorld()) Testear rendimiento
        
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            final ArrayList<Player> jugadores = getPlayers(player);
            
            //Esconder todos los jugadores
            updateEntities(player, jugadores, false);
            //Volverlos a mostrar un tick más tarde...
            plugin.getServer().getScheduler().runTask(plugin, () -> {
               updateEntities(player, jugadores, true); 
            });
        }, 15); //15 Ticks más tarde
    }
    
    private void updateEntities(Player tpPlayer, ArrayList<Player> players, boolean show) {
        for (Player player : players) {
            if (show) {
                tpPlayer.showPlayer(player);
                player.showPlayer(tpPlayer);
            } else {
                tpPlayer.hidePlayer(player);
                player.hidePlayer(tpPlayer);
            }
        }
    }
    
    private ArrayList<Player> getPlayers(Player player) {
        ArrayList<Player> list = new ArrayList<>();
        plugin.getServer().getOnlinePlayers().stream()
                .filter(p -> (p != player && p.getWorld() == player.getWorld()))
                .forEach(p -> list.add(p));
        return list;
    }
}
