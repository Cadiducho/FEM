package com.cadiducho.fem.tnt;

import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.core.api.FEMUser;
import com.cadiducho.fem.core.listeners.TeleportFix;
import com.cadiducho.fem.tnt.listener.PlayerListener;
import com.cadiducho.fem.tnt.listener.SignListener;
import com.cadiducho.fem.tnt.listener.WorldListener;
import com.cadiducho.fem.tnt.manager.ArenaManager;
import com.cadiducho.fem.tnt.manager.GameManager;
import com.cadiducho.fem.tnt.manager.GameState;
import com.cadiducho.fem.tnt.util.ChestItems;
import com.cadiducho.fem.tnt.util.Messages;
import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import lombok.Getter;
import org.bukkit.OfflinePlayer;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class TntWars extends JavaPlugin {

    private static TntWars instance;

    public static ArrayList<TntPlayer> players = new ArrayList<>();

    @Getter private ArenaManager am;
    @Getter private GameManager gm;
    @Getter private Messages msg;
    @Getter private ChestItems chestItems;
    public int id;

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
        chestItems = new ChestItems(instance);
        am = new ArenaManager(instance);
        am.prepareWorld(getServer().getWorld(getConfig().getString("Tnt.Arena.mundo")));
        msg = new Messages(instance);
        msg.init();

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerListener(instance), instance);
        pm.registerEvents(new WorldListener(instance), instance);
        pm.registerEvents(new SignListener(instance), instance);
        pm.registerEvents(new TeleportFix(instance), instance);
        
        GameState.state = GameState.LOBBY;
        getLogger().log(Level.INFO, "Tnt: Activado correctamente");
    }

    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, "Tnt: Desativado correctamente");
    }

    public static TntWars getInstance() {
        return instance;
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