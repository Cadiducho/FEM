package com.cadiducho.fem.core.cmds;

import com.cadiducho.fem.core.api.FEMUser;
import java.util.Arrays;
import java.util.Set;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;

public class SpawnerCMD extends FEMCmd {

    public SpawnerCMD() {
        super("spawner", Grupo.Owner, Arrays.asList());
    }

    @Override
    public void run(FEMUser user, String label, String[] args) {
        if (args.length == 0) {
            user.sendMessage("*spawner.uso");
            return;
        }
        Block b = user.getPlayer().getTargetBlock((Set<Material>) null, 10);
        if (!(b.getState() instanceof CreatureSpawner)) {
            user.sendMessage("*spawner.mirando");
            return;
        }
        CreatureSpawner c = (CreatureSpawner) b.getState();
        EntityType m = EntityType.fromName(args[0]);
        if (m == null || m.equals(EntityType.UNKNOWN) || !m.isSpawnable()) {
            user.sendMessage("*spawner.tipoDesconocido", args[0]);
            return;
        }
        c.setSpawnedType(m);
        c.update();
        user.sendMessage("*spawner.mensaje", m.name());
    }
}
