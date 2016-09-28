package com.cadiducho.fem.core.cmds;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.core.api.FEMUser;

public class ClearArmorCMD extends FEMCmd {

    public ClearArmorCMD() {
        super("cleararmor", Grupo.Owner, Arrays.asList("limpiararmadura"));
    }

    @Override
    public void run(FEMUser user, String lbl, String[] args) {
        if (args.length < 1) {
            user.getPlayer().getInventory().setHelmet(new ItemStack(Material.AIR));
            user.getPlayer().getInventory().setChestplate(new ItemStack(Material.AIR));
            user.getPlayer().getInventory().setLeggings(new ItemStack(Material.AIR));
            user.getPlayer().getInventory().setBoots(new ItemStack(Material.AIR));
            user.sendMessage("*clear.armor");
            return;
        }
        
        FEMUser target = FEMServer.getUser(plugin.getServer().getPlayer(args[0]));
        if (target == null) {
            user.sendMessage("*userDesconectado");
            return;
        }
        target.getPlayer().getInventory().setHelmet(new ItemStack(Material.AIR));
        target.getPlayer().getInventory().setChestplate(new ItemStack(Material.AIR));
        target.getPlayer().getInventory().setLeggings(new ItemStack(Material.AIR));
        target.getPlayer().getInventory().setBoots(new ItemStack(Material.AIR));
        target.sendMessage("*clear.armor");
        user.sendMessage("*clear.armorOtro", target.getName());
    }
}
