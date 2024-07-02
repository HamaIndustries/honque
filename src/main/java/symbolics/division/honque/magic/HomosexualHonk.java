package symbolics.division.honque.magic;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.RabbitEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import symbolics.division.honque.Honque;

public class HomosexualHonk implements Honk {
    @Override
    public float baseProbability() {
        return 1f/200;
    }

    public static SoundEvent[] sounds = {
            SoundEvents.ENTITY_RABBIT_AMBIENT,
            SoundEvents.ENTITY_RABBIT_ATTACK,
            SoundEvents.ENTITY_RABBIT_DEATH,
            SoundEvents.ENTITY_RABBIT_HURT,
            SoundEvents.ENTITY_RABBIT_JUMP
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

    public static final Honk[] alternatives = {
            Honque.THE_BLUE_FUNNY.beep(),
            Honque.THE_GREEN_FUNNY.beep(),
            Honque.THE_ORANGE_FUNNY.beep()
    };

    @Override
    public void trulyUnfortunateCircumstance(ServerPlayerEntity player, LivingEntity entity, ItemStack itemStack, Item item) {
        alternatives[player.getRandom().nextInt(alternatives.length)].trulyUnfortunateCircumstance(player, entity, itemStack, item);
        RabbitEntity theBuny = new RabbitEntity(EntityType.RABBIT, player.getServerWorld());
        theBuny.setVariant(RabbitEntity.RabbitType.EVIL);
        player.getServerWorld().spawnEntity(theBuny);
    }
}
