package com.starmeow.smc.worldgen;

import com.mojang.datafixers.util.Pair;
import com.starmeow.smc.StarMeowCraft;
import com.starmeow.smc.config.Config;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = StarMeowCraft.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class VillageStructures {

    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {


        Registry<StructureTemplatePool> templatePools = event.getServer().registryAccess().registryOrThrow(Registries.TEMPLATE_POOL);
        Registry<StructureProcessorList> processorLists = event.getServer().registryAccess().registryOrThrow(Registries.PROCESSOR_LIST);
        if (Config.ENABLE_LATRINE.get()) {
            addBuildingToPool(templatePools, processorLists, new ResourceLocation("minecraft:village/plains/houses"), "smc:village/houses/village_latrine_a", 3);
            addBuildingToPool(templatePools, processorLists, new ResourceLocation("minecraft:village/taiga/houses"), "smc:village/houses/village_latrine_b", 3);
        }
        if (Config.ENABLE_FAULT_HOUSE.get()) {
            addBuildingToPool(templatePools, processorLists, new ResourceLocation("minecraft:village/snowy/houses"), "smc:village/houses/fault_house", 1);
        }

    }

    private static void addBuildingToPool(Registry<StructureTemplatePool> templatePoolRegistry, Registry<StructureProcessorList> processorListRegistry, ResourceLocation poolRL, String nbtPieceRL, int weight) {

        StructureTemplatePool pool = templatePoolRegistry.get(poolRL);
        if (pool == null) {
            return;
        }
        Holder<StructureProcessorList> emptyProcessor = processorListRegistry.getHolderOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, new ResourceLocation("minecraft", "empty")));
        SinglePoolElement piece = SinglePoolElement.single(nbtPieceRL, emptyProcessor).apply(StructureTemplatePool.Projection.RIGID);

        ObjectArrayList<StructurePoolElement> templates = new ObjectArrayList<>(pool.templates);
        for (int i = 0; i < weight; i++) {
            templates.add(piece);
        }
        pool.templates = templates;

        List<Pair<StructurePoolElement, Integer>> rawTemplates = new ArrayList<>(pool.rawTemplates);
        rawTemplates.add(Pair.of(piece, weight));
        pool.rawTemplates = rawTemplates;
    }
}