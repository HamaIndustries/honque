package symbolics.division.honque;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.data.client.*;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class IceCreamFactory implements DataGeneratorEntrypoint {

    private static final Set<Identifier> compats = new HashSet<>();

    private static void addCompat(String funny) {
        compats.add(Identifier.of(Honque.MODID, "compat/" + funny));
    }

    static {
        addCompat("the_ravenous_golden_funny");
    }

    private static RegistryKey<Item> pls(Identifier id) {
        return RegistryKey.of(RegistryKeys.ITEM, id);
    }

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        FabricDataGenerator.Pack pack = generator.createPack();
        pack.addProvider(ItemsTags::new);
        pack.addProvider(Modelator::new);
    }

    private static class ItemsTags extends FabricTagProvider.ItemTagProvider {

        public ItemsTags(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
            super(output, completableFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
            var b = getOrCreateTagBuilder(Honque.Tags.FUNNIES)
                    .add(Honque.getAllFunnies().toArray(TheFunny[]::new));
            compats.forEach(b::addOptional);
        }
    }

    private static class Modelator extends FabricModelProvider {

        public Modelator(FabricDataOutput output) {
            super(output);
        }

        @Override
        public CompletableFuture<?> run(DataWriter writer) {
            return super.run(writer);
        }

        @Override
        public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {

        }

        @Override
        public void generateItemModels(ItemModelGenerator itemModelGenerator) {
            for (Item funny : Honque.getAllFunnies()) {
                itemModelGenerator.register(funny, Models.GENERATED);
            }
            compats.forEach( id -> {
                id = id.withPrefixedPath("item/");
                Models.GENERATED.upload(id, TextureMap.layer0(id), itemModelGenerator.writer);
            });
        }
    }
}
