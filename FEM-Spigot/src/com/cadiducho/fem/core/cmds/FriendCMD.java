package com.cadiducho.fem.core.cmds;

import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.core.api.FEMUser;
import static com.cadiducho.fem.core.cmds.FEMCmd.plugin;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class FriendCMD extends FEMCmd {

    public FriendCMD() {
        super("friend", Grupo.Usuario, Arrays.asList("amigo", "amigos"));
    }

    @Override
    public void run(FEMUser user, String lbl, String[] args) {
        if (args.length == 0) {
            user.sendMessage("*friend.uso");
            return;
        }
        FEMUser target;
        ArrayList<UUID> ams;
        switch (args[0].toLowerCase()) {
            case "list":
                if (user.getUserData().getAmigos().isEmpty()) {
                    user.sendMessage("*friend.noTienes");
                    return;
                }
                String usuarios = "";
                    usuarios = user.getUserData().getAmigos().stream()
                            .map(u -> FEMServer.getUser(u).getDisplayName()+", ")
                            .reduce(usuarios, String::concat);
                    usuarios = usuarios.substring(0, usuarios.length() - 2);
                    user.sendMessage("*friend.list", usuarios);
                break;
            case "add":
                if (args.length != 2) {
                    user.sendMessage("*friend.uso");
                    return;
                }
                target = FEMServer.getUser(plugin.getServer().getPlayer(args[1]));
                if (target == null) {
                    user.sendMessage("*userDesconectado");
                    return;
                }
                ams = user.getUserData().getAmigos();
                if (ams.contains(target.getUuid())) {
                    user.sendMessage("*friend.yaEs", target.getName());
                    return;
                }
                ams.add(target.getUuid());
                plugin.getMysql().addFriend(user, target);
                user.getUserData().setAmigos(ams);
                user.sendMessage("*friend.mensaje", target.getName());
                if (target.getUserData().getFriendRequest()) {
                    target.sendMessage("*friend.mensajeOtro", user.getName());
                }
                user.tryHidePlayers();
                break;
            case "remove":
                if (args.length != 2) {
                    user.sendMessage("*friend.uso");
                    return;
                }
                target = FEMServer.getUser(plugin.getServer().getPlayer(args[1]));
                if (target == null) {
                    user.sendMessage("*userDesconectado");
                    return;
                }
                ams = user.getUserData().getAmigos();
                if (!ams.contains(target.getUuid())) {
                    user.sendMessage("*friend.noEs", target.getName());
                    return;
                }
                ams.remove(target.getUuid());
                plugin.getMysql().removeFriend(user, target);
                user.getUserData().setAmigos(ams);
                user.sendMessage("*friend.msgRemove", target.getName());
                user.tryHidePlayers();
                break;
        }
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}
