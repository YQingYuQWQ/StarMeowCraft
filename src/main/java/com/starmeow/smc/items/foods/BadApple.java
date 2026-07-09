package com.starmeow.smc.items.foods;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class BadApple extends Item {
    public BadApple(Properties p_41383_) {
        super(p_41383_);
    }

    public ItemStack finishUsingItem(ItemStack p_42923_, Level p_42924_, LivingEntity p_42925_) {
        if(p_42925_.hasEffect(MobEffects.BAD_OMEN)){
            int amp = p_42925_.getEffect(MobEffects.BAD_OMEN).getAmplifier();
            p_42925_.addEffect(new MobEffectInstance(MobEffects.BAD_OMEN, 9600, Math.min(amp + 1, 4)));
        }
        else {
            p_42925_.addEffect(new MobEffectInstance(MobEffects.BAD_OMEN, 9600, 0));
        }
        return p_42925_.eat(p_42924_, p_42923_);
    }
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn)
    {
        String string = "tooltip.smc.bad_apple";
        tooltip.add(Component.translatable(string).withStyle(ChatFormatting.RED));
    }
}
