package com.cadiducho.fem.gem;

import com.cadiducho.fem.core.listeners.TeleportFix;
import com.cadiducho.fem.core.util.ScoreboardUtil;
import com.cadiducho.fem.gem.listener.PlayerListener;
import com.cadiducho.fem.gem.listener.WorldListener;
import com.cadiducho.fem.gem.manager.ArenaManager;
import com.cadiducho.fem.gem.manager.GameManager;
import com.cadiducho.fem.gem.manager.GameState;
import com.cadiducho.fem.gem.manager.TeamManager;
import com.cadiducho.fem.core.util.Messages;
import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import lombok.Getter;
import org.bukkit.OfflinePlayer;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class GemHunters extends JavaPlugin {

    @Getter private static GemHunters instance;
    
    public static ArrayList<GemPlayer> players = new ArrayList<>();

    @Getter private TeamManager tm;
    @Getter private ArenaManager am;
    @Getter private GameManager gm;
    @Getter private Messages msg;

    @Getter private ScoreboardUtil lobbyBoard;
    @Getter private ScoreboardUtil hiddingBoard;
    @Getter private ScoreboardUtil gameBoard;
    
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
        gm = new GameManager(instance);
        tm = new TeamManager(instance);
        getServer().getScheduler().runTaskLater(instance, () -> tm.initTeams(), 1);
        am = new ArenaManager(instance);
        am.prepareWorld(getServer().getWorld(getConfig().getString("GemHunters.Arena.mundo")));
        msg = new Messages(instance, "&dGem&eHunters");

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerListener(instance), instance);
        pm.registerEvents(new WorldListener(instance), instance);
        pm.registerEvents(new TeleportFix(instance), instance);
        
        lobbyBoard = new ScoreboardUtil("§d§lGem§e§lHunter", "lobby");
        hiddingBoard = new ScoreboardUtil("§d§lGem§e§lHunter", "hidding");
        gameBoard = new ScoreboardUtil("§d§lGem§e§lHunter", "game");
        
        GameState.state = GameState.LOBBY;
        getLogger().log(Level.INFO, "GH: Activado correctamente");
        
    }

    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, "GH: Desativado correctamente");
    }
    
    public static GemPlayer getPlayer(OfflinePlayer p) {
        for (GemPlayer pl : players) {
            if (pl.getUuid() == null) {
                continue;
            }
            if (pl.getUuid().equals(p.getUniqueId())) {
                return pl;
            }
        }
        GemPlayer us = new GemPlayer(p.getUniqueId());
        if (p.isOnline()) {
            players.add(us);
        }
        return us;
    }
}
