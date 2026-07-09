package com.starmeow.smc.items.foods;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class AsteraMilkshakeItem extends MilkshakeItem {
    public AsteraMilkshakeItem(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public void applyMilkshakeEffect(Level level, LivingEntity living){
        living.heal(living.getActiveEffects().size() * 4);
    }
}
