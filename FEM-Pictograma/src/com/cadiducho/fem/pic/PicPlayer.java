package com.cadiducho.fem.pic;

import com.cadiducho.fem.core.api.FEMUser;
import com.cadiducho.fem.core.util.ItemUtil;
import com.cadiducho.fem.core.util.Metodos;
import com.cadiducho.fem.core.util.ScoreboardUtil;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class PicPlayer extends FEMUser {

    private final Pictograma plugin = Pictograma.getInstance();
    
    public PicPlayer(UUID id) {
        super(id);
    }

    public void setWaitScoreboard() {
        ScoreboardUtil board = new ScoreboardUtil("§d§aPictograma", "lobby");
        new BukkitRunnable() {
            @Override
            public void run() {
                if (getPlayer() == null) cancel();
                
                if (plugin.getGm().acceptPlayers()) {
                    board.setName("§d§aPictograma");
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
        }.runTaskTimer(plugin, 20l, 20l);
    }

    private void setGameScoreboard() {
        ScoreboardUtil board = new ScoreboardUtil("§d§aPictograma", "game");
        new BukkitRunnable() {
            @Override
            public void run() {
                if (getPlayer() == null) cancel();

                board.text(4, "Artista: §b" + plugin.getGm().builder);
                board.text(2, "Puntos: §a" + plugin.getGm().getScore().get(getPlayer()));
                board.text(0, "§cmc.undergames.es");

                if (getPlayer() != null) board.build(getPlayer());
            }
        }.runTaskTimer(plugin, 20l, 20l);
    }

    public void setLobbyPlayer() {
        setWaitScoreboard();
        plugin.getGm().addPlayerToGame(getPlayer());
        setCleanPlayer(GameMode.ADVENTURE);
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
        if (gameMode != GameMode.CREATIVE) getPlayer().setFlying(false);
        getPlayer().getActivePotionEffects().forEach(ef -> getPlayer().removePotionEffect(ef.getType()));
        setGameScoreboard();
    }
    
    public void setArtist() {
        getPlayer().teleport(plugin.getAm().getPaintLoc());
        getPlayer().setGameMode(GameMode.SURVIVAL);
        getPlayer().setAllowFlight(true);
        getPlayer().setFlying(true);
        getPlayer().getInventory().clear();
        getPlayer().getInventory().setArmorContents(null);
        getPlayer().getInventory().setItem(0, ItemUtil.createItem(Material.WOOD_SWORD, "&ePincel"));
        getPlayer().getInventory().setItem(1, ItemUtil.createItem(Material.GOLD_SWORD, "&eBrocha"));
        getPlayer().getInventory().setItem(2, ItemUtil.createItem(Material.BOWL, "&aEscoger color"));
        getPlayer().getInventory().setItem(6, ItemUtil.createItem(Material.LAVA_BUCKET, "&eRellenar"));
        getPlayer().getInventory().setItem(7, ItemUtil.createItem(Material.IRON_SWORD, "&eBorrador"));
        getPlayer().getInventory().setItem(8, ItemUtil.createItem(Material.CLAY_BALL, "&eNueva hoja"));
        sendMessage("&eSelecciona la herramienta que quieras usar y pulsa click derecho para usarla.");
        plugin.getMsg().sendBroadcast(Metodos.colorizar("&e " + getName() + " &b es el artista esta ronda!"));
        setGameScoreboard();
    }

    public void spawn() {
        getPlayer().teleport(plugin.getAm().getSpawn());
    }
}