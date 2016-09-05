package com.cadiducho.fem.uhc.countdown;

import com.cadiducho.fem.uhc.Main;
import com.cadiducho.fem.uhc.manager.GameState;
import java.util.UUID;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class UHCGameCountdown extends BukkitRunnable {

    public int totalTime = 2400;
    private final Main plugin;

    public UHCGameCountdown(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (UUID id : plugin.gm.PLAYERS_IN_GAME) {
            Player players = plugin.getServer().getPlayer(id);
            plugin.msg.sendActionBar(players, "&c&lDonkey&b&lUHC &8| &fEliminación: &e" + plugin.t.tiempoPVE(totalTime));
        }
        if (totalTime % 300 == 0 && totalTime > 0) {

        } else if (totalTime > 0 && totalTime <= 5) {

        } else if (totalTime == 0) {
            plugin.wm.removePlayersFromNether();
            plugin.wm.getArenaWorld().getWorldBorder().setCenter(50d, 10);
            for (UUID id : plugin.gm.PLAYERS_IN_GAME) {
                Player players = plugin.getServer().getPlayer(id);
                players.playSound(players.getLocation(), Sound.ENTITY_IRONGOLEM_DEATH, 1f, 1f);
                plugin.msg.sendTitle(players, "&c&lELIMINACIÓN", "&7ha comenzando la eliminación", 0, 15, 5);
                GameState.state = GameState.ELIMINACION;
                players.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, Integer.MAX_VALUE, 1));
            }
            GameState.state = GameState.ELIMINACION;
            this.cancel();
        }
        --totalTime;
    }

}
