package com.cadiducho.fem.pic.listener;

import com.cadiducho.fem.pic.Pictograma;
import java.util.Set;
import lombok.Getter;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;


public class GameListener implements Listener {

    private final Pictograma plugin;
    @Getter private DyeColor color = DyeColor.BLACK;

    public GameListener(Pictograma instance) {
        plugin = instance;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (((event.getPlayer().getInventory().getItemInMainHand().getType() == Material.STICK) && (event.getAction() == Action.RIGHT_CLICK_AIR)) || ((event.getAction() == Action.RIGHT_CLICK_BLOCK) && (event.getPlayer().isBlocking()))) {
            Block b = event.getPlayer().getTargetBlock((Set<Material>) null, 100);
            if (b.getType() != Material.WOOL || !plugin.getAm().getBuildZone().contains(b)) {
                return;
            }
            b.setTypeIdAndData(Material.WOOL.getId(), color.getData(), true);
            event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.BLOCK_LAVA_EXTINGUISH, 1.0F, 1.0F);
            return;
        }
        if (((event.getPlayer().getInventory().getItemInMainHand().getType() == Material.WOOL) && (event.getAction() == Action.RIGHT_CLICK_AIR)) || (event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            if ((event.getAction() != Action.RIGHT_CLICK_AIR) && (event.getAction() != Action.RIGHT_CLICK_BLOCK)) {
                return;
            }
            Block b = event.getPlayer().getTargetBlock((Set<Material>) null, 100);
            if (b.getType() != Material.WOOL) {
                return;
            }
            b.setTypeIdAndData(Material.WOOL.getId(), DyeColor.WHITE.getData(), true);
            event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_BAT_TAKEOFF, 1.0F, 1.0F);
            return;
        }
        if (((event.getPlayer().getInventory().getItemInMainHand().getType() == Material.BLAZE_ROD) && (event.getAction() == Action.RIGHT_CLICK_AIR)) || (event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            Block b = event.getPlayer().getTargetBlock((Set<Material>) null, 100);
            Block b2 = b.getLocation().add(0.0D, 1.0D, 0.0D).getBlock();
            Block b3 = b.getLocation().subtract(0.0D, -1.0D, 0.0D).getBlock();
            if (b.getType() != Material.WOOL || !plugin.getAm().getBuildZone().contains(b)) {
                return;
            }
            b.setTypeIdAndData(Material.WOOL.getId(), color.getData(), true);
            b2.setTypeIdAndData(Material.WOOL.getId(), color.getData(), true);
            b3.setTypeIdAndData(Material.WOOL.getId(), color.getData(), true);
            event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.BLOCK_LAVA_EXTINGUISH, 1.0F, 1.0F);
            return;
        }
        if (event.getPlayer().getInventory().getItemInMainHand().getType() == Material.COMPASS) {
            if ((event.getAction() != Action.RIGHT_CLICK_AIR) && (event.getAction() != Action.RIGHT_CLICK_BLOCK)) {
                return;
            }
            event.getPlayer().openInventory(plugin.colorPicker);
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        if (plugin.getGm().isInGame()) {
            if (plugin.getGm().builder.getUniqueId() == e.getPlayer().getUniqueId()) {
                Pictograma.getPlayer(e.getPlayer()).getBase().sendMessage("No puedes hablar mientras eres el artista");
                e.setCancelled(true);
            } else {
                String word = plugin.getGm().word;
                if (plugin.getGm().getHasFound().contains(e.getPlayer())) {
                    Pictograma.getPlayer(e.getPlayer()).getBase().sendMessage("Ya has encontrado la palabra");
                    e.setCancelled(true);
                } else if (e.getMessage().toLowerCase().contains(word)) {
                    plugin.getGm().wordFoundBy(e.getPlayer());
                    e.setCancelled(true);
                }
            }
        }
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
