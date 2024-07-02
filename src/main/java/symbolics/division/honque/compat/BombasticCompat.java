package symbolics.division.honque.compat;

import net.minecraft.component.ComponentType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import symbolics.division.honque.Honque;
import symbolics.division.honque.TheFunny;
import symbolics.division.honque.magic.Honk;

import java.util.List;

public class BombasticCompat implements ModCompatibility {

    private static String BMID = "bombastic";

    private static RegistryKey<ComponentType<?>> TRIGGERS = RegistryKey.of(RegistryKeys.DATA_COMPONENT_TYPE, Identifier.of(BMID, "triggers"));
    private static RegistryKey<ComponentType<?>> PINNED = RegistryKey.of(RegistryKeys.DATA_COMPONENT_TYPE, Identifier.of(BMID, "pinned"));
    private static RegistryKey<Item> PIPE_BOMB = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(BMID, "pipe_bomb"));
    private static RegistryKey<SoundEvent> HONK = RegistryKey.of(RegistryKeys.SOUND_EVENT, Identifier.of(BMID, "party_popper"));

    @Override
    public void initialize() {
        Honque.registerFunny("compat/the_black_funny", new TheFunny(new ExplosiveHonk()));
    }

    private static class ExplosiveHonk implements Honk {

        private static void curse(LivingEntity player, int power) {
            ItemStack stack  = Registries.ITEM.get(PIPE_BOMB).getDefaultStack();
            stack.set((ComponentType<List<ItemStack>>) Registries.DATA_COMPONENT_TYPE.get(TRIGGERS), List.of(Items.TNT.getDefaultStack().copyWithCount(power)));
            stack.set((ComponentType<Boolean>)Registries.DATA_COMPONENT_TYPE.get(PINNED), true);
            ItemEntity ie = new ItemEntity(player.getWorld(), player.getX(), player.getY() + 1, player.getZ(), stack);
            player.getWorld().spawnEntity(ie);
        }

        @Override
        public float baseProbability() {
            return 0.01f;
        }

        @Override
        public void honk(ServerPlayerEntity player, LivingEntity entity, ItemStack itemStack, Item item) {
            honk(player, entity, Registries.SOUND_EVENT.get(HONK));
        }

        @Override
        public void badLuck(ServerPlayerEntity player, LivingEntity entity, ItemStack itemStack, Item item) {
            curse(entity, 1);
        }

        @Override
        public void veryBadLuck(ServerPlayerEntity player, LivingEntity entity, ItemStack itemStack, Item item) {
            curse(player, 10);
        }

        @Override
        public void trulyUnfortunateCircumstance(ServerPlayerEntity player, LivingEntity entity, ItemStack itemStack, Item item) {
            curse(player, 64);
        }
    }
}
