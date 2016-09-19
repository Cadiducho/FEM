package com.cadiducho.fem.royale;

import com.cadiducho.fem.core.util.ItemUtil;
import java.util.logging.Level;
import com.cadiducho.fem.royale.listeners.GameListener;
import com.cadiducho.fem.royale.listeners.PlayerListener;
import com.cadiducho.fem.royale.listeners.ServerListener;
import com.cadiducho.fem.royale.manager.ArenaManager;
import com.cadiducho.fem.royale.manager.GameManager;
import com.cadiducho.fem.royale.manager.GameState;
import com.cadiducho.fem.royale.manager.PlayerManager;
import com.cadiducho.fem.royale.utils.ChestItems;
import com.cadiducho.fem.royale.utils.Messages;
import java.io.File;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class BattleRoyale extends JavaPlugin {

    @Getter private static BattleRoyale instance;
    
    @Getter private ArenaManager am;
    @Getter private GameManager gm;
    @Getter private PlayerManager pm;
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
        am = new ArenaManager(instance);
        gm = new GameManager(instance);
        pm = new PlayerManager(instance);
        msg = new Messages(instance);
        world = getServer().getWorld(getConfig().getString("worldName"));
        msg.init();
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        ChestItems.initItems();
        am.init();
        gm.init();
        
        PluginManager plugm = getServer().getPluginManager();
        plugm.registerEvents(new PlayerListener(instance), instance);
        plugm.registerEvents(new GameListener(instance), instance);
        plugm.registerEvents(new ServerListener(instance), instance);
        
        GameState.state = GameState.PREPARING;
        getServer().getLogger().log(Level.INFO, "BattleRoyale: Arena mode Enabled");
        world.setAutoSave(false);
    }

    @Override
    public void onDisable() {
        getServer().getLogger().log(Level.INFO, "BattleRoyale: Disabled");
    }    
}
