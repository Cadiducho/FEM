package com.cadiducho.fem.lobby;

import com.cadiducho.fem.core.FEMCommands;
import com.cadiducho.fem.lobby.cmds.DropPuntosCMD;
import com.cadiducho.fem.lobby.cmds.SetBrujulaCMD;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;

public class Lobby extends JavaPlugin {

    public ConfigurationManager cm;
    public EventManager em;
    public Menu m;
    public PlayerManager pm;

    @Getter
    @Setter
    private ArrayList<FEMServerInfo> servers;

    @Override
    public void onEnable() {
        try {
            cm.load();
            FEMCommands.registrar(new DropPuntosCMD());
            FEMCommands.registrar(new SetBrujulaCMD());
            getLogger().log(Level.INFO, "Lobby: Registrado sus comandos");
        } catch (IOException | InvalidConfigurationException ex) {
            Logger.getLogger(Lobby.class.getName()).log(Level.SEVERE, null, ex);
        }
        servers = new ArrayList<>();
        em.init();
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getMessenger().registerOutgoingPluginChannel(this, "FEM");
        getServer().getMessenger().registerIncomingPluginChannel(this, "FEM", em);
        getLogger().log(Level.INFO, "Lobby: Activado correctamente");
    }

    @Override
    public void onDisable() {
        em.removeEvents();
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
