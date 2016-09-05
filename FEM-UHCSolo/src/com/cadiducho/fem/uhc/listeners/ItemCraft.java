package com.cadiducho.fem.uhc.listeners;

import java.util.Collection;
import com.cadiducho.fem.uhc.Main;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

public class ItemCraft implements Listener {

    public Main plugin;

    public ItemCraft(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCraft(PrepareItemCraftEvent e) {
        Recipe r = e.getRecipe();
        if (r.getResult().getType().equals(Material.GOLDEN_APPLE)) {
            if (recipeContainsMaterial(r, Material.GOLD_BLOCK)) {
                e.getInventory().setResult(new ItemStack(Material.AIR));
            }
        }
    }

    private boolean recipeContainsMaterial(Recipe r, Material mat) {
        Collection<ItemStack> ingredients = null;
        if (r instanceof ShapedRecipe) {
            ingredients = ((ShapedRecipe) r).getIngredientMap().values();
        }
        if (r instanceof ShapelessRecipe) {
            ingredients = ((ShapelessRecipe) r).getIngredientList();
        }
        if (ingredients == null) {
            return false;
        }
        for (ItemStack i : ingredients) {
            if (i.getType().equals(mat)) {
                return true;
            }
        }
        return false;
    }
}
