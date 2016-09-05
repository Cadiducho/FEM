package com.cadiducho.fem.chat;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

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
    public static final String SINTAXIS = "%prefix% %displayName%%suffix%: ";
    public static final String CHAR = "\u00A8";
    
    public final HashMap<UUID, UUID> chatsActivados = new HashMap<>();
    public final HashMap<UUID, UUID> replyTarget = new HashMap<>();
    
    private static FEMChat instance;
    private BungeeListener bungeeListener;
    
    private Configuration ignoredConf;
    private File ignoredFile;
    public final HashMap<UUID, ArrayList<UUID>> ignoredPlayers = new HashMap<>();
    
    @Override
    public void onEnable() {
        instance = this;        
        bungeeListener = new BungeeListener(instance);
        getProxy().registerChannel(MAIN_CHANNEL);
        getProxy().getPluginManager().registerListener(this, bungeeListener);
        
        try {
            ignoredFile = new File(getDataFolder(), "ignored.yml");
            
            if (!getDataFolder().exists()) getDataFolder().mkdir();
            if (!ignoredFile.exists()) ignoredFile.createNewFile();

            ignoredConf = ConfigurationProvider.getProvider(YamlConfiguration.class).load(ignoredFile);
            ignoredConf.getKeys().forEach(key -> {
                UUID ignorer = UUID.fromString(key);
                ArrayList<UUID> ignorados = new ArrayList<>();

                ignoredConf.getStringList(key).forEach(i -> ignorados.add(UUID.fromString(i)));
                ignoredPlayers.put(ignorer, ignorados);
            });
        } catch (IOException ex) {
            getProxy().getLogger().severe("No se ha podido cargar la lista de ignorados");
        }
    }
    
    public void setReply(UUID user, UUID replyTo) {
        if (replyTarget.containsKey(user)) replyTarget.remove(user);
        
        replyTarget.put(user, replyTo);
    }
    
    public void saveIgnoredConf() throws IOException {
        ignoredPlayers.keySet().forEach(id -> {
            ArrayList<String> ignorados = new ArrayList<>();
            ignoredPlayers.get(id).forEach(i -> ignorados.add(i.toString()));
            ignoredConf.set(id.toString(), ignorados);
        });
        ConfigurationProvider.getProvider(YamlConfiguration.class).save(ignoredConf, ignoredFile);
    }
}
