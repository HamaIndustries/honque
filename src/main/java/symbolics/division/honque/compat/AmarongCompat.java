package symbolics.division.honque.compat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import symbolics.division.honque.Honque;
import symbolics.division.honque.TheFunny;
import symbolics.division.honque.magic.Honk;

public class AmarongCompat implements ModCompatibility {

    // its just like the one we had at home
    private static RegistryKey<EntityType<?>> RAILGUN_TYPE = RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of("amarong", "railgun"));
    private static RegistryKey<SoundEvent> DUCK_SOUND = RegistryKey.of(RegistryKeys.SOUND_EVENT, Identifier.of("amarong", "duck_squeaks"));

    @Override
    public void initialize() {
        Honque.registerFunny("compat/the_annihilation_funny", new TheFunny(new AnnihilationHonk()));
    }

    private static class AnnihilationHonk implements Honk {
        private static void curse(LivingEntity entity) {
            Entity rg;
            try {
                rg = Registries.ENTITY_TYPE.get(RAILGUN_TYPE).create(entity.getWorld());
            } catch (NullPointerException err) {
                throw new RuntimeException("Failed to acquire amarong railgun entity from key, got null instead");
            }
            if (rg == null) {
                Honque.LOGGER.debug("Tried to spawn amarong railgun but failed. Is it disabled?");
                return;
            }
            // cant set owner :x good luck
            rg.setPosition(entity.getEyePos().add(entity.getRotationVector().multiply(0.4)));
            rg.setYaw(entity.getHeadYaw());
            rg.setPitch(entity.getPitch());
            entity.getWorld().spawnEntity(rg);
        }

        @Override
        public float baseProbability() {
            return 1f/300;
        }

        @Override
        public void honk(ServerPlayerEntity player, LivingEntity entity, ItemStack itemStack, Item item) {
            honk(player, entity, Registries.SOUND_EVENT.get(DUCK_SOUND));
        }

        @Override
        public void badLuck(ServerPlayerEntity player, LivingEntity entity, ItemStack itemStack, Item item) {
            curse(player);
        }

        @Override
        public void veryBadLuck(ServerPlayerEntity player, LivingEntity entity, ItemStack itemStack, Item item) {
            curse(entity);
        }

        @Override
        public void trulyUnfortunateCircumstance(ServerPlayerEntity player, LivingEntity entity, ItemStack itemStack, Item item) {
            player.getServerWorld().getEntitiesByType(
                    EntityType.PLAYER, player.getBoundingBox().expand(5), p -> true
            ).forEach(AnnihilationHonk::curse);
        }
    }
}
