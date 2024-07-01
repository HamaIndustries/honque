package symbolics.division.honque;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class IceCreamFactory implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        FabricDataGenerator.Pack pack = generator.createPack();
        pack.addProvider(ItemTags::new);
        pack.addProvider(Modelator::new);
    }

    private static class ItemTags extends FabricTagProvider.ItemTagProvider {

        public ItemTags(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
            super(output, completableFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
            getOrCreateTagBuilder(Honque.Tags.FUNNIES).add(Honque.getAllFunnies().toArray(TheFunny[]::new));
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
        }
    }
}
