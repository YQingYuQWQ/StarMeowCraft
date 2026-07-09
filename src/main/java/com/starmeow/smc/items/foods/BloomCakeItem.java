package com.starmeow.smc.items.foods;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class BloomCakeItem extends Item {
    public BloomCakeItem(Properties p_41383_) {
        super(p_41383_);
    }

    public ItemStack finishUsingItem(ItemStack p_40684_, Level p_40685_, LivingEntity p_40686_) {
        ItemStack $$3 = super.finishUsingItem(p_40684_, p_40685_, p_40686_);
        return p_40686_ instanceof Player && ((Player)p_40686_).getAbilities().instabuild ? $$3 : new ItemStack(Items.FLOWER_POT);
    }
}
