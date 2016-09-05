package com.cadiducho.fem.lobby;

import com.cadiducho.fem.core.FEMCommands;
import com.cadiducho.fem.core.util.Metodos;
import com.cadiducho.fem.lobby.cmds.DropPuntosCMD;
import com.cadiducho.fem.lobby.listeners.PlayerListener;
import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Lobby extends JavaPlugin {

    private static Lobby instance;

    @Getter @Setter private ArrayList<FEMServerInfo> servers;
    @Getter @Setter private ArrayList<LobbySign> signs;
    
    @Override
    public void onEnable() {
        instance = this;
        
        File fConf = new File(getDataFolder(), "config.yml");
        if (!fConf.exists()) {
            try {
                getConfig().options().copyDefaults(true);
                saveConfig();
                //log("Generando archivo config.yml correctamente");
            } catch (Exception e) {
                /*log("Fallo al generar el config.yml!");
                debugLog("Causa: " + e.toString());*/
            }
        }
        
        servers = new ArrayList<>();
        signs = new ArrayList<>();
        
        PluginManager pluginManager = getServer().getPluginManager();
        PlayerListener pl = new PlayerListener(instance);
        pluginManager.registerEvents(pl, instance);
        
        getServer().getMessenger().registerOutgoingPluginChannel(instance, "BungeeCord");
        getServer().getMessenger().registerOutgoingPluginChannel(instance, "FEM");
        getServer().getMessenger().registerIncomingPluginChannel(instance, "FEM", pl);
        
        for (String str : getConfig().getConfigurationSection("lobby.sign").getKeys(false)) {
            ConfigurationSection cfg = getConfig().getConfigurationSection("lobby.sign."+str);
            Location pos = Metodos.stringToLocation(cfg.getString("loc"));
            LobbySign lb = new LobbySign();
            lb.setRawname(str);
            
            Sign s = (Sign) pos.getBlock().getState();
            s.setLine(0, Metodos.colorizar(lb.getName()));
            s.setLine(1, Metodos.colorizar("&aEsperando"));
            s.setLine(2, "");
            s.setLine(3, Metodos.colorizar(lb.getPlayers()));
            s.update();
            
            getSigns().remove(lb);
            lb.setLoc(pos);
            getSigns().add(lb);
        }
        
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

    public static Lobby getInstance() {
        return instance;
    }
    
    public void updateSigns() {
        for (String str : getConfig().getConfigurationSection("lobby.sign").getKeys(false)) {
            getLogger().log(Level.INFO, "Actualizando sign de {0}", str);
            ConfigurationSection cfg = getConfig().getConfigurationSection("lobby.sign."+str);
            Location pos = Metodos.stringToLocation(cfg.getString("loc"));
            
            LobbySign lb = LobbySign.getLobbySignByName(str);
            if (pos.getBlock().getType() != Material.WALL_SIGN) {
                getLogger().log(Level.INFO, "No es un letrero, es " + pos.getBlock().getType().name() + " en " +pos.getBlockX()+"/"+pos.getBlockY()+"/"+pos.getBlockZ()+"");
                return;
            }
            if (lb == null) {
                getLogger().log(Level.INFO, "No es un letrero (null)");
                return;
            }
            
            Sign s = (Sign) pos.getBlock().getState();
            s.setLine(0, Metodos.colorizar(lb.getName()));
            s.setLine(1, Metodos.colorizar(lb.getStatus()));
            s.setLine(2, "");
            s.setLine(3, Metodos.colorizar(lb.getPlayers()));
            s.update();
            
            getSigns().remove(lb);
            lb.setLoc(pos);
            getSigns().add(lb);
        }
    }
    
    @Data
    @AllArgsConstructor
    public class FEMServerInfo {
        private String name;
        private Integer players;
        private ArrayList<String> users;
    }
}
