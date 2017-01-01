package com.cadiducho.fem.pro.cmd;

import com.cadiducho.fem.core.api.FEMUser;
import com.cadiducho.fem.core.cmds.FEMCmd;
import com.cadiducho.fem.pro.ProArea;
import com.cadiducho.fem.pro.ProPlayer;
import org.bukkit.Material;

import java.util.Arrays;

public class Pro extends FEMCmd{

    public Pro() {
        super("proteccion", Grupo.Usuario, Arrays.asList("pro"));
    }

    @Override
    public void run(FEMUser user, String label, String[] args) {
        if (args.length == 0){
            user.getPlayer().sendMessage("No valido");
            return;
        }

        if (args.length == 1){
            if (args[0].equalsIgnoreCase("lista")){//Por el momento todas en 1 mensaje, cambiarlo
                new ProArea().getPlayerAreas(new ProPlayer(user.getUuid())).forEach(a -> user.getPlayer().sendMessage(a + ""));
            }
        }

        if (args.length == 2){
            if (args[0].equalsIgnoreCase("borrar")){
                int id = Integer.parseInt(args[1]);
                ProArea area = new ProArea(id);

                if (area.exist() && area.getOwner().equals(new ProPlayer(user.getUuid()))){
                    area.removeArena(Material.AIR);
                }
            }
        }
    }
}
