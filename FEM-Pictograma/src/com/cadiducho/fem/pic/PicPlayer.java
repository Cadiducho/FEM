package com.cadiducho.fem.pic;

import com.cadiducho.fem.core.api.FEMUser;
import com.cadiducho.fem.core.util.ItemUtil;
import com.cadiducho.fem.core.util.Metodos;
import com.cadiducho.fem.core.util.ScoreboardUtil;
import java.util.UUID;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

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
    }
    
    public void setArtist() {
        getPlayer().teleport(plugin.getAm().getPaintLoc());
        getPlayer().setGameMode(GameMode.SURVIVAL);
        getPlayer().setAllowFlight(true);
        getPlayer().setFlying(true);
        getPlayer().getInventory().clear();
        getPlayer().getInventory().setArmorContents(null);
        getPlayer().getInventory().setItem(0, createTool("&ePincel"));
        getPlayer().getInventory().setItem(1, createTool("&eBrocha"));
        getPlayer().getInventory().setItem(2, createTool("&aEscoger color"));
        getPlayer().getInventory().setItem(6, createTool("&eRellenar"));
        getPlayer().getInventory().setItem(7, createTool("&eBorrador"));
        getPlayer().getInventory().setItem(8, createTool("&eNueva hoja"));
        sendMessage("&eSelecciona la herramienta que quieras usar y pulsa click derecho para usarla.");
        plugin.getMsg().sendBroadcast(Metodos.colorizar("&e " + getName() + " &b es el artista esta ronda!"));
    }

    public void spawn() {
        getPlayer().teleport(plugin.getAm().getSpawn());
    }
    
    private ItemStack createTool(String name) {
        short dmg = 0;
        switch (name) {
            case "&ePincel": dmg = 4; break;
            case "&eBrocha": dmg = 1; break;
            case "&aEscoger color": dmg = 3; break;
            case "&eRellenar": dmg = 2; break;
            case "&eBorrador": dmg = 5; break;
            case "&eNueva hoja": dmg = 6; break;
        }
        ItemStack is = new ItemStack(Material.SHIELD, 1, dmg);
        ItemMeta meta = is.getItemMeta();
        meta.setUnbreakable(true);
        meta.setDisplayName(Metodos.colorizar(name));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);
        is.setItemMeta(meta);
        return is;
    }
}