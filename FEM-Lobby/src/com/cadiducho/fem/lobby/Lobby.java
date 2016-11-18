package com.cadiducho.fem.lobby;

import com.cadiducho.fem.core.FEMCommands;
import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.lobby.cmds.*;
import com.cadiducho.fem.lobby.listeners.PlayerListener;
import com.cadiducho.fem.lobby.listeners.WorldListener;
import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Lobby extends JavaPlugin {

    @Getter private static Lobby instance;

    @Getter @Setter private ArrayList<FEMServerInfo> servers;

    @Override
    public void onEnable() {
        instance = this;
        servers = new ArrayList<>();

        File fConf = new File(getDataFolder(), "config.yml");
        if (!fConf.exists()) {
            try {
                getConfig().options().copyDefaults(true);
                saveConfig();
            } catch (Exception e) { }
        }

        //Eventos y canales
        PluginManager pluginManager = getServer().getPluginManager();
        PlayerListener pl = new PlayerListener(instance);
        pluginManager.registerEvents(pl, instance);
        pluginManager.registerEvents(new WorldListener(instance), instance);
        
        getServer().getMessenger().registerOutgoingPluginChannel(instance, "BungeeCord");
        getServer().getMessenger().registerOutgoingPluginChannel(instance, "FEM");
        getServer().getMessenger().registerIncomingPluginChannel(instance, "FEM", pl);
        
        try {
            //Comandos solo para el lobby
            FEMCommands.registrar(new DropPuntosCMD());
            FEMCommands.registrar(new SetBrujulaCMD());
            FEMCommands.registrar(new SpawnCMD());
            FEMCommands.registrar(new FlyCMD());
            getLogger().log(Level.INFO, "Lobby: Registrado sus comandos");
        } catch (Exception ex) {
            getLogger().log(Level.INFO, "Lobby: No se han podido cargar sus comandos");
        }
        
        LobbyMenu lobbyMenu = new LobbyMenu(instance);
        
        FEMServer.setEnableParkour(false); //Activar parkour siempre -> JAJA not yet
        
        //Mini task para que los usuarios no caigan al vacío
        getServer().getScheduler().runTaskTimer(instance, () -> {
            getServer().getOnlinePlayers().stream().forEach(p -> {
                if (p.getLocation().getBlockY() < 0) {
                    p.teleport(p.getWorld().getSpawnLocation());
                }
            });
        }, 20, 20);
        
        getServer().getScheduler().runTaskLater(instance, () -> {
            LobbyTeams.initTeams();
        }, 1);
        getLogger().log(Level.INFO, "Lobby: Activado correctamente");
    }

    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, "Lobby: Desativado correctamente");
    }

    @Data
    @AllArgsConstructor
    public class FEMServerInfo {
        private String name;
        private Integer players;
        private ArrayList<String> users;
    }
}
