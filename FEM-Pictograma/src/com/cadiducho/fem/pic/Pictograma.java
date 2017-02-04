package com.cadiducho.fem.pic;

import com.cadiducho.fem.core.listeners.ResourcePackManager;
import com.cadiducho.fem.core.listeners.TeleportFix;
import com.cadiducho.fem.core.util.ItemUtil;
import com.cadiducho.fem.pic.listener.*;
import com.cadiducho.fem.pic.manager.ArenaManager;
import com.cadiducho.fem.pic.manager.GameManager;
import com.cadiducho.fem.pic.manager.GameState;
import com.cadiducho.fem.pic.tick.Ticker;
import com.cadiducho.fem.core.util.Messages;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import lombok.Getter;
import org.bukkit.DyeColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.WorldCreator;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Pictograma extends JavaPlugin {

    @Getter private static Pictograma instance;
    private static final String packUrl = "https://undergames.es/dl/PictogramaResourcePack.zip"; //TODO: Mantener actualizado
    
    public static ArrayList<PicPlayer> players = new ArrayList<>();

    @Getter private ArenaManager am;
    @Getter private GameManager gm;
    @Getter private Messages msg;
    
    public static List<String> palabras;
    public ArrayList<String> palabrasUsadas = new ArrayList<>();
    public Inventory colorPicker;
    
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
        
        palabras = getConfig().getStringList("palabras");

        new WorldCreator("espera").createWorld();
        gm = new GameManager(instance);
        am = new ArenaManager(instance);
        am.prepareWorld(getServer().getWorld(getConfig().getString("Pictograma.Arena.mundo")));
        msg = new Messages(instance, "&3&lPictograma");

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerListener(instance), instance);
        pm.registerEvents(new WorldListener(instance), instance);
        pm.registerEvents(new GameListener(instance), instance);
        pm.registerEvents(new TeleportFix(instance), instance);
        pm.registerEvents(new ResourcePackManager(instance, packUrl), instance);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new Ticker(this), 1L, 1L);
        
        colorPicker = getServer().createInventory(null, 9, "Escoge el color del pincel");
        colorPicker.setItem(0, ItemUtil.createWool("Blanco", DyeColor.WHITE));
        colorPicker.setItem(1, ItemUtil.createWool("Negro", DyeColor.BLACK));
        colorPicker.setItem(2, ItemUtil.createWool("Rojo", DyeColor.RED));
        colorPicker.setItem(3, ItemUtil.createWool("Naranja", DyeColor.ORANGE));
        colorPicker.setItem(4, ItemUtil.createWool("Amarillo", DyeColor.YELLOW));
        colorPicker.setItem(5, ItemUtil.createWool("Verde", DyeColor.GREEN));
        colorPicker.setItem(6, ItemUtil.createWool("Azul", DyeColor.BLUE));
        colorPicker.setItem(7, ItemUtil.createWool("Morado", DyeColor.PURPLE));
        colorPicker.setItem(8, ItemUtil.createWool("Marrón", DyeColor.BROWN));
            
        GameState.state = GameState.LOBBY;
        getLogger().log(Level.INFO, "Pictograma: Activado correctamente");
    }

    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, "Pictograma: Desativado correctamente");
    }
    
    public String getRandomWord() {
        int i = palabras.size();
        Random r = new Random();
        String palabra = palabras.get(r.nextInt(i - 1));
        if (palabrasUsadas.contains(palabra)) {
            return getRandomWord();
        }
        palabrasUsadas.add(palabra);
        return palabra;
    }
    
    public static PicPlayer getPlayer(OfflinePlayer p) {
        for (PicPlayer pl : players) {
            if (pl.getUuid() == null) {
                continue;
            }
            if (pl.getUuid().equals(p.getUniqueId())) {
                return pl;
            }
        }
        PicPlayer us = new PicPlayer(p.getUniqueId());
        if (us.isOnline()) {
            players.add(us);
        }
        return us;
    }
}
