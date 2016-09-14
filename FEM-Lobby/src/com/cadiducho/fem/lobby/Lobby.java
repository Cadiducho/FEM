package com.cadiducho.fem.lobby;

import com.cadiducho.fem.core.FEMCommands;
import com.cadiducho.fem.core.util.Metodos;
import com.cadiducho.fem.lobby.cmds.DropPuntosCMD;
import com.cadiducho.fem.lobby.listeners.PlayerListener;
import com.cadiducho.fem.lobby.listeners.WorldListener;
import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Lobby extends JavaPlugin {

    @Getter private static Lobby instance;

    @Getter @Setter private ArrayList<FEMServerInfo> servers;
    @Getter @Setter private ArrayList<ServerSign> signs;
    
    @Override
    public void onEnable() {
        instance = this;
        
        System.setProperty("java.net.preferIPv4Stack", "true");
        File fConf = new File(getDataFolder(), "config.yml");
        if (!fConf.exists()) {
            try {
                getConfig().options().copyDefaults(true);
                saveConfig();
            } catch (Exception e) { }
        }

        ServerSign.plugin = instance;
        SignManager.plugin = instance; 
        
        servers = new ArrayList<>();
        signs = new ArrayList<>();
        
        ServerSign.loadSignLines();
        loadServerSigns();
        
        start();
        loadSigns();
        ServerSign.loadSignLines();
        SignManager.getSignManager().start();
        
        PluginManager pluginManager = getServer().getPluginManager();
        PlayerListener pl = new PlayerListener(instance);
        pluginManager.registerEvents(pl, instance);
        pluginManager.registerEvents(new WorldListener(instance), instance);
        pluginManager.registerEvents(SignManager.getSignManager(), instance);
        
        getServer().getMessenger().registerOutgoingPluginChannel(instance, "BungeeCord");
        getServer().getMessenger().registerOutgoingPluginChannel(instance, "FEM");
        getServer().getMessenger().registerIncomingPluginChannel(instance, "FEM", pl);
        
        try {
            //Comandos solo para el lobby
            FEMCommands.registrar(new DropPuntosCMD());
            getLogger().log(Level.INFO, "Lobby: Registrado sus comandos");
        } catch (Exception ex) {
            getLogger().log(Level.INFO, "Lobby: No se han podido cargar sus comandos");
        }
        
        getLogger().log(Level.INFO, "Lobby: Activado correctamente");
    }

    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, "Lobby: Desativado correctamente");
    }

    public void loadServerSigns() {
        if (!getConfig().contains("default")) {
            getConfig().set("servers.default.hostname", "localhost");
            getConfig().set("servers.default.port", 25565);
            getConfig().set("servers.default.mapname", "Testmap");
            saveConfig();
        }
        for (String s : getConfig().getConfigurationSection("servers").getKeys(false)) {
            if (!s.contains("default")) {
                ServerSign serverSign = new ServerSign(s);
                signs.add(serverSign);
                if (serverSign.isOnline()) {
                    SignManager.getSignManager().addToQueue(serverSign);
                }
            }
        }
    }

    private void loadSigns() {
        for (String path : getConfig().getConfigurationSection("signs.signlocations").getKeys(false)) {
            path = "signs.signlocations." + path;

            Location loc = Metodos.stringToLocation(getConfig().getString(path));
            if ((loc.getBlock().getState() instanceof Sign)) {
                SignManager.getSignManager().registerSign((Sign) loc.getBlock().getState());
            } else {
                System.out.println("Block at given location " + path + " isn't a sign!");
            }
        }
    }

    public void start() {
        getServer().getScheduler().runTaskTimerAsynchronously(this, new Runnable() {
            int offlinecounter = 0;
            int onlinecounter = 0;

            @Override
            public void run() {
                if (offlinecounter == 10) {
                    for (ServerSign serverSign : signs) {
                        if (!serverSign.isOnline()) {
                            serverSign.updateServer();
                            if (serverSign.isOnline()) {
                                SignManager.getSignManager().addToQueue(serverSign);
                            }
                        }
                    }
                    offlinecounter = 0;
                }
                offlinecounter += 1;
                if (onlinecounter == 1) {
                    for (ServerSign serverSign : signs) {
                        if (serverSign.isOnline()) {
                            serverSign.updateServer();
                        }
                    }
                    onlinecounter = 0;
                }
                onlinecounter += 1;
            }
        }, 20L, 20L);
    }

    @Data
    @AllArgsConstructor
    public class FEMServerInfo {
        private String name;
        private Integer players;
        private ArrayList<String> users;
    }
}
