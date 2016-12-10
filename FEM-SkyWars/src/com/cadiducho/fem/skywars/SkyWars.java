package com.cadiducho.fem.skywars;

import com.cadiducho.fem.core.listeners.TeleportFix;
import com.cadiducho.fem.skywars.listener.PlayerListener;
import com.cadiducho.fem.skywars.listener.WorldListener;
import com.cadiducho.fem.skywars.manager.ArenaManager;
import com.cadiducho.fem.skywars.manager.GameManager;
import com.cadiducho.fem.skywars.manager.GameState;
import com.cadiducho.fem.skywars.util.ChestItems;
import com.cadiducho.fem.core.util.Messages;
import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import lombok.Getter;
import org.bukkit.OfflinePlayer;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class SkyWars extends JavaPlugin {

    @Getter private static SkyWars instance;

    public static ArrayList<SkyPlayer> players = new ArrayList<>();

    @Getter private ArenaManager am;
    @Getter private GameManager gm;
    @Getter private Messages msg;
    @Getter private ChestItems chestItems;

    @Override
    public void onEnable() {
        instance = this;

        File fConf = new File(getDataFolder(), "config.yml");
        if (!fConf.exists()) {
            try {
                getConfig().options().copyDefaults(true);
                saveConfig();
            } catch (Exception e) {}
        }

        gm = new GameManager(instance);
        chestItems = new ChestItems(instance);
        msg = new Messages(instance, "&dSky&eWars");
        
        //ToDo: Implementar nuevo sistema de rotación de mapas
        new WorldCreator("espera").createWorld();
        am = new ArenaManager(instance);
        am.prepareWorld(getServer().getWorld(getConfig().getString("Sky.Arena.mundo")));
        
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerListener(instance), instance);
        pm.registerEvents(new WorldListener(instance), instance);
        pm.registerEvents(new TeleportFix(instance), instance);
        
        GameState.state = GameState.LOBBY;
        getLogger().log(Level.INFO, "Sky: Activado correctamente");
    }

    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, "Sky: Desativado correctamente");
    }

    public static SkyPlayer getPlayer(OfflinePlayer p) {
        for (SkyPlayer pl : players) {
            if (pl.getUuid() == null) {
                continue;
            }
            if (pl.getUuid().equals(p.getUniqueId())) {
                return pl;
            }
        }
        SkyPlayer us = new SkyPlayer(p.getUniqueId());
        if (us.isOnline()) {
            players.add(us);
        }
        return us;
    }
}