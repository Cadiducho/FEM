package com.cadiducho.fem.core.cmds;

import com.cadiducho.fem.core.api.FEMUser;
import java.util.Arrays;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class MoreCMD extends FEMCmd {
    
    public MoreCMD() {
        super("more", Grupo.Admin, Arrays.asList());
    }
    
    @Override
    public void run(FEMUser user, String label, String[] args) {
        ItemStack stack = user.getPlayer().getInventory().getItemInMainHand();
        if (stack == null || stack.getType().equals(Material.AIR)) {
            user.sendMessage("*more.aire");
            return;
        }
        stack.setAmount(stack.getMaxStackSize());
        user.getPlayer().updateInventory();
    }
    
}
