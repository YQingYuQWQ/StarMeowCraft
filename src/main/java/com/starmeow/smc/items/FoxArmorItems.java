package com.starmeow.smc.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.starmeow.smc.init.ItemRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class FoxArmorItems extends SMCArmorItems{
    public FoxArmorItems(ArmorMaterial p_40386_, Type p_266831_, Properties p_40388_) {
        super(p_40386_, p_266831_, p_40388_);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack)
    {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.putAll(this.getDefaultAttributeModifiers(slot));
        if (slot == EquipmentSlot.HEAD && (stack.is(ItemRegistry.FOX_HELMET.get()) || stack.is(ItemRegistry.SNOW_FOX_HELMET.get()))) {
            builder.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(UUID.fromString("4285f4c7-0b61-4d91-9c60-f505afe984cd"), "fox_armor_add_speed", 0.1D, AttributeModifier.Operation.MULTIPLY_BASE));
        } else if (slot == EquipmentSlot.FEET && (stack.is(ItemRegistry.FOX_BOOTS.get()) || stack.is(ItemRegistry.SNOW_FOX_BOOTS.get()))) {
            builder.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(UUID.fromString("8056afb9-42cb-49f1-b449-dc47a5112b14"), "fox_armor_add_speed", 0.1D, AttributeModifier.Operation.MULTIPLY_BASE));
        }else if (slot == EquipmentSlot.CHEST && (stack.is(ItemRegistry.FOX_CHESTPLATE.get()) || stack.is(ItemRegistry.SNOW_FOX_CHESTPLATE.get()))) {
            builder.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(UUID.fromString("e24bf62a-a02b-468f-846f-7693673cd99f"), "fox_armor_add_speed", 0.1D, AttributeModifier.Operation.MULTIPLY_BASE));
        }else if (slot == EquipmentSlot.LEGS && (stack.is(ItemRegistry.FOX_LEGGINGS.get()) || stack.is(ItemRegistry.SNOW_FOX_LEGGINGS.get()))) {
            builder.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(UUID.fromString("a8a47950-ef6c-482f-9294-ebbcf97a4b14"), "fox_armor_add_speed", 0.1D, AttributeModifier.Operation.MULTIPLY_BASE));
        }
        return builder.build();
    }

    @Override
    public void onArmorTick(ItemStack itemstack, Level level, Player player) {
        if(!level.isClientSide()) {
            if(player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof FoxArmorItems) {
                player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 0, 0, true, true, true));
            }
        }
    }

    @Override
    public boolean canWalkOnPowderedSnow(ItemStack stack, LivingEntity wearer) {
        return stack.is(ItemRegistry.FOX_BOOTS.get()) || stack.is(ItemRegistry.SNOW_FOX_BOOTS.get());
    }

    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn)
    {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        if(stack.is(ItemRegistry.FOX_HELMET.get())||stack.is(ItemRegistry.SNOW_FOX_HELMET.get())){
            tooltip.add(Component.translatable("tooltip.smc.fox_hat").withStyle(ChatFormatting.BLUE));
        }
        if(stack.is(ItemRegistry.FOX_BOOTS.get()) || stack.is(ItemRegistry.SNOW_FOX_BOOTS.get())){
            tooltip.add(Component.translatable("tooltip.smc.fox_boots").withStyle(ChatFormatting.BLUE));
        }
        tooltip.add(Component.translatable("tooltip.smc.fox_suit_0").withStyle(ChatFormatting.AQUA));
    }

}
