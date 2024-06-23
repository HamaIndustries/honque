package symbolics.division.honque;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import symbolics.division.honque.magic.EphemeralHonk;
import symbolics.division.honque.magic.WompWomp;

public class HonqueClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		EntityRendererRegistry.register(Honque.REALLY_FUNNY, FlyingItemEntityRenderer::new);
		ClientPlayNetworking.registerGlobalReceiver(WompWomp.ID,
				((payload, context) -> {
					Honque.doWompWomp(payload, context.player());
				})
		);
	}
}