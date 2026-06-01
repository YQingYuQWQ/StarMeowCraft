package com.starmeow.smc.events;


import com.starmeow.smc.StarMeowCraft;
import com.starmeow.smc.cache.MiningFaceCache;
import com.starmeow.smc.init.ItemRegistry;
import com.starmeow.smc.init.NetworkRegistry;
import com.starmeow.smc.packet.EndBoatControlPacket;
import com.starmeow.smc.packet.SwordSlashC2S;
import com.starmeow.smc.packet.WingBoostC2S;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = StarMeowCraft.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientForgeEvents {

    private static int lastSentTick = -999999;
    private static boolean lastBoostPressed = false;

    public static boolean isValidWeapon(ItemStack stack) {
        return stack.is(ItemRegistry.DEVOUR_SWORD.get()) || stack.is(ItemRegistry.EXCALIBUR.get());
    }

    public static void sendOncePerTick(Player player) {
        int now = player.tickCount;
        if (now == lastSentTick) return;
        lastSentTick = now;

        NetworkRegistry.sendToServer(new SwordSlashC2S());
    }

    @SubscribeEvent
    public static void onLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event) {
        Player player = event.getEntity();
        if (player == null) return;

        ItemStack stack = player.getMainHandItem();
        if (!isValidWeapon(stack)) return;

        if (Minecraft.getInstance().player != player) return;

        sendOncePerTick(player);
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Minecraft mc = Minecraft.getInstance();
            LocalPlayer player = mc.player;

            if (player != null){
                if (player.getVehicle() instanceof Boat boat){
                    if (boat.getVariant() != Boat.Type.byName("end")) return;

                    boolean jumping = mc.options.keyJump.isDown();
                    boolean shift   = mc.options.keyShift.isDown();

                    NetworkRegistry.CHANNEL.sendToServer(new EndBoatControlPacket(jumping, shift));
                }

                boolean boost = mc.options.keyJump.isDown();
                if(player.isFallFlying() && player.getItemBySlot(EquipmentSlot.CHEST).is(ItemRegistry.DIVINE_WING.get()) && !player.getCooldowns().isOnCooldown(ItemRegistry.DIVINE_WING.get())){
                    if(boost && !lastBoostPressed){
                        NetworkRegistry.sendToServer(new WingBoostC2S());
                    }
                }
                lastBoostPressed = boost;
            }


        }
    }

    @SubscribeEvent
    public static void clientTickSetFaceCache(TickEvent.ClientTickEvent event) {

        Minecraft mc = Minecraft.getInstance();
        if(mc.player == null) return;
        if(!(mc.hitResult instanceof BlockHitResult hit)) return;
        Direction face = hit.getDirection();
        MiningFaceCache.set(mc.player.getUUID(), face);
    }
}
