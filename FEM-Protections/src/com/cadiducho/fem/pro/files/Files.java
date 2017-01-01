package com.cadiducho.fem.pro.files;

import com.cadiducho.fem.pro.Protections;
import lombok.Getter;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Files {

    private Protections pro = Protections.getInstance();

    private File fileAreas = new File("plugins/Protections/", "areas.yml");
    @Getter private YamlConfiguration areas = YamlConfiguration.loadConfiguration(fileAreas);

    private File fileBlocks = new File("plugins/Protections/", "blocks.yml");
    @Getter private YamlConfiguration blocks = YamlConfiguration.loadConfiguration(fileBlocks);

    private File fileConfig = new File("plugins/Protections/", "config.yml");
    @Getter private YamlConfiguration config = YamlConfiguration.loadConfiguration(fileConfig);

    public void setupFiles(){
        if (!fileAreas.exists()){
            fileAreas.mkdir();
            areas.set("total", 0);
        }
        if (!fileConfig.exists()){
            config.options().copyDefaults(true);
        }
        if (!fileBlocks.exists()){
            fileBlocks.mkdir();
            blocks.set("total", 0);
        }
        saveFiles();
    }

    public void saveFiles(){
        try{
            areas.save(fileAreas);
            areas.load(fileAreas);
            config.save(fileConfig);
            config.load(fileConfig);
            blocks.save(fileBlocks);
            blocks.load(fileBlocks);
        } catch (IOException | InvalidConfigurationException e){
            e.printStackTrace();
        }
    }

    public int getID(String file){
        int previousID = getCurrentID(file);
        int id = getCurrentID(file);
        id++;
        parseConfig(file).set("total", id);
        saveFiles();
        return previousID;
    }

    public int getCurrentID(String file){
        return parseConfig(file).getInt("total");
    }

    private YamlConfiguration parseConfig(String file){
        switch (file){
            case "areas":
                return areas;
            case "blocks":
                return blocks;
            default:
                return null;
        }
    }
}
