package com.cadiducho.fem.lobby.listeners;

import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.core.api.FEMUser;
import com.cadiducho.fem.core.util.FEMFileLoader;
import com.cadiducho.fem.core.util.ItemUtil;
import com.cadiducho.fem.core.util.Metodos;
import com.cadiducho.fem.lobby.Lobby;
import com.cadiducho.fem.lobby.Lobby.FEMServerInfo;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class PlayerListener implements Listener, PluginMessageListener {

    private final Lobby plugin;

    public PlayerListener(Lobby instance) {
        plugin = instance;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        FEMUser u = FEMServer.getUser(e.getPlayer());

        e.setJoinMessage(Metodos.colorizar("&7" + e.getPlayer().getDisplayName() + " " + FEMFileLoader.getLang().getString("entrar")));
        plugin.getServer().getScheduler().runTask(plugin, () -> u.sendMessage("*motd", u.getName(), plugin.getServer().getOnlinePlayers().size()));
        
        e.getPlayer().setHealth(e.getPlayer().getMaxHealth());
        e.getPlayer().setFoodLevel(20);
        e.getPlayer().getInventory().clear();
        e.getPlayer().getInventory().setItem(0, ItemUtil.createItem(Material.COMPASS, "Viajar", "Desplazate entre los juegos del servidor"));
        //e.getPlayer().getInventory().setItem(1, ItemUtil.createItem(Material.EMPTY_MAP, "Lobbies", "Cambia a otro lobbie"));
        e.getPlayer().getInventory().setItem(8, ItemUtil.createItem(Material.DIAMOND, "Ajustes", "Cambia alguno de tus ajustes de usuario"));
        
        ItemStack guia = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) guia.getItemMeta();
        meta.setDisplayName(Metodos.colorizar("&bGuía de inicio"));
        meta.setAuthor(Metodos.colorizar("&6Staff"));
        meta.addPage(Metodos.colorizar("Bueno, aquí hay que poner cosas interesantes"));
        meta.addPage(Metodos.colorizar("y otras menos interesantes"));
        guia.setItemMeta(meta);
        e.getPlayer().getInventory().setItem(1, guia);
        
        u.tryHidePlayers();
        plugin.getServer().getOnlinePlayers().forEach(p -> FEMServer.getUser(p).tryHidePlayers());
    }
    
    @EventHandler
    public void onPlayerGetDrop(PlayerPickupItemEvent e) {
        FEMUser u = FEMServer.getUser(e.getPlayer());
        ItemStack is = e.getItem().getItemStack();
        if (is.getType().equals(Material.YELLOW_FLOWER) && is.getItemMeta().getDisplayName().equals("Coin")) {
            e.setCancelled(true);
            e.getItem().remove();
            u.getUserData().setCoins(u.getUserData().getCoins() + 1);
            u.save();
            u.sendMessage("Has obtenido un punto del suelo");
        }
    }
    
    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        if (e.getClickedBlock().getType().equals(Material.TRAP_DOOR) || e.getClickedBlock().getType().equals(Material.IRON_TRAPDOOR)) {
            e.setCancelled(true);
        }
        
        //Menu
        if (e.getItem() != null) {
            if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                Inventory inv = null;
                FEMUser u = FEMServer.getUser(e.getPlayer());
                switch (e.getItem().getType()) {
                    case COMPASS:
                        inv = plugin.getServer().createInventory(e.getPlayer(), 36, "Viajar");
                        String amistades = u.getUserData().getFriendRequest() ? "Aceptas" : "No aceptas";
                        String otros = u.getUserData().getHideMode() == 0 ? "Nadie" : (u.getUserData().getHideMode() == 1 ? "Amigos" : "Todos");
                        inv.setItem(31, ItemUtil.createHeadPlayer("Información", Arrays.asList("Pulsa para ver estadísticas", 
                                "Amistades: " + amistades, 
                                "Ver a: " + otros)));
                        inv.setItem(27, ItemUtil.createItem(Material.BEACON, "Lobby"));
                        inv.setItem(35, ItemUtil.createItem(Material.BONE, "Cerrar menú"));

                        inv.setItem(4, ItemUtil.createItem(Material.TNT, "TntWars"));
                        inv.setItem(12, ItemUtil.createItem(Material.GOLD_SWORD, "Battle Royale", "&cEn Mantenimiento"));
                        inv.setItem(13, ItemUtil.createItem(Material.WOOL, "Dye or Die"));
                        inv.setItem(14, ItemUtil.createItem(Material.DIAMOND_AXE, "Lucky Gladiators", "&cEn Mantenimiento"));
                        inv.setItem(22, ItemUtil.createItem(Material.EMERALD_BLOCK, "Gem Hunters"));
                        e.setCancelled(true);
                        break;
                    case DIAMOND:
                        inv = plugin.getServer().createInventory(e.getPlayer(), 18, "Ajustes del jugador");
                        String lore1 = u.getUserData().getFriendRequest() ? "Aceptas amistades" : "No aceptas amistades";
                        inv.setItem(2, ItemUtil.createItem(Material.CHORUS_FRUIT, "Aceptar amistades", lore1));
                        DyeColor glassColor = u.getUserData().getFriendRequest() ? DyeColor.LIME : DyeColor.RED;
                        inv.setItem(11, ItemUtil.createGlass("Aceptar amistades", lore1, glassColor));

                        inv.setItem(6, ItemUtil.createItem(Material.BANNER, "Ver a otros jugadores", "Escoge si ver a tus amigos, a todos o a nadie"));
                        inv.setItem(14, ItemUtil.createWool("No ver a nadie", DyeColor.RED));
                        inv.setItem(15, ItemUtil.createWool("Ver solo a tus amigos", DyeColor.PURPLE));
                        inv.setItem(16, ItemUtil.createWool("Ver a todos los usuarios", DyeColor.LIME));
                        e.setCancelled(true);
                        break;
                    case EMPTY_MAP:
                        inv = plugin.getServer().createInventory(e.getPlayer(), 18, "Lobbies");
                        int i = 0;
                        if (!plugin.getServers().isEmpty()) {
                            for (FEMServerInfo server : plugin.getServers()) {
                                if (server.getName().contains("lobby")) {
                                    Material mat = server.getUsers().contains(e.getPlayer().getUniqueId().toString()) ? Material.DIAMOND : Material.BEACON;
                                    inv.setItem(i, ItemUtil.createItem(mat, server.getName(), server.getPlayers() + "/200"));
                                    i++;
                                }
                            }
                        }
                        e.setCancelled(true);
                        break;
                    default: break;
                }
                e.getPlayer().openInventory(inv);
            }
        }
    }
    
    @EventHandler
    public void inventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        FEMUser u = FEMServer.getUser(p);
        switch (e.getInventory().getTitle()) {
            case "Ajustes del jugador":
                e.setCancelled(true);
                switch (e.getSlot()) {
                    case 2:
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
                }   u.sendMessage("Ajuste cambiado");
                break;
            case "Lobbies":
                e.setCancelled(true);
                switch (e.getCurrentItem().getType()) {
                    case DIAMOND:
                        u.sendMessage("Ya estás en ese lobby");
                        break;
                    case BEACON:
                        String name = e.getCurrentItem().getItemMeta().getDisplayName();
                        ByteArrayDataOutput out = ByteStreams.newDataOutput();
                        out.writeUTF("Connect");
                        out.writeUTF(name);
                        p.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
                        break;
                }   p.closeInventory();
                break;
            case "Viajar":
                e.setCancelled(true);
                switch (e.getSlot()) {
                    case 31:
                        Inventory inv = plugin.getServer().createInventory(p, 9, "Estadisticas del jugador");
                        inv.setItem(3, ItemUtil.createItem(Material.TNT, "TntWars", 
                                Arrays.asList("Partidas Jugadas: " + u.getUserData().getPlays().get(1), "Partidas Ganadas: " + u.getUserData().getWins().get(1),
                                        "Bajas: " + u.getUserData().getKills().get(1), "Muertes: " + u.getUserData().getDeaths().get(1),
                                        "TNT Activadas: " + u.getUserData().getTntPuestas(), "TNT Desactivadas: " + u.getUserData().getTntQuitadas(), 
                                        "TNT Explotadas: " + u.getUserData().getTntExplotadas(), "Generadores mejorados: " + u.getUserData().getGenUpgraded())));
                        inv.setItem(4, ItemUtil.createItem(Material.WOOL, "Dye or Die", Arrays.asList("Partidas Jugadas: " + u.getUserData().getPlays().get(2), "Partidas Ganadas: " + u.getUserData().getWins().get(2),
                                        "Rondas superadas: " + u.getUserData().getRondas_dod(), "Récord de ronda: " + u.getUserData().getRecord_dod())));
                        inv.setItem(5, ItemUtil.createItem(Material.EMERALD_BLOCK, "Gem Hunter", 
                                Arrays.asList("Partidas Jugadas: " + u.getUserData().getPlays().get(3), "Partidas Ganadas: " + u.getUserData().getWins().get(3),
                                        "Bajas: " + u.getUserData().getKills().get(3), "Muertes: " + u.getUserData().getDeaths().get(3),
                                        "Gemas escondidas: " + u.getUserData().getGemPlanted(), "Gemas destruidas: " + u.getUserData().getGemDestroyed())));
                        p.closeInventory();
                        plugin.getServer().getScheduler().runTask(plugin, () -> p.openInventory(inv));
                        break;
                    case 4:
                        u.sendToServer("tntwars");
                        p.closeInventory();
                        break;
                    case 13:
                        u.sendToServer("dyeordie");
                        p.closeInventory();
                        break;
                    case 22:
                        u.sendToServer("gemhunters");
                        p.closeInventory();
                        break;
                    case 27:
                        u.sendToServer("lobby");
                        p.closeInventory();
                        break;
                    case 35:
                        p.closeInventory();
                        break;
                }
                break;
            case "Estadisticas del jugador":
                e.setCancelled(true);
                break;
            default:
                break;
        }
    }
    
    @Override
    public void onPluginMessageReceived(String string, Player player, byte[] bytes) {
        ByteArrayDataInput in = ByteStreams.newDataInput(bytes);
        String subchannel = in.readUTF();
        
        if (subchannel.equalsIgnoreCase("serversinfo")) {
            Type listType = new TypeToken<List<FEMServerInfo>>() {}.getType();
            String json = in.readUTF();
            plugin.setServers(new Gson().fromJson(json, listType));
        }
    }
    
    //Eventos a cancelar en el lobby
    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerBreak(BlockBreakEvent e) {
        e.setCancelled(true);
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerDrop(PlayerDropItemEvent e) {
        e.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = false, priority = EventPriority.HIGHEST)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent e) {
        e.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onBlockDamage(BlockDamageEvent e) {
        e.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerPickupItem(PlayerPickupItemEvent e) {
        e.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onEntityTarget(EntityTargetEvent e) {
        e.setCancelled(true);
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onFoodLevelChange(FoodLevelChangeEvent e) {
        e.setCancelled(true);
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(PlayerDeathEvent e) {
        e.setDeathMessage("");
    }
}
