package com.starmeow.smc.mixin;

import com.starmeow.smc.config.Config;
import com.starmeow.smc.helper.CuriosHelper;
import com.starmeow.smc.init.ItemRegistry;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Villager.class)
public class VillagerMixin {

    @Inject(method = "getPlayerReputation", at = @At("RETURN"), cancellable = true)
    private void smc$decreasePrize(Player player, CallbackInfoReturnable<Integer> cir) {
        if (player != null && CuriosHelper.hasCharm(player, ItemRegistry.RED_FRAME_GLASSES.get())) {
            cir.setReturnValue(cir.getReturnValue() + Config.GLASSES_REPUTATION.get());
        }
    }
}
