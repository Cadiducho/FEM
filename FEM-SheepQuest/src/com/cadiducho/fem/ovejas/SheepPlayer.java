package com.cadiducho.fem.ovejas;

import com.cadiducho.fem.core.api.FEMUser;
import com.cadiducho.fem.ovejas.manager.tasks.GameTask;
import com.cadiducho.fem.ovejas.manager.tasks.OvejasTask;
import com.cadiducho.fem.ovejas.manager.util.ScoreboardUtil;
import java.util.ArrayList;
import lombok.Getter;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Sheep;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class SheepPlayer {

    private final SheepQuest plugin = SheepQuest.getInstance();
    @Getter private final FEMUser base;
    @Getter ArrayList<Sheep> sheeps = new ArrayList<>();
    
    public SheepPlayer(FEMUser instance) {
        base = instance;
    }

    public void setWaitScoreboard() {
        ScoreboardUtil board = new ScoreboardUtil("    §6§lSheep§c§lQuest    ", "lobby");
        new BukkitRunnable() {
            @Override
            public void run() {
                if (base.getPlayer() == null) cancel();
                
                if (plugin.getGm().isInLobby()) {
                    board.setName("    §6§lSheep§c§lQuest    ");
                    board.text(5, "§d ");
                    board.text(4, "§6" + plugin.getGm().getPlayersInGame().size() + "§d/§6" + plugin.getAm().getMaxPlayers());
                    board.text(3, "§a ");
                    board.text(2, "§eEsperando...");
                    board.text(1, "§e ");
                    board.text(0, "§cmc..net");
                    if (base.getPlayer() != null) board.build(base.getPlayer());
                } else {
                    board.reset();
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 20l, 20l);
    }

    public void setScoreboard() {
        ScoreboardUtil board = new ScoreboardUtil("    §6§lSheep§c§lQuest    ", "game");
        new BukkitRunnable() {
            @Override
            public void run() {
                if (base.getPlayer() == null) cancel();
                
                board.setName("    §6§lSheep§c§lQuest    ");
                board.text(12, "§7Equipos:");
                board.text(11, "§cRojo: §f" + plugin.getTm().getPuntos(plugin.getTm().rojo));
                board.text(10, "§eAmarillo: §f" + plugin.getTm().getPuntos(plugin.getTm().amarillo));
                board.text(9, "§aVerde: §f" + plugin.getTm().getPuntos(plugin.getTm().verde));
                board.text(8, "§bAzul: §f" + plugin.getTm().getPuntos(plugin.getTm().azul));
                board.text(7, "§f☺ ");
                board.text(6, "§7Tiempo restante:");
                board.text(5, "§e" + GameTask.getTimeLeft() + " segundos");
                board.text(4, "§f☺ ");
                board.text(3, "§dSiguiente oveja:");
                board.text(2, "§e" + OvejasTask.getCool().getTimeLeft("sheep") + " segundos");
                board.text(1, "§f ");
                board.text(0, "§e127.0.0.257");

                if (base.getPlayer() != null) board.build(base.getPlayer());
            }
        }.runTaskTimer(plugin, 1l, 1l);
    }
    
    public void setLobbyPlayer() {
        setWaitScoreboard();
        plugin.getGm().addPlayerToGame(base.getPlayer());
        setCleanPlayer(GameMode.ADVENTURE);
        //plugin.getAm().teleport(base.getPlayer());
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
    
    public void dressPlayer() {
        LeatherArmorMeta leatherMeta;

        ItemStack tshirt = new ItemStack(Material.LEATHER_CHESTPLATE);
        leatherMeta = (LeatherArmorMeta) tshirt.getItemMeta();
        leatherMeta.setColor(plugin.getTm().getColor(base.getPlayer()));
        tshirt.setItemMeta(leatherMeta);

        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
        leatherMeta = (LeatherArmorMeta) boots.getItemMeta();
        leatherMeta.setColor(plugin.getTm().getColor(base.getPlayer()));
        boots.setItemMeta(leatherMeta);

        ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
        leatherMeta = (LeatherArmorMeta) leggings.getItemMeta();
        leatherMeta.setColor(plugin.getTm().getColor(base.getPlayer()));
        leggings.setItemMeta(leatherMeta);

        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
        leatherMeta = (LeatherArmorMeta) helmet.getItemMeta();
        leatherMeta.setColor(plugin.getTm().getColor(base.getPlayer()));
        helmet.setItemMeta(leatherMeta);

        base.getPlayer().setDisplayName(plugin.getTm().getTeam(base.getPlayer()).getPrefix() + base.getPlayer().getName());
        base.getPlayer().getInventory().setBoots(boots);
        base.getPlayer().getInventory().setChestplate(tshirt);
        base.getPlayer().getInventory().setLeggings(leggings);
        base.getPlayer().getInventory().setHelmet(helmet);
        
        base.getPlayer().getInventory().setItem(0, new ItemStack(Material.SADDLE));
        base.getPlayer().getInventory().setItem(8, new ItemStack(Material.IRON_SWORD));
    }

    public void spawn() {
        base.getPlayer().teleport(plugin.getTm().getAreas().get(plugin.getTm().getTeam(base.getPlayer())).getCentro());
    }
    
    public void addSheep(Sheep sh) {
        if (sheeps.size() == 3) {
            base.sendMessage("No puedes llevar maás de tres ovejas!");
            return;
        }
        Entity last = sheeps.size() == 2 ? sheeps.get(1) : (sheeps.size() == 1 ? sheeps.get(0) : base.getPlayer());
        last.setPassenger(sh);
        sheeps.add(sh);
    }
    
    public void removeAllSheeps() {
        for (int i = sheeps.size() - 1; i >= 0; i--) { //De mayor a menor, ejectar la de arriba (que no tiene a nadie encima)
            removeSheep(sheeps.get(i), i);
        }
        sheeps.clear();
    }
    
    public void removeSheep(Sheep sh, int i) {
        if (sheeps.contains(sh)) sheeps.remove(sh);
        sh.setHealth(sh.getMaxHealth());
        if (i == 0) { //Está sobre el humano, no sobre otra oveja
            base.getPlayer().eject();
        }
        
        sh.eject();
    }
    
    public boolean isInTeamSpawnArea() {
        return plugin.getTm().getAreas().get(plugin.getTm().getTeam(base.getPlayer())).isOnArea(base.getPlayer().getLocation());
        //return (base.getPlayer().getLocation().distanceSquared(plugin.getTm().getSpawn(plugin.getTm().getTeam(base.getPlayer()))) <= plugin.getConfig().getInt("SheepQuest.Arena.radio"));
        /*double px = base.getPlayer().getLocation().getX();
        double pz = base.getPlayer().getLocation().getZ();
       
        Team t = plugin.getTm().getTeam(base.getPlayer());
        Location areaCentro = plugin.getTm().getSpawn(plugin.getTm().getTeam(base.getPlayer()));
        double cx = areaCentro.getX();
        double cz = areaCentro.getZ();
        int radio = plugin.getConfig().getInt("SheepQuest.Arena.radio");
        
        //Comprobar si está en su área de spawn, según el radio establecido
        //return ((cx - radio) <= px && px <= (cx + radio)) && ((cz - radio) <= pz && pz <= (cz + radio));
        return ((px < (cx + radio) && px > (cz - radio)) && (pz < (cz + radio) && pz > (cz - radio)));*/
    }
}
