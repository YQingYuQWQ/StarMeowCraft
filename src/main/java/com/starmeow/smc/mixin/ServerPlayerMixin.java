package com.starmeow.smc.mixin;

import com.starmeow.smc.init.EnchantmentRegistry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {
    @Inject(method = "drop(Lnet/minecraft/world/item/ItemStack;ZZ)Lnet/minecraft/world/entity/item/ItemEntity;", at = @At("HEAD"), cancellable = true)
    private void smc$stopItemToss(ItemStack stack, boolean p_36178_, boolean p_36179_, CallbackInfoReturnable<ItemEntity> cir) {
        Player player = (Player)(Object)this;
        if(EnchantmentHelper.getItemEnchantmentLevel(EnchantmentRegistry.ANCHOR_HOLD.get(), stack) > 0 && false){
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ZOMBIE_ATTACK_IRON_DOOR, SoundSource.PLAYERS, 1F, 2F);
            cir.cancel();
        }
    }
}
