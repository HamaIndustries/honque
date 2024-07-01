package symbolics.division.honque.mixin.client;

import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.HeadFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.ModelWithHead;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import symbolics.division.honque.Honque;

@Mixin(HeadFeatureRenderer.class)
public abstract class HeadFeatureRendererMixin<T extends LivingEntity, M extends EntityModel<T> & ModelWithHead> extends FeatureRenderer<T, M>  {
    public HeadFeatureRendererMixin(FeatureRendererContext<T, M> context) {
        super(context);
    }

    @ModifyVariable(method = "Lnet/minecraft/client/render/entity/feature/HeadFeatureRenderer;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/LivingEntity;FFFFFF)V", at = @At("STORE"), ordinal = 0)
    private ItemStack swapItGirl(ItemStack original) {
        if (original.isIn(Honque.Tags.FUNNIES)) {
            return ItemStack.EMPTY;
        }
        return original;
    }
}
