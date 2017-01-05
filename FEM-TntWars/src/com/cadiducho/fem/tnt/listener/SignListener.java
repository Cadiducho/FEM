package com.cadiducho.fem.tnt.listener;

import com.cadiducho.fem.tnt.Generador;
import com.cadiducho.fem.tnt.TntPlayer;
import com.cadiducho.fem.tnt.TntWars;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;

public class SignListener implements Listener {

    private final TntWars plugin;

    public SignListener(TntWars instnace) {
        plugin = instnace;
    }

    private Inventory ironMenu;
    private ItemStack Info;
    private ItemStack Up;
    private ItemMeta InfoM;
    private ItemMeta UpM;
    private ArrayList<String> InfoL;
    private ArrayList<String> UpL;
    private static final HashMap<Player, Location> clickedSign = new HashMap<>();
        
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (!(e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            return;
        }
        Generador gen = Generador.getGenerador(e.getClickedBlock().getLocation());
        if (e.getClickedBlock().getState() instanceof Sign) {
            final Sign s = (Sign) e.getClickedBlock().getState();
            interactGenerador(s, p, s.getLocation());
        } else if (gen != null) {
            final Sign s = (Sign) gen.getSign().getBlock().getState();
            interactGenerador(s, p, s.getLocation());
        }
    }
    
    private void interactGenerador(Sign s, Player p, Location loc) {
        switch (s.getLine(1).toLowerCase()) {
                case "§chierro":
                    switch (s.getLine(2).toLowerCase()) {
                        case "nivel 1":
                            ironMenu = plugin.getServer().createInventory(null, 27, "§7Generador Hierro §2- Nivel 1");
                            Info = new ItemStack(Material.IRON_INGOT);
                            InfoM = Info.getItemMeta();
                            InfoM.setDisplayName("§2Generador de §7Hierro §2- Nivel 1");
                            InfoL = new ArrayList<>();
                            InfoL.add("§9Produccion: 1.0 segundos");
                            InfoM.setLore(InfoL);
                            Info.setItemMeta(InfoM);
                            Up = new ItemStack(Material.EXP_BOTTLE);
                            UpM = Up.getItemMeta();
                            UpM.setDisplayName("§2Mejorar a: §2Generador de §7Hierro §2- Nivel 2");
                            UpL = new ArrayList<>();
                            UpL.add("§9Produccion: 0.75 segundos");
                            UpL.add("§r");
                            UpL.add("§3Costo: §e20 §2Hierro");
                            UpM.setLore(UpL);
                            Up.setItemMeta(UpM);
                            clickedSign.put(p, loc);
                            ironMenu.setItem(11, Info);
                            ironMenu.setItem(15, Up);

                            p.openInventory(ironMenu);
                            break;
                        case "nivel 2":
                            ironMenu = plugin.getServer().createInventory(null, 27, "§7Generador Hierro §2- Nivel 2");
                            Info = new ItemStack(Material.IRON_INGOT);
                            InfoM = Info.getItemMeta();
                            InfoM.setDisplayName("§2Generador de §7Hierro §2- Nivel 2");
                            InfoL = new ArrayList<>();
                            InfoL.add("§9Produccion: 0.75 segundos");
                            InfoM.setLore(InfoL);
                            Info.setItemMeta(InfoM);
                            Up = new ItemStack(Material.EXP_BOTTLE);
                            UpM = Up.getItemMeta();
                            UpM.setDisplayName("§2Mejorar a: §2Generador de §7Hierro §2- Nivel 3");
                            UpL = new ArrayList<>();
                            UpL.add("§9Produccion: 0.5 segundos");
                            UpL.add("§r");
                            UpL.add("§3Costo: §e20 §2Oro");
                            UpM.setLore(UpL);
                            Up.setItemMeta(UpM);
                            clickedSign.put(p, loc);
                            ironMenu.setItem(11, Info);
                            ironMenu.setItem(15, Up);
                            p.openInventory(ironMenu);
                            break;
                        case "nivel 3":
                            ironMenu = plugin.getServer().createInventory(null, 27, "§7Generador Hierro §2- Nivel 3");
                            Info = new ItemStack(Material.IRON_INGOT);
                            InfoM = Info.getItemMeta();
                            InfoM.setDisplayName("§2Generador de §7Hierro §2- Nivel 3");
                            InfoL = new ArrayList<>();
                            InfoL.add("§9Produccion: 0.5 segundos");
                            InfoM.setLore(InfoL);
                            Info.setItemMeta(InfoM);
                            Up = new ItemStack(Material.EXP_BOTTLE);
                            UpM = Up.getItemMeta();
                            UpM.setDisplayName("§2Mejorar a: §2Generador de §7Hierro §2- Nivel 4");
                            UpL = new ArrayList<>();
                            UpL.add("§9Produccion: 0.25 segundos");
                            UpL.add("§r");
                            UpL.add("§3Costo: §e50 §2Oro");
                            UpM.setLore(UpL);
                            Up.setItemMeta(UpM);
                            clickedSign.put(p, loc);
                            ironMenu.setItem(11, Info);
                            ironMenu.setItem(15, Up);
                            p.openInventory(ironMenu);
                            break;
                        case "nivel 4":
                            ironMenu = plugin.getServer().createInventory(null, 27, "§7Generador Hierro §2- Nivel 4");
                            Info = new ItemStack(Material.IRON_INGOT);
                            InfoM = Info.getItemMeta();
                            InfoM.setDisplayName("§2Generador de §7Hierro §2- Nivel 4");
                            InfoL = new ArrayList<>();
                            InfoL.add("§9Produccion: 0.25 segundos");
                            InfoM.setLore(InfoL);
                            Info.setItemMeta(InfoM);
                            Up = new ItemStack(Material.EXP_BOTTLE);
                            UpM = Up.getItemMeta();
                            UpM.setDisplayName("§2Nivel Máximo");
                            Up.setItemMeta(UpM);
                            clickedSign.put(p, loc);
                            ironMenu.setItem(11, Info);
                            ironMenu.setItem(15, Up);
                            p.openInventory(ironMenu);
                            break;
                    }
                    break;
                case "§coro":
                    switch (s.getLine(2).toLowerCase()) {
                        case "nivel 1":
                            ironMenu = plugin.getServer().createInventory(null, 27, "§7Generador Oro §2- Nivel 1");
                            Info = new ItemStack(Material.GOLD_INGOT);
                            InfoM = Info.getItemMeta();
                            InfoM.setDisplayName("§2Generador de §7Oro §2- Nivel 1");
                            InfoL = new ArrayList<>();
                            InfoL.add("§9Produccion: 4.0 segundos");
                            InfoM.setLore(InfoL);
                            Info.setItemMeta(InfoM);
                            Up = new ItemStack(Material.EXP_BOTTLE);
                            UpM = Up.getItemMeta();
                            UpM.setDisplayName("§2Mejorar a: §2Generador de §7Oro §2- Nivel 2");
                            UpL = new ArrayList<>();
                            UpL.add("§9Produccion: 3.5 segundos");
                            UpL.add("§r");
                            UpL.add("§3Costo: §e10 §2Oro");
                            UpM.setLore(UpL);
                            Up.setItemMeta(UpM);
                            clickedSign.put(p, loc);
                            ironMenu.setItem(11, Info);
                            ironMenu.setItem(15, Up);

                            p.openInventory(ironMenu);
                            break;
                        case "nivel 2":
                            ironMenu = plugin.getServer().createInventory(null, 27, "§7Generador Oro §2- Nivel 2");
                            Info = new ItemStack(Material.GOLD_INGOT);
                            InfoM = Info.getItemMeta();
                            InfoM.setDisplayName("§2Generador de §7Oro §2- Nivel 2");
                            InfoL = new ArrayList<>();
                            InfoL.add("§9Produccion: 3.5 segundos");
                            InfoM.setLore(InfoL);
                            Info.setItemMeta(InfoM);
                            Up = new ItemStack(Material.EXP_BOTTLE);
                            UpM = Up.getItemMeta();
                            UpM.setDisplayName("§2Mejorar a: §2Generador de §7Oro §2- Nivel 3");
                            UpL = new ArrayList<>();
                            UpL.add("§9Produccion: 2.0 segundos");
                            UpL.add("§r");
                            UpL.add("§3Costo: §e20 §2Oro");
                            UpM.setLore(UpL);
                            Up.setItemMeta(UpM);
                            clickedSign.put(p, loc);
                            ironMenu.setItem(11, Info);
                            ironMenu.setItem(15, Up);
                            p.openInventory(ironMenu);
                            break;
                        case "nivel 3":
                            ironMenu = plugin.getServer().createInventory(null, 27, "§7Generador Oro §2- Nivel 3");
                            Info = new ItemStack(Material.GOLD_INGOT);
                            InfoM = Info.getItemMeta();
                            InfoM.setDisplayName("§2Generador de §7Oro §2- Nivel 3");
                            InfoL = new ArrayList<>();
                            InfoL.add("§9Produccion: 2.0 segundos");
                            InfoM.setLore(InfoL);
                            Info.setItemMeta(InfoM);
                            Up = new ItemStack(Material.EXP_BOTTLE);
                            UpM = Up.getItemMeta();
                            UpM.setDisplayName("§2Mejorar a: §2Generador de §7Oro §2- Nivel 4");
                            UpL = new ArrayList<>();
                            UpL.add("§9Produccion: 1.5 segundos");
                            UpL.add("§r");
                            UpL.add("§3Costo: §e20 §2Diamante");
                            UpM.setLore(UpL);
                            Up.setItemMeta(UpM);
                            clickedSign.put(p, loc);
                            ironMenu.setItem(11, Info);
                            ironMenu.setItem(15, Up);
                            p.openInventory(ironMenu);
                            break;
                        case "nivel 4":
                            ironMenu = plugin.getServer().createInventory(null, 27, "§7Generador Oro §2- Nivel 4");
                            Info = new ItemStack(Material.GOLD_INGOT);
                            InfoM = Info.getItemMeta();
                            InfoM.setDisplayName("§2Generador de §7Oro §2- Nivel 4");
                            InfoL = new ArrayList<>();
                            InfoL.add("§9Produccion: 1.5 segundos");
                            InfoM.setLore(InfoL);
                            Info.setItemMeta(InfoM);
                            Up = new ItemStack(Material.EXP_BOTTLE);
                            UpM = Up.getItemMeta();
                            UpM.setDisplayName("§2Nivel Máximo");
                            Up.setItemMeta(UpM);
                            clickedSign.put(p, loc);
                            ironMenu.setItem(11, Info);
                            ironMenu.setItem(15, Up);
                            p.openInventory(ironMenu);
                            break;
                    }
                    break;
                case "§cdiamante":
                    switch (s.getLine(2).toLowerCase()) {
                        case "nivel 0":
                            ironMenu = plugin.getServer().createInventory(null, 27, "§7Generador Diamante §2- No activado");
                            Info = new ItemStack(Material.DIAMOND);
                            InfoM = Info.getItemMeta();
                            InfoM.setDisplayName("§2Generador de §7Diamante §2- No activado");
                            InfoL = new ArrayList<>();
                            InfoL.add("§9Sin producción");
                            InfoM.setLore(InfoL);
                            Info.setItemMeta(InfoM);
                            Up = new ItemStack(Material.EXP_BOTTLE);
                            UpM = Up.getItemMeta();
                            UpM.setDisplayName("§2Mejorar a: §2Generador de §7Diamante §2- Nivel 1");
                            UpL = new ArrayList<>();
                            UpL.add("§9Produccion: 10.0 segundos");
                            UpL.add("§r");
                            UpL.add("§3Costo: §e5 §2Diamante");
                            UpM.setLore(UpL);
                            Up.setItemMeta(UpM);
                            clickedSign.put(p, loc);
                            ironMenu.setItem(11, Info);
                            ironMenu.setItem(15, Up);

                            p.openInventory(ironMenu);
                            break;
                        case "nivel 1":
                            ironMenu = plugin.getServer().createInventory(null, 27, "§7Generador Diamante §2- Nivel 1");
                            Info = new ItemStack(Material.DIAMOND);
                            InfoM = Info.getItemMeta();
                            InfoM.setDisplayName("§2Generador de §7Diamante §2- Nivel 1");
                            InfoL = new ArrayList<>();
                            InfoL.add("§9Produccion: 10.0 segundos");
                            InfoM.setLore(InfoL);
                            Info.setItemMeta(InfoM);
                            Up = new ItemStack(Material.EXP_BOTTLE);
                            UpM = Up.getItemMeta();
                            UpM.setDisplayName("§2Mejorar a: §2Generador de §7Diamante §2- Nivel 2");
                            UpL = new ArrayList<>();
                            UpL.add("§9Produccion: 8.0 segundos");
                            UpL.add("§r");
                            UpL.add("§3Costo: §e10 §2Diamante");
                            UpM.setLore(UpL);
                            Up.setItemMeta(UpM);
                            clickedSign.put(p, loc);
                            ironMenu.setItem(11, Info);
                            ironMenu.setItem(15, Up);

                            p.openInventory(ironMenu);
                            break;
                        case "nivel 2":
                            ironMenu = plugin.getServer().createInventory(null, 27, "§7Generador Diamante §2- Nivel 2");
                            Info = new ItemStack(Material.DIAMOND);
                            InfoM = Info.getItemMeta();
                            InfoM.setDisplayName("§2Generador de §7Diamante §2- Nivel 2");
                            InfoL = new ArrayList<>();
                            InfoL.add("§9Produccion: 8.0 segundos");
                            InfoM.setLore(InfoL);
                            Info.setItemMeta(InfoM);
                            Up = new ItemStack(Material.EXP_BOTTLE);
                            UpM = Up.getItemMeta();
                            UpM.setDisplayName("§2Mejorar a: §2Generador de §7Diamante §2- Nivel 3");
                            UpL = new ArrayList<>();
                            UpL.add("§9Produccion: 6.0 segundos");
                            UpL.add("§r");
                            UpL.add("§3Costo: §e50 §2Diamante");
                            UpM.setLore(UpL);
                            Up.setItemMeta(UpM);
                            clickedSign.put(p, loc);
                            ironMenu.setItem(11, Info);
                            ironMenu.setItem(15, Up);
                            p.openInventory(ironMenu);
                            break;
                        case "nivel 3":
                            ironMenu = plugin.getServer().createInventory(null, 27, "§7Generador Diamante §2- Nivel 3");
                            Info = new ItemStack(Material.DIAMOND);
                            InfoM = Info.getItemMeta();
                            InfoM.setDisplayName("§2Generador de §7Diamante §2- Nivel 3");
                            InfoL = new ArrayList<>();
                            InfoL.add("§9Produccion: 6.0 segundos");
                            InfoM.setLore(InfoL);
                            Info.setItemMeta(InfoM);
                            Up = new ItemStack(Material.EXP_BOTTLE);
                            UpM = Up.getItemMeta();
                            UpM.setDisplayName("§2Mejorar a: §2Generador de §7Diamante §2- Nivel 4");
                            UpL = new ArrayList<>();
                            UpL.add("§9Produccion: 4.0 segundos");
                            UpL.add("§r");
                            UpL.add("§3Costo: §e75 §2Diamante");
                            UpM.setLore(UpL);
                            Up.setItemMeta(UpM);
                            clickedSign.put(p, loc);
                            ironMenu.setItem(11, Info);
                            ironMenu.setItem(15, Up);
                            p.openInventory(ironMenu);
                            break;
                        case "nivel 4":
                            ironMenu = plugin.getServer().createInventory(null, 27, "§7Generador Diamante §2- Nivel 4");
                            Info = new ItemStack(Material.DIAMOND);
                            InfoM = Info.getItemMeta();
                            InfoM.setDisplayName("§2Generador de §7Diamante §2- Nivel 4");
                            InfoL = new ArrayList<>();
                            InfoL.add("§9Produccion: 4.0 segundos");
                            InfoM.setLore(InfoL);
                            Info.setItemMeta(InfoM);
                            Up = new ItemStack(Material.EXP_BOTTLE);
                            UpM = Up.getItemMeta();
                            UpM.setDisplayName("§2Nivel Máximo");
                            Up.setItemMeta(UpM);
                            clickedSign.put(p, loc);
                            ironMenu.setItem(11, Info);
                            ironMenu.setItem(15, Up);
                            p.openInventory(ironMenu);
                            break;
                    }
                    break;
            }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getClick().equals(ClickType.MIDDLE)) return;
        
        if (e.getCurrentItem() != null) {       
            if (e.getInventory().getTitle().contains("§7Generador Hierro")) {
                e.setCancelled(true);
                if (e.getCurrentItem() == null && e.getCurrentItem().getItemMeta() == null) {
                    return;
                }
                switch (e.getCurrentItem().getItemMeta().getDisplayName()) {
                    case "§2Mejorar a: §2Generador de §7Hierro §2- Nivel 2":
                        updateGen(3, p, Material.IRON_INGOT, 20);
                        break;
                    case "§2Mejorar a: §2Generador de §7Hierro §2- Nivel 3":
                        updateGen(3, p, Material.GOLD_INGOT, 20);
                        break;
                    case "§2Mejorar a: §2Generador de §7Hierro §2- Nivel 4":
                        updateGen(4, p, Material.GOLD_INGOT, 50);
                        break;
                }
            } else if (e.getInventory().getTitle().contains("§7Generador Oro")) {
                e.setCancelled(true);
                if (e.getCurrentItem() == null && e.getCurrentItem().getItemMeta() == null) {
                    return;
                }
                switch (e.getCurrentItem().getItemMeta().getDisplayName()) {
                    case "§2Mejorar a: §2Generador de §7Oro §2- Nivel 2":
                        updateGen(2, p, Material.GOLD_INGOT, 10);
                        break;
                    case "§2Mejorar a: §2Generador de §7Oro §2- Nivel 3":
                        updateGen(3, p, Material.GOLD_INGOT, 20);
                        break;
                    case "§2Mejorar a: §2Generador de §7Oro §2- Nivel 4":
                        updateGen(4, p, Material.DIAMOND, 20);
                        break;
                }
            } else if (e.getInventory().getTitle().contains("§7Generador Diamante")) {
                e.setCancelled(true);
                if (e.getCurrentItem() == null && e.getCurrentItem().getItemMeta() == null) {
                    return;
                }
                switch (e.getCurrentItem().getItemMeta().getDisplayName()) {
                    case "§2Mejorar a: §2Generador de §7Diamante §2- Nivel 1":
                        updateGen(1, p, Material.DIAMOND, 5);
                        break;
                    case "§2Mejorar a: §2Generador de §7Diamante §2- Nivel 2":
                        updateGen(2, p, Material.DIAMOND, 10);
                        break;
                    case "§2Mejorar a: §2Generador de §7Diamante §2- Nivel 3":
                        updateGen(3, p, Material.DIAMOND, 50);
                        break;
                    case "§2Mejorar a: §2Generador de §7Diamante §2- Nivel 4":
                        updateGen(4, p, Material.DIAMOND, 75);
                        break;
                }
            } else if (e.getInventory().getTitle().contains("§6Tienda TntWars")) {
                e.setCancelled(true);
                p.closeInventory();
                switch (e.getSlot()) {
                    case 0:
                        plugin.getAm().getBuildingShop().addCustomer(p);
                        break;
                    case 1:
                        plugin.getAm().getWeaponsShop().addCustomer(p);
                        break;
                    case 2:
                        plugin.getAm().getArmourShop().addCustomer(p);
                        break;
                    case 3:
                        plugin.getAm().getFoodShop().addCustomer(p);
                        break;
                    case 4:
                        plugin.getAm().getToolsShop().addCustomer(p);
                        break;
                    case 5:
                        plugin.getAm().getArcheryShop().addCustomer(p);
                        break;
                    case 6:
                        plugin.getAm().getMiscShop().addCustomer(p);
                        break;         
                }
            }
        }
    }
    
    private void updateGen(int level, Player p, Material moneda, int cantidad) {
        if (p.getInventory().contains(moneda, cantidad)) {
            p.getInventory().removeItem(new ItemStack[]{new ItemStack(moneda, cantidad)});
            p.getLocation().getWorld().playSound(p.getLocation(), Sound.ORB_PICKUP, 1, 1);
            p.closeInventory();
            
            final TntPlayer tp = TntWars.getPlayer(p);
            tp.getUserData().setGenUpgraded(tp.getUserData().getGenUpgraded() + 1);
            tp.save();
            Generador.getGenerador(clickedSign.get(p).clone().add(0, -1, 0)).setLevel(level);
            
            clickedSign.remove(p);
        } else {
            String txt = moneda == Material.DIAMOND ? "diamante" : (moneda == Material.GOLD_INGOT ? "oro" : "hierro");
            p.sendMessage("§c¡No tienes suficiente " + txt + "!");
        }
    }
}