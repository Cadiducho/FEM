package com.cadiducho.fem.teamtnt;

import com.cadiducho.fem.core.listeners.TeleportFix;
import com.cadiducho.fem.core.util.Messages;
import com.cadiducho.fem.core.util.ScoreboardUtil;
import com.cadiducho.fem.teamtnt.listener.PlayerListener;
import com.cadiducho.fem.teamtnt.listener.SignListener;
import com.cadiducho.fem.teamtnt.listener.WorldListener;
import com.cadiducho.fem.teamtnt.manager.ArenaManager;
import com.cadiducho.fem.teamtnt.manager.GameManager;
import com.cadiducho.fem.teamtnt.manager.GameState;
import com.cadiducho.fem.teamtnt.manager.TeamManager;
import com.cadiducho.fem.teamtnt.util.ChestItems;
import lombok.Getter;
import org.bukkit.OfflinePlayer;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import org.bukkit.scheduler.BukkitRunnable;

public class TeamTntWars extends JavaPlugin {

    @Getter private static TeamTntWars instance;

    public static ArrayList<TntPlayer> players = new ArrayList<>();

    @Getter private ArenaManager am;
    @Getter private GameManager gm;
    @Getter private TeamManager tm;
    @Getter private Messages msg;
    @Getter private ChestItems chestItems;

    @Getter private static String prefix = "§c§lTeamTnT§r§lWars";

    @Getter private ScoreboardUtil lobbyBoard;
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
        chestItems = new ChestItems(instance);

        am = new ArenaManager(instance);
        am.prepareWorld(getServer().getWorld(getConfig().getString("TeamTntWars.Arena.mundo")));

        msg = new Messages(instance, getPrefix());

        lobbyBoard = new ScoreboardUtil(prefix, "lobby");
        gameBoard = new ScoreboardUtil(prefix, "game");

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerListener(instance), instance);
        pm.registerEvents(new WorldListener(instance), instance);
        pm.registerEvents(new SignListener(instance), instance);
        pm.registerEvents(new TeleportFix(instance), instance);
        
        GameState.state = GameState.LOBBY;
        getLogger().log(Level.INFO, "TeamTntWars: Activado correctamente");    
    }

    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, "TeamTntWars: Desativado correctamente");
    }

    public static TntPlayer getPlayer(OfflinePlayer p) {
        for (TntPlayer pl : players) {
            if (pl.getUuid() == null) {
                continue;
            }
            if (pl.getUuid().equals(p.getUniqueId())) {
                return pl;
            }
        }
        TntPlayer us = new TntPlayer(p.getUniqueId());
        if (us.isOnline()) {
            players.add(us);
        }
        return us;
    }
}