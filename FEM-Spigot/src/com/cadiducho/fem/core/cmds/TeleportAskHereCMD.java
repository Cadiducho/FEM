package com.cadiducho.fem.core.cmds;

import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.core.api.FEMUser;
import java.util.Arrays;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class TeleportAskHereCMD extends FEMCmd {
    
    public TeleportAskHereCMD() {
        super("tpahere", Grupo.Moderador, Arrays.asList("teleportaskhere"));
    }
    
    @Override
    public void run(FEMUser user, String label, String[] args) {
        if (args.length == 0) {
            user.sendMessage("*tp.tpa.usoHere");
            return;
        }
        
        FEMUser target = FEMServer.getUser(plugin.getServer().getPlayerExact(args[0]));
        if (!target.isOnline()) {
            user.sendMessage("*userDesconectado");
            return;
        }
        
        FEMServer.addTeleportHereRequest(target.getUuid(), user.getUuid());
        if (FEMServer.getTeleportRequests().containsKey(target.getUuid())) {
            FEMServer.getTeleportRequests().remove(target.getUuid());
        }

        FEMServer.getTeleportRequests().keySet().stream()
            .filter(u -> FEMServer.getTeleportRequests().get(u).equals(target.getUuid()))
            .forEach(u -> FEMServer.removeTeleportRequest(u));        
        
        target.sendMessage("*tp.tpa.solicitudTarget", user.getName());
        user.sendMessage("*tp.tpa.solicitud", "a &e" + target.getName() + " &hasta tu posición");

        //Eliminar petición a los 2 minutos
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            if (FEMServer.getTeleportRequests().containsKey(target.getUuid()) && FEMServer.getTeleportRequests().get(target.getUuid()).equals(user.getUuid())) {
                FEMServer.removeTeleportRequest(target.getUuid());
                user.sendMessage("*tp.tpa.expirada");
            }
        }, 120 * 20L);
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return null;
    }
}
