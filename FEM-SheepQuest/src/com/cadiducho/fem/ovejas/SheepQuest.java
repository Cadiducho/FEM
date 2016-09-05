package com.cadiducho.fem.ovejas;

import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.core.api.FEMUser;
import com.cadiducho.fem.ovejas.listener.PlayerListener;
import com.cadiducho.fem.ovejas.listener.WorldListener;
import com.cadiducho.fem.ovejas.manager.ArenaManager;
import com.cadiducho.fem.ovejas.manager.GameManager;
import com.cadiducho.fem.ovejas.manager.GameState;
import com.cadiducho.fem.ovejas.manager.TeamManager;
import com.cadiducho.fem.ovejas.manager.util.Messages;
import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import lombok.Getter;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class SheepQuest extends JavaPlugin {

    private static SheepQuest instance;
    
    public static ArrayList<SheepPlayer> players = new ArrayList<>();

    @Getter
    private TeamManager tm;
    //   @Getter private GameListener gl;
    private PlayerListener pl;
    private WorldListener wl;

    //private final WorldManager wm;
    @Getter
    private ArenaManager am;
    @Getter
    private GameManager gm;

    @Getter
    private Messages msg;

    /*private final UHCFileLoader fl;
    
    private final CommandManager cm;*/

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

        tm = new TeamManager(instance);

        am = new ArenaManager(instance);
        am.prepareWorld(getServer().getWorld("world"));
        
        gm = new GameManager(instance);

        msg = new Messages(instance);
        msg.init();

        pl = new PlayerListener(instance);
        pl.init();
        
        wl = new WorldListener(instance);
        wl.init();
        
        GameState.state = GameState.LOBBY;
        getLogger().log(Level.INFO, "Ovejas: Activado correctamente");

        new BukkitRunnable() {
            @Override
            public void run() {
                tm.initTeams();
            }
        }.runTaskLater(this, 1);
    }

    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, "Ovejas: Desativado correctamente");
    }

    public static SheepQuest getInstance() {
        return instance;
    }
    
    public static SheepPlayer getPlayer(OfflinePlayer p) {
        FEMUser u = FEMServer.getUser(p);
        for (SheepPlayer pl : players) {
            if (pl.getBase().getUuid() == null) {
                continue;
            }
            if (pl.getBase().getUuid().equals(p.getUniqueId())) {
                return pl;
            }
        }
        SheepPlayer us = new SheepPlayer(u);
        if (us.getBase().getBase().isOnline()) {
            players.add(us);
        }
        return us;
    }

}
