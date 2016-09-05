package com.cadiducho.fem.ovejas.manager.tasks;

import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.core.util.Title;
import com.cadiducho.fem.ovejas.SheepPlayer;
import com.cadiducho.fem.ovejas.SheepQuest;
import com.cadiducho.fem.ovejas.manager.GameState;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class CountdownTask extends BukkitRunnable {

    private final SheepQuest plugin;

    public CountdownTask(SheepQuest instance) {
        plugin = instance;
    }

    private int count = 10;

    @Override
    public void run() {
        plugin.getGm().getPlayersInGame().forEach(pl -> pl.setLevel(count));
        
        if (count == 10) {
            //Limpiar anteriores ovejas
            plugin.getServer().getWorld("world").getLivingEntities().stream()
                    .filter(e -> !e.getType().equals(EntityType.PLAYER))
                    .forEach(e -> e.damage(e.getMaxHealth()));
            
            //Colocar jugadores
            plugin.getTm().cleanTeams();
            plugin.getTm().drawTeams(plugin.getGm().getPlayersInGame());
            plugin.getGm().getPlayersInGame().forEach(p -> SheepQuest.getPlayer(p).spawn());
            
        } else if (count > 0 && count <= 5) {
            plugin.getMsg().sendBroadcast("&7El juego empezará en " + count);

        } else if (count == 0) {
            for (Player players : plugin.getGm().getPlayersInGame()) {
                players.playSound(players.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1F, 1F);
                SheepQuest.getPlayer(players).setCleanPlayer(GameMode.SURVIVAL);
                SheepQuest.getPlayer(players).dressPlayer();
                new Title("&b&lBuena suerte", "", 1, 2, 1).send(players);
                players.setScoreboard(plugin.getServer().getScoreboardManager().getNewScoreboard());
                SheepQuest.getPlayer(players).setScoreboard();
            }
            
            //Iniciar hilo del juego e hilo de spawneo de ovejas
            //BukkitTask taskOvejas = new OvejasTask(plugin).runTaskTimer(plugin, 0L, 20*20L);
            BukkitTask taskOvejas = new OvejasTask(plugin).runTask(plugin);
            new GameTask(plugin, taskOvejas).runTaskTimer(plugin, 20l, 20l);
            GameState.state = GameState.GAME;
            this.cancel();
        }

        --count;
    }

}
