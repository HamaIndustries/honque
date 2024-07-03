package symbolics.division.honque.magic;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.RabbitEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

public class HomosexualHonk implements Honk {
    @Override
    public float baseProbability() {
        return 1f/200;
    }

    public static SoundEvent[] sounds = {
            SoundEvents.ENTITY_RABBIT_ATTACK,
            SoundEvents.ENTITY_RABBIT_DEATH,
            SoundEvents.ENTITY_RABBIT_HURT
    };

    @Override
    public void honk(ServerPlayerEntity player, LivingEntity entity, ItemStack itemStack, Item item) {
        honk(player, entity, sounds);
    }

    @Override
    public void badLuck(ServerPlayerEntity player, LivingEntity entity, ItemStack itemStack, Item item) {
        var r = new RabbitEntity(EntityType.RABBIT, entity.getWorld());
        r.setPosition(player.getPos());
        entity.getWorld().spawnEntity(r);
    }

    @Override
    public void veryBadLuck(ServerPlayerEntity player, LivingEntity entity, ItemStack itemStack, Item item) {
        badLuck(player, entity, itemStack, item);
        badLuck(player, entity, itemStack, item);
    }

    @Override
    public void trulyUnfortunateCircumstance(ServerPlayerEntity player, LivingEntity entity, ItemStack itemStack, Item item) {
        RabbitEntity theBuny = new RabbitEntity(EntityType.RABBIT, player.getServerWorld());
        theBuny.setVariant(RabbitEntity.RabbitType.EVIL);
        player.getServerWorld().spawnEntity(theBuny);
    }
}
