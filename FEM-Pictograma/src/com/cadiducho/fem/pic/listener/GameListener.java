package com.cadiducho.fem.pic.listener;

import com.cadiducho.fem.pic.Pictograma;
import com.cadiducho.fem.pic.tick.EventTick;
import com.cadiducho.fem.pic.tick.TickType;
import com.cadiducho.fem.pic.util.MathUtil;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.Wool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

public class GameListener implements Listener {

    private final Pictograma plugin;
    private Location oldBrushLoc = null;

    public GameListener(Pictograma instance){
        plugin = instance;
    }

    //EventTick para las brochas (todos sus tipos) y la goma. PlayerInteractEvent para abrir el menu, limpiar hoja o el cubo
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerPaint(EventTick event){
        if(!plugin.getGm().isInGame()){
            return;
        }
        if(plugin.getGm().builder == null){
            return;
        }
        if(event.getType() != TickType.TICK){
            return;
        }

        Player p = plugin.getGm().builder;
        if(p.isBlocking()){ //Está intentando pintar
            if(null != p.getInventory().getItemInHand().getType()){
                switch(p.getInventory().getItemInHand().getType()){
                    case WOOD_SWORD: //Pincel
                        setPincelBlock(p.getTargetBlock((Set<Material>) null, 100));
                        break;
                    case IRON_SWORD: //Borrador
                        eraseBlock(p.getTargetBlock((Set<Material>) null, 100));
                        break;
                    case GOLD_SWORD: //Brocha
                        setBrochaBlock(p.getTargetBlock((Set<Material>) null, 100));
                        break;
                    default:
                        break;
                }
            }
        }else{
            oldBrushLoc = null; //Eliminar posición anterior para fluided, si ya ha pasado el tick
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent e){
        if((e.getAction() == Action.RIGHT_CLICK_AIR) || (e.getAction() == Action.RIGHT_CLICK_BLOCK)){
            switch(e.getPlayer().getInventory().getItemInHand().getType()){
                case COMPASS: //Escoger color
                    e.setCancelled(true);
                    e.getPlayer().openInventory(plugin.colorPicker);
                    break;
                case EMPTY_MAP: //Limpiar folio
                    e.setCancelled(true);
                    plugin.getAm().getBuildZone().clear();
                    plugin.getAm().getBuildZone().setWool(DyeColor.WHITE);
                    Pictograma.getPlayer(e.getPlayer()).sendMessage("&eHas limpiado la hoja completamente");
                    break;
                case LAVA_BUCKET: //Rellenar area
                    e.setCancelled(true);
                    Block b = e.getPlayer().getTargetBlock((Set<Material>) null, 100);
                    if(b.getType() != Material.WOOL || !plugin.getAm().getBuildZone().contains(b)) return;
                    fillArea(b, b.getData(), true); //Rellenar bloques desde b que mantengan su color
                    plugin.getGm().getBlockList().clear();
                    break;
            }
        }

        //Clickar sobre el color y escogerlo
        if((e.getAction() == Action.LEFT_CLICK_AIR) || (e.getAction() == Action.LEFT_CLICK_BLOCK)){
            if(plugin.getGm().builder != null && plugin.getGm().builder.getUniqueId() != null){
                if(e.getPlayer().getUniqueId() == plugin.getGm().builder.getUniqueId()){
                    Block b = e.getPlayer().getTargetBlock((Set<Material>) null, 100);
                    if(b.getType() == Material.WOOL){
                        Wool wool = (Wool) b.getState().getData();
                        if(!plugin.getAm().getBuildZone().contains(b)){
                            setPencilColor(wool.getColor());
                            e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
                        }
                    }
                }
            }
        }
    }

    private void setPincelBlock(Block b){
        if(b.getType() != Material.WOOL || !plugin.getAm().getBuildZone().contains(b)){
            return;
        }
        b.setTypeIdAndData(Material.WOOL.getId(), plugin.getGm().color.getData(), true);
        if(oldBrushLoc != null){
            while(MathUtil.offset(oldBrushLoc, b.getLocation().add(0.5D, 0.5D, 0.5D)) > 0.5D){
                oldBrushLoc.add(MathUtil.getTraj(oldBrushLoc, b.getLocation().add(0.5D, 0.5D, 0.5D)).multiply(0.5D));

                Block fixBlock = oldBrushLoc.getBlock();
                if(plugin.getAm().getBuildZone().contains(fixBlock)){
                    fixBlock.setTypeIdAndData(Material.WOOL.getId(), plugin.getGm().color.getData(), true);
                }
            }
        }
        oldBrushLoc = b.getLocation().add(0.5D, 0.5D, 0.5D);
    }

    private void setBrochaBlock(Block b){
        Block b2 = b.getLocation().clone().add(0.0, 1.0, 0.0).getBlock();
        Block b3 = b.getLocation().clone().add(0.0, -1.0, 0.0).getBlock();
        Block b4 = b.getLocation().clone().add(0.0, 0.0, 1.0).getBlock();
        Block b5 = b.getLocation().clone().add(0.0, 0.0, -1.0).getBlock();
        if(b.getType() != Material.WOOL || !plugin.getAm().getBuildZone().contains(b)){
            return;
        }

        b.setTypeIdAndData(Material.WOOL.getId(), plugin.getGm().color.getData(), true);
        b2.setTypeIdAndData(Material.WOOL.getId(), plugin.getGm().color.getData(), true);
        b3.setTypeIdAndData(Material.WOOL.getId(), plugin.getGm().color.getData(), true);
        b4.setTypeIdAndData(Material.WOOL.getId(), plugin.getGm().color.getData(), true);
        b5.setTypeIdAndData(Material.WOOL.getId(), plugin.getGm().color.getData(), true);

        if(oldBrushLoc != null){
            while(MathUtil.offset(oldBrushLoc, b.getLocation().add(0.5D, 0.5D, 0.5D)) > 0.5D){
                oldBrushLoc.add(MathUtil.getTraj(oldBrushLoc, b.getLocation().add(0.5D, 0.5D, 0.5D)).multiply(0.5D));

                Block fixBlock = oldBrushLoc.getBlock();
                if(plugin.getAm().getBuildZone().contains(fixBlock)){
                    Block bo2 = fixBlock.getLocation().clone().add(0.0, 1.0, 0.0).getBlock();
                    Block bo3 = fixBlock.getLocation().clone().add(0.0, -1.0, 0.0).getBlock();
                    Block bo4 = fixBlock.getLocation().clone().add(0.0, 0.0, 1.0).getBlock();
                    Block bo5 = fixBlock.getLocation().clone().add(0.0, 0.0, -1.0).getBlock();

                    fixBlock.setTypeIdAndData(Material.WOOL.getId(), plugin.getGm().color.getData(), true);
                    bo2.setTypeIdAndData(Material.WOOL.getId(), plugin.getGm().color.getData(), true);
                    bo3.setTypeIdAndData(Material.WOOL.getId(), plugin.getGm().color.getData(), true);
                    bo4.setTypeIdAndData(Material.WOOL.getId(), plugin.getGm().color.getData(), true);
                    bo5.setTypeIdAndData(Material.WOOL.getId(), plugin.getGm().color.getData(), true);
                }
            }
        }
        oldBrushLoc = b.getLocation().add(0.5D, 0.5D, 0.5D);
    }

    private void eraseBlock(Block b){
        if(b.getType() != Material.WOOL || !plugin.getAm().getBuildZone().contains(b)){
            return;
        }
        b.setTypeIdAndData(Material.WOOL.getId(), DyeColor.WHITE.getData(), true);
        if(oldBrushLoc != null){
            while(MathUtil.offset(oldBrushLoc, b.getLocation().add(0.5D, 0.5D, 0.5D)) > 0.5D){
                oldBrushLoc.add(MathUtil.getTraj(oldBrushLoc, b.getLocation().add(0.5D, 0.5D, 0.5D)).multiply(0.5D));

                Block fixBlock = oldBrushLoc.getBlock();
                if(plugin.getAm().getBuildZone().contains(fixBlock)){
                    fixBlock.setTypeIdAndData(Material.WOOL.getId(), DyeColor.WHITE.getData(), true);
                }
            }
        }
        oldBrushLoc = b.getLocation().add(0.5D, 0.5D, 0.5D);
    }

    public void fillArea(Block block, byte color, boolean first){
        if (block.getData() != color) return;
        if (!plugin.getAm().getBuildZone().contains(block)) return;
        if (plugin.getGm().getBlockList().contains(block)) return;

        //Pintar todos los bloques de un area del color elegido
        block.setData(plugin.getGm().color.getData());
        plugin.getGm().getBlockList().add(block);

        try{
            getSurroundingBlocks(block).forEach(other -> fillArea(other, color, false));
        }catch(StackOverflowError e){}

        //Escuchar el cubo solo la primera vez
        if(first){
            plugin.getGm().getPlayersInGame().forEach(p -> p.playSound(p.getLocation(), Sound.SPLASH, 0.4F, 1.0F));
        }
    }

    //ToDo: Usar este metodo con todos los tipos de pincel
    public static ArrayList<Block> getSurroundingBlocks(Block block){
        ArrayList<Block> blocks = new ArrayList();

        Arrays.asList(BlockFace.values()).forEach(bf -> blocks.add(block.getRelative(bf)));

        return blocks;
    }

    @EventHandler
    public void onPlayerChat(PlayerChatEvent e){
        if(plugin.getGm().isInGame()){
            String word = plugin.getGm().word.toLowerCase();
            String intento = e.getMessage().toLowerCase();

            //Remplazar tildes
            intento.replaceAll("á", "a");
            intento.replaceAll("é", "e");
            intento.replaceAll("í", "i");
            intento.replaceAll("ó", "o");
            intento.replaceAll("ú", "u");
            intento.replaceAll("à", "a");
            intento.replaceAll("é", "e");
            intento.replaceAll("ì", "i");
            intento.replaceAll("ò", "o");
            intento.replaceAll("ù", "u");

            word.replaceAll("á", "a");
            word.replaceAll("é", "e");
            word.replaceAll("í", "i");
            word.replaceAll("ó", "o");
            word.replaceAll("ú", "u");
            word.replaceAll("à", "a");
            word.replaceAll("é", "e");
            word.replaceAll("ì", "i");
            word.replaceAll("ò", "o");
            word.replaceAll("ù", "u");

            if(plugin.getGm().builder != null && (plugin.getGm().builder.getUniqueId() == e.getPlayer().getUniqueId())){
                Pictograma.getPlayer(e.getPlayer()).sendMessage("&c¡No puedes chivar la palabra, ni intentarlo!");
                e.setCancelled(true);
            }else{
                if(plugin.getGm().getHasFound().contains(e.getPlayer().getUniqueId())){
                    Pictograma.getPlayer(e.getPlayer()).sendMessage("&aYa has encontrado la palabra y no puedes dar pistas a otros");
                    e.setCancelled(true);
                }else
                if(intento.equals(word)){
                    plugin.getGm().wordFoundBy(e.getPlayer());
                    e.setCancelled(true);
                }
            }
        }
        if(e.getPlayer().getGameMode().equals(GameMode.SPECTATOR)){
            e.setCancelled(true);
        }
        e.setFormat(ChatColor.GREEN + e.getPlayer().getDisplayName() + ChatColor.WHITE + ": " + ChatColor.GRAY + e.getMessage());
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent event){
        Player p = (Player) event.getWhoClicked();
        if(event.getInventory().getName().equals(plugin.colorPicker.getTitle())){
            if((event.getCurrentItem() == null) || (event.getCurrentItem().getType() == Material.AIR)){
                return;
            }
            if(event.getCurrentItem().getItemMeta().getDisplayName().contains("Blanco")){
                setPencilColor(DyeColor.WHITE);
                p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
                p.closeInventory();
                return;
            }
            if(event.getCurrentItem().getItemMeta().getDisplayName().contains("Negro")){
                setPencilColor(DyeColor.BLACK);
                p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
                p.closeInventory();
                return;
            }
            if(event.getCurrentItem().getItemMeta().getDisplayName().contains("Rojo")){
                setPencilColor(DyeColor.RED);
                p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
                p.closeInventory();
                return;
            }
            if(event.getCurrentItem().getItemMeta().getDisplayName().contains("Naranja")){
                setPencilColor(DyeColor.ORANGE);
                p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
                p.closeInventory();
                return;
            }
            if(event.getCurrentItem().getItemMeta().getDisplayName().contains("Amarillo")){
                setPencilColor(DyeColor.YELLOW);
                p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
                p.closeInventory();
                return;
            }
            if(event.getCurrentItem().getItemMeta().getDisplayName().contains("Verde")){
                setPencilColor(DyeColor.LIME);
                p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
                p.closeInventory();
                return;
            }
            if(event.getCurrentItem().getItemMeta().getDisplayName().contains("Azul")){
                setPencilColor(DyeColor.LIGHT_BLUE);
                p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
                p.closeInventory();
                return;
            }
            if(event.getCurrentItem().getItemMeta().getDisplayName().contains("Morado")){
                setPencilColor(DyeColor.PURPLE);
                p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
                p.closeInventory();
                return;
            }
            if(event.getCurrentItem().getItemMeta().getDisplayName().contains("Marron")){
                setPencilColor(DyeColor.BROWN);
                p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
                p.closeInventory();
            }
        }else{
            event.setCancelled(true);
        }
        p.closeInventory();
    }

    public void setPencilColor(DyeColor color){
        plugin.getGm().color = color;
    }
}
