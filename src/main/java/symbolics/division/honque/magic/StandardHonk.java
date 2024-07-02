package symbolics.division.honque.magic;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;

import java.util.Map;

public class StandardHonk implements Honk {
    private final SoundEvent[] honks;
    public StandardHonk() {
        honks = Registries.SOUND_EVENT.getEntrySet().parallelStream()
                .filter(entry -> {
                    var p = entry.getKey().getValue().getPath();
                    return p.startsWith("block") || p.startsWith("entity") || p.startsWith("item");
                }).map(Map.Entry::getValue).toArray(SoundEvent[]::new);
    }

    @Override
    public float baseProbability() {
        return 1f/2000;
    }

    @Override
    public void honk(ServerPlayerEntity player, LivingEntity entity, ItemStack itemStack, Item item) {
        honk(player, entity, pick(entity, honks));
    }

    @Override
    public void badLuck(ServerPlayerEntity player, LivingEntity entity, ItemStack itemStack, Item item) {
        entity.kill();
    }

    @Override
    public void veryBadLuck(ServerPlayerEntity player, LivingEntity entity, ItemStack itemStack, Item item) {
        player.kill();
    }

    @Override
    public void trulyUnfortunateCircumstance(ServerPlayerEntity player, LivingEntity entity, ItemStack itemStack, Item item) {
        EntityType.WARDEN.spawn(player.getServerWorld(), entity.getBlockPos(), SpawnReason.TRIGGERED);
    }
}
