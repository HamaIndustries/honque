package symbolics.division.honque.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import symbolics.division.honque.Honque;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    private PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "interact", at = @At("TAIL"), cancellable = true)
    public void interact(Entity entity, Hand hand, CallbackInfoReturnable<ActionResult> ci) {
        if (this.getStackInHand(hand) != ItemStack.EMPTY) return;
        if (entity instanceof LivingEntity e && e.getEquippedStack(EquipmentSlot.HEAD).isOf(Honque.THE_FUNNY)) {
            var result = Honque.THE_FUNNY.press((PlayerEntity) (Entity) this, e, e.getEquippedStack(EquipmentSlot.HEAD));
            if (result != null) {
                ci.setReturnValue(result);
                ci.cancel();
            }
        }
    }
}
