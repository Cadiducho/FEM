package com.cadiducho.fem.pro;

import com.cadiducho.fem.core.util.ItemBuilder;
import com.cadiducho.fem.core.util.ItemUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;
import java.util.List;

public class ProMenu {

    private Protections plugin;

    public enum MenuType{
        SETTINGS, AREAS
    }

    @Getter @Setter private static int area;

    @Getter static private Inventory invSettings;
    @Getter static private Inventory invAreas;

    @Getter private static final List<String> loreAreas = Arrays.asList(ChatColor.WHITE + "Haz click para acceder a los ajustes");
    @Getter private static final List<String> loreJoin = Arrays.asList("Permitir entrar otros jugadores a tu area", "- Permitir (Verde)", "- Denegar (Rojo)");
    @Getter private static final List<String> lorePvP = Arrays.asList("Permitir el pvp en tu area", "- Permitir (Verde)", "- Denegar (Rojo)");
    @Getter private static final List<String> lorePvE = Arrays.asList("Permitir el pve en tu area", "- Permitir (Verde)", "- Denegar (Rojo)");
    @Getter private static final List<String> loreExplosion = Arrays.asList("Permitir las explosiones en tu area", "- Permitir (Verde)", "- Denegar (Rojo)");

    public ProMenu(Protections Main){
        this.plugin = Main;

        invAreas = plugin.getServer().createInventory(null, 54, "Todas tus areas");

        //TODO: Más Bonito
        invSettings = plugin.getServer().createInventory(null, 18, "Configuracion");
        invSettings.setItem(1, new ItemBuilder().setType(Material.ACACIA_DOOR).setDisplayName("Entrar").setLores(loreJoin).build());
        invSettings.setItem(2, new ItemBuilder().setType(Material.DIAMOND_SWORD).setDisplayName("PvP").setLores(lorePvP).build());
        invSettings.setItem(3, ItemUtil.createBanner("PvE", lorePvE, DyeColor.MAGENTA));
        invSettings.setItem(4, new ItemBuilder().setType(Material.TNT).setDisplayName("Explosion").setLores(loreExplosion).build());
    }

    public static void openMenu(ProPlayer player, MenuType mt, int areaID){
        player.getPlayer().closeInventory();
        Inventory clon = null;

        switch (mt){
            case AREAS:
                clon = invAreas;
                ProArea area1 = new ProArea(player);
                //TODO: Pages
                if (area1.getPlayerAreas(player).size() > 54) invAreas.setItem(50, new ItemBuilder().setType(Material.ARROW).setDisplayName("Siguiente Página").build());
                area1.getPlayerAreas(player).forEach(a -> invSettings.setItem(a, new ItemBuilder().setType(Material.GRASS).setDisplayName("Area " + a).build()));
                break;
            case SETTINGS:
                clon = invSettings;
                ProArea area2 = new ProArea(areaID);

                //Entrar
                DyeColor joinColor = area2.getSetting("join") ? DyeColor.LIME : DyeColor.RED;
                clon.setItem(10, ItemUtil.createClay("Entrar", loreJoin, joinColor));

                //PvP
                DyeColor pvpColor = area2.getSetting("pvp") ? DyeColor.LIME : DyeColor.RED;
                clon.setItem(11, ItemUtil.createClay("PvP", loreJoin, pvpColor));

                //PvE
                DyeColor pveColor = area2.getSetting("pve") ? DyeColor.LIME : DyeColor.RED;
                clon.setItem(12, ItemUtil.createClay("PvE", loreJoin, pveColor));

                //Explosion
                DyeColor explosionColor = area2.getSetting("explosion") ? DyeColor.LIME : DyeColor.RED;
                clon.setItem(13, ItemUtil.createClay("Explosion", loreJoin, explosionColor));

                break;
        }

        if (clon != null) {
            player.getPlayer().closeInventory();
            player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.CLICK, 1F, 1F);
            player.getPlayer().openInventory(clon);
        }
    }
}
