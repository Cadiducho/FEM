package com.cadiducho.fem.teamtnt.util;

import com.cadiducho.fem.core.util.ItemUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;

public class TeamSelector {

    @Getter public ArrayList<Inventory> invs = new ArrayList<>();

    int equipos = 5; //(de 4) De momento, cambiar por 6 (de 3)

    public void teams(Player p){
        Inventory inv = Bukkit.createInventory(null, 9, "Equipos");
        int players = Bukkit.getOnlinePlayers().size();

        if (players <= 5){
            inv.setItem(2, ItemUtil.createGlass("Equipo &cRojo", "", DyeColor.RED));
            inv.setItem(3, ItemUtil.createGlass("Equipo &bAzul", "", DyeColor.BLUE));
            inv.setItem(4, ItemUtil.createGlass("Equipo &aVerde", "", DyeColor.GREEN));
            inv.setItem(5, ItemUtil.createGlass("Equipo &eAmarillo", "", DyeColor.YELLOW));
            inv.setItem(6, ItemUtil.createGlass("Equipo &dMorado", "", DyeColor.PURPLE));
        } else {
            inv.setItem(1, ItemUtil.createGlass("Equipo &cRojo", "", DyeColor.RED));
            inv.setItem(2, ItemUtil.createGlass("Equipo &bAzul", "", DyeColor.BLUE));
            inv.setItem(3, ItemUtil.createGlass("Equipo &aVerde", "", DyeColor.GREEN));
            inv.setItem(5, ItemUtil.createGlass("Equipo &eAmarillo", "", DyeColor.YELLOW));
            inv.setItem(6, ItemUtil.createGlass("Equipo &dMorado", "", DyeColor.PURPLE));
            inv.setItem(7, ItemUtil.createGlass("Equipo &7Gris", "", DyeColor.GRAY));
        }


        p.openInventory(inv);
        invs.add(inv);
    }
}
