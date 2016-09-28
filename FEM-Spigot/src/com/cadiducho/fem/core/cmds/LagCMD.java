package com.cadiducho.fem.core.cmds;

import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.core.api.FEMUser;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Chunk;
import org.bukkit.World;

public class LagCMD extends FEMCmd {
    
    public LagCMD() {
        super("lag", Grupo.Usuario, Arrays.asList("tps", "tickspersecond"));
    }

    List<String> mundos = Arrays.asList("world", "nethera", "netherb", "recursosa", "recursosb", "endcities", "nethercities");
    @Override
    public void run(FEMUser user, String label, String[] args) {
        boolean desglose = (args.length != 0 && args[0].equalsIgnoreCase("-d"));
        user.sendMessage("*lag.mensaje", FEMServer.getFormatedTPS());
        if (user.isOnRank(Grupo.Owner)) {
            if (desglose) {
                plugin.getServer().getWorlds().stream()
                    .filter(w -> mundos.contains(w.getName().toLowerCase()))
                    .forEach(w -> {
                        int tiles = 0;
                        for (Chunk c : w.getLoadedChunks()) {
                            tiles += c.getTileEntities().length;
                        }
                        user.sendMessage("*lag.mundo", "&bMundo " + w.getName(), w.getLoadedChunks().length, w.getEntities().size(), tiles, w.getPlayers().size());
                });
            } else {
                World w = plugin.getServer().getWorld("world");
                int tiles = 0;
                for (Chunk c : w.getLoadedChunks()) {
                    tiles += c.getTileEntities().length;
                }
                user.sendMessage("*lag.mundo", "Lobby", w.getLoadedChunks().length, w.getEntities().size(), tiles, w.getPlayers().size());
                user.sendMessage("*lag.desglose");
            }
            user.sendRawMessage("");
        }
	user.sendMessage("*lag.ping");
    }
}
