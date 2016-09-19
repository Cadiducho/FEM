package com.cadiducho.fem.lobby;

import com.cadiducho.fem.core.api.FEMServer;
import com.cadiducho.fem.core.api.FEMUser;
import com.cadiducho.fem.core.util.FEMFileLoader;
import com.cadiducho.fem.core.util.Metodos;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitTask;

/**
 * Que sólo una clase se encargue de todo el manejo de eventos.
 */
public class EventManager implements Listener, PluginMessageListener {

    private final Lobby plugin;
    private BukkitTask fallControl;

    public EventManager(Lobby plugin) {
        this.plugin = plugin;
    }

    /**
     * Créeme que esperar un tick más hacer el tp es mejor, el jugador no notará
     * la diferencia. Lo digo por los YTBers al entrar mucha gente.
     *
     * @param e
     */
    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent e) {
        plugin.getServer().getScheduler().runTask(plugin, () -> {
            e.getPlayer().teleport(e.getPlayer().getWorld().getSpawnLocation());
        });
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        FEMUser u = FEMServer.getUser(e.getPlayer());

        e.setJoinMessage(Metodos.colorizar("&7" + e.getPlayer().getDisplayName() + " " + FEMFileLoader.getLang().getString("entrar")));
        plugin.getServer().getScheduler().runTask(plugin, () -> u.sendMessage("*motd", u.getName(), plugin.getServer().getOnlinePlayers().size()));
        plugin.pm.setLobbyPlayer(player);

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
            if (e.getClickedBlock().getType().equals(Material.TRAP_DOOR) || e.getClickedBlock().getType().equals(Material.IRON_TRAPDOOR)) {
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
                switch (e.getItem().getType()) {
                    case COMPASS:
                        plugin.m.openNavegatorMenu(e.getPlayer());
                        e.setCancelled(true);
                        break;
                    case COMMAND:
                        plugin.m.openSettings(e.getPlayer());
                        e.setCancelled(true);
                        break;
                    default:
                        e.setCancelled(false);
                        return;
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        plugin.m.checkClick(e);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onWeatherChange(WeatherChangeEvent e) {
        if (e.toWeatherState()) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onExplosion(ExplosionPrimeEvent e) {
        e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCreatureSpawn(CreatureSpawnEvent e) {
        e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockIgnite(BlockIgniteEvent e) {
        e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPiston(BlockPistonExtendEvent e) {
        e.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onLeavesDecay(LeavesDecayEvent e) {
        e.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onItemSpawnEvent(ItemSpawnEvent e) {
        e.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockFromToEvent(BlockFromToEvent e) {
        e.setCancelled(true);
    }

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

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onEntityDamage(EntityDamageEvent e) {
        e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(PlayerDeathEvent e) {
        e.setDeathMessage("");
    }

    /**
     * Registramos todos los pinches eventos aquí ya está.
     */
    public void init() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        if (fallControl == null) {
            fallControl = plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
                plugin.getServer().getOnlinePlayers().stream().forEach(p -> {
                    if (p.getLocation().getBlockY() < 0) {
                        p.teleport(p.getWorld().getSpawnLocation());
                    }
                });
            }, 20l, 20l);
        }
    }

    /**
     * Bukkit de por sí lo hace en onDisable pero estamos en el mundo real y eso
     * no funciona.
     */
    public void removeEvents() {
        HandlerList.unregisterAll(this);
    }

    @Override
    public void onPluginMessageReceived(String string, Player player, byte[] bytes) {
        ByteArrayDataInput in = ByteStreams.newDataInput(bytes);
        String subchannel = in.readUTF();

        if (subchannel.equalsIgnoreCase("serversinfo")) {
            Type listType = new TypeToken<List<Lobby.FEMServerInfo>>() {
            }.getType();
            String json = in.readUTF();
            plugin.setServers(new Gson().fromJson(json, listType));
        }
    }

}
