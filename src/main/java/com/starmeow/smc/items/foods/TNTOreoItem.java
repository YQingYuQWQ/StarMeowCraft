package com.starmeow.smc.items.foods;

import com.starmeow.smc.items.TippedItems;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class TNTOreoItem extends TippedItems {
    public TNTOreoItem(Properties p_41383_) {
        super(p_41383_);
    }

    public ItemStack finishUsingItem(ItemStack p_42923_, Level p_42924_, LivingEntity p_42925_) {
        p_42924_.explode(p_42925_, p_42925_.getX(), p_42925_.getY(), p_42925_.getZ(), 3f, Level.ExplosionInteraction.NONE);
        return p_42925_.eat(p_42924_, p_42923_);
    }
}
