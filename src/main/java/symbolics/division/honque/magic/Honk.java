package symbolics.division.honque.magic;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

public interface Honk {
    float baseProbability();
    void honk(ServerPlayerEntity player, LivingEntity entity, ItemStack itemStack, Item item);
    void badLuck(ServerPlayerEntity player, LivingEntity entity, ItemStack itemStack, Item item);
    void veryBadLuck(ServerPlayerEntity player, LivingEntity entity, ItemStack itemStack, Item item);
    void trulyUnfortunateCircumstance(ServerPlayerEntity player, LivingEntity entity, ItemStack itemStack, Item item);
}
