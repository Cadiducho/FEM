package com.cadiducho.fem.dropper.listener;

import com.cadiducho.fem.core.FEMCore;
import com.cadiducho.fem.core.api.FEMServer.GameID;
import com.cadiducho.fem.core.cmds.FEMCmd;
import com.cadiducho.fem.core.util.Metodos;
import com.cadiducho.fem.dropper.DropMenu;
import com.cadiducho.fem.dropper.DropPlayer;
import com.cadiducho.fem.dropper.Dropper;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Sign;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;

public class PlayerListener implements Listener {

    private final Dropper plugin;

    public PlayerListener(Dropper instance) {
        plugin = instance;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent e) {
        e.setJoinMessage(null);
        DropPlayer dp = Dropper.getPlayer(e.getPlayer());
        dp.getPlayer().teleport(plugin.getAm().getLobby());
        dp.setLobbyInventory();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        e.setQuitMessage(null);

        Dropper.players.remove(Dropper.getPlayer(player));
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e){
        Player p = e.getPlayer();

        if (p.getWorld().getName().equals(plugin.getAm().getLobby().getWorld().getName())) {
            e.setCancelled(true);
            return;
        }
        Material m = e.getPlayer().getLocation().getBlock().getType();
        if (m == Material.STATIONARY_WATER || m == Material.WATER) {
            Dropper.getPlayer(p).endMap();
        }
    }

    @EventHandler
    public void onSignCreate(SignChangeEvent e){
        String[] lines = e.getLines();

        if (!Dropper.getPlayer(e.getPlayer()).isOnRank(FEMCmd.Grupo.Admin)) return;

        if (lines[0] != null && lines[0].equalsIgnoreCase("Dropper") && lines[1] != null && lines[2] != null){
            e.setLine(0, Metodos.colorizar("&e<<&2Dropper&e>>"));
            e.setLine(1, Metodos.colorizar("&3&l" + lines[1]));
            switch (lines[2]){
                case "facil":
                    e.setLine(2, Metodos.colorizar("&2" + lines[2]));
                    break;
                case "medio":
                    e.setLine(2, Metodos.colorizar("&6" + lines[2]));
                    break;
                case "dificil":
                    e.setLine(2, Metodos.colorizar("&c" + lines[2]));
                    break;
                case "imposible":
                    e.setLine(2, Metodos.colorizar("&4&l" + lines[2]));
                    break;
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        DropPlayer dp = Dropper.getPlayer(e.getPlayer());
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (e.getClickedBlock() == null) return;
            
            if (e.getClickedBlock().getType() == Material.WALL_SIGN || e.getClickedBlock().getType() == Material.SIGN_POST) {
                Sign sign = (Sign) e.getClickedBlock().getState();

                if (ChatColor.stripColor(sign.getLine(0)).equalsIgnoreCase("<<Dropper>>")) {
                    String mapa = ChatColor.stripColor(sign.getLine(1));
                    dp.sendToDropper(mapa);
                    dp.setMapInventory();
                    e.setCancelled(true);
                }
            }

            if (e.getClickedBlock().getType() == Material.SKULL) {
                if (dp.getPlayer().getWorld().getName().equals(plugin.getAm().getLobby().getWorld().getName())) {
                    e.setCancelled(true);
                    return;
                }
                dp.checkInsignia();
                e.setCancelled(true);
            }
        }
        
        if (e.getItem() != null) {
            if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                e.setCancelled(false);
                switch (e.getItem().getType()){
                    case BED:
                        dp.getPlayer().teleport(Dropper.getInstance().getAm().getLobby());
                        dp.setLobbyInventory();
                        break;
                    case COMPASS:
                        dp.sendToLobby();
                        break;
                    case DIAMOND:
                        DropMenu.openMapsInv(dp);
                        break;
                    case EMERALD:
                        if (dp.getPlayer().getWorld().getName().equals(plugin.getAm().getLobby().getWorld().getName())) {
                            DropMenu.openIngInv(dp);
                        }
                        break;
                    default:
                        break;
                }
            }
        }

        if (e.getAction() == Action.PHYSICAL && e.getClickedBlock().getType() == Material.GOLD_PLATE) {
            if (!dp.getPlayer().getWorld().getName().equals(plugin.getAm().getLobby().getWorld().getName())) {
                dp.getPlayer().teleport(Dropper.getInstance().getAm().getLobby());
                dp.setLobbyInventory();
                dp.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){
        if (!(e.getWhoClicked() instanceof Player)) return;
        if (e.getClickedInventory().getTitle() == null) return;
        
        DropPlayer dp = Dropper.getPlayer((Player) e.getWhoClicked());
        
        String mapa = "";
        switch (ChatColor.stripColor(e.getClickedInventory().getTitle())) {
            case "UnderGames - Insignias":
                if (e.getSlot() == 35) {
                    dp.getPlayer().closeInventory();
                    DropMenu.openBorarInsig(dp);
                    dp.getPlayer().playSound(dp.getPlayer().getLocation(), Sound.CLICK, 1f, 1f);
                }
                break;
            case "Borrar Insignias":
                if (!e.getCurrentItem().hasItemMeta()) break;
                
                mapa = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).replace("Insignia oculta del mapa ", "");
                dp.sendMessage("&aHas eliminado la insignia del mapa &e" + mapa);
                dp.getUserData().getDropperInsignias().remove(mapa);
                FEMCore.getInstance().getMysql().removeInsignia(dp, mapa);
                dp.getPlayer().playSound(dp.getPlayer().getLocation(), Sound.CLICK, 1f, 1f);
                dp.getPlayer().closeInventory();
                dp.save();
                break;
            case "UnderGames - Mapas superados":
                if (e.getSlot() == 35) {
                    dp.getPlayer().closeInventory();
                    DropMenu.openBorarMapa(dp);
                    dp.getPlayer().playSound(dp.getPlayer().getLocation(), Sound.CLICK, 1f, 1f);
                }
                break;
            case "Borrar Mapas":
                if (!e.getCurrentItem().hasItemMeta()) break;
                
                mapa = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
                dp.sendMessage("&aHas eliminado los registros del mapa &e" + mapa);
                dp.getUserData().getDropper().remove(mapa);
                FEMCore.getInstance().getMysql().removeDropperMap(dp, mapa);
                dp.getPlayer().playSound(dp.getPlayer().getLocation(), Sound.CLICK, 1f, 1f);
                dp.getPlayer().closeInventory();
                dp.save();
                break;
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent e) {
        if (e instanceof LivingEntity) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (e.getCause() != EntityDamageEvent.DamageCause.FALL) {
            e.setCancelled(true);
            return;
        }

        if (e.getEntity() instanceof Player) {
            if (e.getDamage() > 5) { //Por si acaso, caidas tontas
                Player p = (Player) e.getEntity();
                
                if (p.getWorld().getName().equals(plugin.getAm().getLobby().getWorld().getName())) {
                    e.setCancelled(true);
                    return;
                }

                final DropPlayer pl = Dropper.getPlayer(p);

                //Limpiar jugador y respawn
                pl.getUserData().addDeath(GameID.DROPPER);
                pl.save();
                pl.setMapInventory();
                p.teleport(Metodos.stringToLocation(Dropper.getInstance().getConfig().getString("Dropper.spawns." + p.getWorld().getName())));
                e.setDamage(0f);
                e.setCancelled(true);
                return;
            }
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerFood(FoodLevelChangeEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerPickUp(PlayerPickupItemEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        e.setFormat(ChatColor.GREEN + e.getPlayer().getDisplayName() + ChatColor.WHITE + ": " + ChatColor.GRAY + e.getMessage());
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        if (!Dropper.getPlayer(e.getPlayer()).isOnRank(FEMCmd.Grupo.Admin)){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlaceBreak(BlockPlaceEvent e){
        if (!Dropper.getPlayer(e.getPlayer()).isOnRank(FEMCmd.Grupo.Admin)){
            e.setCancelled(true);
        }
    }
}
