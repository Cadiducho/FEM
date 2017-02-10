package com.cadiducho.fem.lobby;

import com.cadiducho.fem.core.FEMCommands;
import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.core.listeners.ResourcePackManager;
import com.cadiducho.fem.lobby.cmds.*;
import com.cadiducho.fem.lobby.listeners.PlayerListener;
import com.cadiducho.fem.lobby.listeners.WorldListener;
import com.cadiducho.fem.lobby.utils.MathsUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;

public class Lobby extends JavaPlugin {

    @Getter private static Lobby instance;

    @Getter @Setter private ArrayList<FEMServerInfo> servers;

    @Getter private MathsUtils mathsUtils;

    @Override
    public void onEnable() {
        instance = this;
        servers = new ArrayList<>();

        mathsUtils = new MathsUtils();

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
        pluginManager.registerEvents(new ResourcePackManager(instance, ResourcePackManager.Games.LOBBY), instance);

        getServer().getMessenger().registerOutgoingPluginChannel(instance, "BungeeCord");
        getServer().getMessenger().registerOutgoingPluginChannel(instance, "FEM");
        getServer().getMessenger().registerOutgoingPluginChannel(instance, "FEMChat");
        getServer().getMessenger().registerIncomingPluginChannel(instance, "FEM", pl);

        try {
            //Comandos solo para el lobby
            FEMCommands.registrar(new DropPuntosCMD());
            FEMCommands.registrar(new SetBrujulaCMD());
            FEMCommands.registrar(new SetNvidiaCMD());
            FEMCommands.registrar(new SpawnCMD());
            FEMCommands.registrar(new FlyCMD());
            FEMCommands.registrar(new PruebasCMD());
            getLogger().log(Level.INFO, "Lobby: Registrado sus comandos");
        } catch (Exception ex) {
            getLogger().log(Level.INFO, "Lobby: No se han podido cargar sus comandos");
        }

        FEMServer.setEnableParkour(false); //Activar parkour siempre -> JAJA not yet

        //Mini task para que los usuarios no caigan al vacío
        getServer().getScheduler().runTaskTimer(instance, () -> {
            getServer().getOnlinePlayers().forEach(p -> {
                if (p.getFireTicks() != 0) p.setFireTicks(0);
                if (p.getLocation().getBlockY() < 0) {
                    p.teleport(p.getWorld().getSpawnLocation());
                }
            });
        }, 20, 20);
        
        /*//Solicitar a Bungee la lista de servidores actualizada // Desactivado, solo un lobby
        if (getConfig().getBoolean("threadLobby")) {
            getServer().getScheduler().runTaskTimer(instance, () -> {
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("askServerList");

                Collection<? extends Player> c = Bukkit.getOnlinePlayers(); //Usar jugador falso
                Player p = (Player) c.toArray()[0];
                p.sendPluginMessage(plugin, "FEM", out.toByteArray());
            }, 20, 100);
        }*/

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
