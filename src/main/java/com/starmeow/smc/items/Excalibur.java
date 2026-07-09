package com.starmeow.smc.items;

import com.starmeow.smc.config.Config;
import com.starmeow.smc.helper.ItemHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class Excalibur extends SwordItem {
    public Excalibur(Tier p_43269_, int p_43270_, float p_43271_, Properties p_43272_) {
        super(p_43269_, p_43270_, p_43271_, p_43272_);
    }
    @Override
    public Component getName(ItemStack stack) {
        int[] colors = {
                ItemHelper.colorToInt(255, 25, 25),
                ItemHelper.colorToInt(255, 182, 25),
                ItemHelper.colorToInt(255, 255, 188),
                ItemHelper.colorToInt(255, 255, 255),
                ItemHelper.colorToInt(255, 182, 188),
                ItemHelper.colorToInt(255, 182, 25)
        };
        return ItemHelper.customRainbowColor(super.getName(stack), 100, true, 0.16f, colors);
    }

    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn)
    {
        String string = "tooltip.smc." + stack.getItem();
        tooltip.add(Component.translatable(string, Config.AURA_EXTRA_DAMAGE.get(), "%").withStyle(ChatFormatting.BLUE));
    }
}
