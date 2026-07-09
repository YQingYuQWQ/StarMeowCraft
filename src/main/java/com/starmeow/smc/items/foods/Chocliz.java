package com.starmeow.smc.items.foods;

import com.starmeow.smc.init.PotionEffectRegistry;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class Chocliz extends Item {
    public Chocliz(Properties p_41383_) {
        super(p_41383_);
    }

    public ItemStack finishUsingItem(ItemStack p_42923_, Level p_42924_, LivingEntity p_42925_) {
        p_42925_.setTicksFrozen(p_42925_.getTicksFrozen() + 50);
        if(p_42925_.hasEffect(PotionEffectRegistry.OVER_SPEED.get())){
            int amp = p_42925_.getEffect(PotionEffectRegistry.OVER_SPEED.get()).getAmplifier();
            if(amp >= 5){

            }
            p_42925_.addEffect(new MobEffectInstance(PotionEffectRegistry.OVER_SPEED.get(), 1200, amp + 1));
        }
        else {
            p_42925_.addEffect(new MobEffectInstance(PotionEffectRegistry.OVER_SPEED.get(), 1200, 0));
        }
        return p_42925_.eat(p_42924_, p_42923_);
    }
}
