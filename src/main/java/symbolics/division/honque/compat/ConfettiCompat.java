package symbolics.division.honque.compat;

import dev.doublekekse.confetti.Confetti;
import dev.doublekekse.confetti.math.Vec3Dist;
import dev.doublekekse.confetti.packet.ExtendedParticlePacket;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import symbolics.division.honque.Honque;
import symbolics.division.honque.TheFunny;
import symbolics.division.honque.magic.Honk;
import symbolics.division.honque.magic.StandardHonk;

public class ConfettiCompat implements ModCompatibility {

    private static final Identifier YID = Identifier.of(Honque.MODID, "compat/confetti");
    private static final SoundEvent YIPPEEEEEE = Registry.register(Registries.SOUND_EVENT, YID, SoundEvent.of(YID));

    @Override
    public void initialize() {
        Honque.registerFunny("compat/the_funny_funny", new TheFunny(new ConfettiHonk()));
    }

    private static class ConfettiHonk extends StandardHonk {

        private static float CONFETTI_DIST_SQ = 30 * 30;

        private static void curse(LivingEntity victim) {
            var pDist = new Vec3Dist(victim.getPos().add(0, 1.5, 0), 0);
            var vDist = new Vec3Dist(Vec3d.ZERO, 1);
            victim.getWorld().getPlayers().stream()
                    .filter(p -> p.squaredDistanceTo(victim) < CONFETTI_DIST_SQ)
                    .forEach(p -> ServerPlayNetworking.send((ServerPlayerEntity) p,
                            new ExtendedParticlePacket(pDist, vDist, 50, false, Confetti.CONFETTI)
                    ));
        }

        @Override
        public float baseProbability() {
            return 1f/100;
        }

        @Override
        public void honk(ServerPlayerEntity player, LivingEntity entity, ItemStack itemStack, Item item) {
            honk(player, entity, YIPPEEEEEE);
            curse(entity);
        }

        @Override
        public void trulyUnfortunateCircumstance(ServerPlayerEntity player, LivingEntity entity, ItemStack itemStack, Item item) {
            honk(player, entity, YIPPEEEEEE);
            curse(player);
            var head = player.getEquippedStack(EquipmentSlot.HEAD);
            if (!head.isOf(item)) {
                if (head != ItemStack.EMPTY) {
                    ItemEntity hat = new ItemEntity(player.getServerWorld(), player.getX(), player.getY() + 2, player.getZ(), head, 0, 1, 0);
                    player.getServerWorld().spawnEntity(hat);
                }
                player.equipStack(EquipmentSlot.HEAD, item.getDefaultStack());
            }
        }
    }
}
