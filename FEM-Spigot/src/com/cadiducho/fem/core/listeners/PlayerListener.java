package com.cadiducho.fem.core.listeners;

import com.cadiducho.fem.core.FEMCore;
import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.core.api.FEMUser;
import com.cadiducho.fem.core.cmds.FEMCmd;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

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
        try {
            URL url = new URL("http://api.predator.wtf/geoip/?arguments=" + u.getPlayer().getAddress().getHostName());
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String strTemp = "";
            while ((strTemp = br.readLine()) != null) {
                if (strTemp.contains("Country: ")) {
                    String country = strTemp.replace("Country: ", "");
                    Integer lang;
                    switch (country.toLowerCase()) {
                        case "france": lang = 1; break;
                        case "italy": lang = 2; break;
                        default: lang = 0; break;
                    }
                    
                    u.getUserData().setLang(lang);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(PlayerListener.class.getName()).log(Level.SEVERE, null, ex);
        }
          
        u.getUserData().setLastConnect(System.currentTimeMillis());
        u.getUserData().setIp(u.getPlayer().getAddress());
        u.save(); 
        
        //Poner usuarios en survival
        if (!u.isOnRank(FEMCmd.Grupo.Owner)) {
            u.getPlayer().setGameMode(GameMode.SURVIVAL);
        }
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

        if(u.isOnRank(FEMCmd.Grupo.Owner)) {
            return;
        }
 
        if (e.getMessage().startsWith("/?") || e.getMessage().startsWith("/bukkit:") || e.getMessage().startsWith("/pl") || e.getMessage().startsWith("/plugins") || e.getMessage().startsWith("/minecraft:")) {
            u.sendMessage("&aPor aquí no hay nada que ver...");
            e.setCancelled(true);
        }
    }
}
