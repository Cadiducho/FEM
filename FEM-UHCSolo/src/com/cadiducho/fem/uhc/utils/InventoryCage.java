package com.cadiducho.fem.uhc.utils;

import com.cadiducho.fem.uhc.Main;
import com.cadiducho.fem.uhc.manager.GroundManager;
import java.util.Arrays;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InventoryCage {

    public Main plugin;

    public InventoryCage(Main plugin) {
        this.plugin = plugin;
    }

    public void openCage(Player player) {
        Inventory cage;
        cage = plugin.getServer().createInventory(null, 9, "§lTipo de spawn");

        ItemStack VERDE = new ItemStack(Material.STAINED_GLASS, 1, (byte) 5);
        ItemMeta VERDEM = VERDE.getItemMeta();
        VERDEM.setDisplayName("§aVerde");
        if (player.hasPermission("DonkeyUHC.Cage.Verde")) {
            VERDEM.setLore(Arrays.asList("§7Click para usar"));
        } else {
            VERDEM.setLore(Arrays.asList("§cNecesitas ser §a§lVIP"));
        }
        VERDE.setItemMeta(VERDEM);
        cage.setItem(0, VERDE);

        ItemStack MORADO = new ItemStack(Material.STAINED_GLASS, 1, (byte) 10);
        ItemMeta MORADOM = MORADO.getItemMeta();
        MORADOM.setDisplayName("§dMorado");
        if (player.hasPermission("DonkeyUHC.Cage.Morado")) {
            MORADOM.setLore(Arrays.asList("§7Click para usar"));
        } else {
            MORADOM.setLore(Arrays.asList("§cNecesitas ser §a§lVIP"));
        }
        MORADO.setItemMeta(MORADOM);
        cage.setItem(1, MORADO);

        ItemStack NARANJA = new ItemStack(Material.STAINED_GLASS, 1, (byte) 1);
        ItemMeta NARANJAM = NARANJA.getItemMeta();
        NARANJAM.setDisplayName("§6Naraja");
        if (player.hasPermission("DonkeyUHC.Cage.Naranja")) {
            NARANJAM.setLore(Arrays.asList("§7Click para usar"));
        } else {
            NARANJAM.setLore(Arrays.asList("§cNecesitas ser §a§lVIP"));
        }
        NARANJA.setItemMeta(NARANJAM);
        cage.setItem(2, NARANJA);

        ItemStack AZUL = new ItemStack(Material.STAINED_GLASS, 1, (byte) 11);
        ItemMeta AZULM = AZUL.getItemMeta();
        AZULM.setDisplayName("§9Azul");
        if (player.hasPermission("DonkeyUHC.Cage.Azul")) {
            AZULM.setLore(Arrays.asList("§7Click para usar"));
        } else {
            AZULM.setLore(Arrays.asList("§cNecesitas ser §a§lVIP§6§l+"));
        }
        AZUL.setItemMeta(AZULM);
        cage.setItem(3, AZUL);

        ItemStack ROJO = new ItemStack(Material.STAINED_GLASS, 1, (byte) 14);
        ItemMeta ROJOM = ROJO.getItemMeta();
        ROJOM.setDisplayName("§cRojo");
        if (player.hasPermission("DonkeyUHC.Cage.Rojo")) {
            ROJOM.setLore(Arrays.asList("§7Click para usar"));
        } else {
            ROJOM.setLore(Arrays.asList("§cNecesitas ser §a§lVIP§6§l+"));
        }
        ROJO.setItemMeta(ROJOM);
        cage.setItem(4, ROJO);

        ItemStack RUSTICO = new ItemStack(Material.WOOD);
        ItemMeta RUSTICOM = RUSTICO.getItemMeta();
        RUSTICOM.setDisplayName("§7Rústico");
        if (player.hasPermission("DonkeyUHC.Cage.Rustico")) {
            RUSTICOM.setLore(Arrays.asList("§7Click para usar"));
        } else {
            RUSTICOM.setLore(Arrays.asList("§cNecesitas ser §a§lVIP§6§l+"));
        }
        RUSTICO.setItemMeta(RUSTICOM);
        cage.setItem(5, RUSTICO);

        ItemStack DIAMND = new ItemStack(Material.DIAMOND_BLOCK);
        ItemMeta DIAMNDM = DIAMND.getItemMeta();
        DIAMNDM.setDisplayName("§bDiamante");
        if (player.hasPermission("DonkeyUHC.Cage.Diamond")) {
            DIAMNDM.setLore(Arrays.asList("§7Click para usar"));
        } else {
            DIAMNDM.setLore(Arrays.asList("§cNecesitas ser §3§lSPONSOR"));
        }
        DIAMND.setItemMeta(DIAMNDM);
        cage.setItem(6, DIAMND);

        ItemStack GRANJA = new ItemStack(Material.FENCE);
        ItemMeta GRANJAM = GRANJA.getItemMeta();
        GRANJAM.setDisplayName("§3Granja");
        if (player.hasPermission("DonkeyUHC.Cage.Granja")) {
            GRANJAM.setLore(Arrays.asList("§7Click para usar"));
        } else {
            GRANJAM.setLore(Arrays.asList("§cNecesitas ser §3§lSPONSOR"));
        }
        GRANJA.setItemMeta(GRANJAM);
        cage.setItem(7, GRANJA);

        ItemStack carcel = new ItemStack(Material.IRON_BARDING);
        ItemMeta carcelm = carcel.getItemMeta();
        carcelm.setDisplayName("§fCárcel");
        if (player.hasPermission("DonkeyUHC.Cage.Carcel")) {
            carcelm.setLore(Arrays.asList("§7Click para usar"));
        } else {
            carcelm.setLore(Arrays.asList("§cNecesitas ser §3§lSPONSOR"));
        }
        carcel.setItemMeta(carcelm);
        cage.setItem(8, carcel);

        player.openInventory(cage);
    }

    public void onClickCage(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getInventory().getTitle().equalsIgnoreCase("§lTipo de spawn")) {
            if (event.getCurrentItem() != null && event.getCurrentItem().getType() != null) {
                event.setCancelled(true);
                if (event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().hasDisplayName()) {
                    event.setCancelled(true);
                    ItemStack clicked = event.getCurrentItem();
                    if (clicked != null) {
                        if (event.getSlot() == 0) {
                            if (player.hasPermission("DonkeyUHC.Cage.Verde")) {
                                plugin.grm.setVIPGround(player.getLocation(), GroundManager.cageType.VERDE);
                                player.closeInventory();
                                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1F, 1F);
                            } else {
                                plugin.msg.sendMessage(player, "&cNecesitas ser &a&lVIP &cpara usar esta caja.");
                            }
                        }
                        if (event.getSlot() == 1) {
                            if (player.hasPermission("DonkeyUHC.Cage.Morado")) {
                                plugin.grm.setVIPGround(player.getLocation(), GroundManager.cageType.MORADO);
                                player.closeInventory();
                                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1F, 1F);
                            } else {
                                plugin.msg.sendMessage(player, "&cNecesitas ser &a&lVIP &cpara usar esta caja.");
                            }
                        }
                        if (event.getSlot() == 2) {
                            if (player.hasPermission("DonkeyUHC.Cage.Naranja")) {
                                plugin.grm.setVIPGround(player.getLocation(), GroundManager.cageType.NARANJO);
                                player.closeInventory();
                                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1F, 1F);
                            } else {
                                plugin.msg.sendMessage(player, "&cNecesitas ser &a&lVIP &cpara usar esta caja.");
                            }
                        }
                        if (event.getSlot() == 3) {
                            if (player.hasPermission("DonkeyUHC.Cage.Azul")) {
                                plugin.grm.setVIPGround(player.getLocation(), GroundManager.cageType.AZUL);
                                player.closeInventory();
                                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1F, 1F);
                            } else {
                                plugin.msg.sendMessage(player, "&cNecesitas ser &a&lVIP&6&l+ &cpara usar esta caja.");
                            }
                        }
                        if (event.getSlot() == 4) {
                            if (player.hasPermission("DonkeyUHC.Cage.Rojo")) {
                                plugin.grm.setVIPGround(player.getLocation(), GroundManager.cageType.ROJO);
                                player.closeInventory();
                                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1F, 1F);
                            } else {
                                plugin.msg.sendMessage(player, "&cNecesitas ser &a&lVIP&6&l+ &cpara usar esta caja.");
                            }
                        }
                        if (event.getSlot() == 5) {
                            if (player.hasPermission("DonkeyUHC.Cage.Rustico")) {
                                plugin.grm.setVIPGround(player.getLocation(), GroundManager.cageType.RUSTICO);
                                player.closeInventory();
                                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1F, 1F);
                            } else {
                                plugin.msg.sendMessage(player, "&cNecesitas ser &a&lVIP&6&l+ &cpara usar esta caja.");
                            }
                        }
                        if (event.getSlot() == 6) {
                            if (player.hasPermission("DonkeyUHC.Cage.Diamond")) {
                                plugin.grm.setVIPGround(player.getLocation(), GroundManager.cageType.DIAMANTE);
                                player.closeInventory();
                                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1F, 1F);
                            } else {
                                plugin.msg.sendMessage(player, "&cNecesitas ser &3&lSPONSOR &cpara usar esta caja.");
                            }
                        }
                        if (event.getSlot() == 7) {
                            if (player.hasPermission("DonkeyUHC.Cage.Granja")) {
                                plugin.grm.setVIPGround(player.getLocation(), GroundManager.cageType.GRANJA);
                                player.closeInventory();
                                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1F, 1F);
                            } else {
                                plugin.msg.sendMessage(player, "&cNecesitas ser &3&lSPONSOR &cpara usar esta caja.");
                            }
                        }
                        if (event.getSlot() == 8) {
                            if (player.hasPermission("DonkeyUHC.Cage.Carcel")) {
                                plugin.grm.setVIPGround(player.getLocation(), GroundManager.cageType.CARCEL);
                                player.closeInventory();
                                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1F, 1F);
                            } else {
                                plugin.msg.sendMessage(player, "&cNecesitas ser &3&lSPONSOR &cpara usar esta caja.");
                            }
                        }
                    }
                }
            }
        }
    }

}
