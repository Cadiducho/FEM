package com.cadiducho.fem.core.listeners;

import com.cadiducho.fem.core.FEMCore;
import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.core.api.FEMUser;
import com.cadiducho.fem.core.cmds.FEMCmd;
import com.cadiducho.fem.core.util.FEMFileLoader;
import com.cadiducho.fem.core.util.Metodos;
import org.bukkit.GameMode;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
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
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerLogin(PlayerLoginEvent e) {
        if (e.getResult() == PlayerLoginEvent.Result.ALLOWED) {
            plugin.getMysql().setupTable(e.getPlayer());
        }
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent e) {
        FEMUser u = FEMServer.getUser(e.getPlayer());
        
        //Actualizar variables
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
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        FEMUser u = FEMServer.getUser(e.getPlayer());

        u.getUserData().setLastLocation(u.getPlayer().getLocation());
        u.getUserData().setLastConnect(System.currentTimeMillis());
        u.save();

        //Evitar sobrecargas con Users null
        if (FEMServer.adminChatMode.contains(u)) {
            FEMServer.adminChatMode.remove(u);
        }

        e.setQuitMessage(Metodos.colorizar("&7" + e.getPlayer().getDisplayName() + " " + FEMFileLoader.getLang().getString("salir")));
        FEMServer.users.remove(u);
    }
    
    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent e) {
        FEMUser u = FEMServer.getUser(e.getPlayer());
        
        switch (e.getCause()) {
            case COMMAND:
            case PLUGIN:
                u.getUserData().setLastLocation(e.getFrom());
                break;
        }
    }
    
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        FEMUser u = FEMServer.getUser(e.getEntity());

    }
    
    @EventHandler
    public void onMobSpawn(CreatureSpawnEvent e) {
        //Parchear error de Spigot 1.9 = Caballos esqueletos en cantidades industriales
        if (e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.LIGHTNING || e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.TRAP) {
            if ((e.getEntity() instanceof Horse)) e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        FEMUser u = FEMServer.getUser(e.getPlayer());
        
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        FEMUser u = FEMServer.getUser(e.getPlayer());

    }
      
    @EventHandler
    public void playerCommand(PlayerCommandPreprocessEvent e) {
        FEMUser u = FEMServer.getUser(e.getPlayer());
        //Bloquear lista de comandos
        if(u.isOnRank(FEMCmd.Grupo.Owner)) {
            return;
        }
 
        if (e.getMessage().startsWith("/?") || e.getMessage().startsWith("/bukkit:") || e.getMessage().startsWith("/pl") || e.getMessage().startsWith("/plugins") || e.getMessage().startsWith("/minecraft:")) {
            u.sendMessage("&aPor aquí no hay nada que ver...");
            e.setCancelled(true);
        }
    }
}
