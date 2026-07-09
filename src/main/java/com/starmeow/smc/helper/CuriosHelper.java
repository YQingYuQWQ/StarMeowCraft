package com.starmeow.smc.helper;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.Optional;

public class CuriosHelper {
    public static ItemStack getCharm(LivingEntity living, Item item) {
        Optional<ICuriosItemHandler> inventory = CuriosApi.getCuriosInventory(living).resolve();
        if(inventory.isEmpty()) return null;

        Optional<SlotResult> curio = inventory.get().findFirstCurio(item);
        return curio.map(SlotResult::stack).orElse(null);
    }

    public static boolean hasCharm(LivingEntity living, Item item) {
        return getCharm(living, item) != null;
    }

}
