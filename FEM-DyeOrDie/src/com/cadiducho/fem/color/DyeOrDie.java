package com.cadiducho.fem.color;

import com.cadiducho.fem.color.listener.PlayerListener;
import com.cadiducho.fem.color.listener.WorldListener;
import com.cadiducho.fem.color.manager.ArenaManager;
import com.cadiducho.fem.color.manager.GameManager;
import com.cadiducho.fem.color.manager.GameState;
import com.cadiducho.fem.color.util.ScoreboardUtil;
import com.cadiducho.fem.core.listeners.TeleportFix;
import com.cadiducho.fem.core.util.Messages;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;

public class DyeOrDie extends JavaPlugin {

    private static DyeOrDie instance;
    
    public static ArrayList<DyePlayer> players = new ArrayList<>();
    private final static ChatColor[] colors = { ChatColor.AQUA, ChatColor.BLUE, ChatColor.DARK_AQUA, ChatColor.DARK_BLUE, ChatColor.DARK_GRAY, 
            ChatColor.DARK_GREEN, ChatColor.DARK_PURPLE, ChatColor.DARK_RED, ChatColor.GOLD, ChatColor.GRAY, ChatColor.GREEN };

    @Getter private ArenaManager am;
    @Getter private GameManager gm;
    @Getter private Messages msg;

    @Getter private ScoreboardUtil gameBoard;
    @Getter private ScoreboardUtil lobbyBoard;
    
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

        new WorldCreator("espera").createWorld();
        gm = new GameManager(instance);
        am = new ArenaManager(instance);
        am.prepareWorld(getServer().getWorld(getConfig().getString("Color.Arena.mundo")));
        msg = new Messages(instance, colorize("Dye or Die"));

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerListener(instance), instance);
        pm.registerEvents(new WorldListener(instance), instance);
        pm.registerEvents(new TeleportFix(instance), instance);
        
        gameBoard = new ScoreboardUtil("Dye or Die", "game");
        lobbyBoard = new ScoreboardUtil("Dye or Die", "lobby");
        
        GameState.state = GameState.LOBBY;
        getLogger().log(Level.INFO, "Color: Activado correctamente");
        //Evento para la caida al vacio
        getServer().getScheduler().runTaskTimer(instance, () -> {
            if (getGm().isInGame()) return;
                getGm().getPlayersInGame().stream().forEach(p -> {
                    if (p.getLocation().getBlockY() < 0) {
                        getPlayer(p).endGame();
                    }
                });
        }, 20, 20);
    }

    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, "Color: Desativado correctamente");
    }

    public static DyeOrDie getInstance() {
        return instance;
    }
    
    public static DyePlayer getPlayer(OfflinePlayer p) {
        for (DyePlayer pl : players) {
            if (pl.getUuid() == null) {
                continue;
            }
            if (pl.getUuid().equals(p.getUniqueId())) {
                return pl;
            }
        }
        DyePlayer us = new DyePlayer(p.getUniqueId());
        if (us.isOnline()) {
            players.add(us);
        }
        return us;
    }
    
    public static String colorize(String message) {
        char[] csarray = message.toCharArray();
        StringBuilder colorized = new StringBuilder();
        char[] arrayOfChar1;
        int j = (arrayOfChar1 = csarray).length;
        for (int i = 0; i < j; i++) {
            char c = arrayOfChar1[i];
            colorized.append(colors[new Random().nextInt(colors.length)]);
            colorized.append(c);
        }
        return colorized.toString();
    }

}
