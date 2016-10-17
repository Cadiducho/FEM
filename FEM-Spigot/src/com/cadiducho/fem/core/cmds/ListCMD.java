package com.cadiducho.fem.core.cmds;

import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.core.api.FEMUser;
import com.cadiducho.fem.core.util.FEMFileLoader;
import com.cadiducho.fem.core.util.Metodos;
import java.util.Arrays;

import org.bukkit.command.CommandSender;

public class ListCMD extends FEMCmd {

    public ListCMD() {
        super("list", Grupo.Usuario, Arrays.asList("usuarios", "gente"));
    }

    @Override
    public void run(FEMUser user, String lbl, String[] args) {
        user.sendMessage("*list.mensaje", plugin.getServer().getOnlinePlayers().size(), online(user.isOnRank(Grupo.Owner)));
    }

    @Override
    public void run(CommandSender sender, String label, String[] args) {
        sender.sendMessage(Metodos.colorizar(FEMFileLoader.getEsLang().getString("list.mensaje")
                .replace("{0}", "" + plugin.getServer().getOnlinePlayers().size()))
                .replace("{1}", "")); //{1} es online(), mensaje a parte en consola
        sender.sendMessage(online(true));
    }

    private String online(boolean op) {
        StringBuilder sb = new StringBuilder();
        plugin.getServer().getOnlinePlayers().stream()
                .map(p -> FEMServer.getUser(p))
                .forEach(target -> {
                    if (null != target.getUserData().getGrupo()) {
                        switch (target.getUserData().getGrupo()) {
                            case Usuario:
                                sb.append("&7").append(target.getName()).append(" ");
                                break;
                            case Vip:
                                sb.append("&e").append(target.getName()).append(" ");
                                break;
                            case VipSuper:
                                sb.append("&l&e").append(target.getName()).append(" ");
                                break;
                            case VipMega:
                                sb.append("&6").append(target.getName()).append(" ");
                                break;
                            case Helper:
                                sb.append("&a").append(target.getName()).append(" ");
                                break;
                            case Admin:
                                sb.append("&1").append(target.getName()).append(" ");
                                break;
                            case Owner:
                                sb.append("&b").append(target.getName()).append(" ");
                                break;
                            case Dev:
                                sb.append("&b").append(target.getName()).append(" ");
                                break;
                            default:
                                break;
                        }
                    }

                });
        return sb.toString();
    }
}
