package com.starmeow.smc.mixin;

import com.starmeow.smc.init.ItemRegistry;
import com.starmeow.smc.tags.SMCTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SweetBerryBushBlock.class)
public class SweetBerryBushBlockMixin {
    @Inject(method = "entityInside", at = @At("HEAD"))
    private void smc$foxDropFurItems(BlockState p_57270_, Level level, BlockPos p_57272_, Entity entity, CallbackInfo ci) {
        if(entity instanceof Fox fox){
            if (!level.isClientSide() && p_57270_.getValue(BlockStateProperties.AGE_3) > 0 && (entity.xOld != entity.getX() || entity.zOld != entity.getZ())) {
                double d0 = Math.abs(entity.getX() - entity.xOld);
                double d1 = Math.abs(entity.getZ() - entity.zOld);
                if (d0 >= 0.003 || d1 >= 0.003) {

                    if(level.getGameTime() % 20L == 0L && fox.getRandom().nextInt(100) < 5){
                        if(fox.getMainHandItem().is(SMCTags.CLOUD)){
                            fox.getMainHandItem().shrink(1);
                            fox.spawnAtLocation(ItemRegistry.PURE_FOX_FUR.get());
                        }else{
                            if(fox.getVariant() == Fox.Type.RED){
                                fox.spawnAtLocation(ItemRegistry.FOX_FUR.get());
                            }
                            if(fox.getVariant() == Fox.Type.SNOW){
                                fox.spawnAtLocation(ItemRegistry.SNOW_FOX_FUR.get());
                            }
                        }
                    }

                }
            }
        }
    }
}
