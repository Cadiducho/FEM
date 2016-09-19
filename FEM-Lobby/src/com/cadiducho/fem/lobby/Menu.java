package com.cadiducho.fem.lobby;

import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.core.api.FEMUser;
import com.cadiducho.fem.core.cmds.FEMCmd;
import com.cadiducho.fem.core.util.ItemUtil;
import com.cadiducho.fem.core.util.Metodos;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import java.util.Arrays;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class Menu {

    private final Lobby plugin;

    public Menu(Lobby plugin) {
        this.plugin = plugin;
    }

    public void openNavegatorMenu(Player player) {
        FEMUser u = FEMServer.getUser(player);
        Inventory inv = plugin.getServer().createInventory(null, 27, "Viajar");
        String amistades = u.getUserData().getFriendRequest() ? "Aceptas" : "No aceptas";
        String otros = u.getUserData().getHideMode() == 0 ? "Nadie" : (u.getUserData().getHideMode() == 1 ? "Amigos" : "Todos");
        inv.setItem(26, ItemUtil.createHeadPlayer("Información", player.getName(), Arrays.asList("Pulsa para ver estadísticas",
                "Amistades: " + amistades,
                "Ver a: " + otros)));
        inv.setItem(18, ItemUtil.createItem(Material.BEACON, "Lobbies"));
        inv.setItem(22, ItemUtil.createItem(Material.DOUBLE_PLANT, "Dinero", "(Proximamente)"));

        inv.setItem(3, ItemUtil.createItem(Material.PAINTING, "&e&lPICTOGRAMA"));
        inv.setItem(4, ItemUtil.createItem(Material.TNT, "&1&lTNT WARS"));
        ItemStack letherBoots = ItemUtil.createItem(Material.LEATHER_BOOTS, "&5&lDYE OR DIE");
        LeatherArmorMeta lam = (LeatherArmorMeta) letherBoots.getItemMeta();
        lam.setColor(Color.BLUE);
        letherBoots.setItemMeta(lam);
        inv.setItem(5, letherBoots);
        inv.setItem(12, ItemUtil.createItem(Material.SKULL_ITEM, "&4&lGLADIATOR"));
        inv.setItem(13, ItemUtil.createItem(Material.EMERALD, "&a&lGEM HUNTERS"));
        inv.setItem(14, ItemUtil.createItem(Material.GOLD_SWORD, "&6&lBATTLE ROYALE"));
    }

    public void openSettings(Player player) {
        FEMUser u = FEMServer.getUser(player);
        Inventory inv = plugin.getServer().createInventory(player, 18, "Ajustes del jugador");
        String lore1 = u.getUserData().getFriendRequest() ? "Aceptas amistades" : "No aceptas amistades";
        inv.setItem(2, ItemUtil.createItem(Material.CHORUS_FRUIT, "Aceptar amistades", lore1));
        DyeColor glassColor = u.getUserData().getFriendRequest() ? DyeColor.LIME : DyeColor.RED;
        inv.setItem(11, ItemUtil.createGlass("Aceptar amistades", lore1, glassColor));
        DyeColor bannerColor = (u.getUserData().getHideMode() == 0 ? DyeColor.RED : (u.getUserData().getHideMode() == 1 ? DyeColor.PURPLE : DyeColor.LIME));
        inv.setItem(6, ItemUtil.createBanner("Ver a otros jugadores", "Escoge si ver a tus amigos, a todos o a nadie", bannerColor));
        inv.setItem(14, ItemUtil.createWool("No ver a nadie", DyeColor.RED));
        inv.setItem(15, ItemUtil.createWool("Ver solo a tus amigos", DyeColor.PURPLE));
        inv.setItem(16, ItemUtil.createWool("Ver a todos los usuarios", DyeColor.LIME));
    }

    public void openStats(Player p) {
        FEMUser u = new FEMUser(p);
        Inventory inv = plugin.getServer().createInventory(p, 18, "Estadisticas del jugador");
        inv.setItem(3, ItemUtil.createItem(Material.PAINTING, "&e&lPICTOGRAMA",
                Arrays.asList("&fPartidas Jugadas: &l" + u.getUserData().getPlays().get(4),
                        "&fPartidas Ganadas: &l" + u.getUserData().getWins().get(4),
                        "&e---{*}---",
                        "&fPalabras acertadas: &l" + u.getUserData().getPicAcertadas(),
                        "&fPalabras bien dibujadas: &l" + u.getUserData().getPicDibujadas())));
        inv.setItem(4, ItemUtil.createItem(Material.TNT, "&1&lTNT WARS",
                Arrays.asList("&fPartidas Jugadas: &l" + u.getUserData().getPlays().get(1),
                        "&fPartidas Ganadas: &l" + u.getUserData().getWins().get(1),
                        "&fBajas: &l" + u.getUserData().getKills().get(1), "&fMuertes: &l" + u.getUserData().getDeaths().get(1),
                        "&1---{*}---",
                        "&fTNT Activadas: &l" + u.getUserData().getTntPuestas(), "&fTNT Desactivadas: &l" + u.getUserData().getTntQuitadas(),
                        "&fTNT Explotadas: &f" + u.getUserData().getTntExplotadas(), "&fGeneradores mejorados: &l" + u.getUserData().getGenUpgraded())));
        ItemStack letherBoots = ItemUtil.createItem(Material.LEATHER_BOOTS, "&5&lDYE OR DIE", Arrays.asList("&fPartidas Jugadas: &l" + u.getUserData().getPlays().get(1),
                "&fPartidas Ganadas: &l" + u.getUserData().getWins().get(1),
                "&5---{*}---",
                "&fRecord de ronda: &l" + u.getUserData().getRecord_dod(),
                "&fRondas jugadas: &l" + u.getUserData().getRondas_dod()));
        LeatherArmorMeta lam = (LeatherArmorMeta) letherBoots.getItemMeta();
        lam.setColor(Color.BLUE);
        letherBoots.setItemMeta(lam);
        inv.setItem(5, letherBoots);
        inv.setItem(12, ItemUtil.createItem(Material.SKULL_ITEM, "&4&lGLADIATOR", Arrays.asList("&fPartidas Jugadas: &l" + u.getUserData().getPlays().get(6),
                "&fPartidas Ganadas: &l" + u.getUserData().getWins().get(6),
                "&fBajas: &l" + u.getUserData().getKills().get(6), "&fMuertes: &l" + u.getUserData().getDeaths().get(6),
                "&4---{*}---",
                "&fLuckies rotos: &l" + u.getUserData().getLuckyRotos())));
        inv.setItem(13, ItemUtil.createItem(Material.EMERALD, "&a&lGEM HUNTERS",
                Arrays.asList("&fPartidas Jugadas: &l" + u.getUserData().getPlays().get(3),
                        "&fPartidas Ganadas: &l" + u.getUserData().getWins().get(3),
                        "&fBajas: &l" + u.getUserData().getKills().get(3), "&fMuertes: &l" + u.getUserData().getDeaths().get(3),
                        "&a---{*}---",
                        "&fGemas escondidas: &l" + u.getUserData().getGemPlanted(), "&fGemas destruidas: &l" + u.getUserData().getGemDestroyed())));
        inv.setItem(14, ItemUtil.createItem(Material.GOLD_SWORD, "&6&lBATTLE ROYALE", Arrays.asList("&fPartidas Jugadas: &l" + u.getUserData().getPlays().get(5),
                "&fPartidas Ganadas: &l" + u.getUserData().getWins().get(5),
                "&fBajas: &l" + u.getUserData().getKills().get(5), "&fMuertes: &l" + u.getUserData().getDeaths().get(5),
                "&6---{*}---",
                "&fIntercambios realizados: &l" + u.getUserData().getBrIntercambios())));
        p.closeInventory();
        plugin.getServer().getScheduler().runTask(plugin, () -> p.openInventory(inv));
    }

    public void checkClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) {
            return;
        }

        Player p = (Player) e.getWhoClicked();
        FEMUser u = FEMServer.getUser(p);
        switch (e.getInventory().getTitle()) {
            case "Ajustes del jugador":
                e.setCancelled(true);
                switch (e.getSlot()) {
                    case 11:
                        u.getUserData().setFriendRequest(!u.getUserData().getFriendRequest());
                        u.save();
                        p.closeInventory();
                        break;
                    case 14:
                        u.getUserData().setHideMode(0);
                        u.save();
                        u.tryHidePlayers();
                        p.closeInventory();
                        break;
                    case 15:
                        u.getUserData().setHideMode(1);
                        u.save();
                        u.tryHidePlayers();
                        p.closeInventory();
                        break;
                    case 16:
                        u.getUserData().setHideMode(2);
                        u.save();
                        u.tryHidePlayers();
                        p.closeInventory();
                        break;
                    default:
                        return;
                }
                u.sendMessage("Ajuste cambiado");
                break;
            case "Lobbies":
                e.setCancelled(true);
                switch (e.getCurrentItem().getType()) {
                    case BEACON:
                        u.sendMessage("Ya estás en ese lobby");
                        break;
                    case GLASS:
                        String name = e.getCurrentItem().getItemMeta().getDisplayName();
                        ByteArrayDataOutput out = ByteStreams.newDataOutput();
                        out.writeUTF("Connect");
                        out.writeUTF(name.toLowerCase().replace(" ", ""));
                        p.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
                        break;
                    default:
                        break;
                }
                p.closeInventory();
                break;
            case "Viajar":
                e.setCancelled(true);
                switch (e.getSlot()) {
                    case 3: //pictograma
                        p.teleport(Metodos.stringToLocation(plugin.getConfig().getString("brujula.pic")));
                        p.closeInventory();
                        break;
                    case 4: //tnt
                        p.teleport(Metodos.stringToLocation(plugin.getConfig().getString("brujula.tnt")));
                        p.closeInventory();
                        break;
                    case 5: //dieordye
                        p.teleport(Metodos.stringToLocation(plugin.getConfig().getString("brujula.dod")));
                        p.closeInventory();
                        break;
                    case 12: //lucky
                        p.teleport(Metodos.stringToLocation(plugin.getConfig().getString("brujula.lg")));
                        p.closeInventory();
                        break;
                    case 13: //gem
                        p.teleport(Metodos.stringToLocation(plugin.getConfig().getString("brujula.gem")));
                        p.closeInventory();
                        break;
                    case 14: //royale
                        p.teleport(Metodos.stringToLocation(plugin.getConfig().getString("brujula.br")));
                        p.closeInventory();
                        break;
                    case 18: //menu lobbies
                        Inventory invLobbies = plugin.getServer().createInventory(p, 18, "Lobbies");
                        int i = 0;
                        if (!plugin.getServers().isEmpty()) {
                            for (Lobby.FEMServerInfo server : plugin.getServers()) {
                                if (server.getName().contains("lobby")) {
                                    Material mat = server.getUsers().contains(p.getUniqueId().toString()) ? Material.BEACON : Material.GLASS;
                                    invLobbies.setItem(i, ItemUtil.createItem(mat, normalize(server.getName()), server.getPlayers() + "/200"));
                                    i++;
                                }
                            }
                        }
                        p.closeInventory();
                        p.openInventory(invLobbies);
                        break;
                    case 22: //puntos
                        p.closeInventory();
                        break;
                    case 26:
                        openStats(p);
                        break;
                }
                break;
            default:
                break;
        }

        if (u.isOnRank(FEMCmd.Grupo.Moderador)) { //Staff poder usar inventarios
            e.setCancelled(false);
            return;
        }
        e.setCancelled(true); //Prevenir que muevan / oculten / tiren objetos de la interfaz del Lobby
    }

    String normalize(String str) {
        String[] a = str.split("y");
        String i = a[1];
        return a[0].replace('l', 'L') + "y " + i;
    }

}
