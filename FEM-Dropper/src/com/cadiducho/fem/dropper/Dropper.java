package com.cadiducho.fem.dropper;

import com.cadiducho.fem.core.listeners.TeleportFix;
import com.cadiducho.fem.core.util.Messages;
import com.cadiducho.fem.dropper.listener.*;
import com.cadiducho.fem.dropper.manager.ArenaManager;
import lombok.Getter;
import org.bukkit.OfflinePlayer;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;

public class Dropper extends JavaPlugin {

    private static Dropper instance;

    public static ArrayList<DropPlayer> players = new ArrayList<>();

    @Getter private ArenaManager am;
    @Getter private Messages msg;

    @Override
    public void onEnable() {
        instance = this;

        File fConf = new File(getDataFolder(), "config.yml");
        if (!fConf.exists()) {
            try {
                getConfig().options().copyDefaults(true);
                saveConfig();
            } catch (Exception e) {
            }
        }

        new WorldCreator("espera").createWorld();
        am = new ArenaManager(instance);
        msg = new Messages(instance, "&1Dropper");

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerListener(instance), instance);
        pm.registerEvents(new WorldListener(instance), instance);
        pm.registerEvents(new TeleportFix(instance), instance);

        getLogger().log(Level.INFO, "Dropper: Activado correctamente");
    }

    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, "Dropper: Desativado correctamente");
    }

    public static Dropper getInstance() {
        return instance;
    }

    public static DropPlayer getPlayer(OfflinePlayer p) {
        for (DropPlayer pl : players) {
            if (pl.getUuid() == null) {
                continue;
            }
            if (pl.getUuid().equals(p.getUniqueId())) {
                return pl;
            }
        }
        DropPlayer us = new DropPlayer(p.getUniqueId());
        if (us.isOnline()) {
            players.add(us);
        }
        return us;
    }

}
