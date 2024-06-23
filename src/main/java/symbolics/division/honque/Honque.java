package symbolics.division.honque;

import com.mojang.serialization.Codec;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.block.dispenser.ProjectileDispenserBehavior;
import net.minecraft.component.ComponentType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.TntMinecartEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import symbolics.division.honque.magic.AscendantHonk;
import symbolics.division.honque.magic.EphemeralHonk;
import symbolics.division.honque.magic.StandardHonk;
import symbolics.division.honque.magic.WompWomp;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Honque implements ModInitializer {
	public static final String MODID = "honque";
    public static final Logger LOGGER = LoggerFactory.getLogger("honque");

	public static final ComponentType<Boolean> HONKED = ComponentType.<Boolean>builder().codec(Codec.BOOL).packetCodec(PacketCodecs.BOOL).build();
	public static final TheFunny THE_FUNNY = registerFunny("the_funny", new TheFunny(new StandardHonk()));
	public static final TheFunny THE_BLUE_FUNNY = registerFunny("the_blue_funny", new TheFunny(new EphemeralHonk()));
	public static final TheFunny THE_GREEN_FUNNY = registerFunny("the_green_funny", new TheFunny(new AscendantHonk()));
	public static final EntityType<InterpersonalHarassmentEnabler> REALLY_FUNNY = EntityType.Builder.<InterpersonalHarassmentEnabler>create(InterpersonalHarassmentEnabler::new, SpawnGroup.MISC).dimensions(0.25F, 0.25F).maxTrackingRange(4).trackingTickInterval(10).build();

	@Override
	public void onInitialize() {
		Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(MODID, "honked"), HONKED);
		HonqueTraquer.init();
		Registry.register(Registries.ENTITY_TYPE, Identifier.of(MODID, "hr_complaint"), REALLY_FUNNY);

		PayloadTypeRegistry.playS2C().register(WompWomp.ID, WompWomp.PACKET_CODEC);
		registerWompWomp(EphemeralHonk.WOMPWOMP, (w, p) -> EphemeralHonk.poof(p));
		registerWompWomp(AscendantHonk.WOMPWOMP, (w, p) -> AscendantHonk.launch(p, w.value()));
	}

	private static <Honquer extends Item> Honquer registerFunny(String path, Honquer theFunnyInQuestion) {
		Registry.register(Registries.ITEM, Identifier.of(MODID, path), theFunnyInQuestion);
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(content -> content.add(theFunnyInQuestion));
		DispenserBlock.registerBehavior(theFunnyInQuestion, new ProjectileDispenserBehavior(theFunnyInQuestion));
		return theFunnyInQuestion;
	}

	private static Map<String, BiConsumer<WompWomp, PlayerEntity>> callbacks = new HashMap<>();
	public static void registerWompWomp(String id, BiConsumer<WompWomp, PlayerEntity> bisexualConsumer) {
		callbacks.put(id, bisexualConsumer);
	}
	public static void doWompWomp(WompWomp w, PlayerEntity player) {
		callbacks.get(w.action()).accept(w, player);
	}

}