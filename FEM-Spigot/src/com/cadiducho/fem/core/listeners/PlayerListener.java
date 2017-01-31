package com.cadiducho.fem.core.listeners;

import com.cadiducho.fem.core.FEMCore;
import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.core.api.FEMUser;
import com.cadiducho.fem.core.cmds.FEMCmd;
import com.cadiducho.fem.core.util.Metodos;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import net.md_5.bungee.api.chat.TextComponent;
import org.inventivetalent.bossbar.BossBarAPI;

public class PlayerListener implements Listener {
    
    private final FEMCore plugin;

    public PlayerListener(FEMCore instance) {
        plugin = instance;
    }
    
    /*
     * Capturar eventos del adminchat antes que el resto de juegos
     */
    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        FEMUser user = FEMServer.getUser(event.getPlayer());
        
        //AdminChat
        if (FEMServer.adminChatMode.contains(user)) {
            Player p = event.getPlayer();
            plugin.getMetodos().enviarPorAdminChat(p.getDisplayName(), event.getMessage());
            event.setCancelled(true);
        }

        //Eventos del chat global enlazados en FEM-Chat
    }
    
    /*
     * Metodo para registrar al usuario por primera vez
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerLogin(PlayerLoginEvent e) {
        if (e.getResult() == PlayerLoginEvent.Result.ALLOWED) {
            plugin.getMysql().setupTable(e.getPlayer());
        }
    }
    
    /*
     * Metodos que se ejecutan nada más un jugador entre al servidor, 
     * por encima del resto de plugins
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent e) {
        FEMUser u = FEMServer.getUser(e.getPlayer());
        
        //Actualizar variables 
        u.getUserData().setLastConnect(System.currentTimeMillis());
        u.getUserData().setTimeJoin(System.currentTimeMillis());
        u.getUserData().setIp(u.getPlayer().getAddress());
        u.save(); 
        
        //Poner usuarios en survival
        if (!u.isOnRank(FEMCmd.Grupo.Admin)) {
            u.getPlayer().setGameMode(GameMode.SURVIVAL);
        }
        
        //Hud
        BossBarAPI.removeAllBars(e.getPlayer());
        BossBarAPI.addBar(e.getPlayer(), new TextComponent(Metodos.colorizar("&6&lUnder&e&lGames&7 &c- &emc.undergames.es")), BossBarAPI.Color.BLUE, BossBarAPI.Style.PROGRESS, 1.0f);
    }
     
    @EventHandler(priority = EventPriority.LOW)
    public void onDamage(EntityDamageEvent e) {
        //God
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (FEMServer.getUser(p).getUserData().getGod()) {
                p.setFireTicks(0);
                p.setHealth(p.getMaxHealth());
                p.setRemainingAir(p.getMaximumAir());
                e.setCancelled(true);
            }
        }
    }
    
    @EventHandler(priority = EventPriority.LOW)
    public void onFoodLevelChange(FoodLevelChangeEvent e) {
        //God
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (FEMServer.getUser(p).getUserData().getGod()) {
                e.setCancelled(true);
                p.setFoodLevel(20);
            }
        }
    }
    
    /*
     * Procesar la desonexión de un jugador. Guardar datos y elimiar su cache
     */
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        FEMUser u = FEMServer.getUser(e.getPlayer());
        
        e.setQuitMessage(null);
        
        u.getUserData().setLastLocation(u.getPlayer().getLocation());
        u.getUserData().setLastConnect(System.currentTimeMillis());
        
        //Tiempo jugado: Tiempo jugado + hora actual - conexion
        u.getUserData().setTimePlayed(u.getUserData().getTimePlayed() + System.currentTimeMillis() - u.getUserData().getTimeJoin());
        u.save();

        //Evitar sobrecargas con Users null
        if (FEMServer.adminChatMode.contains(u)) {
            FEMServer.adminChatMode.remove(u);
        }

        FEMServer.users.remove(u);
        BossBarAPI.removeAllBars(e.getPlayer());
    }
    
    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent e) {
        FEMUser u = FEMServer.getUser(e.getPlayer());
        
        //Procesar el /back
        switch (e.getCause()) {
            case COMMAND:
            case PLUGIN:
                u.getUserData().setLastLocation(e.getFrom());
                break;
        }
    }
    
    /*
     * Bloquear lista de comandos
     */
    @EventHandler
    public void playerCommand(PlayerCommandPreprocessEvent e) {
        FEMUser u = FEMServer.getUser(e.getPlayer());

        if(u.isOnRank(FEMCmd.Grupo.Admin)) {
            return;
        }
 
        if (e.getMessage().startsWith("/?") || e.getMessage().startsWith("/bukkit:") || e.getMessage().startsWith("/pl") || e.getMessage().startsWith("/plugins") || e.getMessage().startsWith("/minecraft:")) {
            u.sendMessage("&aPor aquí no hay nada que ver...");
            e.setCancelled(true);
        }
    }
    
    /*
     * Parkour en todos los servidores, si está activado
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        FEMUser u = FEMServer.getUser(e.getPlayer());
        
        //No destruir tirras de cultivo (soil)
        if (e.getAction() == Action.PHYSICAL && e.getClickedBlock().getType() == Material.SOIL) {
            e.setCancelled(true);
        }
        
        //Parkour
        if (FEMServer.getEnableParkour()) {   
            if (e.getAction() == Action.PHYSICAL && e.getClickedBlock().getType() == Material.IRON_PLATE) {
                //Comenzar u acabar parkour
                if (u.getUserData().getParkourStartTime() == -1L) { //Comenzar
                    u.getUserData().setParkourStartTime(System.currentTimeMillis());
                    u.getUserData().setParkourCheckpoint(e.getPlayer().getLocation());
                    u.sendMessage("&e¡Has comenzado un parkour! Escribe &a/pk &esi te caes para volver a un checkpoint");
                    u.save();
                } else { //Terminar
                    u.sendMessage("&e¡Enhorabuena! &aHas terminado el pakour con un tiempo de " + new SimpleDateFormat("HH:mm:ss").format(new Date(System.currentTimeMillis() - u.getUserData().getParkourStartTime())));
                    u.getUserData().setParkourStartTime(-1L);
                    u.getUserData().setParkourCheckpoint(plugin.getServer().getWorlds().get(0).getSpawnLocation());
                    u.save();
                }
            }

            if (e.getAction() == Action.PHYSICAL && e.getClickedBlock().getType() == Material.GOLD_PLATE) {
                if (u.getUserData().getParkourStartTime() != -1L) {
                    u.sendMessage("&e¡Checkpoint superado!");
                    u.getUserData().setParkourCheckpoint(e.getPlayer().getLocation());
                    u.save();
                }
            }
        }
    }
}
