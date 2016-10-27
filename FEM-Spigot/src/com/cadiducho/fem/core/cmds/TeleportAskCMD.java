package com.cadiducho.fem.core.cmds;

import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.core.api.FEMUser;
import java.util.Arrays;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class TeleportAskCMD extends FEMCmd {
    
    public TeleportAskCMD() {
        super("tpa", Grupo.Moderador, Arrays.asList("teleportask"));
    }
    
    @Override
    public void run(FEMUser user, String label, String[] args) {
        if (args.length == 0) {
            user.sendMessage("*tp.tpa.uso");
            return;
        }
        
        FEMUser target = FEMServer.getUser(plugin.getServer().getPlayerExact(args[0]));
        if (!target.isOnline()) {
            user.sendMessage("*userDesconectado");
            return;
        }
        
        FEMServer.addTeleportRequest(target.getUuid(), user.getUuid());
        if (FEMServer.getTeleportHereRequests().containsKey(target.getUuid())) {
            FEMServer.getTeleportHereRequests().remove(target.getUuid());
        }

        FEMServer.getTeleportHereRequests().keySet().stream()
            .filter(u -> FEMServer.getTeleportHereRequests().get(u).equals(target.getUuid()))
            .forEach(u -> FEMServer.removeTeleportHereRequest(u));        
        
        user.sendMessage("*tp.tpa.solicitud", "a &e" + user.getName());
        target.sendMessage("*tp.tpa.solicitudTarget", user.getName());

        //Eliminar petición a los 2 minutos
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            if (FEMServer.getTeleportRequests().containsKey(target.getUuid()) && FEMServer.getTeleportRequests().get(target.getUuid()).equals(user.getUuid())) {
                FEMServer.removeTeleportRequest(target.getUuid());
                user.sendMessage("*tp.tpa.expirada");
            }
        }, 120 * 20L);
    }
    
    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}
