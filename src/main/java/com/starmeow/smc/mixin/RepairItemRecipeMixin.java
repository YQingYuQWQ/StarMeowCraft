package com.starmeow.smc.mixin;

import com.starmeow.smc.init.EnchantmentRegistry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RepairItemRecipe;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Predicate;
import java.util.stream.Stream;

@Mixin(RepairItemRecipe.class)
public class RepairItemRecipeMixin {
    @Redirect(
            method = "assemble",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/stream/Stream;filter(Ljava/util/function/Predicate;)Ljava/util/stream/Stream;"
            )
    )
    private Stream<Enchantment> smc$redirectFilter(Stream<Enchantment> stream, Predicate<Enchantment> predicate, CraftingContainer container, RegistryAccess access) {
        ItemStack stack1 = ItemStack.EMPTY;
        ItemStack stack2 = ItemStack.EMPTY;
        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (stack.isEmpty()) {
                continue;
            }
            if (stack1.isEmpty()) {
                stack1 = stack;
            } else {
                stack2 = stack;
                break;
            }
        }
        if(!stack1.isEmpty() && !stack2.isEmpty()){
            int e1 = EnchantmentHelper.getItemEnchantmentLevel(EnchantmentRegistry.INHERITANCE.get(), stack1);
            int e2 = EnchantmentHelper.getItemEnchantmentLevel(EnchantmentRegistry.INHERITANCE.get(), stack2);
            if (e1 > 0){
                return stack1.getAllEnchantments().keySet().stream();
            }
            if (e2 > 0 && e1 == 0) {
                return stack2.getAllEnchantments().keySet().stream();
            }
        }

        return stream.filter(predicate);
    }
}
