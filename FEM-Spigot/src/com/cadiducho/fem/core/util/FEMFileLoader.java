package com.cadiducho.fem.core.util;

import com.cadiducho.fem.core.FEMCore;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import java.util.logging.Level;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class FEMFileLoader {
    
    private static final FEMCore plugin = FEMCore.getInstance();
    
    public static File fConf;
    public static File fWarps;
    public static File fMessages;

    public static void load() {
        fConf = new File(plugin.getDataFolder(), "config.yml");
        if (!fConf.exists()) {
            try {
                plugin.getConfig().options().copyDefaults(true);
                plugin.saveConfig();
                plugin.log("Generando archivo config.yml correctamente");
            } catch (Exception e) {
                plugin.log("Fallo al generar el config.yml!");
                plugin.debugLog("Causa: " + e.toString());
            }
        }
        File lf = new File(plugin.getDataFolder(), "lang.yml");
        if (!lf.exists()) {
            try {
                getLang().options().copyDefaults(true);
                saveLang();
                plugin.log("Generated lang.yml succesfully!");
            } catch (Exception e) {
                plugin.log("Failed to generate lang.yml!");
            }
        }
        
        File datos = new File(plugin.getDataFolder(), "datos");
        if (!datos.exists()) {
            datos.mkdir();
        }
        
       fWarps = new File(datos, "warps.json");
       
       try {
            if (!fWarps.exists()) {
                fWarps.createNewFile();
            }
        } catch (Exception ex) {
            plugin.log("Fallo al crear los archivos de configuración");
            plugin.debugLog("Causa: " + ex.getMessage());
        }
    }
    
    public static File getPlayerFile(final OfflinePlayer p) {
        UUID id = p.getUniqueId();
        final File file = new File(plugin.getDataFolder() + File.separator + "usuarios" + File.separator + id.toString() + ".json");
        File directory = new File(plugin.getDataFolder() + File.separator + "usuarios");
        if (!file.exists()) {
            try {
                if (!directory.exists()) {
                    directory.mkdirs();
                }
                file.createNewFile();
            } catch (IOException ex) {
                plugin.log("Fallo al cargar los datos de: " + p.getName() + " - " + p.getUniqueId());
                plugin.debugLog("Error: " + ex.getMessage());
            }
        }
        return file;
    }

    public static JsonConfig getPlayerConfig(OfflinePlayer p) {
        File file = getPlayerFile(p);
        return new JsonConfig(file);
    }
    
    private static FileConfiguration lang = null;
    private static File langFile = null;

    public static void reloadLang() {
        if (langFile == null) {
            langFile = new File(plugin.getDataFolder(), "lang.yml");
        }
        lang = YamlConfiguration.loadConfiguration(langFile);

        InputStream defStream = plugin.getResource("lang.yml");
        if (defStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defStream);
            lang.setDefaults(defConfig);
        }
    }

    public static FileConfiguration getLang() {
        if (lang == null) {
            reloadLang();
        }
        return lang;
    }

    public static void saveLang() {
        if (lang == null || langFile == null) {
            return;
        }
        try {
            getLang().save(langFile);
        } catch (Exception ex) {
            plugin.log(Level.SEVERE, "No se ha podido guardar " + langFile);
            plugin.debugLog("Error: " + ex);
        }
    }
}
