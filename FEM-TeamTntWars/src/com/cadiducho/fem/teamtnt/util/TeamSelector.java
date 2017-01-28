package com.cadiducho.fem.teamtnt.util;

import com.cadiducho.fem.core.util.ItemBuilder;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;

public class TeamSelector {

    @Getter public ArrayList<Inventory> invs = new ArrayList<>();

    int equipos = 5; //(de 4) De momento, cambiar por 6 (de 3)

    public void teams(Player p){
        Inventory inv = Bukkit.createInventory(null, 9, "Equipos");
        int players = Bukkit.getOnlinePlayers().size();
/*        if (players % 5 == 0){

        }*/

        inv.setItem(2, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability((short)14).setDisplayName("Equipo &cRojo").build());
        inv.setItem(3, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability((short)11).setDisplayName("Equipo &bAzul").build());
        inv.setItem(4, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability((short)13).setDisplayName("Equipo &aVerde").build());
        inv.setItem(5, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability((short)4).setDisplayName("Equipo &eAmarillo").build());
        inv.setItem(6, new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability((short)2).setDisplayName("Equipo &dMorado").build());

        p.openInventory(inv);
        invs.add(inv);
    }
}
