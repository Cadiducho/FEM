package com.cadiducho.fem.pic;

import com.cadiducho.fem.core.api.FEMUser;
import com.cadiducho.fem.core.util.ItemUtil;
import com.cadiducho.fem.core.util.Metodos;
import com.cadiducho.fem.core.util.ScoreboardUtil;
import lombok.Getter;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

public class PicPlayer {

    private final Pictograma plugin = Pictograma.getInstance();
    @Getter private final FEMUser base;
    
    public PicPlayer(FEMUser instance) {
        base = instance;
    }

    public void setWaitScoreboard() {
        ScoreboardUtil board = new ScoreboardUtil("§d§aPictograma", "lobby");
        new BukkitRunnable() {
            @Override
            public void run() {
                if (base.getPlayer() == null) cancel();
                
                if (plugin.getGm().acceptPlayers()) {
                    board.setName("§d§aPictograma");
                    board.text(5, "§d ");
                    board.text(4, "§6" + plugin.getGm().getPlayersInGame().size() + "§d/§6" + plugin.getAm().getMaxPlayers());
                    board.text(3, "§a ");
                    board.text(2, "§eEsperando...");
                    board.text(1, "§e ");
                    board.text(0, "§cmc.undergames.es");
                    if (base.getPlayer() != null) board.build(base.getPlayer());
                } else {
                    board.reset();
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 20l, 20l);
    }
    
    public void setLobbyPlayer() {
        setWaitScoreboard();
        plugin.getGm().addPlayerToGame(base.getPlayer());
        setCleanPlayer(GameMode.ADVENTURE);
    }

    public void setCleanPlayer(GameMode gameMode) {
        base.getPlayer().setHealth(base.getPlayer().getMaxHealth());
        base.getPlayer().setFoodLevel(20);
        base.getPlayer().setExp(0);
        base.getPlayer().setTotalExperience(0);
        base.getPlayer().setLevel(0);
        base.getPlayer().setFireTicks(0);
        base.getPlayer().getInventory().clear();
        base.getPlayer().getInventory().setArmorContents(null);
        base.getPlayer().setGameMode(gameMode);
        base.getPlayer().getActivePotionEffects().forEach(ef -> base.getPlayer().removePotionEffect(ef.getType()));
    }
    
    public void setArtist() {
        base.getPlayer().teleport(plugin.getAm().getPaintLoc());
        base.getPlayer().setGameMode(GameMode.CREATIVE);
        base.getPlayer().setFlying(true);
        base.getPlayer().getInventory().clear();
        base.getPlayer().getInventory().setArmorContents(null);
        base.getPlayer().getInventory().setItem(0, ItemUtil.createItem(Material.WOOD_SWORD, "&ePincel"));
        base.getPlayer().getInventory().setItem(1, ItemUtil.createItem(Material.GOLD_SWORD, "&eBrocha"));
        base.getPlayer().getInventory().setItem(2, ItemUtil.createItem(Material.COMPASS, "&aEscoger color"));
        base.getPlayer().getInventory().setItem(7, ItemUtil.createItem(Material.IRON_SWORD, "&eBorrador"));
        base.getPlayer().getInventory().setItem(8, ItemUtil.createItem(Material.EMPTY_MAP, "&eNueva hoja"));
        base.sendMessage("&eSelecciona la herramienta que quieras usar y pulsa click derecho para usarla.");
        plugin.getMsg().sendBroadcast(Metodos.colorizar("&e " + base.getName() + " &b es el artista esta ronda!"));
    }

    public void spawn() {
        base.getPlayer().teleport(plugin.getAm().getSpawn());
    }
}