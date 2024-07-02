package symbolics.division.honque;

import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.registry.Registries;
import symbolics.division.honque.magic.WompWomp;
import symbolics.division.honque.render.HonqueRenderer;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class HonqueClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		EntityRendererRegistry.register(Honque.REALLY_FUNNY, FlyingItemEntityRenderer::new);
		ClientPlayNetworking.registerGlobalReceiver(WompWomp.ID,
				((payload, context) -> {
					Honque.doWompWomp(payload, context.player());
				})
		);

		Honque.getAllFunnies().stream()
				.map(save(Registries.ITEM::getId, Pair::of))
				.map(Pair::swap)
				.map(lift(Pair::mapFirst, HonqueRenderer::new))
				.forEach(biconsume(Pair::getFirst, Pair::getSecond, ArmorRenderer::register));
	}

	private static <T1, T2, V> Function<T1, V> save(Function<T1, T2> f, BiFunction<T1, T2, V> s) {
		return x -> s.apply(x, f.apply(x));
	}

	private static <P, V, R, S> Function<P, S> lift(BiFunction<P, Function<V, R>, S> m, Function<V, R> f) {
		return t -> m.apply(t, f);
	}

	private static  <P, T1, T2, R> Consumer<P> biconsume (Function<P, T1> p1, Function<P, T2> p2, BiConsumer<T1, T2> f) {
		return p -> f.accept(p1.apply(p), p2.apply(p));
	}
}