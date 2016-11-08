package com.cadiducho.fem.chat;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import lombok.Setter;

import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class FEMChat extends Plugin implements Listener {

    public static final String MAIN_CHANNEL = "FEMChat";
    public static final String MAIN_SUBCHANNEL = "chat";
    public static final String PRIVATE_SUBCHANNEL = "priv";
    public static final String IGNORE_SUBCHANNEL = "ign";
    public static final String IGNLIST_SUBCHANNEL = "ignlist";
    public static final String SINTAXIS = "%prefix%%displayName%%suffix%: ";
    public static final String CHAR = "\u00A8";
    
    public final HashMap<UUID, UUID> chatsActivados = new HashMap<>();
    public final HashMap<UUID, UUID> replyTarget = new HashMap<>();
    @Getter @Setter private ArrayList<UUID> disableTell = new ArrayList<>();
    
    @Getter private static FEMChat instance;
    @Getter private final String tag = "&7[&6Under&eGames&7]&r ";
    
    private BungeeListener bungeeListener; 
    @Getter private MySQL mysql = null;
    
    private Configuration config;
    private File configFile;
    
    public HashMap<UUID, ArrayList<UUID>> ignoredPlayers = new HashMap<>();
    public final HashMap<UUID, AntiSpamData> spamDataMap = new HashMap<>();
    
    @Override
    public void onEnable() {
        instance = this;        
        bungeeListener = new BungeeListener(instance);
        getProxy().registerChannel(MAIN_CHANNEL);
        getProxy().getPluginManager().registerListener(this, bungeeListener);
         
        try {
            configFile = new File(getDataFolder(), "config.yml");
            
            if (!getDataFolder().exists()) getDataFolder().mkdir();
            if (!configFile.exists()) configFile.createNewFile();

            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
        } catch (IOException ex) {
            getProxy().getLogger().severe("No se ha podido cargar la config");
        }
        
        try {
            mysql = new MySQL(config.getString("mysql.host"), config.getString("mysql.port"),
                        config.getString("mysql.database"), config.getString("mysql.username"),
                        config.getString("mysql.password"));
            mysql.openConnection();
            ignoredPlayers = mysql.loadIgnoredList();
        } catch (SQLException | ClassNotFoundException ex) {
            getProxy().getLogger().severe("No se ha podido abrir conexión mysql");
            ex.printStackTrace();
        }
        
        //Mantener actualizada la lista de gente que tiene desactivada los tell
        getProxy().getScheduler().schedule(this, () -> {
            mysql.updateDisableTellList();
        }, 5, 5, TimeUnit.SECONDS);
    }
    
    @Override
    public void onDisable() {
        try {
            mysql.closeConnection();
        } catch (SQLException ex) {} //Ignorar
    }
    
    public void setReply(UUID user, UUID replyTo) {
        if (replyTarget.containsKey(user)) replyTarget.remove(user);
        
        replyTarget.put(user, replyTo);
    }
}
