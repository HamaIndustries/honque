package symbolics.division.honque.render;

import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.entity.LivingEntity;

public class HonqueModel extends BipedEntityModel<LivingEntity> {
    public HonqueModel(ModelPart root) {
        super(root);
    }

    public static TexturedModelData texturedModelData() {
        ModelData modelData = BipedEntityModel.getModelData(new Dilation(1f), 0.0f);
        ModelPartData root = modelData.getRoot();
        root.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create().uv(0, 0).cuboid(-1.0F, -3F, -6F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));
        return TexturedModelData.of(modelData, 16, 16);
    }

}
