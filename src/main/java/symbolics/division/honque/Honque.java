package symbolics.division.honque;

import com.mojang.serialization.Codec;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.ComponentType;
import net.minecraft.item.ItemGroup;
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

	@Override
	public void onInitialize() {
		Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(MODID, "honked"), HONKED);
		Registry.register(Registries.ITEM, Identifier.of(MODID, "the_funny"), THE_FUNNY);
		HonqueTraquer.init();
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(content -> content.add(THE_FUNNY));
	}

}