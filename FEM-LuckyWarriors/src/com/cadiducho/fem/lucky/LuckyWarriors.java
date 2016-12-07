package com.cadiducho.fem.lucky;

import com.cadiducho.fem.core.listeners.TeleportFix;
import com.cadiducho.fem.core.util.Messages;
import java.util.logging.Level;
import com.cadiducho.fem.lucky.listeners.GameListener;
import com.cadiducho.fem.lucky.listeners.PlayerListener;
import com.cadiducho.fem.lucky.listeners.ServerListener;
import com.cadiducho.fem.lucky.manager.ArenaManager;
import com.cadiducho.fem.lucky.manager.GameManager;
import com.cadiducho.fem.lucky.manager.GameState;
import com.cadiducho.fem.lucky.utils.LuckyPacks;
import java.io.File;
import java.util.ArrayList;
import lombok.Getter;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class LuckyWarriors extends JavaPlugin {

    @Getter private static LuckyWarriors instance;
    
    public static ArrayList<LuckyPlayer> players = new ArrayList<>();
    
    @Getter private ArenaManager am;
    @Getter private GameManager gm;
    @Getter private Messages msg;    
    @Getter private World world;

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
        
        new WorldCreator("espera").createWorld();
        am = new ArenaManager(instance);
        gm = new GameManager(instance);
        msg = new Messages(instance, "&eLucky&aWarriors");
        world = getServer().getWorld(getConfig().getString("worldName"));
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        LuckyPacks.initItems();
        
        PluginManager plugm = getServer().getPluginManager();
        plugm.registerEvents(new PlayerListener(instance), instance);
        plugm.registerEvents(new GameListener(instance), instance);
        plugm.registerEvents(new ServerListener(instance), instance);
        plugm.registerEvents(new TeleportFix(instance), instance);
        
        GameState.state = GameState.PREPARING;
        getServer().getLogger().log(Level.INFO, "LuckyWarriors: Juego activado");
        world.setAutoSave(false);
    }

    @Override
    public void onDisable() {
        getServer().getLogger().log(Level.INFO, "LuckyWarriors: Juego desactivado");
    }
    
    public static LuckyPlayer getPlayer(OfflinePlayer p) {
        for (LuckyPlayer pl : players) {
            if (pl.getUuid() == null) {
                continue;
            }
            if (pl.getUuid().equals(p.getUniqueId())) {
                return pl;
            }
        }
        LuckyPlayer us = new LuckyPlayer(p.getUniqueId());
        if (p.isOnline()) {
            players.add(us);
        }
        return us;
    }
}
