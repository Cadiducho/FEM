package com.cadiducho.fem.pic.listener;

import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.pic.Pictograma;
import com.cadiducho.fem.pic.tick.EventTick;
import com.cadiducho.fem.pic.tick.TickType;
import java.util.Set;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;


public class GameListener implements Listener {

    private final Pictograma plugin;
    @Getter private DyeColor color = DyeColor.BLACK;

    public GameListener(Pictograma instance) {
        plugin = instance;
    }
    
    //EventTick para las brochas (todos sus tipos) y la goma. PlayerInteractEvent para abrir el menu, limpiar hoja o el cubo
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerPaint(EventTick event) {
        if (!plugin.getGm().isInGame()) return;       
        if (plugin.getGm().builder == null) return;
        if (event.getType() != TickType.TICK) return;
        
        Player p = plugin.getGm().builder;
        if (p.isSneaking()) { //Está intentando pintar
            if (null != p.getInventory().getItemInMainHand().getType()) {
                switch (p.getInventory().getItemInMainHand().getType()) {
                    case STICK:
                        setPincelBlock(p.getTargetBlock((Set<Material>) null, 100));
                        break;
                    case WOOL:
                        eraseBlock(p.getTargetBlock((Set<Material>) null, 100), p);
                        break;
                    case BLAZE_ROD:
                        setBrochaBlock(p.getTargetBlock((Set<Material>) null, 100));
                        break;
                    default:
                        break;
                }
            }
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if ((event.getAction() != Action.RIGHT_CLICK_AIR) && (event.getAction() != Action.RIGHT_CLICK_BLOCK)) {
            return;
        }
        if (event.getPlayer().getInventory().getItemInMainHand().getType() == Material.COMPASS) {
            event.getPlayer().openInventory(plugin.colorPicker);
        } else if (event.getPlayer().getInventory().getItemInMainHand().getType() == Material.PAPER) {
            plugin.getAm().getBuildZone().clear();
            plugin.getAm().getBuildZone().setWool(DyeColor.WHITE);
            FEMServer.getUser(event.getPlayer()).sendMessage("&eHas limpiado la hoja completamente");
        }
    }
    
    private void setPincelBlock(Block b) {
        if (b.getType() != Material.WOOL || !plugin.getAm().getBuildZone().contains(b)) {
            return;
        }
        plugin.getServer().getScheduler().runTask(plugin, () -> {
            b.setTypeIdAndData(Material.WOOL.getId(), color.getData(), true);
        });
    }
    
    private void setBrochaBlock(Block b) {
        Block b2 = b.getLocation().clone().add(0D, 1D, 0D).getBlock();
        Block b3 = b.getLocation().clone().subtract(0D, -1D, 0D).getBlock();
        Block b4 = b.getLocation().clone().subtract(0D, 0D, 1D).getBlock();
        Block b5 = b.getLocation().clone().subtract(0D, 0D, -1D).getBlock();
        if (b.getType() != Material.WOOL || !plugin.getAm().getBuildZone().contains(b)) {
            return;
        }
        plugin.getServer().getScheduler().runTask(plugin, () -> {
            b.setTypeIdAndData(Material.WOOL.getId(), color.getData(), true);
            b2.setTypeIdAndData(Material.WOOL.getId(), color.getData(), true);
            b3.setTypeIdAndData(Material.WOOL.getId(), color.getData(), true);
            b4.setTypeIdAndData(Material.WOOL.getId(), color.getData(), true);
            b5.setTypeIdAndData(Material.WOOL.getId(), color.getData(), true);
        });
    }
    private void eraseBlock(Block b, Player p) {
        if (b.getType() != Material.WOOL) {
            return;
        }
        plugin.getServer().getScheduler().runTask(plugin, () -> {
            b.setTypeIdAndData(Material.WOOL.getId(), DyeColor.WHITE.getData(), true);
        });
        p.playSound(p.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 1.0F, 1.0F);
    }

    @EventHandler
    public void onPlayerChat(PlayerChatEvent e) {
        if (plugin.getGm().isInGame()) {
            if (plugin.getGm().builder != null  && (plugin.getGm().builder.getUniqueId() == e.getPlayer().getUniqueId())) {
                Pictograma.getPlayer(e.getPlayer()).getBase().sendMessage("No puedes hablar mientras eres el artista");
                e.setCancelled(true);
            } else {
                String word = plugin.getGm().word.toLowerCase();
                String intento = e.getMessage().toLowerCase();
                
                //Remplazar tildes
                intento.replace("á", "a");
                intento.replace("é", "e");
                intento.replace("í", "i");
                intento.replace("ó", "o");
                intento.replace("ú", "u");
                intento.replace("à", "a");
                intento.replace("é", "e");
                intento.replace("ì", "i");
                intento.replace("ù", "u");
                if (plugin.getGm().getHasFound().contains(e.getPlayer())) {
                    Pictograma.getPlayer(e.getPlayer()).getBase().sendMessage("Ya has encontrado la palabra");
                    e.setCancelled(true);
                } else if (e.getMessage().toLowerCase().contains(word)) {
                    plugin.getGm().wordFoundBy(e.getPlayer());
                    e.setCancelled(true);
                }
            }
        }
        if (e.getPlayer().getGameMode().equals(GameMode.SPECTATOR)) {
            e.setCancelled(true);
        }
        e.setFormat(ChatColor.GREEN + e.getPlayer().getDisplayName() + ChatColor.WHITE + ": " + ChatColor.GRAY + e.getMessage());
    }
    
    @EventHandler
    public void onInvClick(InventoryClickEvent event) {
        Player p = (Player) event.getWhoClicked();
        if (event.getInventory().getName().equals(plugin.colorPicker.getTitle())) {
            if ((event.getCurrentItem() == null) || (event.getCurrentItem().getType() == Material.AIR)) {
                return;
            }
            if (event.getCurrentItem().getItemMeta().getDisplayName().contains("Blanco")) {
                setPencilColor(DyeColor.WHITE);
                p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
                p.closeInventory();
                return;
            }
            if (event.getCurrentItem().getItemMeta().getDisplayName().contains("Negro")) {
                setPencilColor(DyeColor.BLACK);
                p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
                p.closeInventory();
                return;
            }
            if (event.getCurrentItem().getItemMeta().getDisplayName().contains("Rojo")) {
                setPencilColor(DyeColor.RED);
                p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
                p.closeInventory();
                return;
            }
            if (event.getCurrentItem().getItemMeta().getDisplayName().contains("Naranja")) {
                setPencilColor(DyeColor.ORANGE);
                p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
                p.closeInventory();
                return;
            }
            if (event.getCurrentItem().getItemMeta().getDisplayName().contains("Amarillo")) {
                setPencilColor(DyeColor.YELLOW);
                p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
                p.closeInventory();
                return;
            }
            if (event.getCurrentItem().getItemMeta().getDisplayName().contains("Verde")) {
                setPencilColor(DyeColor.LIME);
                p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
                p.closeInventory();
                return;
            }
            if (event.getCurrentItem().getItemMeta().getDisplayName().contains("Azul")) {
                setPencilColor(DyeColor.LIGHT_BLUE);
                p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
                p.closeInventory();
                return;
            }
            if (event.getCurrentItem().getItemMeta().getDisplayName().contains("Morado")) {
                setPencilColor(DyeColor.PURPLE);
                p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
                p.closeInventory();
                return;
            }
            if (event.getCurrentItem().getItemMeta().getDisplayName().contains("Marron")) {
                setPencilColor(DyeColor.BROWN);
                p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
                p.closeInventory();
            }
        } else {
            event.setCancelled(true);
        }
        p.closeInventory();
    }

    public void setPencilColor(DyeColor color) {
        this.color = color;
    }
}
