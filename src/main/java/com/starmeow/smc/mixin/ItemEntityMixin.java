package com.starmeow.smc.mixin;

import com.starmeow.smc.init.EnchantmentRegistry;
import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {
    @Inject(method = "fireImmune", at = @At("HEAD"), cancellable = true)
    private void smc$fireImmune(CallbackInfoReturnable<Boolean> cir){
        ItemEntity item = (ItemEntity)(Object)this;
        if(item.getItem().getEnchantmentLevel(EnchantmentRegistry.NEHTERITE_CLADDING.get()) > 0){
            cir.setReturnValue(true);
        }
    }
}
