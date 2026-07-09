package com.starmeow.smc.items;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolAction;

import javax.annotation.Nullable;
import java.util.List;

public class GoldenHanger extends SwordItem {
    public GoldenHanger(Tier p_43269_, int p_43270_, float p_43271_, Properties p_43272_) {
        super(p_43269_, p_43270_, p_43271_, p_43272_);
    }

    public InteractionResultHolder<ItemStack> use(Level p_40672_, Player p_40673_, InteractionHand p_40674_) {
        ItemStack itemstack = p_40673_.getItemInHand(p_40674_);
        p_40673_.startUsingItem(p_40674_);
        return InteractionResultHolder.consume(itemstack);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity p_40669_, int p_40670_) {
        if (!level.isClientSide() && p_40669_ instanceof Player player) {
            int i = this.getUseDuration(stack) - p_40670_;
            float f = getPowerForTime(i);
            if (!((double)f < 0.1)) {
                float strength = f * 3;
                CompoundTag tag = stack.getOrCreateTag();
                tag.putBoolean("SMCHangerFlying", true);
                player.setDeltaMovement(player.getLookAngle().x * strength, player.getLookAngle().y * strength, player.getLookAngle().z * strength);
                player.hurtMarked = true;
                stack.hurtAndBreak(1, player, (p_43296_) -> {
                    p_43296_.broadcastBreakEvent(EquipmentSlot.MAINHAND);
                });
            }
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, level, entity, slot, selected);
        if(!level.isClientSide() && entity instanceof Player player) {
            CompoundTag tag = stack.getOrCreateTag();
            if(tag.getBoolean("SMCHangerFlying")){
                Vec3 motion = player.getDeltaMovement();
                double speed = motion.length();
                if (speed < 0.1) {
                    tag.putBoolean("SMCHangerFlying", false);
                    return;
                }
                Vec3 dir = motion.normalize().scale(Math.min(speed * 1.2, 4.0));
                AABB playerBox = player.getBoundingBox();
                AABB sweepBox = playerBox.expandTowards(dir).inflate(0.6);
                List<Entity> hits = level.getEntities(player, sweepBox, e -> e instanceof LivingEntity && e != player);
                if (!hits.isEmpty()) {
                    LivingEntity target = (LivingEntity) hits.get(0);
                    if(target.hurt(player.damageSources().playerAttack(player), (float)(player.getAttributeValue(Attributes.ARMOR) + 3))){
                        tag.putBoolean("SMCHangerFlying", false);
                    }
                }
            }
        }
    }

    public static float getPowerForTime(int p_40662_) {
        float f = (float)p_40662_ / 20.0F;
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }
        return f;
    }

    public int getUseDuration(ItemStack p_40680_) {
        return 72000;
    }

    public UseAnim getUseAnimation(ItemStack p_40678_) {
        return UseAnim.BOW;
    }

    @Override
    public boolean isCorrectToolForDrops(BlockState p_43298_) {
        return false;
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        return false;
    }

    @Override
    public boolean canAttackBlock(BlockState p_43291_, Level p_43292_, BlockPos p_43293_, Player p_43294_) {
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn)
    {
        String string = "tooltip.smc." + stack.getItem();
        tooltip.add(Component.translatable(string).withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
        tooltip.add(Component.translatable(string + "_0").withStyle(ChatFormatting.BLUE));
    }
}
