package com.starmeow.smc.items.foods;

import com.starmeow.smc.helper.EntityHelper;
import com.starmeow.smc.helper.ItemHelper;
import com.starmeow.smc.init.ItemRegistry;
import com.starmeow.smc.init.PotionEffectRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class RainbowCakeSlice extends Item {
    public RainbowCakeSlice(Properties p_41383_) {
        super(p_41383_);
    }

    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        RandomSource random = entity.getRandom();
        if(!level.isClientSide()){
            Vector3f color = new Vector3f(1, 1, 1);
            switch (random.nextInt(7)){
                case 0 -> {
                    entity.addEffect(new MobEffectInstance(PotionEffectRegistry.RAINBOW.get(), 1200, 0));
                    color = new Vector3f(1, 0, 0);
                }
                case 1 -> {
                    entity.addEffect(new MobEffectInstance(PotionEffectRegistry.CHOP_PROTECTION.get(), 1200, 0));
                    color = new Vector3f(1, 0.5f, 0);
                }
                case 2 -> {
                    List<MobEffectInstance> list = new ArrayList<>(entity.getActiveEffects());
                    if(!list.isEmpty()){
                        MobEffectInstance effect = list.get(random.nextInt(list.size()));
                        entity.addEffect(new MobEffectInstance(effect.getEffect(), effect.getDuration(), effect.getAmplifier() + 1, effect.isAmbient(), effect.isVisible(), effect.showIcon()));
                        color = new Vector3f(1, 1, 0);
                    }
                }
                case 3 -> {
                    List<MobEffectInstance> list = new ArrayList<>(entity.getActiveEffects()).stream().filter(effect -> effect.getEffect().getCategory() != MobEffectCategory.BENEFICIAL).toList();
                    for (MobEffectInstance ins : list) {
                        entity.removeEffect(ins.getEffect());
                        if (entity.hasEffect(ins.getEffect())) {
                            entity.getActiveEffectsMap().remove(ins.getEffect());
                        }
                    }
                    color = new Vector3f(0, 1, 0);
                }
                case 4 -> {
                    EntityHelper.spawnLingeringCloud(entity, 0);
                    color = new Vector3f(0, 1, 1);
                }
                case 5 -> {
                    level.addFreshEntity(new ItemEntity(level, entity.getX(), entity.getY(), entity.getZ(), new ItemStack(ItemRegistry.LUCKY_CLOVER.get())));
                    color = new Vector3f(0, 0, 1);
                }
                case 6 -> {
                    level.addFreshEntity(new ItemEntity(level, entity.getX(), entity.getY(), entity.getZ(), new ItemStack(Items.ENCHANTED_GOLDEN_APPLE)));
                    color = new Vector3f(1, 0, 1);
                }
                default -> {}
            }
            DustParticleOptions options = new DustParticleOptions(color, 1);
            if(level instanceof ServerLevel serverLevel)serverLevel.sendParticles(options, entity.getX(), entity.getY() + entity.getBbHeight() * (0.5F) , entity.getZ(), 65, 1.5, 1.5, 1.5, 0);

        }
        return entity.eat(level, stack);
    }

    @Override
    public Component getName(ItemStack stack) {
        return ItemHelper.rainbowColor(super.getName(stack), 20, false);
    }

    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn)
    {
        String string = "tooltip.smc.rainbow_cake";
        if(Screen.hasShiftDown()){
            tooltip.add(Component.translatable(string + "_0").withStyle(ChatFormatting.GRAY));
            tooltip.add(Component.translatable(string + "_1").withStyle(ChatFormatting.RED));
            tooltip.add(Component.translatable(string + "_2").withStyle(ChatFormatting.GOLD));
            tooltip.add(Component.translatable(string + "_3").withStyle(ChatFormatting.YELLOW));
            tooltip.add(Component.translatable(string + "_4").withStyle(ChatFormatting.GREEN));
            tooltip.add(Component.translatable(string + "_5").withStyle(ChatFormatting.AQUA));
            tooltip.add(Component.translatable(string + "_6").withStyle(ChatFormatting.BLUE));
            tooltip.add(Component.translatable(string + "_7").withStyle(ChatFormatting.LIGHT_PURPLE));
        }else{
            tooltip.add(Component.translatable(string).withStyle(ChatFormatting.YELLOW).withStyle(ChatFormatting.BOLD));
        }
    }
}
