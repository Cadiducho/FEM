package com.cadiducho.fem.core.util;

import java.util.List;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

public class MerchantBuilder {

    private final MerchantRecipe mRecipe;

    public MerchantBuilder() {
        this(null);
    }

    public MerchantBuilder(ItemStack result) {
        mRecipe = new MerchantRecipe(result, -1);
    }

    public MerchantBuilder setExperiencieReward(boolean flag) {
        mRecipe.setExperienceReward(flag);
        return this;
    }

    public MerchantBuilder setUses(int uses) {
        mRecipe.setUses(uses);
        return this;
    }

    public MerchantBuilder addIngredient(ItemStack ingredient) {
        List<ItemStack> ing = mRecipe.getIngredients();
        ing.add(ingredient);
        mRecipe.setIngredients(ing);
        return this;
    }
    
    public MerchantRecipe build() {
        return this.mRecipe;
    }
}