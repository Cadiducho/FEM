package com.cadiducho.fem.pro;

import com.cadiducho.fem.core.util.ItemBuilder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;
import java.util.List;

public class ProMenu {

    private Protections plugin;

    public enum MenuType{
        SETTINGS, AREAS;
    }

    @Getter @Setter private String area;

    @Getter static private Inventory invSettings;
    @Getter static private Inventory invAreas;

    @Getter private static final List<String> loreAreas = Arrays.asList(ChatColor.WHITE + "Haz click para acceder a los ajustes");
    @Getter private static final List<String> loreJoin = Arrays.asList("Permitir entrar otros jugadores a tu area", "- Permitir (Verde)", "- Denegar (Rojo)");

    public ProMenu(Protections Main){
        this.plugin = Main;

        invAreas = plugin.getServer().createInventory(null, 54, "Todas tus areas");

        invSettings = plugin.getServer().createInventory(null, 36, area + " Configuracion");
        invSettings.setItem(10, new ItemBuilder().setType(Material.ACACIA_DOOR).setDisplayName("Entrar").setLores(loreJoin).build());
    }

    public static void openMenu(ProPlayer player, MenuType mt, int areaID){
        player.getPlayer().closeInventory();
        Inventory clon = null;

        switch (mt){
            case AREAS:
                clon = invAreas;
                ProArea area1 = new ProArea(player);

                if (area1.getPlayerAreas(player).size() > 54) invAreas.setItem(50, new ItemBuilder().setType(Material.ARROW).setDisplayName("Siguiente Página").build());
                area1.getPlayerAreas(player).forEach(a -> invSettings.setItem(a, new ItemBuilder().setType(Material.GRASS).setDisplayName("Area " + a).build()));
                break;
            case SETTINGS:
                clon = invSettings;
                ProArea area2 = new ProArea(areaID);

                //TODO: Settings
                break;
        }

        if (clon != null) {
            player.getPlayer().closeInventory();
            player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.UI_BUTTON_CLICK, 1F, 1F);
            player.getPlayer().openInventory(clon);
        }
    }
}
