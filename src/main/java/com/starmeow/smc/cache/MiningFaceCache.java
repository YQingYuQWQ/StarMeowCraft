package com.starmeow.smc.cache;

import net.minecraft.core.Direction;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MiningFaceCache {

    private static final Map<UUID, Direction> CACHE = new HashMap<>();

    public static void set(UUID uuid, Direction face) {
        CACHE.put(uuid, face);
    }

    public static Direction get(UUID uuid) {
        return CACHE.getOrDefault(uuid, Direction.UP);
    }
}