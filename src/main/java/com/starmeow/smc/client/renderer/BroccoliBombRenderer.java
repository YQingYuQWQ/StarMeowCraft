package com.starmeow.smc.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.starmeow.smc.entities.PrimedBroccoliBomb;
import com.starmeow.smc.init.BlockRegistry;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.TntMinecartRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class BroccoliBombRenderer extends EntityRenderer<PrimedBroccoliBomb> {
    private final BlockRenderDispatcher blockRenderer;

    public BroccoliBombRenderer(EntityRendererProvider.Context p_174426_) {
        super(p_174426_);
        this.shadowRadius = 0.5F;
        this.blockRenderer = p_174426_.getBlockRenderDispatcher();
    }

    public void render(PrimedBroccoliBomb p_116177_, float p_116178_, float p_116179_, PoseStack p_116180_, MultiBufferSource p_116181_, int p_116182_) {
        p_116180_.pushPose();
        p_116180_.translate(0.0F, 0.5F, 0.0F);
        int $$6 = p_116177_.getFuse();
        if ((float)$$6 - p_116179_ + 1.0F < 10.0F) {
            float $$7 = 1.0F - ((float)$$6 - p_116179_ + 1.0F) / 10.0F;
            $$7 = Mth.clamp($$7, 0.0F, 1.0F);
            $$7 *= $$7;
            $$7 *= $$7;
            float $$8 = 1.0F + $$7 * 0.3F;
            p_116180_.scale($$8, $$8, $$8);
        }

        p_116180_.mulPose(Axis.YP.rotationDegrees(-90.0F));
        p_116180_.translate(-0.5F, -0.5F, 0.5F);
        p_116180_.mulPose(Axis.YP.rotationDegrees(90.0F));
        TntMinecartRenderer.renderWhiteSolidBlock(this.blockRenderer, BlockRegistry.BROCCOLI_NUKE.get().defaultBlockState(), p_116180_, p_116181_, p_116182_, $$6 / 5 % 2 == 0);
        p_116180_.popPose();
        super.render(p_116177_, p_116178_, p_116179_, p_116180_, p_116181_, p_116182_);
    }

    public ResourceLocation getTextureLocation(PrimedBroccoliBomb p_116175_) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
