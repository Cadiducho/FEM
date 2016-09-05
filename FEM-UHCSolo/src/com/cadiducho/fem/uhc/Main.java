package com.cadiducho.fem.uhc;

import java.util.logging.Level;

import com.cadiducho.fem.uhc.cmds.SetupCommand;
import com.cadiducho.fem.uhc.cmds.StartCommand;
import com.cadiducho.fem.uhc.countdown.PVECountdown;
import com.cadiducho.fem.uhc.countdown.UHCGameCountdown;
import com.cadiducho.fem.uhc.countdown.WinnerCountdown;
import com.cadiducho.fem.uhc.listeners.BlockListener;
import com.cadiducho.fem.uhc.listeners.ChunkListener;
import com.cadiducho.fem.uhc.listeners.EntityDamage;
import com.cadiducho.fem.uhc.listeners.InventoryClick;
import com.cadiducho.fem.uhc.listeners.ItemCraft;
import com.cadiducho.fem.uhc.listeners.Motd;
import com.cadiducho.fem.uhc.listeners.PlayerChat;
import com.cadiducho.fem.uhc.listeners.PlayerDeath;
import com.cadiducho.fem.uhc.listeners.PlayerFood;
import com.cadiducho.fem.uhc.listeners.PlayerInteract;
import com.cadiducho.fem.uhc.listeners.PlayerItem;
import com.cadiducho.fem.uhc.listeners.PlayerJoin;
import com.cadiducho.fem.uhc.listeners.PlayerLogin;
import com.cadiducho.fem.uhc.listeners.PlayerMove;
import com.cadiducho.fem.uhc.listeners.PlayerPortal;
import com.cadiducho.fem.uhc.listeners.PlayerQuit;
import com.cadiducho.fem.uhc.listeners.onPotionII;
import com.cadiducho.fem.uhc.manager.GameManager;
import com.cadiducho.fem.uhc.manager.GameState;
import com.cadiducho.fem.uhc.manager.LobbyManager;
import com.cadiducho.fem.uhc.manager.WorldManager;
import com.cadiducho.fem.uhc.player.UHCPlayer;
import com.cadiducho.fem.uhc.player.UHCScoreboard;

import com.cadiducho.fem.uhc.manager.GroundManager;
import com.cadiducho.fem.uhc.utils.InventoryCage;
import com.cadiducho.fem.uhc.utils.InventoryInfo;
import com.cadiducho.fem.uhc.utils.InventoryVIP;
import com.cadiducho.fem.uhc.utils.LocationUtil;
import com.cadiducho.fem.uhc.utils.Messages;
import com.cadiducho.fem.uhc.utils.Transformador;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Main extends JavaPlugin {

    public SetupCommand sc;
    public StartCommand stc;
    public GameManager gm;
    public WorldManager wm;
    public GroundManager grm;
    public Messages msg;
    public UHCPlayer up;
    public Transformador t;
    public InventoryInfo iv;
    public PVECountdown pc;
    public UHCGameCountdown uhcg;
    public UHCScoreboard ub;
    public LocationUtil lu;
    public LobbyManager lm;
    public InventoryCage ic;
    public InventoryVIP ivip;
    public WinnerCountdown wc;

    @Override
    public void onEnable() {//sidebar
        wm = new WorldManager(this);
        wm.deleteWorld("world", true);
        wm.deleteWorld("world_nether", true);
        registers();
        setupMap();
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "scoreboard objectives add vida health");
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "scoreboard objectives setdisplay list vida");
        GameState.state = GameState.LOBBY;
        getLogger().log(Level.INFO, "DonkeyUHC - Activado correctamente");
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().log(Level.INFO, "DonkeyUHC - Desactivado correctamente");
    }

    public void setupMap() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (wm.loadWorld("world")) {
                    wm.prepareWorld(getServer().getWorld("world"));
                    wm.prepareNether(getServer().getWorld("world_nether"));
                    lm.loadSpawns();
                    getLogger().log(Level.INFO, "DonkeyUHC - Preperación de mundo hecha.");
                    cancel();
                }
            }
        }.runTaskTimer(this, 40L, 40L);
    }

    public void registers() {
        gm = new GameManager(this);
        grm = new GroundManager(this);
        msg = new Messages();
        up = new UHCPlayer(this);
        t = new Transformador(this);
        sc = new SetupCommand(this);
        pc = new PVECountdown(this);
        iv = new InventoryInfo(this);
        stc = new StartCommand(this);
        uhcg = new UHCGameCountdown(this);
        ub = new com.cadiducho.fem.uhc.player.UHCScoreboard(this);
        lu = new LocationUtil(this);
        lm = new LobbyManager(this);
        ic = new InventoryCage(this);
        ivip = new InventoryVIP(this);
        wc = new WinnerCountdown(this);
        Bukkit.getPluginManager().registerEvents(new PlayerLogin(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoin(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuit(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerDeath(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerPortal(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerChat(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerItem(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerMove(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerFood(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerInteract(this), this);
        Bukkit.getPluginManager().registerEvents(new ItemCraft(this), this);
        Bukkit.getPluginManager().registerEvents(new EntityDamage(this), this);
        Bukkit.getPluginManager().registerEvents(new BlockListener(this), this);
        Bukkit.getPluginManager().registerEvents(new InventoryClick(this), this);
        Bukkit.getPluginManager().registerEvents(new ChunkListener(this), this);
        Bukkit.getPluginManager().registerEvents(new onPotionII(this), this);
        Bukkit.getPluginManager().registerEvents(new Motd(this), this);
        getLogger().log(Level.INFO, "DonkeyUHC - Listeners & clases registradas.");
    }
}