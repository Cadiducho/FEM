package com.cadiducho.fem.core.api;

import com.cadiducho.fem.core.FEMCore;
import com.google.common.io.Files;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;

public class FEMMap {

	//TODO: Hacer código más eficiente

	@Getter private String fileLocation;
	@Getter private String name;
	@Getter private String gameType; //Por si añadimos más mapas que no son del juego
    @Getter private String config;

	private static final FEMCore plugin = FEMCore.getInstance();

    public FEMMap(String name){
        this.name = name;
        fileLocation = plugin.getDataFolder() + "/mapas/";
        config = plugin.getDataFolder() + "/mapConfig/" + name + ".yml";

        cambiarMapa();
    }

    @Deprecated
	public FEMMap(String name, String game){
		this.name = name;
		this.gameType = game;
		fileLocation = plugin.getDataFolder() + "/mapas/" + game + "/";
        config = plugin.getDataFolder() + "/mapConfig/" + game + "/" + name + ".yml";

		cambiarMapa();
	}

	//TODO: Cargar configuración
	public void cambiarMapa(){
		if(new File("world").delete()){ //Nombre del mapa principal
			try{
				Files.copy(getRandomMap(name), new File("world")); //Mapa
				Files.copy(new File(config), new File(plugin.getDataFolder(), "config.yml")); //Config
			}catch(IOException e){
				plugin.log(Level.WARNING, "Error al copiar el mapa o configuración");
			}
		}else{
			plugin.log(Level.WARNING, "Error al cambiar de mapa o configuración");
		}
	}

	private File getRandomMap(String currentMap){
		File[] dir = new File(fileLocation).listFiles(File::isDirectory);
		File[] mapas = {};

		for(int x = 0; x < dir.length; x++){
			if(!dir[x].getName().equalsIgnoreCase(currentMap)){
				mapas[x] = dir[x];
				//¿Cambiar?
			}
		}
		return mapas[new Random().nextInt(dir.length)];
	}
}
