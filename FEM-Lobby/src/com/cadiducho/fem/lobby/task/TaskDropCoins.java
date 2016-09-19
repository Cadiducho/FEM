/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cadiducho.fem.lobby.task;

import com.cadiducho.fem.core.api.FEMUser;
import com.cadiducho.fem.core.util.ItemUtil;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

public class TaskDropCoins extends BukkitRunnable {

    private final FEMUser user;

    public TaskDropCoins(FEMUser p) {
        user = p;
    }

    @Override
    public void run() {
        if (!user.isOnline()) {
            cancel();
        }

        user.getPlayer().getWorld().dropItem(user.getPlayer().getLocation().add(user.getPlayer().getLocation().getDirection().multiply(-1)), ItemUtil.createItem(Material.DOUBLE_PLANT, "Coin"));
        user.getUserData().setCoins(user.getUserData().getCoins() - 1);
    }
}
