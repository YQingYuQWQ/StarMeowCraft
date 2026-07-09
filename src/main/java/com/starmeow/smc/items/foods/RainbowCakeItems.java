package com.starmeow.smc.items.foods;

import com.starmeow.smc.helper.ItemHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class RainbowCakeItems extends ItemNameBlockItem {
    public RainbowCakeItems(Block p_40565_, Properties p_40566_) {
        super(p_40565_, p_40566_);
    }

    @Override
    public Component getName(ItemStack stack) {
        return ItemHelper.rainbowColor(super.getName(stack), 20, false);
    }

    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn)
    {
        String string = "tooltip.smc.rainbow_cake";
        if(Screen.hasShiftDown()){
            tooltip.add(Component.translatable(string + "_0").withStyle(ChatFormatting.GRAY));
            tooltip.add(Component.translatable(string + "_1").withStyle(ChatFormatting.RED));
            tooltip.add(Component.translatable(string + "_2").withStyle(ChatFormatting.GOLD));
            tooltip.add(Component.translatable(string + "_3").withStyle(ChatFormatting.YELLOW));
            tooltip.add(Component.translatable(string + "_4").withStyle(ChatFormatting.GREEN));
            tooltip.add(Component.translatable(string + "_5").withStyle(ChatFormatting.AQUA));
            tooltip.add(Component.translatable(string + "_6").withStyle(ChatFormatting.BLUE));
            tooltip.add(Component.translatable(string + "_7").withStyle(ChatFormatting.LIGHT_PURPLE));
        }else{
            tooltip.add(Component.translatable(string).withStyle(ChatFormatting.YELLOW).withStyle(ChatFormatting.BOLD));
        }
    }
}
