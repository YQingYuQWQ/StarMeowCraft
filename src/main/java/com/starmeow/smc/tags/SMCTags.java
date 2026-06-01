package com.starmeow.smc.tags;

import com.starmeow.smc.StarMeowCraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

public class SMCTags {
    public static final TagKey<Item> CHICKEN_COMPAT = modItemTag("chicken_compat");
    public static final TagKey<Item> BEEF_COMPAT = modItemTag("beef_compat");
    public static final TagKey<Item> MUTTON_COMPAT = modItemTag("mutton_compat");
    public static final TagKey<Item> MILK_COMPAT = modItemTag("milk_compat");
    public static final TagKey<Item> PORK_COMPAT = modItemTag("pork_compat");
    public static final TagKey<Item> MEAT_COMPAT = modItemTag("meat_compat");
    public static final TagKey<Item> VACUUM_SNIFFER_DROP = modItemTag("vacuum_sniffer_drop");
    public static final TagKey<Item> CLOUD = modItemTag("cloud");
    public static final TagKey<Item> EASTER_BUNNY_EGG_COMPONENTS = modItemTag("easter_bunny_egg_components");
    public static final TagKey<Block> CLOUD_BLOCK = modBlockTag("cloud");
    public static final TagKey<Block> EASTER_BUNNY_EGG_HATCH_BOOST = modBlockTag("easter_bunny_egg_hatch_boost");

    public static final TagKey<Biome> PERKIN_ORE_GENERATE = modBiomeTag("perkin_ore_generate");

    private static TagKey<Item> modItemTag(String path) {
        return ItemTags.create(new ResourceLocation(StarMeowCraft.MODID, path));
    }

    private static TagKey<Block> modBlockTag(String path) {
        return BlockTags.create(new ResourceLocation(StarMeowCraft.MODID, path));
    }

    private static TagKey<Biome> modBiomeTag(String path) {
        return TagKey.create(Registries.BIOME, (new ResourceLocation(StarMeowCraft.MODID, path)));
    }
}
