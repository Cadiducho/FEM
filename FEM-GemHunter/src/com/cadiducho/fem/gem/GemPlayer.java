package com.cadiducho.fem.gem;

import com.cadiducho.fem.core.api.FEMUser;
import com.cadiducho.fem.core.util.ScoreboardUtil;
import com.cadiducho.fem.gem.task.GameTask;
import com.cadiducho.fem.gem.task.HiddingTask;
import java.util.UUID;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class GemPlayer extends FEMUser {

    private final GemHunters plugin = GemHunters.getInstance();

    public GemPlayer(UUID id) {
        super(id);
    }

    public void setWaitScoreboard() {
        ScoreboardUtil board = plugin.getLobbyBoard();
        new BukkitRunnable() {
            @Override
            public void run() {
                if (getPlayer() == null) cancel();
                
                if (plugin.getGm().isInLobby()) {
                    board.setName("§d§lGem§e§lHunter");
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
    
    public void setHiddingScoreboard() {
        ScoreboardUtil board = plugin.getHiddingBoard();
        new BukkitRunnable() {
            @Override
            public void run() {
                if (getPlayer() == null) cancel();
                
                if (plugin.getGm().isHidding()) {
                    board.setName("§d§lGem§e§lHunter");
                    board.text(12, "§7Equipos:");
                    board.text(11, "§cRojo: §f" + plugin.getTm().getPuntos(plugin.getTm().rojo));
                    board.text(10, "§3Azul: §f" + plugin.getTm().getPuntos(plugin.getTm().azul));
                    board.text(7, "§f☺ ");
                    board.text(6, "§7Tiempo restante:");
                    board.text(5, "§e" + HiddingTask.getTimeLeft() + " segundos");
                    board.text(4, "§f¡Esconde tu gema! ");
                    board.text(0, "§emc.undergames.es");
                } else {
                    board.reset();
                    cancel();
                }
                if (getPlayer() != null) board.build(getPlayer());
            }
        }.runTaskTimer(plugin, 1l, 1l);
    }

    public void setGameScoreboard() {
        ScoreboardUtil board = plugin.getGameBoard();
        new BukkitRunnable() {
            @Override
            public void run() {
                if (getPlayer() == null) cancel();
                
                if (plugin.getGm().isInGame()) {
                    board.setName("§d§lGem§e§lHunter");
                    board.text(12, "§7Equipos:");
                    board.text(11, "§cRojo: §f" + plugin.getTm().getPuntos(plugin.getTm().rojo));
                    board.text(10, "§1Azul: §f" + plugin.getTm().getPuntos(plugin.getTm().azul));
                    board.text(7, "§f☺ ");
                    board.text(6, "§7Tiempo restante:");
                    board.text(5, "§e" + GameTask.getTimeLeft() + " segundos");
                    board.text(4, "§f☺ ");
                    board.text(0, "§emc.undergames.es");
                } else {
                    board.reset();
                    cancel();
                }

                if (getPlayer() != null) board.build(getPlayer());
            }
        }.runTaskTimer(plugin, 1l, 1l);
    }
    
    public void setLobbyPlayer() {
        System.out.println("SetLobby");
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
        getPlayer().getActivePotionEffects().forEach(ef -> getPlayer().removePotionEffect(ef.getType()));
    }
    
    public void dressPlayer() {
        LeatherArmorMeta leatherMeta;

        ItemStack tshirt = new ItemStack(Material.LEATHER_CHESTPLATE);
        leatherMeta = (LeatherArmorMeta) tshirt.getItemMeta();
        leatherMeta.setColor(plugin.getTm().getColor(getPlayer()));
        tshirt.setItemMeta(leatherMeta);

        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
        leatherMeta = (LeatherArmorMeta) boots.getItemMeta();
        leatherMeta.setColor(plugin.getTm().getColor(getPlayer()));
        boots.setItemMeta(leatherMeta);

        ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
        leatherMeta = (LeatherArmorMeta) leggings.getItemMeta();
        leatherMeta.setColor(plugin.getTm().getColor(getPlayer()));
        leggings.setItemMeta(leatherMeta);

        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
        leatherMeta = (LeatherArmorMeta) helmet.getItemMeta();
        leatherMeta.setColor(plugin.getTm().getColor(getPlayer()));
        helmet.setItemMeta(leatherMeta);

        getPlayer().setDisplayName(plugin.getTm().getTeam(getPlayer()).getPrefix() + getPlayer().getName());
        getPlayer().getInventory().setBoots(boots);
        getPlayer().getInventory().setChestplate(tshirt);
        getPlayer().getInventory().setLeggings(leggings);
        getPlayer().getInventory().setHelmet(helmet);

        if (plugin.getGm().isHidding()) {
            getPlayer().getInventory().setItem(0, new ItemStack(plugin.getAm().getTypeGema()));
        } else if (plugin.getGm().isInGame()) {
            getPlayer().getInventory().setItem(0, new ItemStack(Material.IRON_PICKAXE));
            getPlayer().getInventory().setItem(8, new ItemStack(Material.IRON_SWORD));
        }
    }

    public void spawn() {
        getPlayer().teleport(plugin.getTm().getTeams().get(plugin.getTm().getTeam(getPlayer())));
    }
}
