package symbolics.division.honque.magic;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;

public class EphemeralHonk implements Honk {
    public static final String POOFED = "MISTER_ELECTRIC_KILL_THEM";
    public static final String WOMPWOMP = "ephemeral....";

    public float baseProbability() {
        return 1f/100;
    }

    public static void poof(Entity victim) {
        if (!(victim instanceof LivingEntity)) return;
        victim.addCommandTag(POOFED);
        if (victim instanceof ServerPlayerEntity player) {
            ServerPlayNetworking.send(player, new WompWomp(WOMPWOMP, 1));
        }
        victim.noClip = true;
    }


    @Override
    public void honk(ServerPlayerEntity player, LivingEntity entity, ItemStack itemStack, Item item) {
        entity.playSound(SoundEvents.ENTITY_RABBIT_HURT);
    }

    @Override
    public void badLuck(ServerPlayerEntity player, LivingEntity entity, ItemStack itemStack, Item item) {
        poof(entity);
    }

    @Override
    public void veryBadLuck(ServerPlayerEntity player, LivingEntity entity, ItemStack itemStack, Item item) {
        poof(player);
    }

    @Override
    public void trulyUnfortunateCircumstance(ServerPlayerEntity player, LivingEntity entity, ItemStack itemStack, Item item) {
        poof(player);
        player.getServerWorld()
                .getOtherEntities(player, player.getBoundingBox().expand(4, 4, 4))
                .forEach(EphemeralHonk::poof);
    }

    public static boolean shouldPoof(Entity entity) {
        return entity.getCommandTags().contains(POOFED);
    }
}