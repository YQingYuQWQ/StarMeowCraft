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

public class Hanger extends SwordItem {
    public Hanger(Tier p_43269_, int p_43270_, float p_43271_, Properties p_43272_) {
        super(p_43269_, p_43270_, p_43271_, p_43272_);
    }

    @Override
    public boolean canAttackBlock(BlockState p_43291_, Level p_43292_, BlockPos p_43293_, Player p_43294_) {
        return true;
    }

    @Override
    public boolean isCorrectToolForDrops(BlockState p_43298_) {
        return false;
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn)
    {
        tooltip.add(Component.translatable("tooltip.smc.golden_hanger").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
    }
}
