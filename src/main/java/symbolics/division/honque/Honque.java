package symbolics.division.honque;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ProjectileDispenserBehavior;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.Main;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import symbolics.division.honque.compat.MawCompat;
import symbolics.division.honque.compat.ModCompatibility;
import symbolics.division.honque.magic.*;

import java.util.*;
import java.util.function.BiConsumer;

public class Honque implements ModInitializer {
	public static final String MODID = "honque";
    public static final Logger LOGGER = LoggerFactory.getLogger("honque");

	private static Set<Item> ALL_FUNNIES = new HashSet<>();

	public static final TheFunny THE_FUNNY = registerFunny("the_funny", new TheFunny(new StandardHonk()));
	public static final TheFunny THE_BLUE_FUNNY = registerFunny("the_blue_funny", new TheFunny(new EphemeralHonk()));
	public static final TheFunny THE_GREEN_FUNNY = registerFunny("the_green_funny", new TheFunny(new AscendantHonk()));
	public static final TheFunny THE_ORANGE_FUNNY = registerFunny("the_orange_funny", new TheFunny(new AlchemicalHonk()));
	public static final TheFunny THE_GAY_FUNNY = registerFunny("the_gay_funny", new TheFunny(new HomosexualHonk()));
	public static final EntityType<InterpersonalHarassmentEnabler> REALLY_FUNNY = EntityType.Builder.<InterpersonalHarassmentEnabler>create(InterpersonalHarassmentEnabler::new, SpawnGroup.MISC).dimensions(0.25F, 0.25F).maxTrackingRange(4).trackingTickInterval(10).build();

	public static final class Tags {
		public static final TagKey<Item> FUNNIES = TagKey.of(RegistryKeys.ITEM, Identifier.of(MODID, "funnies"));
	}

	@Override
	public void onInitialize() {
		LOGGER.info("HONK HONK HONK");
		ServerTickEvents.START_SERVER_TICK.register(Honque::tickSchedule);
		HonqueTraquer.init();
		Registry.register(Registries.ENTITY_TYPE, Identifier.of(MODID, "hr_complaint"), REALLY_FUNNY);

		PayloadTypeRegistry.playS2C().register(WompWomp.ID, WompWomp.PACKET_CODEC);
		registerWompWomp(EphemeralHonk.WOMPWOMP, (w, p) -> EphemeralHonk.poof(p));
		registerWompWomp(AscendantHonk.WOMPWOMP, (w, p) -> AscendantHonk.launch(p, w.value()));

		ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> EphemeralHonk.unpoof(newPlayer));

		ModCompatibility.init();
	}

	public static <Honquer extends Item> Honquer registerFunny(String path, Honquer theFunnyInQuestion) {
		Registry.register(Registries.ITEM, Identifier.of(MODID, path), theFunnyInQuestion);
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(content -> content.add(theFunnyInQuestion));
		DispenserBlock.registerBehavior(theFunnyInQuestion, new ProjectileDispenserBehavior(theFunnyInQuestion));
		ALL_FUNNIES.add(theFunnyInQuestion);
		return theFunnyInQuestion;
	}

	private static Map<String, BiConsumer<WompWomp, PlayerEntity>> callbacks = new HashMap<>();
	public static void registerWompWomp(String id, BiConsumer<WompWomp, PlayerEntity> bisexualConsumer) {
		callbacks.put(id, bisexualConsumer);
	}
	public static void doWompWomp(WompWomp w, PlayerEntity player) {
		callbacks.get(w.action()).accept(w, player);
	}

	public static Set<Item> getAllFunnies() {
		return Collections.unmodifiableSet(ALL_FUNNIES);
	}

	// time
	private static List<Pair<Integer, Runnable>> schedules = new ArrayList<>();
	private static void tickSchedule(MinecraftServer server) {
		schedules.removeIf( p -> {
			if (p.getLeft() <= server.getTicks()) {
				p.getRight().run();
				return true;
			}
			return false;
		});
	}

	public static void scheduleTick(@Nullable MinecraftServer server, int tickDelta, Runnable task) {
		if (server == null) {
			throw new NullPointerException("Attempted to schedule server tick on client");
		}
		schedules.add(new Pair<>(server.getTicks()+tickDelta, task));
	}
}