package symbolics.division.honque.compat;

import io.github.afamiliarquiet.MagnificentMaw;
import io.github.afamiliarquiet.util.MawBearer;
import io.github.afamiliarquiet.util.MawUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import symbolics.division.honque.Honque;
import symbolics.division.honque.TheFunny;
import symbolics.division.honque.magic.Honk;

public class MawCompat implements ModCompatibility {

    public void initialize() {
        Honque.registerFunny("compat/the_ravenous_golden_funny", new TheFunny(new RavenousGoldenHonk()));
    }

    private static class RavenousGoldenHonk implements Honk {

        private static void curse(LivingEntity entity) {
            if (entity instanceof MawBearer maw) {
                MawUtils.applyDraconicTf(entity);
                maw.magnificent_maw$setBreathing(false);
                maw.magnificent_maw$setBreathing(true);
                Honque.scheduleTick(
                        entity.getServer(),
                        20 * 5,
                        () -> {
                            if (!entity.isDead()) {
                                maw.magnificent_maw$setBreathing(false);
                                MawUtils.stripDraconicTf(entity);
                            }
                        }
                );
            }
        }

        private static void eat(LivingEntity entity) {
            if (entity instanceof PlayerEntity player) {
                var inv = player.getInventory();
                for (var stack : inv.main) {
                    // fake LivingEntity.consumeItem()
                    if (stack.isIn(MagnificentMaw.SWORDLY_SWALLOWABLE)) {
                        int slot = inv.getSlotWithStack(stack);
                        Hand hand = player.getActiveHand();
                        if (player.getStackInHand(hand).isEmpty()) {
                            inv.removeStack(slot);
                            player.setStackInHand(hand, stack);
                            ItemStack after = stack.finishUsing(player.getWorld(), player);
                            if (after != stack) {
                                player.setStackInHand(hand, after);
                            }
                            player.clearActiveItem();
                            player.getWorld().playSound(
                                    null, player.getBlockPos(), SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 1, 1
                            );
                        }
                        return;
                    }
                }
            }
        }

        @Override
        public float baseProbability() {
            return .01f;
        }

        @Override
        public void honk(ServerPlayerEntity player, LivingEntity entity, ItemStack itemStack, Item item) {
            honk(player, entity, SoundEvents.ENTITY_GENERIC_EXPLODE.value());
        }

        @Override
        public void badLuck(ServerPlayerEntity player, LivingEntity entity, ItemStack itemStack, Item item) {
            curse(entity);
        }

        @Override
        public void veryBadLuck(ServerPlayerEntity player, LivingEntity entity, ItemStack itemStack, Item item) {
            eat(player);
        }

        @Override
        public void trulyUnfortunateCircumstance(ServerPlayerEntity player, LivingEntity entity, ItemStack itemStack, Item item) {
            curse(player);
            curse(entity);
            eat(player);
            eat(entity);
        }
    }
}
