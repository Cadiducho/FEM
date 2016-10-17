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
    public static File fMessagesEs;
    public static File fMessagesFr;
    public static File fMessagesIt;

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
        File lf = new File(plugin.getDataFolder(), "lang_es.yml");
        if (!lf.exists()) {
            try {
                getEsLang().options().copyDefaults(true);
                saveEsLang();
                plugin.log("Generado lang_es.yml correctamente!");
            } catch (Exception e) {
                plugin.log("Fallo al generar un archivo de lenguaje");
            }
        }
        File langFrFile = new File(plugin.getDataFolder(), "lang_fr.yml");
        if (!langFrFile.exists()) {
            try {
                getFrLang().options().copyDefaults(true);
                saveFrLang();
                plugin.log("Generado lang_fr.yml correctamente!");
            } catch (Exception e) {
                plugin.log("Fallo al generar un archivo de lenguaje");
            }
        }
        File langItFile = new File(plugin.getDataFolder(), "lang_it.yml");
        if (!langItFile.exists()) {
            try {
                getItLang().options().copyDefaults(true);
                saveItLang();
                plugin.log("Generado lang_it.yml correctamente!");
            } catch (Exception e) {
                plugin.log("Fallo al generar un archivo de lenguaje");
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
    
    //Archivos de idiomas
    
    private static FileConfiguration esLang = null;
    private static File esLangFile = null;

    public static void reloadEsLang() {
        if (esLangFile == null) {
            esLangFile = new File(plugin.getDataFolder(), "lang_es.yml");
        }
        esLang = YamlConfiguration.loadConfiguration(esLangFile);

        InputStream defStream = plugin.getResource("lang_es.yml");
        if (defStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defStream);
            esLang.setDefaults(defConfig);
        }
    }

    public static FileConfiguration getEsLang() {
        if (esLang == null) {
            reloadEsLang();
        }
        return esLang;
    }

    public static void saveEsLang() {
        if (esLang == null || esLangFile == null) {
            return;
        }
        try {
            getEsLang().save(esLangFile);
        } catch (Exception ex) {
            plugin.log(Level.SEVERE, "No se ha podido guardar " + esLangFile);
            plugin.debugLog("Error: " + ex);
        }
    }
    
    private static FileConfiguration frLang = null;
    private static File frLangFile = null;

    public static void reloadFrLang() {
        if (frLangFile == null) {
            frLangFile = new File(plugin.getDataFolder(), "lang_fr.yml");
        }
        frLang = YamlConfiguration.loadConfiguration(frLangFile);

        InputStream defStream = plugin.getResource("lang_fr.yml");
        if (defStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defStream);
            frLang.setDefaults(defConfig);
        }
    }

    public static FileConfiguration getFrLang() {
        if (frLang == null) {
            reloadFrLang();
        }
        return frLang;
    }

    public static void saveFrLang() {
        if (frLang == null || frLangFile == null) {
            return;
        }
        try {
            getFrLang().save(frLangFile);
        } catch (Exception ex) {
            plugin.log(Level.SEVERE, "No se ha podido guardar " + frLangFile);
            plugin.debugLog("Error: " + ex);
        }
    }
    
    private static FileConfiguration itLang = null;
    private static File itLangFile = null;

    public static void reloadItLang() {
        if (itLangFile == null) {
            itLangFile = new File(plugin.getDataFolder(), "lang_it.yml");
        }
        itLang = YamlConfiguration.loadConfiguration(itLangFile);

        InputStream defStream = plugin.getResource("lang_it.yml");
        if (defStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defStream);
            itLang.setDefaults(defConfig);
        }
    }

    public static FileConfiguration getItLang() {
        if (itLang == null) {
            reloadItLang();
        }
        return itLang;
    }

    public static void saveItLang() {
        if (itLang == null || itLangFile == null) {
            return;
        }
        try {
            getItLang().save(itLangFile);
        } catch (Exception ex) {
            plugin.log(Level.SEVERE, "No se ha podido guardar " + itLangFile);
            plugin.debugLog("Error: " + ex);
        }
    }
}
