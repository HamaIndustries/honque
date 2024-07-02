package symbolics.division.honque.magic;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;

import java.util.List;

public interface Honk {
    default SoundEvent pick(LivingEntity entity, SoundEvent[] wwwwww) {
        return wwwwww[entity.getWorld().random.nextInt(wwwwww.length)];
    }
    default SoundEvent pick(LivingEntity entity, List<RegistryEntry.Reference<SoundEvent>> wwwwww) {
        return wwwwww.get(entity.getWorld().random.nextInt(wwwwww.size())).value();
    }
    default void honk(ServerPlayerEntity spe, LivingEntity entity, SoundEvent[] sound) {
        spe.getServerWorld().playSound(null, entity.getBlockPos(), pick(entity, sound), SoundCategory.PLAYERS, 1, 1);
    };
    default void honk(ServerPlayerEntity spe, LivingEntity entity, List<RegistryEntry.Reference<SoundEvent>> sound) {
        spe.getServerWorld().playSound(null, entity.getBlockPos(), pick(entity, sound), SoundCategory.PLAYERS, 1, 1);
    };
    default void honk(ServerPlayerEntity spe, LivingEntity entity, SoundEvent sound) {
        spe.getServerWorld().playSound(null, entity.getBlockPos(), sound, SoundCategory.PLAYERS, 1, 1);
    };
    float baseProbability();
    void honk(ServerPlayerEntity player, LivingEntity entity, ItemStack itemStack, Item item);
    void badLuck(ServerPlayerEntity player, LivingEntity entity, ItemStack itemStack, Item item);
    void veryBadLuck(ServerPlayerEntity player, LivingEntity entity, ItemStack itemStack, Item item);
    void trulyUnfortunateCircumstance(ServerPlayerEntity player, LivingEntity entity, ItemStack itemStack, Item item);
}
