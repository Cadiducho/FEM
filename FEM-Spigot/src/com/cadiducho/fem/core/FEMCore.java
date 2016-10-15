package com.cadiducho.fem.core;

import com.cadiducho.fem.core.listeners.PlayerListener;
import com.cadiducho.fem.core.listeners.BungeeListener;
import com.cadiducho.fem.core.listeners.InventoryListener;
import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.core.util.FEMFileLoader;
import com.cadiducho.fem.core.util.Metodos;
import com.cadiducho.fem.core.util.MySQL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.logging.Level;
import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class FEMCore extends JavaPlugin {

    @Getter  private final Metodos metodos = new Metodos(this);
    private static FEMCore instance;

    @Getter private MySQL mysql = null;
    private Connection connection = null;

    public static FEMServer server;

    @Getter private final String tag = Metodos.colorizar("&7[&6Under&eGames&7]&r");

    @Override
    public void onEnable() {
        instance = this;
        try {
            /*
             * Generar y cargar Config.yml
             */
            debugLog("Modo Debug activado");
            debugLog("Cargando modulo de config");

            FEMFileLoader.load();

            /*
             * Abrir conexión Mysql
             */
            try {
                debugLog("Cargando modulo de MySQL");
                mysql = new MySQL(getConfig().getString("mysql.host"), getConfig().getString("mysql.port"),
                        getConfig().getString("mysql.database"), getConfig().getString("mysql.username"),
                        getConfig().getString("mysql.password"));
                connection = mysql.openConnection();
            } catch (SQLException | ClassNotFoundException exc) {
                getLogger().severe("Error al abrir la conexion MySQL!");
                debugLog("Causa: " + exc.toString());
                getLogger().severe("FEMCore desactivado por imposibilidad de conexiones");
                getServer().getPluginManager().disablePlugin(this); //Desactivar si no hay MySQL (Solo dará errores si esta activo)
            }

            /*
             * Registrar eventos, listeners y channels
             */
            debugLog("Cargando modóulo de eventos, listeners y channels...");
            PluginManager pluginManager = getServer().getPluginManager();

            pluginManager.registerEvents(new PlayerListener(instance), instance);
            pluginManager.registerEvents(new InventoryListener(instance), instance);

            //Bungee
            getServer().getMessenger().registerOutgoingPluginChannel(instance, "BungeeCord");
            getServer().getMessenger().registerOutgoingPluginChannel(instance, "FEM");
            getServer().getMessenger().registerIncomingPluginChannel(instance, "FEM", new BungeeListener(instance));

            /*
             * Registrar comandos
             */
            debugLog("Cargando módulo de comandos...");
            FEMCommands.load();
            
            log("FEMCore v" + getDescription().getVersion() + " ha sido cargado completamente!");
        } catch (Throwable t) {
            log("No se ha podido cargar FEMCore v" + getDescription().getVersion());
            debugLog("Causa: " + t.toString());
            getPluginLoader().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        if (connection != null) { //Evitar NullPointerExceptions 
            try {
                connection.close();
            } catch (SQLException ex) {
            } //Ignora
        }
        log("FEMCore desactivado");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        try {
            FEMCommands.onCmd(sender, cmd, label, args);
        } catch (Exception ex) {
            log(Level.SEVERE, "Error al ejecutar el comando '" + label + Arrays.toString(args) + "'");
            ex.printStackTrace();
        }
        return true;
    }

    /*
     * Variables declaradas
     */
    public static FEMCore getInstance() {
        return instance;
    }
    
    public boolean isDebug() {
        return getConfig().getBoolean("debug");
    }

    public void debugLog(String s) {
        if (isDebug()) {
            log("[Debug] " + s);
        }
    }

    public void log(String s) {
        getLogger().log(Level.INFO, s);
    }

    public void log(Level l, String s) {
        getLogger().log(l, s);
    }
}
