package com.cadiducho.fem.lobby.listeners;

import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.core.api.FEMUser;
import com.cadiducho.fem.core.cmds.FEMCmd;
import com.cadiducho.fem.core.util.ItemUtil;
import com.cadiducho.fem.core.util.Metodos;
import com.cadiducho.fem.lobby.Lobby;
import com.cadiducho.fem.lobby.Lobby.FEMServerInfo;
import com.cadiducho.fem.lobby.LobbyMenu;
import com.cadiducho.fem.lobby.LobbyTeams;
import com.cadiducho.fem.lobby.task.TaskParticles;
import com.cadiducho.fem.lobby.utils.ParticleType;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

public class PlayerListener implements Listener, PluginMessageListener {

    private HashMap<FEMUser, BukkitRunnable> particles = new HashMap<>();

    private final Lobby plugin;

    public PlayerListener(Lobby instance) {
        plugin = instance;
    }

    /*
	 * Acciones que realiza el lobby al entrar un jugador
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        e.setJoinMessage(null);

        FEMUser u = FEMServer.getUser(e.getPlayer());

        plugin.getServer().getScheduler().runTask(plugin, () -> u.sendMessage("*motd", u.getName(), plugin.getServer().getOnlinePlayers().size()));

        e.getPlayer().setHealth(e.getPlayer().getMaxHealth());
        e.getPlayer().setFoodLevel(20);
        e.getPlayer().getInventory().clear();
        e.getPlayer().getInventory().setItem(0, ItemUtil.createItem(Material.COMPASS, "&lJuegos", "Desplázate entre los juegos del servidor"));
        e.getPlayer().getInventory().setItem(8, ItemUtil.createItem(Material.COMMAND, "&lAjustes", "Cambia alguno de tus ajustes de usuario"));

        e.getPlayer().getInventory().setItem(4, LobbyMenu.getLibro());
        e.getPlayer().teleport(e.getPlayer().getWorld().getSpawnLocation());

        u.tryHidePlayers();
        plugin.getServer().getOnlinePlayers().forEach(p -> FEMServer.getUser(p).tryHidePlayers());

        LobbyTeams.setScoreboardTeam(u);

        e.getPlayer().setGameMode(GameMode.ADVENTURE);
    }

    /*
	 * Obtener monedas del suelo
     */
    @EventHandler
    public void onPlayerGetDrop(PlayerPickupItemEvent e) {
        FEMUser u = FEMServer.getUser(e.getPlayer());
        ItemStack is = e.getItem().getItemStack();
        if (is.getType().equals(Material.DOUBLE_PLANT) && is.getItemMeta().getDisplayName().equals("Coin")) {
            e.setCancelled(true);
            e.getItem().remove();
            u.getUserData().setCoins(u.getUserData().getCoins() + 1);
            u.save();
            e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.LEVEL_UP, 1F, 1F);
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
    public void onEntityFire(EntityCombustEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        FEMUser u = FEMServer.getUser(e.getPlayer());
        //Sin interacción
        if (e.getClickedBlock() != null) {
            if (e.getClickedBlock().getType().equals(Material.TRAP_DOOR) || e.getClickedBlock().getType().equals(Material.IRON_TRAPDOOR) || e.getClickedBlock().getType().equals(Material.FENCE_GATE) || e.getClickedBlock().getType().equals(Material.FIRE) || e.getClickedBlock().getType().equals(Material.CAULDRON) || e.getClickedBlock().getRelative(BlockFace.UP).getType().equals(Material.FIRE) || e.getClickedBlock().getType() == Material.CHEST || e.getClickedBlock().getType() == Material.TRAPPED_CHEST || e.getClickedBlock().getType() == Material.DROPPER || e.getClickedBlock().getType() == Material.DISPENSER || e.getClickedBlock().getType() == Material.BED_BLOCK || e.getClickedBlock().getType() == Material.BED) {
                e.setCancelled(true);
            }
        }

        //Menu
        if (e.getItem() != null) {
            if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                e.setCancelled(false);
                switch (e.getItem().getType()) {
                    case COMPASS:
                        LobbyMenu.openMenu(u, LobbyMenu.Menu.VIAJAR);
                        break;
                    case COMMAND:
                        LobbyMenu.openMenu(u, LobbyMenu.Menu.AJUSTES);
                        break;
                }
            }
        }
    }

    @EventHandler
    public void inventoryClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) {
            return;
        }

        Player p = (Player) e.getWhoClicked();
        FEMUser u = FEMServer.getUser(p);
        switch (ChatColor.stripColor(e.getInventory().getTitle())) {
            case "Ajustes del jugador":
                e.setCancelled(true);
                switch (e.getSlot()) {
                    case 1:
                    case 10:
                        u.getUserData().setFriendRequest(!u.getUserData().getFriendRequest());
                        u.save();
                        LobbyMenu.openMenu(u, LobbyMenu.Menu.AJUSTES);
                        u.getPlayer().playSound(u.getPlayer().getLocation(), Sound.ORB_PICKUP, 1F, 1F);
                        u.sendMessage("&eAjuste cambiado");
                        break;
                    case 4:
                    case 13:
                        u.getUserData().setEnableTell(!u.getUserData().getEnableTell());
                        u.save();
                        LobbyMenu.openMenu(u, LobbyMenu.Menu.AJUSTES);
                        u.getPlayer().playSound(u.getPlayer().getLocation(), Sound.ORB_PICKUP, 1F, 1F);
                        
                        //Mandar a bungee la lista actualizada
                        ByteArrayDataOutput out = ByteStreams.newDataOutput(); 
                        out.writeUTF("FEMChat");
                        out.writeUTF("update"); 
                        p.sendPluginMessage(plugin, "FEM", out.toByteArray());
                        
                        u.sendMessage("&eAjuste cambiado");
                        break;
                    case 7:
                    case 16:
                        u.getUserData().setHideMode(u.getUserData().getHideMode() == 2 ? 0 : (u.getUserData().getHideMode() + 1));
                        u.save();
                        u.tryHidePlayers();
                        LobbyMenu.openMenu(u, LobbyMenu.Menu.AJUSTES);
                        u.getPlayer().playSound(u.getPlayer().getLocation(), Sound.ORB_PICKUP, 1F, 1F);
                        u.sendMessage("&eAjuste cambiado");
                        break;
                }
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
                    case 22: //puntos
                        p.closeInventory();
                        break;
                    case 26:
                        LobbyMenu.openMenu(u, LobbyMenu.Menu.STATS);
                        break;
                }
                p.playSound(u.getPlayer().getLocation(), Sound.CLICK, 1F, 1F);
                break;
            case "NVIDIA Point":
                e.setCancelled(true);
                switch (e.getSlot()) {
                    case 11:
                        LobbyMenu.openMenu(u, LobbyMenu.Menu.PARTICULAS);
                        break;
                    case 13:
                        u.sendMessage("Falta Inv");
                        break;
                    case 15:
                        u.sendMessage("Falta Inv");
                        break;
                    case 31:
                        u.sendMessage("Falta Inv");
                        break;
                    default:
                        break;
                }
                p.closeInventory();
                break;
            case "Particulas":
                e.setCancelled(true);
                switch (e.getSlot()){
                    case 30: //Cambiar botón atrás
                        LobbyMenu.openMenu(u, LobbyMenu.Menu.NVIDIA);
                         break;
                    case 32: //Cambiar botón parar
                        if (particles.containsKey(u)) particles.get(u).cancel();
                        break;
                    default:
                        if (particles.containsKey(u)) particles.get(u).cancel(); particles.remove(u);

                        BukkitRunnable b = new TaskParticles(p, ParticleType.values()[e.getSlot()]);
                        particles.put(u, b);
                        break;
                }
                break;
            case "Eventos":
                //LobbyMenu.openMenu(u, LobbyMenu.Menu.EVENTOS);
                e.setCancelled(true);
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

    @Override
    public void onPluginMessageReceived(String string, Player player, byte[] bytes) {
        ByteArrayDataInput in = ByteStreams.newDataInput(bytes);
        String subchannel = in.readUTF();

        if (subchannel.equalsIgnoreCase("serversinfo")) {
            Type listType = new TypeToken<List<FEMServerInfo>>() {
            }.getType();
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
        if (!FEMServer.getUser(e.getPlayer()).isOnRank(FEMCmd.Grupo.Admin)) {
            e.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerPlace(BlockPlaceEvent e) {
        if (!FEMServer.getUser(e.getPlayer()).isOnRank(FEMCmd.Grupo.Admin)) {
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
