package com.cadiducho.fem.core.cmds;

import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.core.api.FEMUser;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import org.bukkit.entity.Player;

public class TeleportAskAllCMD extends FEMCmd {

    public TeleportAskAllCMD() {
        super("tpaall", Grupo.Admin, Arrays.asList("teleportaskall"));
    }

    @Override
    public void run(FEMUser user, String label, String[] args) {
        final ArrayList<UUID> targets = new ArrayList<>();

        for (Player p : plugin.getServer().getOnlinePlayers()) {
            FEMUser target = FEMServer.getUser(p);
            FEMServer.addTeleportHereRequest(target.getUuid(), user.getUuid());
            if (FEMServer.getTeleportRequests().containsKey(target.getUuid())) {
                FEMServer.getTeleportRequests().remove(target.getUuid());
            }

            FEMServer.getTeleportRequests().keySet().stream()
                .filter(u -> FEMServer.getTeleportRequests().get(u).equals(target.getUuid()))
                .forEach(u -> FEMServer.removeTeleportRequest(u)
            );

            target.sendMessage("*tp.tpa.solicitudTarget", user.getName());

            targets.add(target.getUuid());
        }
        user.sendMessage("*tp.tpa.solicitud", "&eglobal");
        //Eliminar petición a los 2 minutos
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            targets.stream()
                .filter(t -> FEMServer.getTeleportHereRequests().containsKey(t) && FEMServer.getTeleportHereRequests().get(t).equals(user.getUuid()))
                .forEach(t -> FEMServer.removeTeleportHereRequest(t)
            );
            user.sendMessage("*tp.tpa.expirada");
        }, 120 * 20L);
    }
}
