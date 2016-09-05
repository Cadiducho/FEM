package com.cadiducho.fem.core.cmds;

import com.cadiducho.fem.core.api.FEMUser;
import java.util.Arrays;
import java.util.Random;
import org.bukkit.World;

public class WeatherCMD extends FEMCmd {
    
    public WeatherCMD(){
        super("weather", Grupo.Admin, Arrays.asList("tiempo", "metereologia"));
    }

    @Override
    public void run (FEMUser user, String lbl, String[] args) {
        if (args.length < 1) {
            user.sendMessage("*weather.modo");
            return;
        }
        World mundo = user.getPlayer().getWorld();
        int parametrosdelluvia = (300 + (new Random()).nextInt(600)) * 20;
        switch(args[0].toLowerCase()) {
        	case "sun":
        	case "sol":
        	case "clear":
                //sol
                mundo.setWeatherDuration(0);
                mundo.setStorm(false);
                mundo.setThundering(false);
                mundo.setThunderDuration(0);
                user.sendMessage("*weather.sol");
                break;
        	case "rain":
        	case "lluvia":
                //luvia
                mundo.setWeatherDuration(parametrosdelluvia);
                mundo.setStorm(true);
                mundo.setThundering(false);
                user.sendMessage("*weather.lluvia");
                break;
        	case "thunder":
        	case "tormenta":
                //tormenta
                mundo.setWeatherDuration(parametrosdelluvia);
                mundo.setThunderDuration(parametrosdelluvia);
                mundo.setStorm(true);
                mundo.setThundering(true);
                user.sendMessage("weather.tormenta");
                break;
            default:
            	user.sendMessage("*weather.modo");
                break;
        }
        
    }
}
