package com.cadiducho.fem.core.api;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;

import com.cadiducho.fem.core.FEMCore;
import com.google.common.io.Files;

import lombok.Getter;

public class FEMMap {

	//TODO: Hacer código más eficiente

	@Getter
	private String fileLocation = plugin.getDataFolder() + "/mapas";
	@Getter
	private String name;
	@Getter
	private String gameType; //Por si añadimos más mapas que no son del juego

	private static final FEMCore plugin = FEMCore.getInstance();

	public FEMMap(String name){
		this.name = name;

		cambiarMapa();
	}

	//TODO: Cargar configuración
	public void cambiarMapa(){
		if(new File("world").delete()){ //Nombre del mapa principal
			try{
				Files.copy(getRandomMap(name), new File("world"));
			}catch(IOException e){
				plugin.log(Level.WARNING, "Error al copiar el mapa");
			}
		}else{
			plugin.log(Level.WARNING, "Error al cambiar de mapa");
		}
	}

	@SuppressWarnings("null")
	private File getRandomMap(String currentMap){
		File[] dir = new File(fileLocation).listFiles(File::isDirectory);
		File[] mapas = null;

		for(int x = 0; x < dir.length; x++){
			if(!dir[x].getName().equalsIgnoreCase(currentMap)){
				mapas[x] = dir[x];
				//¿Cambiar?
			}
		}

		return mapas[new Random().nextInt(dir.length)];
	}
}
