package com.starmeow.smc.items;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class BatteryItem extends TippedItems{
    public BatteryItem(Properties p_41383_) {
        super(p_41383_);
    }
    //一定要吃吗？？。。。
    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        level.explode(entity, entity.getX(), entity.getY(), entity.getZ(), 2, false, Level.ExplosionInteraction.NONE);
        LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(level);
        if (bolt != null) {
            bolt.moveTo(entity.getX(), entity.getY(), entity.getZ(), 0, 0.0F);
            level.addFreshEntity(bolt);
        }
        entity.setDeltaMovement(entity.getDeltaMovement().add(new Vec3(0, 15, 0)));
        entity.hurtMarked = true;
        return super.finishUsingItem(stack, level, entity);
    }
}
