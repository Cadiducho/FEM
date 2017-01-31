package com.cadiducho.fem.royale;

import com.cadiducho.fem.core.api.FEMUser;
import com.cadiducho.fem.core.util.ScoreboardUtil;
import org.bukkit.GameMode;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class BattlePlayer extends FEMUser {

    private final BattleRoyale plugin = BattleRoyale.getInstance();

    public BattlePlayer(UUID id) {
        super(id);
    }

    public void setWaitScoreboard() {
        ScoreboardUtil board = new ScoreboardUtil("§aBattleRoyale", "lobby"); 
        new BukkitRunnable() {
            @Override
            public void run() {
                if (getPlayer() == null) cancel();
                
                if (plugin.getGm().acceptPlayers()) {
                    board.text(5, "§d ");
                    board.text(4, "§6" + plugin.getGm().getPlayersInGame().size() + "§d/§6" + plugin.getAm().getMaxPlayers());
                    board.text(3, "§a ");
                    board.text(2, "§eEsperando...");
                    board.text(1, "§e ");
                    board.text(0, "§cmc.undergames.es");
                    if (getPlayer() != null) board.build(getPlayer());
                } else {
                    board.reset();
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 1l, 20l);
    }

    public void setGameScoreboard() {
        ScoreboardUtil board = new ScoreboardUtil("§aBattleRoyale", "game"); 
        new BukkitRunnable() { 
            @Override 
            public void run() {
                if (getPlayer() == null) cancel();
                
                board.text(10, "§c "); 
                board.text(9, "Jugadores"); 
                board.text(8, "§f" + plugin.getGm().getPlayersInGame().size() + "/" + plugin.getAm().getMaxPlayers()); 
                board.text(7, "§3 "); 
                board.text(6, "Asesinatos"); 
                board.text(5, "§f" + getKillsToString());
                board.text(3, "§cmc.undergames.es");
 
                if (getPlayer() != null) board.build(getPlayer()); 
            } 
        }.runTaskTimer(plugin, 20l, 20l); 
    }
   
    public void setCleanPlayer(GameMode gameMode) {
        getPlayer().setHealth(getPlayer().getMaxHealth());
        getPlayer().setFoodLevel(20);
        getPlayer().setExp(0);
        getPlayer().setTotalExperience(0);
        getPlayer().setLevel(0);
        getPlayer().setFireTicks(0);
        getPlayer().getInventory().clear();
        getPlayer().getInventory().setArmorContents(null);
        getPlayer().setGameMode(gameMode);
        getPlayer().getActivePotionEffects().forEach(ef -> getPlayer().removePotionEffect(ef.getType()));
    }
    
    public void loadKit(){
        setCleanPlayer(GameMode.SURVIVAL);
        getPlayer().getInventory().setItem(0, plugin.getMoneda());
        setGameScoreboard();
    }
    
    public void setLobbyPlayer() {
        plugin.getGm().addPlayerToGame(getPlayer());
        setCleanPlayer( GameMode.ADVENTURE);
        setWaitScoreboard();
    }

    public void setSpectator() {
        setCleanPlayer(GameMode.SPECTATOR);
        plugin.getGm().getSpectators().add(getPlayer());
        plugin.getGm().getPlayersInGame().stream().forEach(ig -> ig.hidePlayer(getPlayer()));
    }
    
    public int getKillsToString() {
        if (plugin.getGm().getKills().containsKey(getUuid())) {
            return plugin.getGm().getKills().get(getUuid());
        }
        return 0;
    }

    public void addKillToPlayer() {
        int actual = 0;
        if (plugin.getGm().getKills().containsKey(getUuid())) {
            actual = plugin.getGm().getKills().get(getUuid());
        }
        plugin.getGm().getKills().put(getUuid(), actual + 1);
    }
}
