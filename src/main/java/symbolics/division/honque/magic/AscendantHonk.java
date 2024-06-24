package symbolics.division.honque.magic;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;

public class AscendantHonk implements Honk {
    public static String WOMPWOMP = "yippee!!!!!";

    @Override
    public float baseProbability() {
        return 1/200f;
    }

    public static void launch(Entity victim, int power) {
        if (victim instanceof ServerPlayerEntity player) {
            ServerPlayNetworking.send(player, new WompWomp(WOMPWOMP, power));
        }
        victim.setVelocity(0, power, 0);
    }

    @Override
    public void honk(ServerPlayerEntity player, LivingEntity entity, ItemStack itemStack, Item item) {
        entity.playSound(SoundEvents.GOAT_HORN_SOUNDS.getFirst().value());
    }

    @Override
    public void badLuck(ServerPlayerEntity player, LivingEntity entity, ItemStack itemStack, Item item) {
        launch(entity, player.experienceLevel);
    }

    @Override
    public void veryBadLuck(ServerPlayerEntity player, LivingEntity entity, ItemStack itemStack, Item item) {
        launch(player, player.experienceLevel);
    }

    @Override
    public void trulyUnfortunateCircumstance(ServerPlayerEntity player, LivingEntity entity, ItemStack itemStack, Item item) {
        launch(entity, player.experienceLevel);
        launch(player, player.experienceLevel);
    }
}
