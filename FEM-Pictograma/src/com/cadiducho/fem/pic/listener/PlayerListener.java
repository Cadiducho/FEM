package com.cadiducho.fem.pic.listener;

import com.cadiducho.fem.core.util.Title;
import com.cadiducho.fem.pic.Pictograma;
import com.cadiducho.fem.pic.task.GameTask;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.*;

public class PlayerListener implements Listener {

    private final Pictograma plugin;

    public PlayerListener(Pictograma instance) {
        plugin = instance;
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent e) {
        if (plugin.getGm().acceptPlayers() && plugin.getGm().getPlayersInGame().size() < plugin.getAm().getMaxPlayers()) {
            e.allow();
        } else {
            e.setResult(PlayerLoginEvent.Result.KICK_OTHER);
            e.setKickMessage("No tienes acceso a entrar aquí.");
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        e.setJoinMessage(null);
        player.setResourcePack("enlace");
        if (plugin.getGm().acceptPlayers()) {
            plugin.getServer().getOnlinePlayers().stream().forEach(p -> player.showPlayer(p)); // Mostrar todos los jugadores a todos
            plugin.getServer().getOnlinePlayers().stream().forEach(p -> p.showPlayer(player));
            player.teleport(plugin.getAm().getLobby());
            Pictograma.getPlayer(player).setLobbyPlayer();
            plugin.getMsg().sendBroadcast("&7Ha entrado al juego &e" + player.getDisplayName() + " &3(&b" + plugin.getGm().getPlayersInGame().size() + "&d/&b" + plugin.getAm().getMaxPlayers() + "&3)");
            plugin.getMsg().sendHeaderAndFooter(player, "&6Under&eGames&7", "&cmc.undergames.es");
            plugin.getGm().checkStart();
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        e.setQuitMessage(null);
        //Eliminar de las listas y cancelar sus puntos
        plugin.getGm().removePlayerFromGame(player);
        plugin.getMsg().sendBroadcast("&e " + player.getDisplayName() + " &7abandonó la partida");
        
        //Comprobar si terminar la partida (solo queda uno)
        if (plugin.getGm().isInGame()) {
            if (plugin.getGm().getPlayersInGame().size() < 2) {
                GameTask.getGameInstance().cancel();
                plugin.getGm().endGame();
            } else if (plugin.getGm().builder.getUniqueId() == player.getUniqueId()) {
                //Si el constructor se va, terminar la ronda
                GameTask.getGameInstance().prepareNextRound();
            }
        }
        Pictograma.players.remove(Pictograma.getPlayer(player));
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (plugin.getGm().acceptPlayers()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent e) {
        if (e instanceof LivingEntity) {
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerFood(FoodLevelChangeEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerPickUp(PlayerPickupItemEvent e) {
        e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onResourcePlayer(PlayerResourcePackStatusEvent e){
        Player p = e.getPlayer();
        PlayerResourcePackStatusEvent.Status status = e.getStatus();
        if(status == PlayerResourcePackStatusEvent.Status.ACCEPTED || status == PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED){
            return;
        }
        final String reason = "";
        switch (status) {
            case DECLINED:
                plugin.getGm().removePlayerFromGame(p);
                new Title(ChatColor.RED + "Debes aceptar el ResourcePack");
                break;
            case FAILED_DOWNLOAD:
                plugin.getGm().removePlayerFromGame(p);
                new Title(ChatColor.RED + "Debes aceptar el ResourcePack");
                break;
        }
        Bukkit.getScheduler().runTaskLater(Pictograma.getInstance(), ()-> Pictograma.getPlayer(p).sendToLobby(), 40L);
    }
}
