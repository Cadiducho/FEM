package com.cadiducho.fem.lobby.gadgets.list;

import com.cadiducho.fem.core.util.ItemUtil;
import com.cadiducho.fem.lobby.gadgets.Gadgets;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

public class Bonfire extends Gadgets{

    public Bonfire(){
        super(0, "Fogata", ItemUtil.createItem(Material.FIRE, "Fogata"));
    }

    public void effects(Player p){
        Location l = p.getEyeLocation();

        if (l.getWorld().getBlockAt((int)l.getX(), (int)l.getY(), (int)l.getZ()).isLiquid()) return;

        ArmorStand as1 = l.getWorld().spawn(l.subtract(0, 1, 0), ArmorStand.class);
        as1.setMetadata("fem", new FixedMetadataValue(pro, "fem"));
        as1.setVisible(false);
        as1.setHelmet(new ItemStack(Material.STICK));
    }
}
