package com.cadiducho.fem.dropper.task;

import com.cadiducho.fem.dropper.DropPlayer;

import com.cadiducho.fem.dropper.Dropper;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

public class RespawnTask extends BukkitRunnable {

    private final DropPlayer player;

    public RespawnTask(DropPlayer instance) {
        player = instance;
    }

    private int count = 3;

    @Override
    public void run() {
        if (player.getPlayer() == null) cancel();

        player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.CLICK, 1f, 1f);
        switch (count) {
            case 3:
            case 2:
            case 1:
                player.sendMessage("Respawnearás en " + count + " segundos");/*
            HashMap<Integer, Integer> deaths = player.getUserData().getDeaths();
            deaths.replace(7, deaths.get(7) + 1);
            player.getUserData().setDeaths(deaths);
            player.save();*/
                break;
            case 0:
                player.setCleanPlayer(GameMode.SURVIVAL);
                player.getPlayer().teleport(Dropper.getInstance().getAm().getLobby());
                cancel();
                break;
        }
        count--;
    }

}
