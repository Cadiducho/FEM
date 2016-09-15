package com.cadiducho.fem.royale.task;

import com.cadiducho.fem.royale.BattleRoyale;
import com.cadiducho.fem.royale.manager.GameState;
import java.util.Random;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.FallingBlock;
import org.bukkit.scheduler.BukkitRunnable;

public class GameCountdown extends BukkitRunnable {

    private final BattleRoyale plugin;

    public GameCountdown(BattleRoyale instance) {
        plugin = instance;
    }
    
    Random r = new Random();

    @Override
    public void run() {
        plugin.getGm().getPlayersInGame().stream().forEach(players -> {
            plugin.getMsg().sendActionBar(players, "&a&l" + plugin.getAm().gameTime);
        });
        if (plugin.getAm().gameTime == (plugin.getConfig().getInt("gameTime") - 1)) {
            plugin.getMsg().sendBroadcast("&7El juego ha comenzado");
            plugin.getAm().getWb().setSize(50, plugin.getAm().gameTime + 400);
        } else if (plugin.getAm().gameTime == (plugin.getConfig().getInt("gameTime") - 25)){
            plugin.getMsg().sendBroadcast("Ahora hay PVP");
            GameState.state = GameState.GAME;
        } else if (plugin.getAm().gameTime == 0) {
            plugin.getGm().dm = true;
            new DeathMatchCountdown(plugin).runTaskTimer(plugin, 20l, 20l);
            GameState.state = GameState.DEATHMATCH;
            cancel();
        }
        
        if (r.nextInt(100) == 1) { //1 / 100, algo como 5 cofres cada 10 mins... maximo       
            Location loc = plugin.getAm().spawnRandomChest();
            FallingBlock chest = plugin.getWorld().spawnFallingBlock(loc, Material.TRAPPED_CHEST, (byte)0);
            System.out.println("Cofre caido en " + loc.getBlockX() + "/"+ loc.getBlockY() + "/"+ loc.getBlockZ() + "/");
        }
        
        -- plugin.getAm().gameTime;
    }
}
