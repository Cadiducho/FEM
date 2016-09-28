package com.cadiducho.fem.lobby.listeners;

import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.core.api.FEMUser;
import com.cadiducho.fem.core.cmds.FEMCmd;
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
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
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
        e.getPlayer().getInventory().setItem(0, ItemUtil.createItem(Material.COMPASS, "&lJuegos", "Desplazate entre los juegos del servidor"));
        e.getPlayer().getInventory().setItem(8, ItemUtil.createItem(Material.COMMAND, "&lAjustes", "Cambia alguno de tus ajustes de usuario"));

        e.getPlayer().getInventory().setItem(4, plugin.getLibro());
        e.getPlayer().teleport(e.getPlayer().getWorld().getSpawnLocation());
        
        u.tryHidePlayers();
        plugin.getServer().getOnlinePlayers().forEach(p -> FEMServer.getUser(p).tryHidePlayers());
    }
    
    @EventHandler
    public void onPlayerGetDrop(PlayerPickupItemEvent e) {
        FEMUser u = FEMServer.getUser(e.getPlayer());
        ItemStack is = e.getItem().getItemStack();
        if (is.getType().equals(Material.DOUBLE_PLANT) && is.getItemMeta().getDisplayName().equals("Coin")) {
            e.setCancelled(true);
            e.getItem().remove();
            u.getUserData().setCoins(u.getUserData().getCoins() + 1);
            u.save();
            u.sendMessage("Has obtenido un punto del suelo");
        }
    }
    
    @EventHandler
    public void onPlayerInteractVillager(PlayerInteractEntityEvent e) {
        if (e.getRightClicked() instanceof Villager) {
            e.setCancelled(false);
        }
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        //No abrir trampillas
        if (e.getClickedBlock() != null) {
            if (e.getClickedBlock().getType().equals(Material.TRAP_DOOR) || e.getClickedBlock().getType().equals(Material.IRON_TRAPDOOR) || e.getClickedBlock().getType().equals(Material.FENCE_GATE)) {
                e.setCancelled(true);
            }
        }
        
        //No destruir tirras de cultivo (soil)
        if (e.getAction() == Action.PHYSICAL && e.getClickedBlock().getType() == Material.SOIL) {
            e.setCancelled(true);
        }

        //Menu
        if (e.getItem() != null) {
            if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                Inventory inv = null;
                FEMUser u = FEMServer.getUser(e.getPlayer());
                switch (e.getItem().getType()) {
                    case COMPASS:
                        inv = plugin.getServer().createInventory(e.getPlayer(), 27, "Viajar");
                        String amistades = u.getUserData().getFriendRequest() ? "Aceptas" : "No aceptas";
                        String otros = u.getUserData().getHideMode() == 0 ? "Nadie" : (u.getUserData().getHideMode() == 1 ? "Amigos" : "Todos");
                        inv.setItem(26, ItemUtil.createHeadPlayer("Información", e.getPlayer().getName(), Arrays.asList("Pulsa para ver estadísticas", 
                                "Amistades: " + amistades, 
                                "Ver a: " + otros)));
                        inv.setItem(18, ItemUtil.createItem(Material.BEACON, "Lobbies"));
                        inv.setItem(22, ItemUtil.createItem(Material.DOUBLE_PLANT, "Dinero", "(Proximamente)"));
                        
                        inv.setItem(3, ItemUtil.createItem(Material.PAINTING, "&e&lPICTOGRAMA"));
                        inv.setItem(4, ItemUtil.createItem(Material.TNT, "&1&lTNT WARS"));
                        ItemStack letherBoots = ItemUtil.createItem(Material.LEATHER_BOOTS, "&5&lDYE OR DIE");
                            LeatherArmorMeta lam = (LeatherArmorMeta) letherBoots.getItemMeta();
                            lam.setColor(Color.BLUE);
                            lam.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_ATTRIBUTES, 
                                ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_UNBREAKABLE);
                            letherBoots.setItemMeta(lam);
                        inv.setItem(5, letherBoots);
                        inv.setItem(12, ItemUtil.createItem(Material.SKULL_ITEM, "&4&lLUCKY WARRIOR"));
                        inv.setItem(13, ItemUtil.createItem(Material.EMERALD, "&a&lGEM HUNTERS"));
                        inv.setItem(14, ItemUtil.createItem(Material.GOLD_SWORD, "&6&lBATTLE ROYALE"));
                        e.setCancelled(true);
                        break;
                    case COMMAND:
                        inv = plugin.getServer().createInventory(e.getPlayer(), 18, "Ajustes del jugador");
                        String lore1 = u.getUserData().getFriendRequest() ? "Aceptas amistades" : "No aceptas amistades";
                        inv.setItem(2, ItemUtil.createItem(Material.CHORUS_FRUIT, "Aceptar amistades", lore1));
                        DyeColor glassColor = u.getUserData().getFriendRequest() ? DyeColor.LIME : DyeColor.RED;
                        inv.setItem(11, ItemUtil.createGlass("Aceptar amistades", lore1, glassColor));

                        DyeColor bannerColor = (u.getUserData().getHideMode() == 0 ? DyeColor.RED : (u.getUserData().getHideMode() == 1 ? DyeColor.PURPLE : DyeColor.LIME));
                        inv.setItem(6, ItemUtil.createBanner("Ver a otros jugadores", "Escoge si ver a tus amigos, a todos o a nadie", bannerColor));
                        inv.setItem(14, ItemUtil.createWool("No ver a nadie", DyeColor.RED));
                        inv.setItem(15, ItemUtil.createWool("Ver solo a tus amigos", DyeColor.PURPLE));
                        inv.setItem(16, ItemUtil.createWool("Ver a todos los usuarios", DyeColor.LIME));
                        e.setCancelled(true);
                        break;
                    default: 
                        e.setCancelled(false);
                        return;
                }
                if (inv != null) {
                    e.getPlayer().openInventory(inv);
                }
            }
        }
    }
    
    @EventHandler
    public void inventoryClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;

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
                }   p.closeInventory();
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
                            for (FEMServerInfo server : plugin.getServers()) {
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
                        break;
                }
                break;
            default:
                break;
        }
        
        if (u.isOnRank(FEMCmd.Grupo.Admin)) { //Staff poder usar inventarios
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
    
     @EventHandler(priority = EventPriority.NORMAL)
    public void onInventoryMoveItemEvent(InventoryMoveItemEvent e) {
        e.setCancelled(true);
    }
    
    //Eventos a cancelar en el lobby
    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerBreak(BlockBreakEvent e) {
        if (!FEMServer.getUser(e.getPlayer()).isOnRank(FEMCmd.Grupo.Owner)) {
            e.setCancelled(true);
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerPlace(BlockPlaceEvent e) {
        if (!FEMServer.getUser(e.getPlayer()).isOnRank(FEMCmd.Grupo.Owner)) {
            e.setCancelled(true);
        }
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
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onEntityDamage(EntityDamageEvent e) {
        e.setCancelled(true);
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(PlayerDeathEvent e) {
        e.setDeathMessage("");
    }
}
