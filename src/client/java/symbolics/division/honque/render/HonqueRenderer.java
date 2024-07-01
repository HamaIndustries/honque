package symbolics.division.honque.render;

import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class HonqueRenderer implements ArmorRenderer {
    private final HonqueModel model;
    private final Identifier texture;

    public HonqueRenderer(
            Identifier id
    ) {
        this.model = new HonqueModel(HonqueModel.texturedModelData().createModel());
        this.texture = id.withPath(p -> "textures/armor/" + p + ".png");
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, ItemStack stack, LivingEntity entity, EquipmentSlot slot, int light, BipedEntityModel<LivingEntity> contextModel) {
        VertexConsumer vtx = ItemRenderer.getArmorGlintConsumer(vertexConsumers, RenderLayer.getArmorCutoutNoCull(texture), stack.hasGlint());
        contextModel.copyBipedStateTo(this.model);
        model.setVisible(false);
        model.head.visible = true;
        this.model.render(matrices, vtx, light, OverlayTexture.DEFAULT_UV, 0xFFFFFFFF);
    }
}
