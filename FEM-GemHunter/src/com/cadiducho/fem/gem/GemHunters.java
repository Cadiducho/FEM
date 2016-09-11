package com.cadiducho.fem.gem;

import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.core.api.FEMUser;
import com.cadiducho.fem.gem.listener.PlayerListener;
import com.cadiducho.fem.gem.listener.WorldListener;
import com.cadiducho.fem.gem.manager.ArenaManager;
import com.cadiducho.fem.gem.manager.GameManager;
import com.cadiducho.fem.gem.manager.GameState;
import com.cadiducho.fem.gem.manager.TeamManager;
import com.cadiducho.fem.gem.util.Messages;
import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import lombok.Getter;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class GemHunters extends JavaPlugin {

    private static GemHunters instance;
    
    public static ArrayList<GemPlayer> players = new ArrayList<>();

    @Getter private TeamManager tm;
    @Getter private ArenaManager am;
    @Getter private GameManager gm;
    @Getter private Messages msg;

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
        tm = new TeamManager(instance);
        am = new ArenaManager(instance);
        am.prepareWorld(getServer().getWorld(getConfig().getString("GemHunters.Arena.mundo")));
        msg = new Messages(instance);
        msg.init();

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerListener(instance), instance);
        pm.registerEvents(new WorldListener(instance), instance);
        
        GameState.state = GameState.LOBBY;
        getLogger().log(Level.INFO, "ByD: Activado correctamente");

        new BukkitRunnable() {
            @Override
            public void run() {
                tm.initTeams();
            }
        }.runTaskLater(this, 1);
    }

    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, "ByD: Desativado correctamente");
    }

    public static GemHunters getInstance() {
        return instance;
    }
    
    public static GemPlayer getPlayer(OfflinePlayer p) {
        FEMUser u = FEMServer.getUser(p);
        for (GemPlayer pl : players) {
            if (pl.getBase().getUuid() == null) {
                continue;
            }
            if (pl.getBase().getUuid().equals(p.getUniqueId())) {
                return pl;
            }
        }
        GemPlayer us = new GemPlayer(u);
        if (us.getBase().getBase().isOnline()) {
            players.add(us);
        }
        return us;
    }
}
