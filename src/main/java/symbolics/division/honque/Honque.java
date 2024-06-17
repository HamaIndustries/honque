package symbolics.division.honque;

import com.mojang.serialization.Codec;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.block.dispenser.ProjectileDispenserBehavior;
import net.minecraft.component.ComponentType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Honque implements ModInitializer {
	public static final String MODID = "honque";
    public static final Logger LOGGER = LoggerFactory.getLogger("honque");

	public static final ComponentType<Boolean> HONKED = ComponentType.<Boolean>builder().codec(Codec.BOOL).packetCodec(PacketCodecs.BOOL).build();
	public static final TheFunny THE_FUNNY = new TheFunny();
	public static final EntityType<InterpersonalHarassmentEnabler> REALLY_FUNNY = EntityType.Builder.<InterpersonalHarassmentEnabler>create(InterpersonalHarassmentEnabler::new, SpawnGroup.MISC).dimensions(0.25F, 0.25F).maxTrackingRange(4).trackingTickInterval(10).build();

	@Override
	public void onInitialize() {
		Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(MODID, "honked"), HONKED);
		Registry.register(Registries.ITEM, Identifier.of(MODID, "the_funny"), THE_FUNNY);
		HonqueTraquer.init();
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(content -> content.add(THE_FUNNY));
		Registry.register(Registries.ENTITY_TYPE, Identifier.of(MODID, "hr_complaint"), REALLY_FUNNY);
		DispenserBlock.registerBehavior(THE_FUNNY, new ProjectileDispenserBehavior(THE_FUNNY));
	}

}