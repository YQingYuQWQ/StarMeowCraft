package com.starmeow.smc.items;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.Nullable;

public class InfiniteFuel extends TippedItems {
    public InfiniteFuel(Properties p_41383_) {
        super(p_41383_);
    }

    public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
        return 1600;
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack stack) {
        return stack.copy();
    }
}
