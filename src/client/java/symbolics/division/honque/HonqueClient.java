package symbolics.division.honque;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.Const;
import com.mojang.datafixers.kinds.Functor;
import com.mojang.datafixers.kinds.Monoid;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.impl.client.rendering.ArmorRendererRegistryImpl;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import symbolics.division.honque.magic.WompWomp;
import symbolics.division.honque.render.HonqueRenderer;
import symbolics.division.honque.render.MethodReferencesExamples;

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

	// alpha btw
	private static <A, B, O> Function<A, Function<B, O>> curry(BiFunction<A, B, O> f) {
		return  a -> b -> f.apply(a, b);
	}

	private static  <P, T1, T2, R> Function<P, R> bimap (Function<P, T1> p1, Function<P, T2> p2, BiFunction<T1, T2, R> f) {

		return p -> f.apply(p1.apply(p), p2.apply(p));
	}

	private static  <P, T1, T2, R> Consumer<P> biconsume (Function<P, T1> p1, Function<P, T2> p2, BiConsumer<T1, T2> f) {
		return p -> f.accept(p1.apply(p), p2.apply(p));
	}
}