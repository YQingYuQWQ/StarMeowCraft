package com.starmeow.smc.blocks;

import com.starmeow.smc.init.ItemRegistry;
import com.starmeow.smc.init.PotionEffectRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;

public class FrostBerryBushBlock extends SweetBerryBushBlock {
    public FrostBerryBushBlock(Properties p_57249_) {
        super(p_57249_);
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter p_57256_, BlockPos p_57257_, BlockState p_57258_) {
        return new ItemStack(ItemRegistry.FROST_BERRIES.get());
    }

    @Override
    public void randomTick(BlockState p_222563_, ServerLevel p_222564_, BlockPos p_222565_, RandomSource p_222566_) {
        int i = p_222563_.getValue(AGE);
        if (i < 3 && p_222564_.getRawBrightness(p_222565_.above(), 0) >= 9) {
            int rate = p_222564_.getBiome(p_222565_).is(BiomeTags.SPAWNS_SNOW_FOXES) ? 5 : 15;
            if(ForgeHooks.onCropsGrowPre(p_222564_, p_222565_, p_222563_, p_222566_.nextInt(rate) == 0)){
                BlockState blockstate = p_222563_.setValue(AGE, i + 1);
                p_222564_.setBlock(p_222565_, blockstate, 2);
                p_222564_.gameEvent(GameEvent.BLOCK_CHANGE, p_222565_, GameEvent.Context.of(blockstate));
                ForgeHooks.onCropsGrowPost(p_222564_, p_222565_, p_222563_);
            }
        }

    }

    @Override
    public void entityInside(BlockState p_57270_, Level p_57271_, BlockPos p_57272_, Entity p_57273_) {
        if (p_57273_ instanceof LivingEntity living && living.getType() != EntityType.FOX && living.getType() != EntityType.BEE) {
            living.makeStuckInBlock(p_57270_, new Vec3(0.800000011920929, 0.75, 0.800000011920929));
            if (!p_57271_.isClientSide && (Integer)p_57270_.getValue(AGE) > 0 && (living.xOld != living.getX() || living.zOld != living.getZ())) {
                double d0 = Math.abs(living.getX() - living.xOld);
                double d1 = Math.abs(living.getZ() - living.zOld);
                if (d0 >= 0.003000000026077032 || d1 >= 0.003000000026077032) {
                    living.hurt(p_57271_.damageSources().sweetBerryBush(), 1.0F);
                    living.addEffect(new MobEffectInstance(PotionEffectRegistry.FROST.get(), 60, 0));
                }
            }
        }

    }

    @Override
    public InteractionResult use(BlockState p_57275_, Level p_57276_, BlockPos p_57277_, Player p_57278_, InteractionHand p_57279_, BlockHitResult p_57280_) {
        int i = (Integer)p_57275_.getValue(AGE);
        boolean flag = i == 3;
        if (!flag && p_57278_.getItemInHand(p_57279_).is(Items.BONE_MEAL)) {
            return InteractionResult.PASS;
        } else if (i > 2) {
            popResource(p_57276_, p_57277_, new ItemStack(ItemRegistry.FROST_BERRIES.get(), 1 + (flag ? 1 : 0)));
            p_57276_.playSound((Player)null, p_57277_, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + p_57276_.random.nextFloat() * 0.4F);
            BlockState blockstate = (BlockState)p_57275_.setValue(AGE, 2);
            p_57276_.setBlock(p_57277_, blockstate, 2);
            p_57276_.gameEvent(GameEvent.BLOCK_CHANGE, p_57277_, GameEvent.Context.of(p_57278_, blockstate));
            return InteractionResult.sidedSuccess(p_57276_.isClientSide);
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    protected boolean mayPlaceOn(BlockState p_51042_, BlockGetter p_51043_, BlockPos p_51044_) {
        return p_51042_.is(BlockTags.ICE);
    }

    public boolean canSurvive(BlockState p_51028_, LevelReader p_51029_, BlockPos p_51030_) {
        BlockState below = p_51029_.getBlockState(p_51030_.below());
        return this.mayPlaceOn(below, p_51029_, p_51030_);
    }

    public boolean isValidBonemealTarget(LevelReader p_256056_, BlockPos p_57261_, BlockState p_57262_, boolean p_57263_) {
        return p_256056_.getBiome(p_57261_).is(BiomeTags.SPAWNS_SNOW_FOXES) && p_57262_.getValue(AGE) < 3;
    }
}
