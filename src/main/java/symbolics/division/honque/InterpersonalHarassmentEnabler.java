package symbolics.division.honque;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;

public class InterpersonalHarassmentEnabler extends ThrownItemEntity {
    public InterpersonalHarassmentEnabler(EntityType<? extends InterpersonalHarassmentEnabler> entityType, World world) {
        super(entityType, world);
    }

    public InterpersonalHarassmentEnabler(World world, double x, double y, double z) {
        super(Honque.REALLY_FUNNY, x, y, z, world);
    }

    public InterpersonalHarassmentEnabler (World world, LivingEntity owner) {
        super(Honque.REALLY_FUNNY, owner, world);
    }

    @Override
    protected Item getDefaultItem() {
        return Honque.THE_FUNNY;
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        Entity e = entityHitResult.getEntity();
        if (e instanceof LivingEntity living) {
            if (living.getEquippedStack(EquipmentSlot.HEAD).isOf(Items.AIR)) {
                living.equipStack(EquipmentSlot.HEAD, getStack());
            }
        }
        this.discard();
    }
}
