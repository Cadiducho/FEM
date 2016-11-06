package com.cadiducho.fem.royale;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import lombok.Getter;
import com.cadiducho.fem.core.listeners.TeleportFix;
import com.cadiducho.fem.core.util.ItemUtil;
import com.cadiducho.fem.royale.listeners.GameListener;
import com.cadiducho.fem.royale.listeners.PlayerListener;
import com.cadiducho.fem.royale.listeners.ServerListener;
import com.cadiducho.fem.royale.manager.ArenaManager;
import com.cadiducho.fem.royale.manager.GameManager;
import com.cadiducho.fem.royale.manager.GameState;
import com.cadiducho.fem.royale.utils.ChestItems;
import com.cadiducho.fem.royale.utils.Messages;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class BattleRoyale extends JavaPlugin {

    @Getter private static BattleRoyale instance;
    
    public static ArrayList<BattlePlayer> players = new ArrayList<>();
    
    @Getter private ArenaManager am;
    @Getter private GameManager gm;
    @Getter private Messages msg;    
    @Getter private World world;
    @Getter private final ItemStack moneda = ItemUtil.createItem(Material.DOUBLE_PLANT, "&6Moneda", "&aTe permitirá comprar otros objetos");

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
        world = getServer().getWorld(getConfig().getString("worldName"));
        ChestItems.initItems();
        am = new ArenaManager(instance);
        gm = new GameManager(instance);
        msg = new Messages(instance);
        msg.init();
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerListener(instance), instance);
        pm.registerEvents(new GameListener(instance), instance);
        pm.registerEvents(new ServerListener(instance), instance);
        pm.registerEvents(new TeleportFix(instance), instance);
        
        GameState.state = GameState.PREPARING;
        getServer().getLogger().log(Level.INFO, "BattleRoyale: Activado");
        world.setAutoSave(false);
    }

    @Override
    public void onDisable() {
        getServer().getLogger().log(Level.INFO, "BattleRoyale: Desactivado");
    }
    
    public static BattlePlayer getPlayer(OfflinePlayer p) {
        for (BattlePlayer pl : players) {
            if (pl.getUuid() == null) {
                continue;
            }
            if (pl.getUuid().equals(p.getUniqueId())) {
                return pl;
            }
        }
        BattlePlayer us = new BattlePlayer(p.getUniqueId());
        if (p.isOnline()) {
            players.add(us);
        }
        return us;
    }
}
