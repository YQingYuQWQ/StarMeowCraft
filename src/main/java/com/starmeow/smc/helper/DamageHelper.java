package com.starmeow.smc.helper;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.LivingEntity;

public class DamageHelper {
    public static DamageSource customDamageSource(LivingEntity attacker, ResourceKey<DamageType> res) {
        return new DamageSource(attacker.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(res)
        );
    }
}
