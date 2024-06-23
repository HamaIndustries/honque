package symbolics.division.honque.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import symbolics.division.honque.magic.EphemeralHonk;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(method = "move", at = @At("HEAD"))
    public void move(MovementType movementType, Vec3d movement, CallbackInfo ci) {
        Entity thisssssss = (Entity)(Object)this;
        if (EphemeralHonk.shouldPoof(thisssssss)) {
            thisssssss.noClip = true;
        }
    }
}
