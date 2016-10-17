package com.cadiducho.fem.core.cmds;

import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.core.api.FEMServer.Warp;
import com.cadiducho.fem.core.api.FEMUser;
import com.cadiducho.fem.core.util.ItemUtil;
import com.cadiducho.fem.core.util.FEMFileLoader;
import com.cadiducho.fem.core.util.Metodos;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.Inventory;

public class WarpCMD extends FEMCmd {
    
    public WarpCMD() {
        super("warp", Grupo.Admin, Arrays.asList("warps", "warplist"));
    }
    
    @Override
    public void run(FEMUser user, String label, String[] args) {
        if (FEMServer.getWarpNames().isEmpty()) {
            user.sendMessage("*warp.noHay");
            return;
        }
        
        if (args.length != 0) {
            if (!FEMServer.getWarpNames().contains(args[0])) {
                user.sendMessage("*warp.noExiste", args[0]); 
                return;
            }
            
            Warp warp = FEMServer.getWarp(args[0]);
            warp.teleport(user);
            return;
        }
        
        int in = FEMServer.getWarps().size();
        int invSize = 9;
        for (int i = 54; i >= 9; i -= 9) {
            if (in < i) {
                invSize = i;
            }
        }
        
        Inventory inv = plugin.getServer().createInventory(user.getPlayer(), invSize, Metodos.colorizar(FEMFileLoader.getEsLang().getString("warp.menu")));
        int i = 0;
        for (Warp warp : FEMServer.getWarps()) {
            inv.setItem(i, ItemUtil.createItem(Material.getMaterial(warp.getIcon()), warp.getName(), warp.getDesc()));
            i++;
        }
        user.getPlayer().openInventory(inv);
    }
    
    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String alias, String[] args, String curs, Integer curn) {
        return FEMServer.getWarpNames();
    }
}
