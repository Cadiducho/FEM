package com.cadiducho.fem.core.api;

import com.cadiducho.fem.core.FEMCore;

import java.io.File;
import java.util.HashMap;
import lombok.Data;

@Data
public class FEMMap {

    private static final FEMCore plugin = FEMCore.getInstance();

    private String nombre;
    private String juego;
    private String path;
    private int maxPlayer;
    private int minPlayer;
    private HashMap<String, Object> parametros;

    File getWorld() {
        return new File(path);
    }

    /*
	//TODO: Implementar nuevo sistema
    
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

*/
}
