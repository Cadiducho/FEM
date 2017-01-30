package com.cadiducho.fem.royale.task;

import com.cadiducho.fem.royale.BattleRoyale;
import com.cadiducho.fem.royale.manager.GameState;
import org.bukkit.scheduler.BukkitRunnable;

public class GameCountdown extends BukkitRunnable {

    private final BattleRoyale plugin;

    public GameCountdown(BattleRoyale instance) {
        plugin = instance;
    }

    @Override
    public void run() {
        plugin.getGm().getPlayersInGame().stream().forEach(players -> {
            plugin.getMsg().sendActionBar(players, "&a&l" + plugin.getAm().gameTime);
        });
        if (plugin.getAm().gameTime == (plugin.getConfig().getInt("gameTime") - 1)) {
            plugin.getMsg().sendBroadcast("&7El juego ha comenzado");
            plugin.getAm().getWb().setSize(50, plugin.getAm().gameTime + 400); //Comenzar a mover el WorldBorder
            new ChestTask(plugin).runTaskTimer(plugin, 2399l, 2400l); //Iniciar hilo de spawneo de cofres (2 minutos)
        } else if (plugin.getAm().gameTime == (plugin.getConfig().getInt("gameTime") - 25)) {
            plugin.getMsg().sendBroadcast("Ahora hay PVP");
            GameState.state = GameState.GAME;
        } else if (plugin.getAm().gameTime == (plugin.getConfig().getInt("gameTime") - 290)) {
            plugin.getMsg().sendBroadcast("&6¡En 10 segundos los cofres serán reabastecidos!");
        } else if (plugin.getAm().gameTime == (plugin.getConfig().getInt("gameTime") - 300)) { // Cuando queden 5 minutos, rellenar cofres
            plugin.getAm().refillChests();
            plugin.getMsg().sendBroadcast("&6Los cofres de provisiones han sido reabastecidos");
        } else if (plugin.getAm().gameTime == (plugin.getConfig().getInt("gameTime") - 420)) { // Avisar de deathmatch. 3, 1 mins, 30, 10 segs
            plugin.getMsg().sendBroadcast("&6¡En 3 minutos comenzará la fase Deathmatch!");
        } else if (plugin.getAm().gameTime == (plugin.getConfig().getInt("gameTime") - 480)) {
            plugin.getMsg().sendBroadcast("&6¡En 1 minuto comenzará la fase Deathmatch!");
        } else if (plugin.getAm().gameTime == (plugin.getConfig().getInt("gameTime") - 570)) {
            plugin.getMsg().sendBroadcast("&6¡En 30 segundos comenzará la fase Deathmatch!");
        } else if (plugin.getAm().gameTime == (plugin.getConfig().getInt("gameTime") - 590)) {
            plugin.getMsg().sendBroadcast("&6¡En 10 segundos comenzará la fase Deathmatch!");
        } else if (plugin.getAm().gameTime == 0) {
            plugin.getGm().dm = true;
            new DeathMatchCountdown(plugin).runTaskTimer(plugin, 20l, 20l);
            GameState.state = GameState.DEATHMATCH;
            cancel();
        }
        -- plugin.getAm().gameTime;
        noPlayers();
    }

    private void noPlayers(){
        if (plugin.getGm().getPlayersInGame().isEmpty()){
            plugin.getServer().shutdown();
        }
    }
}
