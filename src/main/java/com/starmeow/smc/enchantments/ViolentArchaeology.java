package com.starmeow.smc.enchantments;

import com.starmeow.smc.init.EnchantmentRegistry;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class ViolentArchaeology extends Enchantment {
    public ViolentArchaeology(Rarity rarity) {
        super(rarity, EnchantmentCategory.DIGGER, new EquipmentSlot[] {EquipmentSlot.MAINHAND});
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    public int getMinCost(int p_44652_) {
        return 5 + (p_44652_ - 1) * 8;
    }

    public int getMaxCost(int p_44660_) {
        return super.getMinCost(p_44660_) + 50;
    }

    public boolean isTreasureOnly() {
        return true;
    }

    public boolean checkCompatibility(Enchantment p_45113_) {
        return super.checkCompatibility(p_45113_)
                && p_45113_ != EnchantmentRegistry.ADAPTION.get();
    }
}