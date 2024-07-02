package symbolics.division.honque.magic;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;

public class AlchemicalHonk implements Honk {
    @Override
    public float baseProbability() {
        return 1f/200;
    }

    private void curse (LivingEntity victim) {
        int n_fun = 10 + victim.getRandom().nextInt(10);
        for (int i=0; i < n_fun; i++) {
            var status = Registries.STATUS_EFFECT.getIndexedEntries().get(victim.getRandom().nextInt(Registries.STATUS_EFFECT.size()));
            StatusEffectInstance instance;
            if (victim.getRandom().nextFloat() < 0.5) {
                instance = new StatusEffectInstance(status, 100, 1);
            } else {
                instance = new StatusEffectInstance(status, 1, 100);
            }
            var pot = new PotionEntity(victim.getWorld(), victim.getX(), victim.getY()+2, victim.getZ());
            var stack = Items.POTION.getDefaultStack();
            stack.set(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT.with(instance));
            pot.setItem(stack);
            pot.setVelocity(0, -1, 0);
            victim.getWorld().spawnEntity(pot);
        }
    }

    @Override
    public void honk(ServerPlayerEntity player, LivingEntity entity, ItemStack itemStack, Item item) {
        honk(player, entity, SoundEvents.BLOCK_GLASS_BREAK);
    }

    @Override
    public void badLuck(ServerPlayerEntity player, LivingEntity entity, ItemStack itemStack, Item item) {
        curse(entity);
    }

    @Override
    public void veryBadLuck(ServerPlayerEntity player, LivingEntity entity, ItemStack itemStack, Item item) {
        curse(player);
    }

    @Override
    public void trulyUnfortunateCircumstance(ServerPlayerEntity player, LivingEntity entity, ItemStack itemStack, Item item) {
        for (var p : entity.getWorld().getEntitiesByClass(ServerPlayerEntity.class, Box.of(entity.getPos(), 5, 5, 5), p -> true)) {
            curse(p);
        }
    }
}
