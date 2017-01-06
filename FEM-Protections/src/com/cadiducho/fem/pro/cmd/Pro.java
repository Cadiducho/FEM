package com.cadiducho.fem.pro.cmd;

import com.cadiducho.fem.core.api.FEMUser;
import com.cadiducho.fem.core.cmds.FEMCmd;
import com.cadiducho.fem.core.util.Metodos;
import com.cadiducho.fem.pro.ProArea;
import com.cadiducho.fem.pro.ProPlayer;
import com.cadiducho.fem.pro.utils.ProType;
import org.bukkit.Material;

import java.util.Arrays;

public class Pro extends FEMCmd{

    public Pro() {
        super("proteccion", Grupo.Usuario, Arrays.asList("pro"));
    }

    @Override
    public void run(FEMUser user, String label, String[] args) {
        if (args.length == 0){
            user.sendMessage("&8<--------------------------------------------------->");
            user.sendMessage("&6/pro tp <id> &0-> &2Te teleporta a la localización de un area");
            user.sendMessage("&6/pro lista &0-> &2Te devuelve las ids de tus protecciones");
            user.sendMessage("&6/pro borrar <id> &0-> &aBorras la protección del bloque con esa id");
            user.sendMessage("&6/pro flags lista <id> &0-> &aEstado de las flags del bloque");
            user.sendMessage("&6/pro flags <id> <flag> &0-> &aEstado de la flag especificada del bloque");
            user.sendMessage("&6/pro flags <id> <flag> <true/false> &0-> &aEstablece el valor a la flag para el bloque especificado");
            user.sendMessage("&8<--------------------------------------------------->");
            return;
        }

        if (args.length == 1){
            if (args[0].equalsIgnoreCase("lista")) {//Por el momento todas en 1 mensaje, cambiarlo
                new ProArea().getPlayerAreas(new ProPlayer(user.getUuid())).forEach(a -> user.getPlayer().sendMessage(a + ""));
            }
        }

        if (args.length == 2){
            if (args[0].equalsIgnoreCase("borrar")) {
                int id = Integer.parseInt(args[1]);
                ProArea area = new ProArea(id);

                if (area.exist() && area.getDueño().equals(new ProPlayer(user.getUuid()))){
                    area.removeArena(Material.AIR);
                    user.getPlayer().getInventory().addItem(ProType.generateItemStack(area.getProType()));
                    user.getPlayer().updateInventory();
                }
            }

            if (args[0].equalsIgnoreCase("tp")) {
                int id = Integer.parseInt(args[1]);
                ProArea area = new ProArea(id);
                ProPlayer player = new ProPlayer(user.getUuid());

                if (area.exist()) {
                    if (area.getAreaUsers().contains(player) || area.getAreaOwners().contains(player)) {
                        player.teleportArea(id);
                        //¿Tp sólo a principal?
                    }
                }
            }
        }

        if (args.length == 3){
            if (args[0].equalsIgnoreCase("flag")) {
                if (args[1].equalsIgnoreCase("lista")){
                    int id;
                    if (!Metodos.isInt(args[2])) {
                        user.sendMessage("&cLa id debe ser un número");
                        return;
                    }
                    id = Integer.parseInt(args[2]);
                    ProArea area = new ProArea(id);

                    area.getAllFlags().keySet().forEach(s -> user.sendMessage("&a" + s + ": &c" + area.getFlags(s)));
                    return;
                }

                int id;
                if (!Metodos.isInt(args[1])) {
                    user.sendMessage("&cLa id debe ser un número");
                    return;
                }
                id = Integer.parseInt(args[1]);
                ProArea area = new ProArea(id);

                area.getAllFlags().keySet().forEach(s -> {
                    if (args[2].equalsIgnoreCase(s)) {
                        user.sendMessage("&a" + s + ": &c" + area.getFlags(s));
                    } else {
                        user.sendMessage("&cNo hay ningúna flag con ese nombre");
                        return;
                    }
                });
            }
        }

        if (args.length == 4){
            if (args[0].equalsIgnoreCase("flag")) {
                int id;
                if (!Metodos.isInt(args[1])) {
                    user.sendMessage("&cLa id debe ser un número");
                    return;
                }
                id = Integer.parseInt(args[1]);
                ProArea area = new ProArea(id);

                area.getAllFlags().keySet().forEach(s -> {
                    if (args[2].equalsIgnoreCase(s)) {
                        switch (args[3]) {
                            case "true":
                            case "false":
                                area.setFlags(s, Boolean.valueOf(args[3]));
                                return;
                            default:
                                user.sendMessage("&cLos valores sólo pueden ser true or false");
                                return;
                        }
                    } else {
                        user.sendMessage("&cNo hay ningúna flag con ese nombre");
                        return;
                    }
                });
            }
        }
    }
}
