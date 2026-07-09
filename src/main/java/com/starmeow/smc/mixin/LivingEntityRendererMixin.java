package com.starmeow.smc.mixin;
import com.starmeow.smc.helper.CuriosHelper;
import com.starmeow.smc.helper.EntityHelper;
import com.starmeow.smc.init.ItemRegistry;
import com.starmeow.smc.init.PotionEffectRegistry;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.Curios;

import javax.annotation.Nullable;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin {
    @Inject(method = "isEntityUpsideDown", at = @At("HEAD"), cancellable = true)
    private static void smc$shouldUpsideDown(LivingEntity living, CallbackInfoReturnable<Boolean> cir) {
        if(CuriosHelper.hasCharm(living, ItemRegistry.GLITCH_PARTICLE.get())){
            cir.setReturnValue(true);
        }
    }
}
